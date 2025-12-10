package gremlins;

import processing.core.PImage;

import java.util.ArrayList;

/**
 * The final enemy type in level 10 represented by 'F'
 * Phase 1 cannot move. Generate laser mutations every 8s.
 * Life hurt by Firestorm: 25s. Clear all mutations after down.
 * Phase 2 Create 8 laser to block the exit. Self move horizontally and shoot laser lasting(cd 3s).
 * Life hurt by Firelaser: 15s. Clear all everything after down.
 * Phase 3 Rush toward player location in 2s. Wait 1s after each stop.
 * Life hurt by Fireball&Firestorm: 50s. Exit appear.
 */
public class MutationBoss {
    public int speed = 1;
    public int hitCounter = 0; //Animation frame
    public int lifeCounter = 0; //Frames hit by player
    private int state = 0;
    public int phase = 1;
    public int creatorCounter = 0; //CD to create laser mutations in phase 1
    private Integer[] creatorCol = {1, 1, 34, 34, 2, 2, 33, 33, 3, 3, 32, 32, 4, 4, 31, 31, 5, 5, 30, 30};
    private Integer[] creatorRow = {1, 31, 1, 31, 1, 31, 1, 31, 1, 31, 1, 31, 1, 31, 1, 31, 1, 31, 1, 31};
    private int creatorIdx = 0;
    public int laserCounter = 300; //CD to shoot laser in phase 2
    public int moveCounter = 0; //CD to adjust position in phase 2
    private boolean forward = true; //Phase 2 move forward once
    private boolean moveFinish = true; //Requirment to shoot laser
    public int rushCounter = 0; //CD to rush against player in phase 3
    public int velCounter = 0; //CD to rush against player in phase 3
    private boolean rushed = false;
    private boolean rushing = false;
    private double velX = 1;
    private double velY = 1;
    private int locX; //Recorded location of player
    private int locY;
    private int x;
    private int xTile;
    private int y;
    private int yTile;
    private int direction; //left or right in phase 2
    public boolean hitting; //Is being hit
    public boolean exist = true;
    private boolean animated = false;
    public int spriteCounter;
    public PImage sprite;
    public PImage sprite_hit;


