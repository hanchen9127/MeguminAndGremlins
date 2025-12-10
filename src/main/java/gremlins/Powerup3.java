package gremlins;

import processing.core.PImage;

/**
 * The powerup which can be collected by player in the game.
 * Only one powerup should appear in one level represented by "H" in map config.
 * If "H" is identified always spawn in this location.
 * Heal status to 5 max
 */
public class Powerup3 {
    private int duration = 10; //Firestorm lasts for 10 seconds
    private int interval = 0; //Randomised delay of respawn after collected
    private int state = 0;
    private int tempDelay;
    private int x; //x-coordinate
    private int xTile;
    private int y; //y-coordinate
    private int yTile;
    public boolean collect = false; //false if collect
    public boolean effect = false;
    public boolean delayed = false;
    private PImage powerupSprite;

    /**
     * Powerup constructor which initializes location and image
     * @param app
     * @param xTile
     * @param yTile
     * @param powerupSprite
     */
    public Powerup3(App app, int xTile, int yTile, PImage powerupSprite) {
        this.x = xTile * 20;
        this.y = yTile * 20;
        this.xTile = xTile;
        this.yTile = yTile;
        this.powerupSprite = powerupSprite;
    }

    /**
     * Effect getter of Powerup
     * @return Powerup.effect
     */
    public boolean getEffect() {
        return this.effect;
    }

    /**
     * Collect getter of Powerup
     * @return Powerup.collect
     */
    public boolean getCollect() {
        return this.collect;
    }

    /**
     * Inteval setter of Powerup
     */
    public void setInterval(int num) {
        this.interval = num;
    }

    /**
     * Collected by player
     * @param app
     */
    public void collected(App app) {
        if (!collect) {
            if (Math.abs(this.y - app.player.getY()) < 20 && Math.abs(this.x - app.player.getX()) < 20) {
                this.collect = true;
                app.bgm("src\\main\\resources\\gremlins\\healSE.wav", "");
            }
        }
    }

    /**
     * Draw Powerup and count durations
     * @param app
     */
    public void draw(App app) {
        if (!collect) {
            app.image(powerupSprite, x, y);
        } else {
            app.lives = 10;
            app.image(app.empty, x, y);
        }
    }
}
