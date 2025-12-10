package gremlins;

import processing.core.PImage;

/**
 * The projectile Slime of Monster in the game.
 * One Monster could shoot unlimited Slime depending on cooldown.
 * Cause player lose life if collides.
 */
public class Slime {
    private int speed = 4;
    private int x; //x-coordinate
    private int xTile;
    private int y; //y-coordinate
    private int yTile;
    private String direction;
    public boolean exist = true; //false if absorbed by fireball or firestorm
    private PImage slimeSprite;

    /**
     * Slime constructor which initializes location, shooting direction and image
     * @param app
     * @param x
     * @param y
     * @param direction
     * @param slimeSprite
     */
    public Slime(App app, int x, int y, String direction, PImage slimeSprite) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.slimeSprite = slimeSprite;
    }

    /**
     * x getter of Slime
     * @return Slime.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Slime
     * @return Slime.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Update location depends on direction
     * @param app
     */
    public void move(App app) {
        if (exist){
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
     * Blocked by wall
     * Self remove existence
     * @param app
     */
    public void block(App app) {
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
        if (app.existence[yTile][xTile] == 'X' || app.existence[yTile][xTile] == 'B'){
            this.exist = false;
            this.slimeSprite = app.empty;
        }
    }

    /**
     * Collide and cause player losing one life
     * @param app
     */
    public void kill(App app) {
        if (this.exist && Math.abs(this.y - app.player.getY()) < 20 && Math.abs(this.x - app.player.getX()) < 20) {
            app.lifeLost();
        }
    }

    /**
     * Draw Slime
     * @param app
     */
    public void draw(App app) {
        app.image(slimeSprite, x, y);
    }
}
