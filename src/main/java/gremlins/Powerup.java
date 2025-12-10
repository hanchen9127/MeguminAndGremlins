package gremlins;

import processing.core.PImage;
import java.util.*;

/**
 * The powerup which can be collected by player in the game.
 * Only one powerup should appear in one level represented by "P" in map config.
 * If "P" is identified always spawn in this location.
 * If not identified it would spawn in random available location.
 * Appear some seconds after level loaded and come back in a random time range after effect finishes.
 * Create Firestorm around player for a constant duration after collected.
 */
public class Powerup {
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
    public Powerup(App app, int xTile, int yTile, PImage powerupSprite) {
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
            if (app.definePowerup) {
                this.x = app.xPowerup*20;
                this.y = app.yPowerup*20;
                collect = false;
                delayed = false;
                state = 0; //reset effect time
            } else {
                ArrayList<Integer> xTemp = new ArrayList<Integer>();
                ArrayList<Integer> yTemp = new ArrayList<Integer>();
                xTemp.clear();
                yTemp.clear();
                for (int i = 0; i < 100; i++) {
                    xTemp.add(app.randomGenerator.nextInt(36 - 1) + 1);
                    yTemp.add(app.randomGenerator.nextInt(33 - 1) + 1);
                }
                for (int i = 0; i < xTemp.size() && xTemp.get(i) != null; i++) {
                    for (int j = 0; j < yTemp.size() && yTemp.get(j) != null; j++) {
                        if (app.existence[yTemp.get(j)][xTemp.get(i)] != 'B' && app.existence[yTemp.get(j)][xTemp.get(i)] != 'X') {
                            this.x = xTemp.get(i) * 20;
                            this.y = yTemp.get(j) * 20;
                            this.xTile = xTemp.get(i);
                            this.yTile = yTemp.get(j);
                            collect = false;
                            delayed = false;
                            state = 0; //reset effect time
                            break;
                        }
                    }
                }
            }
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
                app.bgm("src\\main\\resources\\gremlins\\magicSE.wav", "");
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
                    app.fill(255, 0, 0); //Red bar
                    app.rect(620,690, percentage2*80,5);
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
                    app.fill(255, 0, 0); //Red bar
                    app.rect(620,690, percentage2*80,5);
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
