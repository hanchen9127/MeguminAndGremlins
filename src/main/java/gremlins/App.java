package gremlins;

import java.io.File;
import java.net.URI;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.data.JSONObject;

import java.net.URI;
import java.nio.file.Paths;
import java.util.Random;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;

/**
 * The integrated process of all classes in the project.
 */
public class App extends PApplet {
    //Private static or non-static value variables in App
    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;
    private static final int FPS = 60;
    public static final Random randomGenerator = new Random();
    public int levelNum = 1; //Index of levels
    public int levelTotal;
    public int levelTimer; //Record in frame
    public int lives;
    private int stormState = 0;
    private int lazerState = 0;
    private double rechargeFire = 0;
    private double cdFire; //Cooldown of fireballs in config
    private double rechargeSlime = 0;
    private double rechargeSlimelaser = 0;
    public double cdSlime; //Cooldown of slimes in config
    private String configPath;
    public String winText;
    public String loseText;

    //Public object or array variables in App
    public PImage empty; //Void
    public PImage wizard; //Player
    public PImage wizardP; //Poweruped player
    public PImage wizardD; //Player down
    public PImage potion; //Powerup Firestorm
    public PImage storm0; //Powerup skill
    public PImage storm1; //Powerup skill next frame
    public PImage storm2; //Powerup skill next frame
    public PImage storm3; //Powerup skill next frame
    public PImage potion2; //Fireball upgrade to Laser
    public PImage potion3; //Heal status
    public PImage fire; //Fireball
    public PImage brickwall; //Usual breakable wall
    public PImage brickD0; //Destroyed wall
    public PImage brickD1; //Destroyed wall next frame
    public PImage brickD2; //Destroyed wall next frame
    public PImage brickD3; //Destroyed wall next frame
    public PImage stonewall; //Unbreakable wall
    public PImage gremlin; //Usual enemy
    public PImage green; //Slime shot by usual enemy
    public PImage gremlinS; //Extension enemy
    public PImage gremlinS1; //Extension enemy sleep sprite
    public PImage gremlinSB0; //Extension enemy death frame
    public PImage gremlinSB1; //Extension enemy death next frame
    public PImage gremlinL; //Extra enemy
    public PImage gremlinLT0; //Extra enemy death frame
    public PImage gremlinLT1; //Extra enemy death next frame
    public PImage gremlinG; //Final boss phase 1
    public PImage gremlinG_hit;
    public PImage gremlinG2; //Final boss phase 2
    public PImage gremlinG2_hit;
    public PImage gremlinG3; //Final boss phase 3
    public PImage gremlinG3_hit;
    public PImage gremlinG3B0; //Death
    public PImage gremlinG3B1; //Death
    public PImage gremlinG3B2; //Death
    public PImage gremlinG3B3; //Death
    public PImage door;
    public PImage life1; //Face status
    public PImage life2;
    public PImage life3;
    public PImage life4;
    public PImage life5;
    public PImage cave; //Background picture
    public PImage forest;
    public PImage dark;
    public PImage sacred;
    public PImage winner;
    public PImage loser;
    public PImage blueUp; //Lazer types
    public PImage blueRight;
    public PImage blueDown;
    public PImage blueLeft;
    public PImage redUp;
    public PImage redRight;
    public PImage redDown;
    public PImage redLeft;
    public PImage yellowUp;
    public PImage yellowRight;
    public PImage yellowDown;
    public PImage yellowLeft;
    public PImage white;
    public PImage black;
    public PImage grey;

