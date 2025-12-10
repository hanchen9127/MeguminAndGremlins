package gremlins;

import processing.core.PImage;

/**
 * The door tile in the game.
 * Only one exit is expected in each level represented by "E" in map config.
 * Send player to next level when collides.
 */
public class Exit {
    public int x; //x-coordinate
    public int y; //y-coordinate
    private PImage exitSprite;

    /**
     * Exit constructor which initialize its location and sprite
     * @param app
     * @param columes
     * @param rows
     * @param exitSprite
     */
    public Exit(App app, int columes, int rows, PImage exitSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.exitSprite = exitSprite;
    }

    /**
     * x getter of Exit
     * @return Exit.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Exit
     * @return Exit.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Draw Exit
     * @param app
     */
    public void draw(App app) {
        app.image(exitSprite, x, y);
    }
}
