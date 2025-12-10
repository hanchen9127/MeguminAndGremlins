package gremlins;

import processing.core.PImage;

/**
 * The unbreakable tile in the game.
 * Numerous Stone could appear in the level represented by "X" in map config.
 */
public class Stone {
    private int x;
    public int xTile;
    private int y;
    public int yTile;
    public PImage stoneSprite;

    /**
     * Stone constructor which initializes location and sprite
     * @param app
     * @param columes
     * @param rows
     * @param stoneSprite
     */
    public Stone(App app, int columes, int rows, PImage stoneSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.stoneSprite = stoneSprite;
    }

    /**
     * x getter of Stone
     * @return Stone.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * xTile getter of Stone
     * @return Stone.xTile
     */
    public int getXTile() {
        return this.xTile;
    }

    /**
     * y getter of Stone
     * @return Stone.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * xTile getter of Stone
     * @return Stone.yTile
     */
    public int getYTile() {
        return this.yTile;
    }

    /**
     * Draw Stone
     * @param app
     */
    public void draw(App app) {
        app.image(stoneSprite, x, y);
    }
}
