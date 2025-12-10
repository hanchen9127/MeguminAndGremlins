package gremlins;

import processing.core.PImage;

import java.util.Objects;

/**
 * The projectile of wizard in the game.
 * The frequency of generation depends on cool-down and space-pressed in the level.
 */
public class Fireball {
    private int speed = 4;
    private int x; //x-coordinate
    private int xTile; //x-tileIndex
    private int y; //y-coordinate
    private int yTile; //y-tileIndex
    private String direction;
    public boolean exist = true; //false if collides with fireball
    private PImage fireballSprite;
    private boolean changeDirect = true;

    /**
     * Fireball constructor which initializes its location, direction and sprite
     * Can only be generated in entire row or column
     * @param app
     * @param x
     * @param y
     * @param direction
     * @param fireballSprite
     */
    public Fireball(App app, int x, int y, String direction, PImage fireballSprite) {
        int xReminder = x % 20;
        int yReminder = y % 20;
        //Correction to tile depending on direction
        if (Objects.equals(direction, "right") || Objects.equals(direction, "left")) {
            this.x = x;
            if (yReminder > 10) {
                this.y = y + 20 - yReminder;
            } else {
                this.y = y - yReminder;
            }
        } else if (Objects.equals(direction, "up") || Objects.equals(direction, "down")) {
            this.y = y;
            if (xReminder > 10) {
                this.x = x + 20 - xReminder;
            } else {
                this.x = x - xReminder;
            }
        }
        this.direction = direction;
        this.fireballSprite = fireballSprite;
    }


    /**
     * x getter of Fireball
     * @return Fireball.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Fireball
     * @return Fireball.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Update location using speed depends on direction
     * @param app
     */
    public void move(App app) {
        if (exist){
            //Fireball could change direction once in level 10
            if (app.levelNum == 10 && changeDirect && this.direction != app.player.shoot) {
                this.direction = app.player.shoot;
                this.speed = 8;
                changeDirect = false;
            }
            if (direction == "right") {
                this.x += speed;
            } else if (direction == "left") {
                this.x -= speed;
            } else if (direction == "up") {
                this.y -= speed;
            } else if (direction == "down") {
                this.y += speed;
            }
        }
    }

    /**
     * Collision with Stone
     * Self remove existence
     * @param app
     */
    public void hitX(App app) {
        if (this.direction == "right") {
            xTile = x/20 + 1;
            yTile = y/20;
        } else if (this.direction == "left") {
            xTile = (x+1)/20;
            yTile = y/20;
        } else if (this.direction == "down") {
            xTile = x/20;
            yTile = y/20 + 1;
        } else if (this.direction == "up") {
            xTile = x/20;
            yTile = y/20;
        }
        if (app.existence[yTile][xTile] == 'X'){
            this.exist = false;
            this.fireballSprite = app.empty;
        }
    }

    /**
     * Collision with Brick
     * Self remove existence and destroy Brick
     * @param app
     */
    public void hitB(App app) {
        if (this.direction == "right") {
            xTile = x/20;
            yTile = y/20;
        } else if (this.direction == "left") {
            xTile = (x+19)/20;
            yTile = y/20;
        } else if (this.direction == "down") {
            xTile = x/20;
            yTile = y/20;
        } else if (this.direction == "up") {
            xTile = x/20;
            yTile = (y+19)/20;
        }
        if (app.existence[yTile][xTile] == 'B') {
            app.existence[yTile][xTile] = ' '; //Update array
            this.exist = false;
            this.fireballSprite = app.empty;
            //Loop the object array and change the property
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (app.bricks[b].getXTile() == this.xTile && app.bricks[b].getYTile() == this.yTile) {
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                    break;
                }
            }
        }
    }

    /**
     * Collision with Monster
     * Self remove existence and cause Monster to respawn
     * @param app
     */
    public void hitG(App app) {
        if (this.exist) {
            for(int m = 0; m < app.monsters.length && app.monsters[m] != null; m++) {
                if (Math.abs(this.y - app.monsters[m].getY()) < 20 && Math.abs(this.x - app.monsters[m].getX()) < 20) {
                    app.monsters[m].respawn(app);
                    this.exist = false;
                    this.fireballSprite = app.empty;
                }
            }
        }
    }

    /**
     * Collision with Slime
     * Cause damage to Boss Phase 3
     * Self remove existence
     * @param app
     */
    public void hitS(App app) {
        if (this.exist) {
            for(int l = 0; l < app.slimes.size() && app.slimes.get(l) != null; l++) {
                if (app.slimes.get(l).exist && Math.abs(this.y - app.slimes.get(l).getY()) < 20 && Math.abs(this.x - app.slimes.get(l).getX()) < 20) {
                    this.exist = false;
                    this.fireballSprite = app.empty;
                    app.slimes.get(l).exist = false;
                    //app.slimes.get(l).slimeSprite = app.empty;
                }
            }
            if (app.levelNum == 10) {
                if (app.mutationBoss.phase == 3) {
                    for(int f = 0; f < app.BossPhase.size() && app.BossPhase.get(f) != null; f++) {
                        if (app.BossPhase.get(f).exist && Math.abs(this.y - app.BossPhase.get(f).getY()) < 20 && Math.abs(this.x - app.BossPhase.get(f).getX()) < 20) {
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
     * Self remove existence and knock Mutation back a distance
     * @param app
     */
    public void hitM(App app) {
        if (this.exist) {
            for(int M = 0; M < app.mutations.length && app.mutations[M] != null; M++) {
                if (app.mutations[M].exist && Math.abs(this.y - app.mutations[M].getY()) < 20 && Math.abs(this.x - app.mutations[M].getX()) < 20) {
                    this.exist = false;
                    this.fireballSprite = app.empty;
                    if (app.mutations[M].getX() > 20 && app.mutations[M].getX() < 680 && app.mutations[M].getY() > 20 && app.mutations[M].getY() < 680) {
                        if (this.direction == "right" && app.mutations[M].sleep > app.mutations[M].wake*60) {
                            app.mutations[M].setX(40);
                        } else if (this.direction == "left" && app.mutations[M].sleep > app.mutations[M].wake*60) {
                            app.mutations[M].setX(-40);
                        } else if (this.direction == "up" && app.mutations[M].sleep > app.mutations[M].wake*60) {
                            app.mutations[M].setY(-40);
                        } else if (this.direction == "down" && app.mutations[M].sleep > app.mutations[M].wake*60) {
                            app.mutations[M].setY(40);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw Fireball
     * @param app
     */
    public void draw(App app) {
        app.image(fireballSprite, x, y);
    }
}