    /**
     * Mutation constructor which initializes location, image and a random waking time
     * @param app
     * @param columes
     * @param rows
     */
    public MutationBoss(App app, int columes, int rows) {
        this.x = columes * 20;
        this.y = rows * 20;
        this.xTile = columes;
        this.yTile = rows;
        this.sprite = app.gremlinG;
        this.sprite_hit = app.gremlinG_hit;
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
     * Be damaged
     */
    public void damage() {
        this.hitting = true;
        this.lifeCounter += 1;
        this.spriteCounter += 1;
        if (spriteCounter > 4) {
            this.hitting = false;
            spriteCounter = 0;
        }
    }

    /**
     * Turn off hit sprite
     */
    public void reset() {
        this.hitting = false;
    }

    /**
     * Phase 1 skill: Laser mutation creator
     */
    public void creator(App app) {
        creatorCounter += 1;
        if (creatorCounter >= 60*8 && creatorIdx < creatorCol.length) {
            app.mutations2[0+creatorIdx] = new Mutation2(app, creatorCol[creatorIdx], creatorRow[creatorIdx], app.gremlinL);
            creatorCounter = 0;
            creatorIdx += 1;
        }
    }

    /**
     * Phase 2 skill: Upward Laser shooting
     */
    public void laser(App app) {
        laserCounter += 1;
        if (laserCounter >= 60*3 && moveFinish) {
            app.slimelasers.add(new Slimelaser(app, app.mutationBoss.getX(), app.mutationBoss.getY(), 0));
            laserCounter = 0;
        }
    }
    public void move(App app) {
        if (forward) {
            this.y -= 1;
            if (this.y == 29*20) {
                moveFinish = true;
                forward = false;
            }
        } else {
            moveCounter += 1;
            if (moveCounter >= 60 * 3) {
                if (this.x > app.player.getX()) { //Player left to boss
                    int desX = app.player.getX() - 20;
                    if (this.x > desX) {
                        this.x -= speed;
                    } else {
                        moveCounter = 0;
                    }
                } else if (this.x < app.player.getX()) {
                    int desX = app.player.getX() + 20;
                    if (this.x < desX) {
                        this.x += speed;
                    } else {
                        moveCounter = 0;
                    }
                }
            }
        }
    }

    /**
     * Phase 3 skill: Rush against player diagonally
     */
    public void rush(App app) {
        rushCounter += 1;
        if (rushCounter >= 60) {
            rushing(app); //Update velocity
            this.x += velX;
            this.y += velY;
        }
    }
    public void locate(App app) { //Record player location and initialize velocity
        locX = app.player.getX();
        locY = app.player.getY();
        velX = (locX-this.x)/5;
        velY = (locY-this.y)/5;
    }
    public void rushing(App app) {
        velCounter += 1;
        if (velCounter > 2*60) { //Accelerate every 0.5s
            if (velX > 5 || velX < -5) {} else {velX = velX * 1.01;}
            if (velY > 5 || velY < -5) {} else {velY = velY * 1.01;}
        }
        if (Math.abs(this.x-locX) < 20 && Math.abs(this.y-locY) < 20) {
            locate(app);
            rushCounter = 0;
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
     * Control phase change and counter sprite
     * @param app
     */
    public void draw(App app) {
        kill(app);
        if (hitting) {
            app.image(this.sprite_hit, x, y);
        } else {
            app.image(this.sprite, x, y);
        }
        if (this.phase == 1) {
            creator(app); //Phase 1 skill
            float bar = (float) (1.0-((float) (lifeCounter)/ (float)(25*60)));
            app.fill(255, 255, 255); //White bar
            app.rect(20,645, bar*680,10 );
            if (lifeCounter > 25*60) { //Change phase -> clear all mutations -> Create two lasers at exit row
                app.monsters = new Monster[33*36];
                app.mutations = new Mutation[33*36];
                app.mutations2 = new Mutation2[33*36];
                app.slimes = new ArrayList<Slime>();
                app.slimelasers = new ArrayList<Slimelaser>();
                this.sprite = app.gremlinG2; //Update sprite
                this.sprite_hit = app.gremlinG2_hit;
                this.lifeCounter = 0; //Reset lifeCounter
                this.hitting = false;
                app.mutations2[0] = new Mutation2(app, 1, 1, app.gremlinL);
                app.mutations2[1] = new Mutation2(app, 1, 31, app.gremlinL);
                app.mutations2[2] = new Mutation2(app, 34, 1, app.gremlinL);
                app.mutations2[3] = new Mutation2(app, 34, 31, app.gremlinL);
                app.mutations2[4] = new Mutation2(app, 3, 3, app.gremlinL);
                app.mutations2[5] = new Mutation2(app, 3, 29, app.gremlinL);
                app.mutations2[6] = new Mutation2(app, 32, 3, app.gremlinL);
                app.mutations2[7] = new Mutation2(app, 32, 29, app.gremlinL);
                app.bgm("src\\main\\resources\\gremlins\\devilSE.wav", "");
                phase += 1;
            }
        } else if (this.phase == 2) {
            move(app); //Adjust position
            laser(app); //Shoot laser
            float bar = (float) (1.0-((float) (lifeCounter)/ (float)(15*60)));
            app.fill(255, 255, 0); //Yellow bar
            app.rect(20,645, bar*680,10 );
            if (lifeCounter > 15*60) {
                this.sprite = app.gremlinG3;
                this.sprite_hit = app.gremlinG3_hit;
                this.lifeCounter = 0; //Reset lifeCounter
                app.mutations2 = new Mutation2[33*36];
                app.slimelasers = new ArrayList<Slimelaser>();
                this.hitting = false;
                //Remove exit
                app.exit.x = -20;
                app.exit.y = -20;
                //Remove all edge stones
                for (int b = 0; b < app.bricks.length && app.bricks[b] != null; b++) {
                    app.existence[app.bricks[b].yTile][app.bricks[b].xTile] = ' ';
                }
                app.bricks = new Brick[33*36];
                //Clear all edge stones and remove all middle stones
                for (int s = 0; s < app.stones.length && app.stones[s] != null; s++) {
                    if (app.stones[s].xTile == 0 || app.stones[s].xTile == 35 || app.stones[s].yTile == 0 || app.stones[s].yTile == 32) {
                        app.stones[s].stoneSprite = app.empty;
                    } else {
                        app.stones[s].stoneSprite = app.empty;
                        app.existence[app.stones[s].yTile][app.stones[s].xTile] = ' ';
                    }

                }
                locate(app);
                app.bgm("src\\main\\resources\\gremlins\\darknessSE.wav", "");
                phase += 1;
            }
        } else if (this.phase == 3) {
            rush(app);
            float bar = (float) (1.0-((float) (lifeCounter)/ (float)(50*60)));
            app.fill(148, 0, 211); //Purple bar
            app.rect(20,645, bar*680,10 );
            if (lifeCounter > 50*60) {
                for (int i = 0; i < app.BGMs.size(); i++) {
                    app.BGMs.get(i).stop();
                }
                this.lifeCounter = 0; //Reset lifeCounter
                this.hitting = false;
                app.bgm("src\\main\\resources\\gremlins\\endSE.wav", "");
                phase += 1;
            }
        } else {
            app.exit.x = 17*20;
            app.exit.y = 31*20;
            this.exist = false;
            this.state += 1;
            if (state < 5) {
                app.image(app.gremlinG3B0, x, y);
            } else if (state < 10) {
                app.image(app.gremlinG3B1, x, y);
            } else if (state < 15) {
                app.image(app.gremlinG3B2, x, y);
            } else if (state < 20) {
                app.image(app.gremlinG3B3, x, y);
            } else if (state < 25) {
                app.image(app.white, 0, 0);
            } else if (state < 30) {
                app.image(app.black, 0, 0);
            } else if (state < 35) {
                app.image(app.white, 0, 0);
            } else {
                this.x = -20;
                this.y = -20;
                app.image(app.empty, -20, -20);
            }
        }
    }
}
