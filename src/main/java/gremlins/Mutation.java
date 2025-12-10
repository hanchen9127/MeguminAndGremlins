package gremlins;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The special enemy type called Mutation as an extention.
 * Numerous Mutation could appear in one level represented by "M" in map config.
 * Sleep for a random duration and move in a specific logic after waking up.
 * On its path chasing after the wizard, Brick is destroyed and Stone is ignored.
 */
public class Mutation {
    private int speed = 1;
    public int sleep = 0; //When count to wake, would chase after player
    private int state = 0; //Animation frame
    private int x;
    private int xTile;
    private int y;
    private int yTile;
    public int wake; //Time between 10 and 20 seconds
    private String direction;
    public boolean exist = true;
    private boolean animated = false;
    private PImage mutationSprite;

    /**
     * Mutation constructor which initializes location, image and a random waking time
     * @param app
     * @param columes
     * @param rows
     * @param mutationSprite
     */
    public Mutation(App app, int columes, int rows, PImage mutationSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.mutationSprite = mutationSprite;
        this.wake = app.randomGenerator.nextInt(21 - 10) + 10;
        //System.out.println(this.wake); //Check randomed sleep time
    }

    /**
     * x getter of Mutation
     * @return Mutation.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Mutation
     * @return Mutation.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Direction getter of Mutation
     * @return Mutation.directiom
     */
    public String getDirection() {
        return this.direction;
    }

    /**
     * Animated getter of Mutation
     * @return Mutation.animated
     */
    public boolean getAnimated() {
        return this.animated;
    }

    /**
     * x setter of Mutation
     */
    public void setX(int distance) {
        this.x += distance;
    }

    /**
     * y setter of Mutation
     */
    public void setY(int distance) {
        this.y += distance;
    }

    /**
     * Animated setter of Mutation
     */
    public void setAnimated() {
        this.animated = true;
    }

    /**
     * Wizard location detector
     */
    public void detect(App app) {
        this.xTile = (int) Math.floor((this.x+2)/20);
        this.yTile = (int) Math.ceil((this.y+2)/20);
        if (this.x > app.player.getX()) {
            this.direction = "left";
        } else if (this.x < app.player.getX()) {
            this.direction = "right";
        }
        if (this.y > app.player.getY()) {
            this.direction = "up";
        } else if (this.y < app.player.getY()) {
            this.direction = "down";
        }
    }

    /**
     * Logic of movement
     */
    public void move(App app) {
        if (sleep > wake*60 && !animated && exist) {
            destroy(app);
            if (this.x % 20 == 0 || this.y % 20 == 0) {
                this.detect(app);
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
     * Collision with Brick
     * Destroy Brick
     * @param app
     */
    public void destroy(App app) {
        if (!animated && exist) {
            //Loop the object array and change the property
            for(int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                if (Math.abs(this.y - app.bricks[b].getY()) < 20 && Math.abs(this.x - app.bricks[b].getX()) < 20) {
                    app.existence[app.bricks[b].getY()/20][app.bricks[b].getX()/20] = ' ';//Update array
                    app.bricks[b].exist = false;
                    app.bricks[b].setAnimated();
                }
            }
        }
    }

    /**
     * Collide and cause player losing one life
     */
    public void kill(App app) {
        if (this.exist && Math.abs(this.y - app.player.getY()) < 20 && Math.abs(this.x - app.player.getX()) < 20) {
            app.lifeLost();
        }
    }

    /**
     * Draw Mutation and update its image depends on conditions
     * @param app
     */
    public void draw(App app) {
        if (!animated && exist) {
            this.sleep += 1;
            if (sleep < wake*60) {
                app.image(app.gremlinS1, x, y); //Sleep sprite
            } else {
                app.image(mutationSprite, x, y); //Open eye sprite
            }
        } else {
            this.state += 1;
            if (state < 2) {
                app.bgm("src\\main\\resources\\gremlins\\burnSE.wav", "");
                app.image(app.gremlinSB0, x, y);
            } else if (state < 5) {
                app.image(app.gremlinSB0, x, y);
            } else if (state < 10) {
                app.image(app.gremlinSB1, x, y);
            } else {
                app.image(app.empty, x, y);
            }
        }
    }
}
