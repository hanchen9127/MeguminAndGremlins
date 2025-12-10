package gremlins;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * The powerup which can be collected by player in the game.
 * Only one powerup should appear in one level represented by "L" in map config.
 * Upgrade fireball to firelaser
 */
public class Powerup2 {
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
    public Powerup2(App app, int xTile, int yTile, PImage powerupSprite) {
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
     * Respawn controller if conditions met
     * Decide which location to respawn
     * @param app
     */
    public void respawn(App app) {
        if (!delayed) {
            interval = 0; //reset generate interval
            tempDelay = app.randomGenerator.nextInt(11 - 5) + 5;
            delayed = true;
        }
        if (interval >= tempDelay*60) { //Cooled down
            this.x = app.xPowerup2*20;
            this.y = app.yPowerup2*20;
            collect = false;
            delayed = false;
            state = 0; //reset effect time
        }
    }

    /**
     * Collected by player
     * @param app
     */
    public void collected(App app) {
        if (!collect) {
            if (Math.abs(this.y - app.player.getY()) < 20 && Math.abs(this.x - app.player.getX()) < 20) {
                this.collect = true;
                app.bgm("src\\main\\resources\\gremlins\\laserSE.wav", "");
                app.firelasers.clear();
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
            state += 1;
            if (app.levelNum == 10) {
                if (state <= 99999*60) { //last 10 seconds
                    float percentage2 = (float) (1.0-((float) (state)/ (float)(99999*60)));
                    app.fill(0, 0, 255); //Red bar
                    app.rect(620,700, percentage2*80,5);
                    effect = true;
                    app.image(app.empty, x, y);
                } else {
                    effect = false;
                    app.firestorms.clear(); //Remove blackhole
                    respawn(app);
                    interval += 1;
                }
            } else {
                if (state <= duration*60) { //last 10 seconds
                    float percentage2 = (float) (1.0-((float) (state)/ (float)(duration*60)));
                    app.fill(0, 0, 255); //Red bar
                    app.rect(620,700, percentage2*80,5);
                    effect = true;
                    app.image(app.empty, x, y);
                } else {
                    effect = false;
                    app.firestorms.clear(); //Remove blackhole
                    respawn(app);
                    interval += 1;
                }
            }
        }
    }
}
