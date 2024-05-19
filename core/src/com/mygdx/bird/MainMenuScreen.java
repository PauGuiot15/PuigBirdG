package com.mygdx.bird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final Bird game;
    OrthographicCamera camera;

    public MainMenuScreen(final Bird gam) {
        game = gam;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Configurar el AssetManager
        setManager(game.manager);
    }

    // Método para configurar el AssetManager
    void setManager(AssetManager manager) {
        game.manager = manager;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(game.manager.get("background.png", Texture.class), 0, 0);
        game.bigFont.draw(game.batch, "Welcome to Puig Bird!!! ", 30, 300);
        game.bigFont.draw(game.batch, "Tap anywhere to begin!", 20, 180);
        game.batch.end();
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    // Métodos restantes de la interfaz Screen (resize, show, hide, pause, resume, dispose) pueden permanecer sin cambios o implementarse según sea necesario
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
}
