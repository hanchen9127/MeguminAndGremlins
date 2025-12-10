package gremlins;

import processing.core.PImage;

/**
 * The projectile of wizard after collecting blue potion.
 * Fireball is replaced by laser.
 * The frequency of generation depends on cool-down and space-pressed in the level.
 * Laser last for half of cooldown each level.
 */
public class Firelaser {
    private int x; //x-coordinate
    private int xTile; //x-tileIndex
    private int y; //y-coordinate
    private int yTile; //y-tileIndex
    private String direction;
    public boolean exist = true; //false if collides with fireball
    private PImage firelaserSprite;

    /**
     * Firelazer constructor which initializes its location, direction and sprite
     * Can only be generated in entire row or column
     * @param app
     * @param x
     * @param y
     * @param direction
     */
    public Firelaser(App app, int x, int y, String direction) {
        int xReminder = x % 20;
        int yReminder = y % 20;
        //Correction to tile depending on direction
        if (direction == "right" || direction == "left") {
            this.x = x;
            if (yReminder > 10) {
                this.y = y + 20 - yReminder;
            } else {
                this.y = y - yReminder;
            }
        } else if (direction == "up" || direction == "down") {
            this.y = y;
            if (xReminder > 10) {
                this.x = x + 20 - xReminder;
            } else {
                this.x = x - xReminder;
            }
        }
        this.direction = direction;
        if (direction == "right") {
            this.firelaserSprite = app.redRight;
        } else if (direction == "down") {
            this.firelaserSprite = app.redDown;
        } else if (direction == "left") {
            this.x -= 700;
            this.firelaserSprite = app.redLeft;
        } else if (direction == "up") {
            this.y -= 700;
            this.firelaserSprite = app.redUp;
        }
    }


