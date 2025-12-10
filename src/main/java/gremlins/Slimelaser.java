package gremlins;

import processing.core.PImage;

/**
 * The projectile of mutation2 blue slime in the game.
 * The frequency of generation depends on double of cool-down.
 * Last for half of cool-down.
 */
public class Slimelaser {
    private int x; //x-coordinate
    private int xTile; //x-tileIndex
    private int y; //y-coordinate
    private int yTile; //y-tileIndex
    private String direction;
    public int last = 0;
    public boolean exist = true; //false if collides with fireball
    private PImage slimelaserSprite;

    /**
     * Slimelazer constructor which initializes its location, direction and sprite
     * Can only be generated in entire row or column
     * @param app
     * @param x
     * @param y
     * @param d
     */
    public Slimelaser(App app, int x, int y, int d) {
        int xReminder = x % 20;
        int yReminder = y % 20;
        //Transition to string
        if (d % 4 == 0) {
            direction = "up";
        } else if (d % 4 == 1) {
            direction = "right";
        } else if (d % 4 == 2) {
            direction = "down";
        } else if (d % 4 == 3) {
            direction = "left";
        }

        //Determine x y
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

        //Find sprite
        if (app.levelNum == 10) {
            if (app.mutationBoss.phase == 2) {
                if (direction == "right") {
                    this.slimelaserSprite = app.yellowRight;
                } else if (direction == "down") {
                    this.slimelaserSprite = app.yellowDown;
                } else if (direction == "left") {
                    this.x -= 700;
                    this.slimelaserSprite = app.yellowLeft;
                } else if (direction == "up") {
                    this.y -= 700;
                    this.slimelaserSprite = app.yellowUp;
                }
            } else {
                if (direction == "right") {
                    this.slimelaserSprite = app.blueRight;
                } else if (direction == "down") {
                    this.slimelaserSprite = app.blueDown;
                } else if (direction == "left") {
                    this.x -= 700;
                    this.slimelaserSprite = app.blueLeft;
                } else if (direction == "up") {
                    this.y -= 700;
                    this.slimelaserSprite = app.blueUp;
                }
            }
        } else {
            if (direction == "right") {
                this.slimelaserSprite = app.blueRight;
            } else if (direction == "down") {
                this.slimelaserSprite = app.blueDown;
            } else if (direction == "left") {
                this.x -= 700;
                this.slimelaserSprite = app.blueLeft;
            } else if (direction == "up") {
                this.y -= 700;
                this.slimelaserSprite = app.blueUp;
            }
        }

    }

    /**
     * x getter of Slimelaser
     * @return Slimelaser.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Slimelaser
     * @return Slimelaser.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Kill player
     * @param app
     */
    public void kill(App app) {
        if (this.exist) {
            if (this.direction == "right") {
                if (app.player.getX() >= this.x && Math.abs(app.player.getY() - this.y) < 10) {
                    app.lifeLost();
                }
            } else if (this.direction == "left") {
                if (app.player.getX() <= this.x+34*20 && Math.abs(app.player.getY() - this.y) < 10) {
                    app.lifeLost();
                }
            } else if (this.direction == "down") {
                if (Math.abs(app.player.getX() - this.x) < 10 && app.player.getY() >= this.y) {
                    app.lifeLost();
                }
            } else if (this.direction == "up") {
                if (Math.abs(app.player.getX() - this.x) < 10 && app.player.getY() <= this.y+34*20) {
                    app.lifeLost();
                }
            }
        }
    }

    /**
     * Draw Slimelaser
     * @param app
     */
    public void draw(App app) {
        if (exist) {
            app.image(slimelaserSprite, x, y);
            last += 1;
            if (app.levelNum == 10) {
                if (last > 10) {
                    this.exist = false;
                }
            } else {
                if (last > 60) {
                    this.exist = false;
                }
            }
        } else {
            app.image(app.empty, x, y);
        }
    }
}