    //Config read(Once creation)
    public Level level;
    public ArrayList<Level> levels = new ArrayList<Level>();
    //Map objects(Multiple creation each levelLoad)
    public char[][] existence = new char[33][36]; //Map initialization
    public Player player;
    public Powerup powerup;
    public int xPowerup; //If defined
    public int yPowerup;
    public Powerup2 powerup2;
    public int xPowerup2;
    public int yPowerup2;
    public Powerup3 powerup3;
    public int xPowerup3;
    public int yPowerup3;
    public Fireball fireball;
    public ArrayList<Fireball> fireballs = new ArrayList<Fireball>();
    public Slime slime;
    public ArrayList<Slime> slimes = new ArrayList<Slime>();
    public Exit exit;
    public Stone stone;
    public Stone[] stones = new Stone[33*36];
    public Brick brick;
    public Brick[] bricks = new Brick[33*36];
    public Monster monster;
    public Monster[] monsters = new Monster[33*36];
    public Mutation mutation;
    public Mutation[] mutations = new Mutation[33*36];
    public Mutation2 mutation2;
    public Mutation2[] mutations2 = new Mutation2[33*36];
    public MutationBoss mutationBoss;
    public ArrayList<MutationBoss> BossPhase = new ArrayList<MutationBoss>();
    public Firestorm firestorm;
    public ArrayList<Firestorm> firestorms = new ArrayList<Firestorm>();
    public Firelaser firelaser;
    public ArrayList<Firelaser> firelasers = new ArrayList<Firelaser>();
    public Slimelaser slimelaser;
    public ArrayList<Slimelaser> slimelasers = new ArrayList<Slimelaser>();
    public ArrayList<Clip> BGMs = new ArrayList<Clip>();

    //Boolean variables which work as flags or switches in App
    public boolean newPowerup = true;
    public boolean newPowerup2 = false;
    public boolean newPowerup3 = false;
    public boolean definePowerup = false;
    public boolean fKey = false; //Space pressed
    public boolean fireReady = true; //Cooldown to 0
    public boolean fired = false; //Start cooldown
    public boolean rKey = false; //Right pressed
    public boolean lKey = false; //Left pressed
    public boolean uKey = false; //Up pressed
    public boolean dKey = false; //Down pressed
    public LinkedList<String> keyOrder = new LinkedList<>();
    //Essential flags
    public boolean win = false; //Down pressed
    public boolean lose = false; //Down pressed
    public boolean dead = false; //Lose 1 life
    public boolean turn = true; //Sound control


    public App() {
        this.configPath = "src/config.json";
    }

    /**
     * Initialize window size
     */
    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Initialize config levels
     */
    @Override
    public void setup() {
        frameRate(FPS);
        configLoading(); //Load config
        imageLoading(); //Load PImages
        levelLoading(); //Load txt into arrays
    }

