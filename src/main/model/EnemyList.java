package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// EnemyList represents how many enemies you are adding into the game through a list of enemies with each
// having a different position.

public class EnemyList {

    private Random random = new Random();
    private List<Enemy> enemies;

    // MODIFIES: this
    // EFFECTS: adds x amount of enemies, each with a given position, into a list of enemies.
    // MODIFIED THIS: BOUNDARIES TESTS ADD
    public List<Enemy> addEnemies(int numEnemies) {
        enemies = new ArrayList<>();
        for (int i = 0; i < numEnemies; i++) {
            int x = random.nextInt(571);
            int y = random.nextInt(481) + 55;
            enemies.add(new Enemy(new Position(x, y)));
        }
        return enemies;
    }

    // EFFECTS: returns a list of the current amount of enemies.
    public List<Enemy> getEnemies() {
        return enemies;
    }



}