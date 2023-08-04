package model;

import org.w3c.dom.css.Rect;
import ui.GameKeyHandler;
import ui.Inventory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Game represents the state of the game, including the update method which is used to update the game.
// It also includes what items are added and how to check them.

// Some parts of the code is referenced from the Game class in the snake console project (Update):
// https://github.students.cs.ubc.ca/CPSC210/SnakeConsole-Lanterna/blob/main/src/main/java/com/mazenk/snake/model/Game.java

// Map is arrayList

public class Game {

    private Character character;
/*    public static final int TPS = 10;*/
    private boolean ended = false;

    private Set<Position> coin = new HashSet<>();
    private Set<Position> treasures = new HashSet<>();
    private int coinAmount;
    private Treasure treasure = new Treasure("None");
    private Inventory inventory = new Inventory();

    private Position coinPos;
    private Position coinPosStart;
    private Position checkCoinPos;
    private Position spawnTreasurePos;
    private Position checkTreasurePos;

/*    private int dx;
    private int dy;*/

    private final Random rand = new Random();
    private List<Enemy> enemies = new ArrayList<>();

/*    private boolean isRand;
    private int randDx;
    private int randDy;*/

    private GameKeyHandler keyHandler;

    private int maxX = 570;
    private int maxY = 481;

    private int minY = 55;

    private long lastEnemyUpdateTime = System.currentTimeMillis();
    private static final long ENEMY_UPDATE_CD = 300;

    private List<Projectile> projectiles = new ArrayList<>();

    private long lastFired = 0;
    private long interval = 1000;

    private Boolean nextLevelBoss = true;

    private List<Enemy> boss = new ArrayList<>();

    private long enemyLastFired = 0;
    private List<Projectile> bossProjectiles = new ArrayList<>();

    private boolean hitByProjectile = false;
    private long lastHit;
    private long hitDuration = 1500;
    private boolean onCooldown = false;


    // EFFECTS: Constructs a game
    // Generates a coin at a random location in the board.
    public Game(GameKeyHandler keyHandler) {
        this.keyHandler = keyHandler;
        this.character = new Character();

        Position pos = generateRandomPosition();
        makeValidRandomPos(pos);
        coin.add(pos);
        coinPosStart = pos;
    }

    // MODIFIES: this, character
    // EFFECTS: Progresses the game state, moving the character, adding an infinite amount of coins and treasures
    // and handling them. Checks how the game ends as well when hp = 0.
    public void update() {
        checkFire(); // ADDED, need Tests
        moveProjectile(); // ADDED, need TESTS
        character.move(keyHandler);
        enemyUpdate();

        checkProjectiles(); // ADDED, need TESTS

        checkCoin();

        if (coin.isEmpty()) {
            spawnCoins();
        }

        checkTreasure();

        if (treasures.isEmpty()) {
            spawnTreasure();
        }

        checkEnemyFire();

        checkEnemy(); // ADDED

        checkNextLevel();


        if (getCharacter().getHp() <= 0) {
            ended = true;
        }

    }

    public void checkNextLevel() {
        if (nextLevelBoss) {

            checkBoss(); // ADDED

            ifBoss();
            checkCooldownHit();

            if (!boss.isEmpty()) {
                if (boss.get(0).getHp() <= 0) {
                    boss.clear();
                    bossProjectiles.clear();
                    nextLevelBoss = false;
                }
            }
        }
    }

    public void checkCooldownHit() {
        long currentTime = System.currentTimeMillis();
        if (hitByProjectile && !onCooldown) {
            character.changeHp();
            onCooldown = true;
            lastHit = currentTime;
        }
        if (currentTime - lastHit >= hitDuration) {
            hitByProjectile = false;
            onCooldown = false;
        }
    }

    public void ifBoss() {
        if (!boss.isEmpty()) {
            bossFire();
            moveBossProjectile();
            bossUpdate();
            checkBossProjectiles();
        }
    }


    public void checkEnemy() {
        if (character.getCharacterPos().getPosX() < 610 && (character.getCharacterPos().getPosX() + 20) > 585
                && character.getCharacterPos().getPosY() < 370
                && (character.getCharacterPos().getPosY() + 20) > 270
                && enemies.size() == 0 && !nextLevelBoss) {
            character.setCharacterPos(new Position(30, 200));
            EnemyList enemyList = new EnemyList();
            enemyList.addEnemies(5);
            List<Enemy> newEnemies = enemyList.getEnemies();
            setEnemies(newEnemies);
            boss.clear();
            bossProjectiles.clear();
            nextLevelBoss = true;
        }
    }

