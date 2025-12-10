package gremlins;

import processing.core.PImage;

/**
 * The breakable tile in the game.
 * Numerous Brick could appear in the level represented by "B" in map config.
 */
public class Brick {
    private int state = 0; //Cound of frame when animated
    private int x; //x-coordinate
    public int xTile;
    private int y; //y-coordinate
    public int yTile;
    public boolean exist = true; //false if collides with fireball
    private boolean animated = false; //Changing of sprite
    private PImage brickSprite;

    /**
     * Brick constructor which initialize its location and sprite
     * @param app
     * @param columes
     * @param rows
     * @param brickSprite
     */
    public Brick(App app, int columes, int rows, PImage brickSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.brickSprite = brickSprite;
    }

    /**
     * x getter of Brick
     * @return Brick.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * xTile getter of Brick
     * @return Brick.xTile
     */
    public int getXTile() {
        return this.xTile;
    }

    /**
     * y getter of Brick
     * @return Brick.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * yTile getter of Brick
     * @return Brick.yTile
     */
    public int getYTile() {
        return this.yTile;
    }

    /**
     * Animated getter of Brick
     * @return Brick.animated
     */
    public boolean getAnimated() {
        return this.animated;
    }

    /**
     * Sprite getter of Brick
     * @return Brick.brickSprite
     */
    public PImage getSprite() {
        return this.brickSprite;
    }

    /**
     * Animated setter of Brick
     */
    public void setAnimated() {
        this.animated = true;
    }

    /**
     * State setter of Brick
     */
    public void setState(int num) {
        this.state = num;
    }

    /**
     * Draw Brick
     * Update sprite according to the frame counter state
     * @param app
     */
    public void draw(App app) {
        if (!animated && exist) {
            app.image(brickSprite, x, y);
        } else {
            app.image(brickSprite, x, y);
            this.state += 1;
            if (state < 5) {
                this.brickSprite = app.brickD0;
            } else if (state < 9) {
                this.brickSprite = app.brickD1;
            } else if (state < 13) {
                this.brickSprite = app.brickD2;
            } else if (state < 17) {
                this.brickSprite = app.brickD3;
            } else {
                this.brickSprite = app.empty;
            }
        }
    }
}
