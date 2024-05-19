package com.mygdx.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Bird game;
    Player player;
    OrthographicCamera camera;
    Array<Pipe> obstacles;
    long lastObstacleTime;
    Stage stage;
    boolean dead;
    float score;


    ElFua boost;
    long boostedTime;
    boolean boosted;
    boolean boostTouch;
    int lastBoost;

    public GameScreen(final Bird gam) {
        this.game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        player = new Player();
        player.setManager(game.manager);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);
        obstacles = new Array<Pipe>();
        spawnObstacle();
        score = 0;
        lastBoost = 0;
    }

    @Override
    public void render(float delta) {
        dead = false;
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.batch.end();
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();
        if (Gdx.input.justTouched()) {
            player.impulso();
        }
        stage.act();


        if (player.getBounds().y > 480 - 45) {
            player.setY(480 - 45);
        }


        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }

        if (TimeUtils.nanoTime() - lastObstacleTime >= 750000000 && lastBoost == 5) {
            spawnElFua();
            boostTouch = true;
            lastBoost = 0;
        }

        if (boosted && TimeUtils.nanoTime() >= boostedTime) {
            boosted = false;
            player.setBoosted(false);
            player.setVisible(true);
        }

        if (boostTouch) {
            if (boost.getBounds().overlaps(player.getBounds())) {
                boosted = true;
                boostTouch = false;
                boostedTime = TimeUtils.nanoTime() + 10000000000L;
                boost.remove();
                player.setBoosted(true);


                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        player.setBoosted(false);
                    }
                }, 10);

                // Parpadeo antes de que termine el boost
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Timer.schedule(new Timer.Task() {
                            boolean visible = false;
                            @Override
                            public void run() {
                                player.setVisible(visible);
                                visible = !visible;
                            }
                        }, 0, 0.1f, 15);
                    }
                }, 8.5f);
            }
        }

        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000) {
            spawnObstacle();
            if (!boosted) {
                lastBoost++;
            }
        }
        // Comprova si les tuberies colisionen amb el jugador
        Iterator<Pipe> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getBounds().overlaps(player.getBounds()) && !boosted) {
                dead = true;
            }
        }

        // Treure de l'array les tuberies que estan fora de pantalla
        iter = obstacles.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            if (pipe.getX() < -64) {
                obstacles.removeValue(pipe, true);
            }
        }

        game.batch.begin();
        game.smallFont.draw(game.batch, "Score: " + (int) score, 10, 470);
        game.batch.end();


        if (dead) {
            game.lastScore = (int) score;
            if (game.lastScore > game.topScore) {
                game.topScore = game.lastScore;
            }
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        // La puntuació augmenta amb el temps de joc
        score += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void spawnElFua() {
        float holey = MathUtils.random(50, 230);
        ElFua elFua = new ElFua();
        elFua.setX(800);
        elFua.setY(holey);
        elFua.setAssetManager(game.manager);
        stage.addActor(elFua);
        this.boost = elFua;
    }

    private void spawnObstacle() {
        float holey = MathUtils.random(50, 230);
        Pipe pipe1 = new Pipe();
        pipe1.setX(800);
        pipe1.setY(holey - 230);
        pipe1.setUpsideDown(true);
        pipe1.setManager(game.manager);
        obstacles.add(pipe1);
        stage.addActor(pipe1);
        Pipe pipe2 = new Pipe();
        pipe2.setX(800);
        pipe2.setY(holey + 200);
        pipe2.setUpsideDown(false);
        pipe2.setManager(game.manager);
        obstacles.add(pipe2);
        stage.addActor(pipe2);
        lastObstacleTime = TimeUtils.nanoTime();
    }

}