    public void checkBoss() {
        if (character.getCharacterPos().getPosX() < 610 && (character.getCharacterPos().getPosX() + 20) > 585
                && character.getCharacterPos().getPosY() < 370 && (character.getCharacterPos().getPosY() + 20) > 270
                && enemies.size() == 0 && nextLevelBoss) {
            character.setCharacterPos(new Position(30, 200));
            seeWhichEnemy();
        }
    }

    public void bossUpdate() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeSinceLastUpdate = currentTime - lastEnemyUpdateTime;

        if (elapsedTimeSinceLastUpdate >= ENEMY_UPDATE_CD) {
            for (Enemy enemy : boss) {
                int newPosX = enemy.getEnemyPos().getPosX() + (rand.nextInt(11) - 5);
                int newPosY = enemy.getEnemyPos().getPosY() + (rand.nextInt(11) - 5);
                if (newPosX > maxX) {
                    newPosX = maxX - 15;
                } else if (newPosY > 535) {
                    newPosY = 535 - 15;
                } else if (newPosX < 0) {
                    newPosX += 15;
                } else if (newPosY < minY) {
                    newPosY += 15;
                }
                Position pos = new Position(newPosX, newPosY);
                enemy.setEnemyPos(pos);
                getCharacter().checkCollision(pos);
                lastEnemyUpdateTime = currentTime;
            }

        }
    }

    public void bossFire() {
        long currentTime = System.currentTimeMillis();
        Random rand = new Random();
        if (currentTime - enemyLastFired >= 500) {
            Projectile projectile = new Projectile(new Position(boss.get(0).getEnemyPos().getPosX(),
                    boss.get(0).getEnemyPos().getPosY() + rand.nextInt(125)));
            bossProjectiles.add(projectile);
            enemyLastFired = currentTime;
        }
    }

    public void seeWhichEnemy() {
        if (boss.size() == 0) {
            Position bossPos = new Position(375, 200);
            Enemy bossEnemy = new Enemy(bossPos);
            bossEnemy.setHp(200);
            boss.add(bossEnemy);
        }
    }

    public void moveBossProjectile() {
        for (Projectile projectile : bossProjectiles) {
            projectile.moveBossPos();
        }
    }

    public void checkBossProjectiles() {
        List<Projectile> badProjectiles = new ArrayList<>();
        List<Enemy> badEnemies = new ArrayList<>();

        checkCharHit(character, badProjectiles);

        for (Enemy enemyBoss : boss) {
            if (checkBossHit(enemyBoss, badProjectiles)) {
                if (enemyBoss.getHp() <= 0) {
                    badEnemies.add(enemyBoss);
                }
            }
        }
        boss.remove(badEnemies);
        bossProjectiles.removeAll(badProjectiles);
        projectiles.removeAll(badProjectiles);
    }

    public boolean checkBossHit(Enemy enemy, List<Projectile> badProjectiles) {
        for (Projectile projectile : projectiles) {
/*            if (enemy.hasCollided(projectile.getPos(), 150)) {
                badProjectiles.add(projectile);
                enemy.minusHp(character.getAtk());
                return true;
            }*/
            Rectangle projRect = new Rectangle(projectile.getPos().getPosX(), projectile.getPos().getPosY(),
                    18, 18);
            if (enemy.getHitBox(150, 150).intersects(projRect)) {
                badProjectiles.add(projectile);
                enemy.minusHp(character.getAtk());
                return true;
            }
        }
        return false;
    }

    public boolean checkCharHit(Character character, List<Projectile> badProjectiles) {
        for (Projectile projectile : bossProjectiles) {
            if (character.hasCollided(projectile.getPos())) {
                badProjectiles.add(projectile);
                hitByProjectile = true;
                return true;
            }
        }
        return false;
    }





    // ADDED, need TESTS
    public void checkFire() {
        if (keyHandler.isFirePressed()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFired >= interval) {
                Projectile projectile = new Projectile(character.getCharacterPos());
                projectiles.add(projectile);
                lastFired = currentTime;
            }
        }
    }

    public void enemyUpdate() {
        long currentTime = System.currentTimeMillis();
        long elapsedTimeSinceLastUpdate = currentTime - lastEnemyUpdateTime;

        if (elapsedTimeSinceLastUpdate >= ENEMY_UPDATE_CD) {
            for (Enemy enemy : enemies) {
                int newPosX = enemy.getEnemyPos().getPosX() + (rand.nextInt(11) - 5);
                int newPosY = enemy.getEnemyPos().getPosY() + (rand.nextInt(11) - 5);
                if (newPosX > maxX) {
                    newPosX = maxX - 15;
                } else if (newPosY > 535) {
                    newPosY = 535 - 15;
                } else if (newPosX < 0) {
                    newPosX += 15;
                } else if (newPosY < minY) {
                    newPosY += 15;
                }
                Position pos = new Position(newPosX, newPosY);
                enemy.setEnemyPos(pos);
                getCharacter().checkCollision(pos);
                lastEnemyUpdateTime = currentTime;
            }
        }
    }

    // ADDED, need TESTS
    public void moveProjectile() {
        for (Projectile projectile : projectiles) {
            projectile.movePos();
        }
    }




    // MODIFIES: this
    // EFFECTS: Spawns a new coin into a valid position in the game.
    // Note: Code inspired from snake console.
    public void spawnCoins() {
        Position pos = generateRandomPosition();

        makeValidRandomPos(pos);

        coin.add(pos);
        coinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: Spawns a new treasure into a valid position in the game.
    // Note: Code inspired from snake console.
    public void spawnTreasure() {
        Position pos = generateRandomPosition();

        makeValidRandomPos(pos);

        treasures.add(pos);
        spawnTreasurePos = pos;
    }

    // REQUIRES: maxX >= x >= 0 and maxY >= y >= minY for the position.
    // EFFECTS: Makes a valid random position.
    public Position makeValidRandomPos(Position pos) {
        while (!isValidPosition(pos)) {
            pos = generateRandomPosition();
        }
        return pos;
    }

    // ADDED, need TESTS
    public void checkProjectiles() {
        List<Projectile> removeProjectileList = new ArrayList<>();

        for (Projectile projectile : projectiles) {
            if (outOfBoundary(projectile.getPos())) {
                removeProjectileList.add(projectile);
            }
        }

        for (Projectile bossProjectile : bossProjectiles) {
            if (outOfBoundary(bossProjectile.getPos())) {
                removeProjectileList.add(bossProjectile);
            }
        }

        projectiles.removeAll(removeProjectileList);
        bossProjectiles.removeAll(removeProjectileList);
    }

    // ADDED, need TESTS
    public void checkEnemyFire() {
        List<Projectile> badProjectiles = new ArrayList<>();
        List<Enemy> badEnemies = new ArrayList<>();

        for (Enemy enemy : enemies) {
            if (checkHit(enemy, badProjectiles)) {
                if (enemy.getHp() <= 0) {
                    badEnemies.add(enemy);
                }
            }
        }
        projectiles.removeAll(badProjectiles);
        enemies.removeAll(badEnemies);
    }

    // ADDED, need TESTS
    public boolean checkHit(Enemy enemy, List<Projectile> badProjectiles) {
        for (Projectile projectile : projectiles) {
            Rectangle projRect = new Rectangle(projectile.getPos().getPosX(), projectile.getPos().getPosY(),
                    18, 18);
/*            if (enemy.hasCollided(projectile.getPos(), 18)) {
                badProjectiles.add(projectile);
                enemy.minusHp(character.getAtk());
                return true;
            }*/

            if (enemy.getHitBox(36, 36).intersects(projRect)) {
                badProjectiles.add(projectile);
                enemy.minusHp(character.getAtk());
                return true;
            }
        }
        return false;
    }


    // MODIFIES: this
    // EFFECTS: Checks for coins that the character has collided. If there is one, then remove it from the
    // set and board. Add to coin score.
    public void checkCoin() {
        Position coinsEarned = coin.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (coinsEarned == null) {
            return;
        }
        coin.remove(coinsEarned);
        coinAmount++;
        checkCoinPos = coinsEarned;
    }

    // MODIFIES: this, treasure
    // EFFECTS: Checks for treasures that the character has collided. If there is one, then remove it
    // from the set and thus the board. Adds the treasure to the inventory.
    public void checkTreasure() {
        Position checkTreasures = treasures.stream().filter(character::hasCollided).findFirst().orElse(null);
        if (checkTreasures == null) {
            return;
        }
        treasures.remove(checkTreasures);
        inventory.addTreasure(new Treasure(treasure.whatTreasure()));
        checkTreasurePos = checkTreasures;
    }


/*    // EFFECTS: Generates a random position, which is guaranteed to be in bounds but not necessary valid.
    public Position generateRandomPosition() {
        return new Position(ThreadLocalRandom.current().nextInt(40),
                ThreadLocalRandom.current().nextInt(22));
    }*/

    // EFFECTS: Generates a random position, which is guaranteed to be in bounds but not necessary valid.
    public Position generateRandomPosition() {
        return new Position(ThreadLocalRandom.current().nextInt(maxX + 1),
                ThreadLocalRandom.current().nextInt(maxY + 1) + 55);
    }

    // REQUIRES: maxX >= x >= 0 and maxY >= y >= minY for the position.
    // EFFECTS: Returns whether a given position is in the boundary and not occupied.
    public boolean isValidPosition(Position pos) {
        return !outOfBoundary(pos)
                && !coin.contains(pos)
                && !treasures.contains(pos)
                && !character.hasCollided(pos);
    }

    // EFFECTS: Returns true when a position is out of boundary.
    public boolean outOfBoundary(Position pos) {
        return pos.getPosX() < 0 || pos.getPosY() < minY || pos.getPosX() > maxX || pos.getPosY() > (maxY + 54);
    }

    // MODIFIES: this
    // EFFECTS: sets the enemy list to the given input.
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    // MODIFIES: this
    // EFFECTS: sets the inventory to the given input.
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // EFFECTS: returns the enemy list.
    public List<Enemy> getEnemies() {
        return enemies;
    }

    // EFFECTS: returns the character.
    public Character getCharacter() {
        return character;
    }

    // EFFECTS: returns the inventory with the treasures.
    public Inventory getInventory() {
        return inventory;
    }

    // EFFECTS: returns whether game has ended or not.
    public boolean isEnded() {
        return ended;
    }

    // EFFECTS: returns the position of the coins in the board.
    public Set<Position> getCoin() {
        return coin;
    }

    // EFFECTS: returns the position of the treasures in the board.
    public Set<Position> getTreasures() {
        return treasures;
    }

    // MODIFIES: this
    // EFFECTS: sets the set of treasures to the given input.
    public void setTreasures(Set<Position> treasures) {
        this.treasures = treasures;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the treasure spawn to the given input.
    public void setSpawnTreasurePos(Position pos) {
        spawnTreasurePos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the checkTreasure method to the given input.
    public void setCheckTreasurePos(Position pos) {
        checkTreasurePos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the starting coin position to the given input.
    public void setCoinPosStart(Position pos) {
        coinPosStart = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the spawned coin to the given input.
    public void setCoinPos(Position pos) {
        coinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the position of the checkCoin method to the given input.
    public void setCheckCoinPos(Position pos) {
        checkCoinPos = pos;
    }

    // MODIFIES: this
    // EFFECTS: sets the coin amount into the given input.
    public void setCoinAmount(Integer amount) {
        coinAmount = amount;
    }

    // EFFECTS: returns the amount of coins the player has earned throughout the game.
    public int getCoinAmount() {
        return coinAmount;
    }

    // EFFECTS: returns maxX
    public int getMaxX() {
        return maxX;
    }

    // EFFECTS: returns maxY.
    public int getMaxY() {
        return maxY;
    }

    // EFFECTS: returns coin position, mainly for testing purposes.
    public Position getCoinPos() {
        return coinPos;
    }

    // EFFECTS: returns the starting position of the coin, mainly for testing purposes.
    public Position getCoinPosStart() {
        return coinPosStart;
    }

    // EFFECTS: returns the coinsEarned position from the checkCoinPos method.
    public Position getCheckCoinPos() {
        return checkCoinPos;
    }

    // EFFECTS: returns the treasure position, mainly for testing purposes.
    public Position getSpawnTreasurePos() {
        return spawnTreasurePos;
    }

    // EFFECTS: returns the position of checkTreasures in checkTreasure. Mainly for testing purposes.
    public Position getCheckTreasurePos() {
        return checkTreasurePos;
    }

    public long getLastEnemyUpdateTime() {
        return lastEnemyUpdateTime;
    }

    public void setLastEnemyUpdateTime(long c) {
        lastEnemyUpdateTime = c;
    }

    // ADDED, need Tests
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    // ADDED, need TESTS
    public void setProjectiles(List<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public void setLastFired(Long e) {
        lastFired = e;
    }

    public long getInterval() {
        return interval;
    }

    public List<Enemy> getBoss() {
        return boss;
    }

    public List<Projectile> getBossProjectiles() {
        return bossProjectiles;
    }

    public void setNextLevelBoss(Boolean bool) {
        nextLevelBoss = bool;
    }

    public void setBoss(List<Enemy> bossEnemy) {
        boss = bossEnemy;
    }

    public boolean getNextLevelBoss() {
        return nextLevelBoss;
    }

    public boolean gethitByProjectile() {
        return hitByProjectile;
    }

    public void setHitByProjectile(Boolean b) {
        hitByProjectile = b;
    }

    public boolean getOnCooldown() {
        return onCooldown;
    }

    public void setBossProjectiles(List<Projectile> projectiles) {
        bossProjectiles = projectiles;
    }

    public void setEnemyLastFired(Long e) {
        enemyLastFired = e;
    }

    public void setOnCooldown(Boolean b) {
        onCooldown = b;
    }
}