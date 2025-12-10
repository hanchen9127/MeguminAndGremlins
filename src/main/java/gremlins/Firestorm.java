package gremlins;

import processing.core.PImage;

/**
 * The effect after collect powerup in the game.
 * Surround and protect the player in a constant duration.
 * Burn all Brick, Slime, Monster, Mutation around the player.
 */
public class Firestorm {
    private int radius = 90;
    private int x; //x-coordinate
    private int xCentre;
    private int y; //y-coordinate
    private int yCentre;
    public boolean exist = true; //false if collides with fireball
    private PImage firestormSprite;

    /**
     * Firestorm constructor which initializes its centre location and sprite
     * @param app
     * @param x
     * @param y
     * @param firestormSprite
     */
    public Firestorm(App app, int x, int y, PImage firestormSprite) {
        this.x = x- 100;
        this.y = y - 100;
        this.xCentre = x;
        this.yCentre = y;
        this.firestormSprite = firestormSprite;
    }

    /**
     * Collision with Brick
     * Destroy Brick
     * @param app
     */
    public void hitB(App app) {
        //Loop the object array and change the property
        for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
            if (Math.abs(app.bricks[b].getX() - this.xCentre) <= radius && Math.abs(app.bricks[b].getY() - this.yCentre) <= radius) {
                app.bricks[b].exist = false;
                app.existence[app.bricks[b].getYTile()][app.bricks[b].getXTile()] = ' '; //Update array
                app.bricks[b].setAnimated();
            }
        }
    }

    /**
     * Collision with Monster
     * Cause Monster to respawn
     * @param app
     */
    public void hitG(App app) {
        if (this.exist) {
            for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                if (Math.abs(this.yCentre - app.monsters[m].getY()) <= radius && Math.abs(this.xCentre - app.monsters[m].getX()) <= radius) {
                    app.monsters[m].respawn(app);
                }
            }
        }
    }

    /**
     * Collision with Slime and evaporate Slime
     * Cause damage to Boss Phase 1
     * @param app
     */
    public void hitS(App app) {
        if (this.exist) {
            for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                if (app.slimes.get(l).exist && Math.abs(this.yCentre - app.slimes.get(l).getY()) <= radius && Math.abs(this.xCentre - app.slimes.get(l).getX()) <= radius) {
                    app.slimes.get(l).exist = false;
                }
            }
            if (app.levelNum == 10) {
                if (app.mutationBoss.phase == 1 || app.mutationBoss.phase == 3) {
                    for(int f = 0; f < app.BossPhase.size() && app.BossPhase.get(f) != null; f++) {
                        if (app.BossPhase.get(f).exist && Math.abs(this.yCentre - app.BossPhase.get(f).getY()) <= radius && Math.abs(this.xCentre - app.BossPhase.get(f).getX()) <= radius) {
                            app.BossPhase.get(f).damage();
                        } else {
                            app.BossPhase.get(f).reset();
                        }
                    }
                }
            }
        }
    }

    /**
     * Collision with Mutation
     * Burn Mutation
     * @param app
     */
    public void hitM(App app) {
        if (this.exist) {
            for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                if (app.mutations[M].exist && Math.abs(this.yCentre - app.mutations[M].getY()) <= radius && Math.abs(this.xCentre - app.mutations[M].getX()) <= radius) {
                    app.mutations[M].exist = false;
                    app.mutations[M].setAnimated();
                }
            }
            for(int Z = 0; Z < app.mutations2.length && app.mutations2[Z] != null; Z++) {
                if (app.mutations2[Z].exist && Math.abs(this.yCentre - app.mutations2[Z].getY()) <= radius && Math.abs(this.xCentre - app.mutations2[Z].getX()) <= radius) {
                    app.mutations2[Z].exist = false;
                    app.mutations2[Z].setAnimated();
                }
            }
        }
    }

    /**
     * Draw Firestorm
     * @param app
     */
    public void draw(App app) {
        app.image(firestormSprite, x, y);
    }
}
