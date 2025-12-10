package gremlins;

import org.junit.jupiter.api.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class GremlinsTest {

    static App sharedApp;
    static PImage testImage;

    @BeforeAll
    static void setupAll() {
        sharedApp = new App();
        sharedApp.noLoop();
        PApplet.runSketch(new String[]{"App"}, sharedApp);
        sharedApp.getSurface().setVisible(false); // hide window
        testImage = new PImage(20, 20);
    }

    @BeforeEach
    void resetApp() {
        // Clear mutable arrays to avoid memory leaks
        sharedApp.slimes.clear();
        sharedApp.fireballs.clear();
        sharedApp.monsters = new Monster[10];
        sharedApp.mutations = new Mutation[10];
        sharedApp.bricks = new Brick[10];
        sharedApp.player = null;

        // Reset flags and counters
        sharedApp.lives = 3;
        sharedApp.levelNum = 0;
        sharedApp.levelTotal = 3;
        sharedApp.win = false;
        sharedApp.lose = false;
        sharedApp.levelTimer = 0;
        sharedApp.newPowerup = false;
        sharedApp.fired = false;
        sharedApp.winText = "";
        sharedApp.definePowerup = false;
        sharedApp.rKey = false;
        sharedApp.lKey = false;
        sharedApp.uKey = false;
        sharedApp.dKey = false;
        sharedApp.fKey = false;

        // Clear 2D arrays if needed
        for (int i = 0; i < sharedApp.existence.length; i++) {
            for (int j = 0; j < sharedApp.existence[i].length; j++) {
                sharedApp.existence[i][j] = ' ';
            }
        }
    }

    // --- Stone ---
    @Test
    void testStone1() {
        Stone stone = new Stone(sharedApp, 1, 2, testImage);
        assertEquals(20, stone.getX());
        assertEquals(1, stone.getXTile());
        assertEquals(40, stone.getY());
        assertEquals(2, stone.getYTile());
    }

    // --- Brick ---
    @Test
    void testBrick1() {
        Brick brick = new Brick(sharedApp, 10, 10, testImage);
        brick.exist = false;
        assertEquals(200, brick.getX());
        assertEquals(10, brick.getXTile());
        assertEquals(200, brick.getY());
        assertEquals(10, brick.getYTile());
        brick.setAnimated();
        assertTrue(brick.getAnimated());
    }

    @Test
    void testBrick2() {
        sharedApp.brickD0 = new PImage(20, 20);
        sharedApp.brickD1 = new PImage(20, 20);
        sharedApp.brickD2 = new PImage(20, 20);
        sharedApp.brickD3 = new PImage(20, 20);
        sharedApp.empty   = new PImage(20, 20);

        Brick brick = new Brick(sharedApp, 10, 10, testImage);
        brick.exist = false;
        brick.draw(sharedApp);
        assertEquals(brick.getSprite(), sharedApp.brickD0);
        brick.setState(4);
        brick.draw(sharedApp);
        assertEquals(brick.getSprite(), sharedApp.brickD1);
        brick.setState(8);
        brick.draw(sharedApp);
        assertEquals(brick.getSprite(), sharedApp.brickD2);
        brick.setState(12);
        brick.draw(sharedApp);
        assertEquals(brick.getSprite(), sharedApp.brickD3);
        brick.setState(16);
        brick.draw(sharedApp);
        assertEquals(brick.getSprite(), sharedApp.empty);
    }

    // --- Exit ---
    @Test
    void testExit1() {
        Exit exit = new Exit(sharedApp, 30, 30, testImage);
        assertEquals(600, exit.getX());
        assertEquals(600, exit.getY());
    }

    // --- Level ---
    @Test
    void testLevel1() {
        Level level = new Level(sharedApp, "Level_Name.txt", 0.333, 3.0);
        assertEquals("Level_Name.txt", level.getLayout());
        assertEquals(0.333, level.getEnemy_cooldown());
        assertEquals(3.0, level.getWizard_cooldown());
    }

    // --- Fireball ---
    @Test
    void testFireball1() {
        Fireball fireball = new Fireball(sharedApp, 360, 0, "down", testImage);
        fireball.move(sharedApp);
        assertEquals(360, fireball.getX());
        assertEquals(4, fireball.getY());
    }

    @Test
    void testFireball2() {
        sharedApp.existence[1][1] = 'X';
        Fireball f1 = new Fireball(sharedApp, 20, 20, "up", testImage);
        f1.hitX(sharedApp);
        assertFalse(f1.exist);
    }

    // --- Powerup ---
    @Test
    void testPowerup1() {
        sharedApp.player = new Player(sharedApp, 10, 10, testImage);
        Powerup powerup = new Powerup(sharedApp, 10, 10, testImage);
        powerup.collected(sharedApp);
        assertTrue(powerup.getCollect());
        powerup.draw(sharedApp);
        assertTrue(powerup.getEffect());
    }

    // --- Firestorm ---
    @Test
    void testFirestorm1() {
        Firestorm firestorm = new Firestorm(sharedApp, 300, 300, testImage);
        Brick brick = new Brick(sharedApp, 15, 15, testImage);
        sharedApp.bricks[0] = brick;
        firestorm.hitB(sharedApp);
        assertFalse(brick.exist);
    }

    // --- Mutation ---
    @Test
    void testMutation1() {
        Mutation mutation = new Mutation(sharedApp, 10, 10, testImage);
        mutation.draw(sharedApp);
        mutation.setAnimated();
        assertTrue(mutation.getAnimated());
    }

    // --- Slime ---
    @Test
    void testSlime1() {
        Slime slime = new Slime(sharedApp, 100, 100, "right", testImage);
        sharedApp.slimes.add(slime);
        slime.draw(sharedApp);
        slime.move(sharedApp);
        assertEquals(104, slime.getX());
    }

    // --- Player ---
    @Test
    void testPlayer1() {
        sharedApp.player = new Player(sharedApp, 3, 3, testImage);
        sharedApp.keyPressed(new KeyEvent(sharedApp, 0, 0, 0, (char)0, 39));
        sharedApp.player.move(sharedApp);
        assertEquals("right", sharedApp.player.shoot);
    }

    // --- App ---
    @Test
    void testApp1() {
        sharedApp.keyPressed(new KeyEvent(sharedApp, 0, 0, 0, (char)0, 32));
        assertTrue(sharedApp.fKey);
        sharedApp.keyReleased(new KeyEvent(sharedApp, 0, 0, 0, (char)0, 32));
        assertFalse(sharedApp.fKey);
    }

    @Test
    void testApp4_clearCollections() {
        Slime s1 = new Slime(sharedApp, 0,0,"up", testImage);
        sharedApp.slimes.add(s1);
        Fireball f1 = new Fireball(sharedApp, 0,0,"up", testImage);
        f1.exist = false;
        sharedApp.fireballs.add(f1);
        sharedApp.emptyClear();
        assertEquals(1, sharedApp.slimes.size());
        assertEquals(0, sharedApp.fireballs.size());
    }


}
