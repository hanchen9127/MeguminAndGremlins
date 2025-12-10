package gremlins;

import processing.core.PImage;

import java.util.Objects;

/**
 * The wizard controlled by player in the game.
 * Only one wizard is expected in one level represented by "W" in map config.
 * Movement depends on the pressing arrow keys.
 * Shoot Fireball projectile if pressing space key.
 * Collect powerup could gain Firestorm buff.
 */
public class Player {
    private int speed = 2;
    private int x; //x-coordinate
    private int xTile; //Column index on map
    private int y; //y-coordinate
    private int yTile; //Row index on map
    public String horizontal; //Left or Right
    public String verticle; //Up or Down
    public String shoot = "right"; //Default shooting direction
    public boolean upBan = false;
    public boolean downBan = false;
    public boolean rightBan = false;
    public boolean leftBan = false;
    public boolean lifelost = false;
    public PImage playerSprite;

    /**
     * Player constructor which initializes location and image
     * @param app
     * @param columes
     * @param rows
     * @param playerSprite
     */
    public Player(App app, int columes, int rows, PImage playerSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.playerSprite = playerSprite;
    }

    /**
     * x getter of Player
     * @return Player.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * xTile getter of Player
     * @return Player.xTile
     */
    public int getXTile() {
        return this.xTile;
    }

    /**
     * y getter of Player
     * @return Player.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * yTile getter of Player
     * @return Player.yTile
     */
    public int getYTile() {
        return this.yTile;
    }

    /**
     * Wall detector and give movement restriction
     * @param app
     */
    public void collides(App app) {
        // Center of sprite
        if (app.dKey || app.rKey) {
            xTile = (x+1)/20;
            yTile = y/20;
        } else if (app.uKey) {
            xTile = (x+1)/20;
            yTile = (y+19)/20;
        } else if (app.lKey) {
            xTile = (x+19)/20;
            yTile = y/20;
        } else {
            xTile = x/20;
            yTile = y/20;
        }

        //right wall
        rightBan = app.existence[yTile][xTile + 1] == 'X' || app.existence[yTile][xTile + 1] == 'B';

        //left wall
        leftBan = app.existence[yTile][xTile - 1] == 'X' || app.existence[yTile][xTile - 1] == 'B';

        //up wall
        upBan = app.existence[yTile - 1][xTile] == 'X' || app.existence[yTile - 1][xTile] == 'B';

        //down wall
        downBan = app.existence[yTile + 1][xTile] == 'X' || app.existence[yTile + 1][xTile] == 'B';

        //right-down wall
        if (app.existence[yTile+1][xTile+1] == 'X' || app.existence[yTile+1][xTile+1] == 'B') {
            if (this.x % 20 != 0) {
                downBan = true;
            }
            if (this.y % 20 != 0) {
                rightBan = true;
            }
        }
        //right-up wall
        if (app.existence[yTile-1][xTile+1] == 'X' || app.existence[yTile-1][xTile+1] == 'B') {
            if (this.x % 20 != 0) {
                upBan = true;
            }
            if (this.y % 20 != 0) {
                rightBan = true;
            }
        }
        //left-down wall
        if (app.existence[yTile+1][xTile-1] == 'X' || app.existence[yTile+1][xTile-1] == 'B') {
            if (this.x % 20 != 0) {
                downBan = true;
            }
            if (this.y % 20 != 0) {
                leftBan = true;
            }
        }
        //left-up wall
        if (app.existence[yTile-1][xTile-1] == 'X' || app.existence[yTile-1][xTile-1] == 'B') {
            if (this.x % 20 != 0) {
                upBan = true;
            }
            if (this.y % 20 != 0) {
                leftBan = true;
            }
        }
    }

    /**
     * Stop at a complete tile
     * @param app
     */
    public void correction(App app) {
        if (!app.lKey && !app.rKey) {
            if (Objects.equals(horizontal, "right")) {
                if (this.x % 20 > 0) {
                    this.x += speed;
                }
            }
            if (Objects.equals(horizontal, "left")) {
                if (this.x % 20 > 0) {
                    this.x -= speed;
                }
            }
        }
        if (!app.uKey && !app.dKey) {
            if (Objects.equals(verticle, "up")) {
                if (this.y % 20 > 0) {
                    this.y -= speed;
                }
            }
            if (Objects.equals(verticle, "down")) {
                if ((this.y % 20 > 0)) {
                    this.y += speed;
                }
            }
        }
    }

    /**
     * Combination of basic movement
     * @param app
     */
    public void move(App app) {
        if (!app.keyOrder.isEmpty()) {
            String key = app.keyOrder.getLast();

            switch (key) {
                case "left":
                    playerSprite = app.loadImage("wizard0.png");
                    left(app);
                    break;
                case "up":
                    playerSprite = app.loadImage("wizard2.png");
                    up(app);
                    break;
                case "right":
                    playerSprite = app.loadImage("wizard1.png");
                    right(app);
                    break;
                case "down":
                    playerSprite = app.loadImage("wizard3.png");
                    down(app);
                    break;
            }
        }
    }

    /**
     * Basic movement of going left
     * @param app
     */
    public void left(App app) {
        shoot = "left";
        if (!leftBan) {
            this.x -= speed;
            horizontal = "left";
        }
    }

    /**
     * Basic movement of going right
     * @param app
     */
    public void right(App app) {
        shoot = "right";
        if (!rightBan){
            this.x += speed;
            horizontal = "right";
        }
    }

    /**
     * Basic movement of going up
     * @param app
     */
    public void up(App app) {
        shoot = "up";
        if (!upBan){
            this.y -= speed;
            verticle = "up";
        }
    }

    /**
     * Basic movement of going down
     * @param app
     */
    public void down(App app) {
        shoot = "down";
        if (!downBan){
            this.y += speed;
            verticle = "down";
        }
    }

    /**
     * Reach Exit
     * Go to next level or win
     * @param app
     */
    public void enter(App app) {
        if (this.y == app.exit.getY() && this.x == app.exit.getX()) {
            app.levelNext();
        }
    }

    /**
     * Draw Player
     * Update sprites depends on condition
     * @param app
     */
    public void draw(App app) {
        if (!lifelost) {
            if (!app.newPowerup) {
                if (app.powerup.getEffect()) {
                    app.image(app.wizardP, x, y); // Extented sprite for powerup duration
                } else {
                    app.image(playerSprite, x, y);
                }
            } else {
                app.image(playerSprite, x, y);
            }
        } else  {
            app.image(app.wizardD, x, y);
        }

    }
}