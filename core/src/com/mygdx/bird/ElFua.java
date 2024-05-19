package com.mygdx.bird;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ElFua extends Actor {
    Rectangle bounds;
    AssetManager manager;



    public ElFua() {
        bounds = new Rectangle();
        setVisible(false);
        setSize(57, 145);
    }

    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        bounds.setPosition(getX(), getY());
        if (!isVisible()) {
            setVisible(true);
        }


        if (getX() < -64) {
            remove();
        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch,parentAlpha);
        batch.draw(manager.get("especial.png", Texture.class), getX(), getY());

    }



    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public AssetManager getAssetManager() {
        return manager;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.manager = assetManager;
    }
}
