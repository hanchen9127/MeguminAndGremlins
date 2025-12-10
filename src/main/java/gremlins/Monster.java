package gremlins;

import processing.core.PImage;
import java.util.*;

/**
 * The basic enemy called Gremlin in the game.
 * Numerous Monster could appear in one level represented by "G" in map config.
 * Shoot Slime projectile and cause player lose life if collides.
 */
public class Monster {
    private int speed = 1;
    private int x;
    private int xTile;
    private int y;
    private int yTile;
    private String direction;
    private ArrayList<String> choice = new ArrayList<String>(); //Four directions
    private boolean freeze = false;
    private boolean borned = false;
    private PImage monsterSprite;

    /**
     * Monster constructor which initializes location, image and arraylist of moving direction
     * @param app
     * @param columes
     * @param rows
     * @param monsterSprite
     */
    public Monster(App app, int columes, int rows, PImage monsterSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.monsterSprite = monsterSprite;
        this.choice.add("left");
        this.choice.add("right");
        this.choice.add("down");
        this.choice.add("up");
    }

    /**
     * x getter of Monster
     * @return Monster.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Monster
     * @return Monster.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Direction getter of Monster
     * @return Monster.direction
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * First generation switch of Monster
     */
    public void born(App app) {
        if (!borned) {
            detect(app);
            borned = true;
        }
    }

    /**
     * Surrounding tile detector
     * @param app
     */
    public void detect(App app) {
        //Reset the choices of movement
        this.choice.clear();
        choice.add("right");
        choice.add("left");
        choice.add("up");
        choice.add("down");

        //Tile correction
        xTile = (int) Math.floor((x+2)/20);
        yTile = (int) Math.ceil((y+2)/20);

        //right wall collision
        if (app.existence[yTile][xTile+1] == 'X' || app.existence[yTile][xTile+1] == 'B') {
            choice.remove("right");
        }
        //left wall collision
        if (app.existence[yTile][xTile-1] == 'X' || app.existence[yTile][xTile-1] == 'B') {
            choice.remove("left");
        }
        //up wall collision
        if (app.existence[yTile-1][xTile] == 'X' || app.existence[yTile-1][xTile] == 'B') {
            choice.remove("up");
        }
        //down wall collision
        if (app.existence[yTile+1][xTile] == 'X' || app.existence[yTile+1][xTile] == 'B') {
            choice.remove("down");
        }

        //Reverse back direction as should not go back
        if (direction == "right") {
            choice.remove("left");
        } else if (direction == "left") {
            choice.remove("right");
        } else if (direction == "up") {
            choice.remove("down");
        } else if (direction == "down") {
            choice.remove("up");
        }

        //Randomize the sequence
        Collections.shuffle(choice);
        if (choice.size() == 0) { //If blocked by three walls, go back
            if (direction == "right") {
                direction = "left";
            } else if (direction == "left") {
                direction = "right";
            } else if (direction == "up") {
                direction = "down";
            } else if (direction == "down") {
                direction = "up";
            } else {
                direction = "right";
            }
        } else {
            //Choose randomised choice
            this.direction = choice.get(0);
        }
    }

    /**
     * Move of logic
     * @param app
     */
    public void move(App app) {
        //Tile correction
        xTile = (int) Math.floor((x+2)/20);
        yTile = (int) Math.ceil((y+2)/20);

        //All walls collision
        if (app.existence[yTile][xTile+1] == 'X' || app.existence[yTile][xTile+1] == 'B') {
            if (app.existence[yTile][xTile-1] == 'X' || app.existence[yTile][xTile-1] == 'B') {
                if (app.existence[yTile-1][xTile] == 'X' || app.existence[yTile-1][xTile] == 'B') {
                    if (app.existence[yTile+1][xTile] == 'X' || app.existence[yTile+1][xTile] == 'B') {
                        this.freeze = true;
                        this.direction = "freeze";
                    } else {
                        this.freeze = false;
                    }
                } else {
                    this.freeze = false;
                }
            } else {
                this.freeze = false;
            }
        } else {
            this.freeze = false;
        }

        //Not jailed in four bricks
        if (!this.freeze) {
            if (direction == "right") {
                this.x += speed;
                if (x % 20 == 0) {
                    if (app.existence[yTile][xTile+1] == 'X' || app.existence[yTile][xTile+1] == 'B') {
                        this.detect(app);
                    }
                }
            } else if (direction == "left") {
                this.x -= speed;
                if (x % 20 == 0) {
                    if (app.existence[yTile][xTile-1] == 'X' || app.existence[yTile][xTile-1] == 'B') {
                        this.detect(app);
                    }
                }
            } else if (direction == "up") {
                this.y -= speed;
                if (y % 20 == 0) {
                    if (app.existence[yTile-1][xTile] == 'X' || app.existence[yTile-1][xTile] == 'B') {
                        this.detect(app);
                    }
                }
            } else if (direction == "down") {
                this.y += speed;
                if (y % 20 == 0) {
                    if (app.existence[yTile+1][xTile] == 'X' || app.existence[yTile+1][xTile] == 'B') {
                        this.detect(app);
                    }
                }
            }
        }
    }

    /**
     * Defeated by fireball or firestorm and change available location
     * @param app
     */
    public void respawn(App app) {
        if (app.levelNum != 10) {
            //Create temp arrays for shuffle available coordinates
            ArrayList<Integer> xRandom = new ArrayList<Integer>();
            ArrayList<Integer> yRandom = new ArrayList<Integer>();
            for(int i = 1; i < 35; i++) {
                if (i < (app.player.getXTile() - 10) || i > (app.player.getXTile() + 10)) {
                    xRandom.add(i);
                }
            }
            for(int i = 1; i < 32; i++) {
                if (i < (app.player.getYTile() - 10) || i > (app.player.getYTile() + 10)) {
                    yRandom.add(i);
                }
            }
            //Randomise
            Collections.shuffle(xRandom);
            Collections.shuffle(yRandom);
            for(int i = 0; i < xRandom.size() && xRandom.get(i) != null; i++) {
                for(int j = 0; j < yRandom.size() && yRandom.get(j) != null; j++) {
                    if (app.existence[yRandom.get(j)][xRandom.get(i)] != 'X' && app.existence[yRandom.get(j)][xRandom.get(i)] != 'B'){
                        this.x = xRandom.get(i)*20;
                        this.y = yRandom.get(j)*20;
                        this.detect(app);
                        break;
                    }
                }
            }
        } else {
            this.x = 17*20;
            this.y = 30*20;
            this.direction = "up";
            //this.detect(app);
        }

    }

    /**
     * Collide and cause player losing one life
     * @param app
     */
    public void kill(App app) {
        if (Math.abs(this.y - app.player.getY()) < 20 && Math.abs(this.x - app.player.getX()) < 20) {
            app.lifeLost();
        }
    }

    /**
     * Draw Monster and update its direction according to tile condition
     * @param app
     */
    public void draw(App app) {
        if (this.freeze) {
            //Update direction for slime projectile creation
            if (app.existence[yTile][xTile+1] != 'X' && app.existence[yTile][xTile+1] != 'B') {
                this.direction = "right";
            } else if (app.existence[yTile][xTile-1] != 'X' && app.existence[yTile][xTile-1] != 'B') {
                this.direction = "left";
            } else if (app.existence[yTile-1][xTile] != 'X' && app.existence[yTile-1][xTile] != 'B') {
                this.direction = "up";
            } else if (app.existence[yTile+1][xTile] != 'X' && app.existence[yTile+1][xTile] != 'B') {
                this.direction = "down";
            }
        }
        app.image(monsterSprite, x, y);
    }
}