    /**
     * x getter of Firelaser
     * @return Firelaser.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Firelaser
     * @return Firelaser.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Collision with Brick
     * Destroy Brick
     * @param app
     */
    public void destroy(App app) {
        if (this.direction == "right") {
            xTile = x/20;
            yTile = y/20;
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (app.bricks[b].getXTile() >= this.xTile && app.bricks[b].getYTile() == this.yTile) {
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                    app.existence[app.bricks[b].getYTile()][app.bricks[b].getXTile()] = ' '; //Update array
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                }
            }
        } else if (this.direction == "left") {
            xTile = (x+19)/20;
            yTile = y/20;
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (app.bricks[b].getXTile() <= this.xTile+34 && app.bricks[b].getYTile() == this.yTile) {
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                    app.existence[app.bricks[b].getYTile()][app.bricks[b].getXTile()] = ' '; //Update array
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                }
            }
        } else if (this.direction == "down") {
            xTile = x/20;
            yTile = y/20;
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (app.bricks[b].getXTile() == this.xTile && app.bricks[b].getYTile() >= this.yTile) {
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                    app.existence[app.bricks[b].getYTile()][app.bricks[b].getXTile()] = ' '; //Update array
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                }
            }
        } else if (this.direction == "up") {
            xTile = x/20;
            yTile = (y+19)/20;
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (app.bricks[b].getXTile() == this.xTile && app.bricks[b].getYTile() <= this.yTile+34) {
                    app.existence[app.bricks[b].getYTile()][app.bricks[b].getXTile()] = ' '; //Update array
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                }
            }
        }
    }

    /**
     * Collision with all moving things
     * Cause damage to Boss Phase 2
     * @param app
     */
    public void penetrate(App app) {
        if (this.exist) {
            if (this.direction == "right") {
                for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                    if (app.monsters[m].getX() >= this.x && Math.abs(app.monsters[m].getY() - this.y) < 20) {
                        app.monsters[m].respawn(app);
                    }
                }
                for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                    if (app.mutations[M].getX() >= this.x && Math.abs(app.mutations[M].getY() - this.y) < 20) {
                        app.mutations[M].exist = false;
                        app.mutations[M].setAnimated();
                    }
                }
                for(int Z = 0; Z < app.mutations2.length && app.mutations2[Z] != null; Z++) {
                    if (app.mutations2[Z].getX() >= this.x && Math.abs(app.mutations2[Z].getY() - this.y) < 20) {
                        app.mutations2[Z].exist = false;
                        app.mutations2[Z].setAnimated();
                    }
                }
                for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                    if (app.slimes.get(l).exist && app.slimes.get(l).getX() >= this.x && Math.abs(this.y - app.slimes.get(l).getY()) < 20) {
                        app.slimes.get(l).exist = false;
                    }
                }
            } else if (this.direction == "left") {
                for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                    if (app.monsters[m].getX() <= this.x+34*20 && Math.abs(app.monsters[m].getY() - this.y) < 20) {
                        app.monsters[m].respawn(app);
                    }
                }
                for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                    if (app.mutations[M].getX() <= this.x+34*20 && Math.abs(app.mutations[M].getY() - this.y) < 20) {
                        app.mutations[M].exist = false;
                        app.mutations[M].setAnimated();
                    }
                }
                for(int Z = 0; Z < app.mutations2.length && app.mutations2[Z] != null; Z++) {
                    if (app.mutations2[Z].getX() <= this.x+34*20 && Math.abs(app.mutations2[Z].getY() - this.y) < 20) {
                        app.mutations2[Z].exist = false;
                        app.mutations2[Z].setAnimated();
                    }
                }
                for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                    if (app.slimes.get(l).exist && app.slimes.get(l).getX() <= this.x+34*20 && Math.abs(this.y - app.slimes.get(l).getY()) < 20) {
                        app.slimes.get(l).exist = false;
                    }
                }
            } else if (this.direction == "down") {
                for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                    if (Math.abs(app.monsters[m].getX() - this.x) < 20 && app.monsters[m].getY() >= this.y) {
                        app.monsters[m].respawn(app);
                    }
                }
                for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                    if (Math.abs(app.mutations[M].getX() - this.x) < 20 && app.mutations[M].getY() >= this.y) {
                        app.mutations[M].exist = false;
                        app.mutations[M].setAnimated();
                    }
                }
                for(int Z = 0; Z < app.mutations2.length && app.mutations2[Z] != null; Z++) {
                    if (Math.abs(app.mutations2[Z].getX() - this.x) < 20 && app.mutations2[Z].getY() >= this.y) {
                        app.mutations2[Z].exist = false;
                        app.mutations2[Z].setAnimated();
                    }
                }
                for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                    if (app.slimes.get(l).exist && Math.abs(this.x - app.slimes.get(l).getX()) < 20 && app.slimes.get(l).getY() >= this.y) {
                        app.slimes.get(l).exist = false;
                    }
                }
                if (app.levelNum == 10) {
                    if (app.mutationBoss.phase == 2) {
                        for(int f = 0; f < app.BossPhase.size() && app.BossPhase.get(f) != null; f++) {
                            if (app.BossPhase.get(f).exist && Math.abs(this.x - app.BossPhase.get(f).getX()) < 20 && app.BossPhase.get(f).getY() >= this.y) {
                                app.BossPhase.get(f).damage();
                            } else {
                                app.BossPhase.get(f).reset();
                            }
                        }
                    }
                }
            } else if (this.direction == "up") {
                for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                    if (Math.abs(app.monsters[m].getX() - this.x) < 20 && app.monsters[m].getY() <= this.y+34*20) {
                        app.monsters[m].respawn(app);
                    }
                }
                for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                    if (Math.abs(app.mutations[M].getX() - this.x) < 20 && app.mutations[M].getY() <= this.y+34*20) {
                        app.mutations[M].exist = false;
                        app.mutations[M].setAnimated();
                    }
                }
                for(int Z = 0; Z < app.mutations2.length && app.mutations2[Z] != null; Z++) {
                    if (Math.abs(app.mutations2[Z].getX() - this.x) < 20 && app.mutations2[Z].getY() <= this.y+34*20) {
                        app.mutations2[Z].exist = false;
                        app.mutations2[Z].setAnimated();
                    }
                }
                for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                    if (app.slimes.get(l).exist && Math.abs(this.x - app.slimes.get(l).getX()) < 20 && app.slimes.get(l).getY() <= this.y+34*20) {
                        app.slimes.get(l).exist = false;
                    }
                }
            }
        }
    }

    /**
     * Draw Firelaser
     * @param app
     */
    public void draw(App app) {
        app.image(firelaserSprite, x, y);
    }
}
