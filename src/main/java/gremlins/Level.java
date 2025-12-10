package gremlins;

/**
 * The information of each level depends on the config file.
 */
public class Level {
    private double enemy_cooldown;
    private double wizard_cooldown;
    private String layout;

    /**
     * Level constructor
     * @param app
     * @param layout
     * @param enemy_cooldown
     * @param wizard_cooldown
     */
    public Level(App app, String layout, double enemy_cooldown, double wizard_cooldown) {
        this.layout = layout;
        this.enemy_cooldown = enemy_cooldown;
        this.wizard_cooldown = wizard_cooldown;
    }

    /**
     * Enemy_cooldown getter
     * @return this level's Slime cooldown
     */
    public double getEnemy_cooldown() {
        return this.enemy_cooldown;
    }

    /**
     * Wizard_cooldown getter
     * @return this level's Fireball cooldown
     */
    public double getWizard_cooldown() {
        return this.wizard_cooldown;
    }

    /**
     * Layout getter
     * @return this level's name
     */
    public String getLayout() {
        return this.layout;
    }
}
