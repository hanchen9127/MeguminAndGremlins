package gremlins;

import processing.core.PImage;

/**
 * The special enemy type called Mutation2.
 * Numerous Mutation2 could appear in one level represented by "Z" in map config.
 * Shoot lazer in four directions in clockwise (0,1,2,3)*90 degrees.
 * Cannot move
 */
public class Mutation2 {
    private int state = 0; //Animation frame
    private int x;
    private int xTile;
    private int y;
    private int yTile;
    private int direction;
    public int changer = 0; //Count and change direction
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
    public Mutation2(App app, int columes, int rows, PImage mutationSprite) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.mutationSprite = mutationSprite;
        this.direction = app.randomGenerator.nextInt(4 - 0) + 0;
    }

    /**
     * x getter of Mutation2
     * @return Mutation2.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * y getter of Mutation2
     * @return Mutation2.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Direction getter of Mutation2
     * @return Mutation2.direction
     */
    public int getDirection() {
        return this.direction;
    }

    /**
     * Animated getter of Mutation2
     * @return Mutation2.animated
     */
    public boolean getAnimated() {
        return this.animated;
    }

    /**
     * Animated setter of Mutation2
     */
    public void setAnimated() {
        this.animated = true;
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
        if (app.levelNum == 10) {
            if (app.mutationBoss.phase == 2) { //Would no longer be killed
                app.image(app.empty, x, y);
                this.changer += 1;
                if (changer > 0) {
                    direction += 1;
                    changer = 0;
                }
            } else {
                if (!animated && exist) {
                    app.image(app.gremlinL, x, y);
                    this.changer += 1;
                    if (changer > app.cdSlime*60*2) {
                        direction += 1;
                        changer = 0;
                    }
                } else {
                    this.state += 1;
                    if (state < 2) {
                        app.bgm("src\\main\\resources\\gremlins\\splitSE.wav", "");
                        app.image(app.gremlinLT0, x, y);
                    } else if (state < 5) {
                        app.image(app.gremlinLT0, x, y);
                    } else if (state < 10) {
                        app.image(app.gremlinLT1, x, y);
                    } else {
                        app.image(app.empty, x, y);
                    }
                }
            }
        } else {
            if (!animated && exist) {
                app.image(app.gremlinL, x, y);
                this.changer += 1;
                if (changer > app.cdSlime*60*2) {
                    direction += 1;
                    changer = 0;
                }
            } else {
                this.state += 1;
                if (state < 2) {
                    app.bgm("src\\main\\resources\\gremlins\\splitSE.wav", "");
                    app.image(app.gremlinLT0, x, y);
                } else if (state < 5) {
                    app.image(app.gremlinLT0, x, y);
                } else if (state < 10) {
                    app.image(app.gremlinLT1, x, y);
                } else {
                    app.image(app.empty, x, y);
                }
            }
        }

    }
}