    /**
     * Draw the screen every frame
     */
    @Override
    public void draw() {
        levelTimer += 1;
        if (win) {
            if (turn) {
                for (Clip bgm : BGMs) {
                    bgm.stop();
                }
                bgm("src\\main\\resources\\gremlins\\winBGM.wav", "loop");
                turn = false;
            }

            this.image(winner, 0, 0);
            textSize(40);
            this.fill(255, 255, 255); //White
            winText = "END";
            text(winText, 320, 320);
            if (levelTimer > 120) {
                textSize(20);
                text("Press any key to restart", 245, 500);
            }
        } else if (lose) {
            if (turn) {
                for (int i = 0; i < BGMs.size(); i++) {
                    BGMs.get(i).stop();
                }
                bgm("src\\main\\resources\\gremlins\\loseBGM.wav", "");
                turn = false;
            }
            this.image(loser, 0, 0);
            textSize(40);
            this.fill(255, 255, 255); //White
            //loseText = "GAME OVER!";
            //text(loseText, 255, 340);
            if (levelTimer > 120) {
                textSize(20);
                text("Press any key to restart", 245, 500);
            }
        } else {
            //Background system control
            //background(191, 153, 114); //Beige-colour
            if (this.levelNum == 9) {
                this.image(sacred, 0, 0);
            } else if (this.levelNum > 9) {
                this.image(dark, 0, 0);
            } else if (this.levelNum > 5){
                this.image(cave, 0, 0);
            } else {
                this.image(forest, 0, 0);
            }
            if (levelNum == 10) {
                if (mutationBoss.phase == 3) {
                    this.image(grey, 0, 0);
                }
            }

            //Firestorm system control
            if (levelNum == 10) {
                newPowerup = false;
                this.powerup = new Powerup(this, -1, -1, this.empty);
                powerup.effect = true;
            }
            if (!newPowerup && powerup.getEffect()) {
                stormState += 1;
                if (stormState < 10) {
                    firestorms.clear(); //Only one firestorm around player should exist
                    this.firestorm = new Firestorm(this, player.getX(), player.getY(), this.storm3);
                    firestorms.add(firestorm);
                } else if (stormState < 20) {
                    firestorms.clear();
                    this.firestorm = new Firestorm(this, player.getX(), player.getY(), this.storm2);
                    firestorms.add(firestorm);
                } else if (stormState < 30) {
                    firestorms.clear();
                    this.firestorm = new Firestorm(this, player.getX(), player.getY(), this.storm1);
                    firestorms.add(firestorm);
                } else if (stormState < 40) {
                    firestorms.clear();
                    this.firestorm = new Firestorm(this, player.getX(), player.getY(), this.storm0);
                    firestorms.add(firestorm);
                } else {
                    stormState = 0; //Reset frame
                }
            }
            for (int h = 0; h < firestorms.size() && firestorms.get(h) != null; h++) {
                firestorms.get(0).draw(this);
                firestorms.get(0).hitB(this);
                firestorms.get(0).hitM(this);
                firestorms.get(0).hitS(this);
                firestorms.get(0).hitG(this);
            }

            exit.draw(this);

            //Shooting system control
            if (fKey && fireReady) {
                if (levelNum == 10) {
                    newPowerup2 = true;
                    this.powerup2 = new Powerup2(this, -1, -1, this.empty);
                    powerup2.effect = true;
                    powerup2.draw(this);
                    firelasers.clear();
                    this.firelaser = new Firelaser(this, player.getX(), player.getY(), player.shoot);
                    firelasers.add(firelaser);
                    lazerState = 0;
                    this.fireball = new Fireball(this, player.getX(), player.getY(), player.shoot, this.fire);
                    fireballs.add(fireball);
                } else {
                    if (newPowerup2) {
                        if (powerup2.getEffect()) {
                            firelasers.clear();
                            this.firelaser = new Firelaser(this, player.getX(), player.getY(), player.shoot);
                            firelasers.add(firelaser);
                            lazerState = 0;
                        } else {
                            this.fireball = new Fireball(this, player.getX(), player.getY(), player.shoot, this.fire);
                            fireballs.add(fireball);
                        }
                    } else {
                        this.fireball = new Fireball(this, player.getX(), player.getY(), player.shoot, this.fire);
                        fireballs.add(fireball);
                    }
                }
                fireReady = false;
                fired = true;
            }
            for (int f = 0; f < fireballs.size() && fireballs.get(f) != null; f++) {
                fireballs.get(f).draw(this);
                fireballs.get(f).move(this);
                fireballs.get(f).hitB(this);
                fireballs.get(f).hitM(this);
                fireballs.get(f).hitS(this);
                fireballs.get(f).hitG(this);
                fireballs.get(f).hitX(this);
            }

            if (fired) {
                if (levelNum != 10) { //Show recharge bar
                    float percentage = (float) (rechargeFire / cdFire);
                    this.fill(255, 255, 255); //White
                    this.rect(620, 680, 80, 5); //X,Y,width,height
                    this.fill(0, 0, 0); //Black
                    this.rect(620, 680, percentage * 80, 5);
                }
                if (cdFire <= rechargeFire) {
                    fireReady = true;
                    fired = false;
                    rechargeFire = 0;
                } else {
                    rechargeFire += 0.01667;
                }
            }

            for (int M = 0; M < mutations.length && mutations[M] != null; M++) {
                mutations[M].draw(this);
                mutations[M].move(this);
                mutations[M].kill(this);
            }
            for (int s = 0; s < stones.length && stones[s] != null; s++) {
                stones[s].draw(this);
            }

            for (int b = 0; b < bricks.length && bricks[b] != null; b++) {
                bricks[b].draw(this);
            }

            //Powerup generation control
            if (newPowerup && levelTimer >= 300) { //First portion appear after 5 seconds
                if (definePowerup) { //Define location in txt
                    this.powerup = new Powerup(this, xPowerup, yPowerup, this.potion);
                    newPowerup = false;
                } else { //Random generation
                    ArrayList<Integer> xTemp = new ArrayList<Integer>();
                    ArrayList<Integer> yTemp = new ArrayList<Integer>();
                    xTemp.clear();
                    yTemp.clear();
                    for (int i = 0; i < 100; i++) {
                        xTemp.add(randomGenerator.nextInt(36 - 1) + 1);
                        yTemp.add(randomGenerator.nextInt(33 - 1) + 1);
                    }
                    for (int i = 0; i < xTemp.size() && xTemp.get(i) != null; i++) {
                        for (int j = 0; j < yTemp.size() && yTemp.get(j) != null; j++) {
                            if (existence[yTemp.get(j)][xTemp.get(i)] != 'B' && existence[yTemp.get(j)][xTemp.get(i)] != 'X') {
                                this.powerup = new Powerup(this, xTemp.get(i), yTemp.get(j), this.potion);
                                newPowerup = false;
                                break;
                            }
                        }
                    }
                }
            } else if (!newPowerup) { //The potion has been created
                powerup.collected(this);
                powerup.draw(this);
            }
            if (newPowerup2) {
                powerup2.collected(this);
                powerup2.draw(this);
            }
            if (newPowerup3) {
                powerup3.collected(this);
                powerup3.draw(this);
            }
            //Enemies system control
            for (int l = 0; l < slimes.size() && slimes.get(l) != null; l++) {
                slimes.get(l).block(this);
                slimes.get(l).draw(this);
                slimes.get(l).move(this);
                slimes.get(l).kill(this);
            }
            for (int m = 0; m < monsters.length && monsters[m] != null; m++) {
                monsters[m].born(this);
                monsters[m].draw(this);
                monsters[m].move(this);
                monsters[m].kill(this);
            }

            if (cdSlime <= rechargeSlime) {
                rechargeSlime = 0;
                for (int m = 0; m < monsters.length && monsters[m] != null; m++) {
                    this.slime = new Slime(this, monsters[m].getX(), monsters[m].getY(), monsters[m].getDirection(), this.green);
                    slimes.add(slime);
                }
            } else {
                rechargeSlime += 0.01667;
            }

            //Firelaser system control
            if (newPowerup2) {
                if (powerup2.getEffect()) {
                    lazerState += 1;
                    if (levelNum == 10) { //Last for 2s
                        if (lazerState < 2*60) {
                            for (int h = 0; h < firelasers.size() && firelasers.get(h) != null; h++) {
                                firelasers.get(0).draw(this);
                                firelasers.get(0).destroy(this);
                                firelasers.get(0).penetrate(this);
                            }
                        } else {
                            firelasers.clear();
                            lazerState = 0;
                        }
                    } else { //Last for half of cooldown
                        if (lazerState < cdFire*60/2) {
                            for (int h = 0; h < firelasers.size() && firelasers.get(h) != null; h++) {
                                firelasers.get(0).draw(this);
                                firelasers.get(0).destroy(this);
                                firelasers.get(0).penetrate(this);
                            }
                        } else {
                            firelasers.clear();
                            lazerState = 0;
                        }
                    }
                }
            }

            //Lazer Enemies system control
            for (int l = 0; l < slimelasers.size() && slimelasers.get(l) != null; l++) {
                if (slimelasers.get(l).exist) {
                    slimelasers.get(l).draw(this);
                    slimelasers.get(l).kill(this);
                }
            }
            for (int z = 0; z < mutations2.length && mutations2[z] != null; z++) {
                mutations2[z].draw(this);
                mutations2[z].kill(this);
            }
            if (cdSlime*2 <= rechargeSlimelaser) {
                rechargeSlimelaser = 0;
                for (int z = 0; z < mutations2.length && mutations2[z] != null; z++) {
                    if (mutations2[z].exist) {
                        this.slimelaser = new Slimelaser(this, mutations2[z].getX(), mutations2[z].getY(), mutations2[z].getDirection());
                        slimelasers.add(slimelaser);
                    }
                }
            } else {
                rechargeSlimelaser += 0.01667;
            }

            for (int f = 0; f < BossPhase.size() && BossPhase.get(f) != null; f++) {
                BossPhase.get(f).draw(this);

            }

            //Player system control
            player.collides(this);
            player.move(this);
            player.enter(this);
            player.correction(this);
            player.draw(this);

            textSize(20);
            this.fill(255, 255, 255); //White
            text("STATUS", 10, 695);
            if (this.lives >= 5) { //Status face
                this.image(life5, 90, 660);
            } else if (this.lives == 4) {
                this.image(life4, 90, 660);
            } else if (this.lives == 3) {
                this.image(life3, 90, 660);
            } else if (this.lives == 2) {
                this.image(life2, 90, 660);
            } else if (this.lives <= 1) {
                this.image(life1, 90, 660);
            }
            //Show level text and remove .txt at the end
            if (levelNum - 1 < levelTotal) {
                text("LEVEL " + levelNum + "/" + levelTotal + "   " + levels.get(levelNum - 1).getLayout().substring(0, levels.get(levelNum - 1).getLayout().length() - 4), 170, 695);
            }
            //Remove non-exist objects from arrays
            emptyClear();

            if (player.lifelost) {
                this.image(wizardD,player.getX(),player.getY());
            }
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     * @param e The input key on keyboard
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (win || lose) {
            //Stop all BGMs
            if (levelTimer > 120) {
                for (int i = 0; i < BGMs.size(); i++) {
                    BGMs.get(i).stop();
                }
                mapClean();
                win = false;
                lose = false;
                turn = true;
                dead = false;
                this.levels = new ArrayList<Level>();
                levelNum = 1;
                configLoading();
                levelLoading();
            }
        } else {
            if (key == 76) { //L key go to next level
                levelNext();
            }
            if (key == 75) { //K key go to kill wizard
                if (levelNum == 10) {
                    this.mutationBoss.lifeCounter += 60;
                }
                lifeLost();
            }
            if (key == 32) {//Space to shoot fireball
                fKey = true;
            } else if (key == 37) { //left arrow
                keyOrder.remove("left");
                keyOrder.add("left");
                lKey = true;
            } else if (key == 39) { //right arrow
                keyOrder.remove("right");
                keyOrder.add("right");
                rKey = true;
            } else if (key == 38) { //up arrow
                keyOrder.remove("up");
                keyOrder.add("up");
                uKey = true;
            } else if (key == 40) { //down arrow
                keyOrder.remove("down");
                keyOrder.add("down");
                dKey = true;
            }
        }
    }

    /**
     * Receive key released signal from the keyboard.
     * @param e The input key on keyboard
     */
    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == 32) {//Space to shoot fireball
            fKey = false;
        } else if (key == 37) {
            keyOrder.remove("left");
            lKey = false;
        } else if (key == 38) {
            keyOrder.remove("up");
            uKey = false;
        } else if (key == 39) {
            keyOrder.remove("right");
            rKey = false;
        } else if (key == 40) {
            keyOrder.remove("down");
            dKey = false;
        }
    }


    /**
     *  Load config.json
     *  Create level objects and update related variables
     */
    public void configLoading(){
        JSONObject conf = loadJSONObject(new File(this.configPath));
        this.lives = (int) conf.get("lives");
        for(int i = 0; i < conf.getJSONArray("levels").size(); i += 1) {
            String layout = (String) conf.getJSONArray("levels").getJSONObject(i).get("layout");
            double enemy_cooldown = (double) conf.getJSONArray("levels").getJSONObject(i).get("enemy_cooldown");
            double wizard_cooldown = (double) conf.getJSONArray("levels").getJSONObject(i).get("wizard_cooldown");
            this.level = new Level(this, layout, enemy_cooldown, wizard_cooldown);
            levels.add(level);
        }
        this.levelTotal = levels.size();
        if (levelTotal == 0) {
            win = true; //Avoid crashing
        } else {
            if (levels.size() != 0) {
                cdFire = levels.get(0).getWizard_cooldown();
                cdSlime = levels.get(0).getEnemy_cooldown();
            }
        }
    }


    /**
     * Load images and assign to PImage variables
     */
    public void imageLoading(){
        this.wizard = loadImage("wizard1.png");
        this.wizardP = loadImage("wizardP.png");
        this.wizardD = loadImage("wizardD.png");
        this.stonewall = loadImage("stonewall.png");
        this.brickwall = loadImage("brickwall.png");
        this.gremlin = loadImage("gremlin.png");
        this.gremlinS = loadImage("gremlinS.png");
        this.gremlinS1 = loadImage("gremlinS_sleep.png");
        this.gremlinSB0 = loadImage("gremlinS_burned0.png");
        this.gremlinSB1 = loadImage("gremlinS_burned1.png");
        this.gremlinL = loadImage("gremlinL.png");
        this.gremlinLT0 = loadImage("gremlinL_split0.png");
        this.gremlinLT1 = loadImage("gremlinL_split1.png");
        this.gremlinG = loadImage("gremlinG.png");
        this.gremlinG_hit = loadImage("gremlinG_hit.png");
        this.gremlinG2 = loadImage("gremlinG2.png");
        this.gremlinG2_hit = loadImage("gremlinG2_hit.png");
        this.gremlinG3 = loadImage("gremlinG3.png");
        this.gremlinG3_hit = loadImage("gremlinG3_hit.png");
        this.gremlinG3B0 = loadImage("gremlinG3_burnt0.png");
        this.gremlinG3B1 = loadImage("gremlinG3_burnt1.png");
        this.gremlinG3B2 = loadImage("gremlinG3_burnt2.png");
        this.gremlinG3B3 = loadImage("gremlinG3_burnt3.png");
        this.door = loadImage("door.png");
        this.empty = loadImage("empty.png");
        this.fire = loadImage("fireball.png");
        this.brickD0 = loadImage("brickwall_destroyed0.png");
        this.brickD1 = loadImage("brickwall_destroyed1.png");
        this.brickD2 = loadImage("brickwall_destroyed2.png");
        this.brickD3 = loadImage("brickwall_destroyed3.png");
        this.green = loadImage("slime.png");
        this.potion = loadImage("potion.png");
        this.potion2 = loadImage("potion2.png");
        this.potion3 = loadImage("potion3.png");
        this.storm0 = loadImage("firestorm.png");
        this.storm1 = loadImage("firestorm1_90.png");
        this.storm2 = loadImage("firestorm2_180.png");
        this.storm3 = loadImage("firestorm3_270.png");
        this.life1 = loadImage("life1.png");
        this.life2 = loadImage("life2.png");
        this.life3 = loadImage("life3.png");
        this.life4 = loadImage("life4.png");
        this.life5 = loadImage("life5.png");
        this.cave = loadImage("cave.png");
        this.forest = loadImage("forest.png");
        this.dark = loadImage("dark.png");
        this.sacred = loadImage("sacred.png");
        this.winner = loadImage("winner.png");
        this.loser = loadImage("loser.png");
        this.blueUp = loadImage("laserB0.png");
        this.blueRight = loadImage("laserB90.png");
        this.blueDown = loadImage("laserB180.png");
        this.blueLeft = loadImage("laserB270.png");
        this.yellowUp = loadImage("laserY0.png");
        this.yellowRight = loadImage("laserY90.png");
        this.yellowDown = loadImage("laserY180.png");
        this.yellowLeft = loadImage("laserY270.png");
        this.redUp = loadImage("laserR0.png");
        this.redRight = loadImage("laserR90.png");
        this.redDown = loadImage("laserR180.png");
        this.redLeft = loadImage("laserR270.png");
        this.white = loadImage("white.png");
        this.black = loadImage("black.png");
        this.grey = loadImage("grey.png");
    }

    /**
     * Load specific level and assign to the existence array
     * Create all included objected
     */
    public void levelLoading(){
        try (BufferedReader br = new BufferedReader(new FileReader("src/"+levels.get(this.levelNum-1).getLayout()))) {
            cdFire = levels.get(this.levelNum-1).getWizard_cooldown(); //Update cooldowns
            cdSlime = levels.get(this.levelNum-1).getEnemy_cooldown();
            String line;
            int rows = 0;
            int columes = 0;
            int s = 0;
            int b = 0;
            int m = 0;
            int M = 0;
            int Z = 0;
            while ((line = br.readLine()) != null && rows < 34) {
                for (char ch: line.toCharArray()) {
                    if (ch == 'X') {
                        this.stone = new Stone(this, columes, rows, this.stonewall);
                        stones[s] = this.stone;
                        existence[rows][columes] = 'X';
                        s += 1;
                    } else if (ch == 'B') {
                        this.brick = new Brick(this, columes, rows, this.brickwall);
                        bricks[b] = this.brick;
                        existence[rows][columes] = 'B';
                        b += 1;
                    } else if (ch == 'G') {
                        this.monster = new Monster(this, columes, rows, this.gremlin);
                        monsters[m] = this.monster;
                        existence[rows][columes] = 'G';
                        m += 1;
                    } else if (ch == 'M') {
                        this.mutation = new Mutation(this, columes, rows, this.gremlinS);
                        mutations[M] = this.mutation;
                        existence[rows][columes] = 'M';
                        M += 1;
                    } else if (ch == 'Z') {
                        this.mutation2 = new Mutation2(this, columes, rows, this.gremlinL);
                        mutations2[Z] = this.mutation2;
                        existence[rows][columes] = 'Z';
                        Z += 1;
                    } else if (ch == 'F') {
                        this.mutationBoss = new MutationBoss(this, columes, rows);
                        BossPhase.add(mutationBoss);
                        existence[rows][columes] = 'F';
                    } else if (ch == 'E') {
                        this.exit = new Exit(this, columes, rows, this.door);
                        existence[rows][columes] = 'E';
                    } else if (ch == 'W') {
                        this.player = new Player(this, columes, rows, this.wizard);
                        existence[rows][columes] = 'W';
                    } else if (ch == 'P') {
                        this.definePowerup = true;
                        this.xPowerup = columes;
                        this.yPowerup = rows;
                        existence[rows][columes] = 'P';
                    } else if (ch == 'L') {
                        this.newPowerup2 = true;
                        this.xPowerup2 = columes;
                        this.yPowerup2 = rows;
                        this.powerup2 = new Powerup2(this, columes, rows, this.potion2);
                        existence[rows][columes] = 'L';
                    } else if (ch == 'H') {
                        this.newPowerup3 = true;
                        this.xPowerup3 = columes;
                        this.yPowerup3 = rows;
                        this.powerup3 = new Powerup3(this, columes, rows, this.potion3);
                        existence[rows][columes] = 'H';
                    }
                    columes += 1;
                }
                rows += 1;
                columes = 0;
            }

            //Load BGM
            if (!dead) {
                if (this.levelNum == 1) {
                    bgm("src\\main\\resources\\gremlins\\caveBGM.wav", "loop");
                } else if (this.levelNum == 5) {
                    for (int i = 0; i < BGMs.size(); i++) {
                        BGMs.get(i).stop();
                    }
                    bgm("src\\main\\resources\\gremlins\\nestBGM.wav", "loop");
                } else if (this.levelNum == 6) {
                    for (int i = 0; i < BGMs.size(); i++) {
                        BGMs.get(i).stop();
                    }
                    bgm("src\\main\\resources\\gremlins\\dungeonBGM.wav", "loop");
                } else if (this.levelNum == 9) {
                    for (int i = 0; i < BGMs.size(); i++) {
                        BGMs.get(i).stop();
                    }
                    bgm("src\\main\\resources\\gremlins\\healBGM.wav", "loop");
                } else if (this.levelNum == 10){
                    for (int i = 0; i < BGMs.size(); i++) {
                        BGMs.get(i).stop();
                    }
                    bgm("src\\main\\resources\\gremlins\\bossBGM.wav", "loop");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Player lost a life
     * Reset the level and reload
     * Determine if lose the game
     */
    public void lifeLost() {
        player.lifelost = true;
        bgm("src\\main\\resources\\gremlins\\downSE.wav", "");
        dead = true;
        lives -= 1;
        if (lives > 0) {
            mapClean();
            levelLoading();
        } else {
            levelTimer = 0;
            this.lose = true;
        }
    }

    /**
     * Player goes to next level
     * Reset the level and reload
     * Determine if win the game
     */
    public void levelNext() {
        dead = false;
        levelNum += 1;
        if (levelNum <= levelTotal) {
            mapClean();
            levelLoading();
        } else {
            levelTimer = 0;
            this.win = true;
        }
    }

    /**
     * Reset all object-related variables
     */
    public void mapClean() {
        this.existence = new char[33][36]; //Map initialization
        this.stones = new Stone[33*36];
        this.bricks = new Brick[33*36];
        this.monsters = new Monster[33*36];
        this.mutations = new Mutation[33*36];
        this.mutations2 = new Mutation2[33*36];
        this.BossPhase = new ArrayList<MutationBoss>();
        this.fireballs = new ArrayList<Fireball>();
        this.firestorms = new ArrayList<Firestorm>();
        this.slimes = new ArrayList<Slime>();
        this.firelasers = new ArrayList<Firelaser>();
        this.slimelasers = new ArrayList<Slimelaser>();
        this.newPowerup = true;
        this.newPowerup2 = false;
        this.newPowerup3 = false;
        this.definePowerup = false;
        this.fKey = false; //Space pressed
        this.fireReady = true; //Cooldown to 0
        this.fired = false; //Start cooldown
        this.rKey = false; //Right pressed
        this.lKey = false; //Left pressed
        this.uKey = false; //Up pressed
        this.dKey = false; //Down pressed
        this.levelTimer = 0;
        this.player.lifelost = false;
    }

    /**
     * Remove all not exist objects from mutable arraylists to prevent memory leak
     */
    public void emptyClear() {
        for (int l = 0; l < slimes.size() && slimes.get(l) != null; l++) {
            if (!slimes.get(l).exist) {
                slimes.remove(l);
                l = 0;
            }
        }
        for (int f = 0; f < fireballs.size() && fireballs.get(f) != null; f++) {
            if (!fireballs.get(f).exist) {
                fireballs.remove(f);
            }
        }
        for (int s = 0; s < slimelasers.size() && slimelasers.get(s) != null; s++) {
            if (!slimelasers.get(s).exist) {
                slimelasers.remove(s);
                s = 0;
            }
        }
    }

    /**
     * Play BGM
     */
    public void bgm(String filename, String mode) {
        try {
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;

            stream = AudioSystem.getAudioInputStream(new File(filename));
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            BGMs.add(clip);
            clip.open(stream);
            clip.start();
            if (mode == "loop") {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

    /**
     * Run main
     * @param args The string array
     */
    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
