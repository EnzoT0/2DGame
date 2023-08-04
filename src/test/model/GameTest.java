package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.GameKeyHandler;
import ui.Inventory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private GameKeyHandler keyHandler = new GameKeyHandler();

    @BeforeEach
    void runBefore() {
        game = new Game(keyHandler);
    }

    @Test
    void testConstructor() {

        assertEquals(1, game.getCoin().size());
    }

    @Test
    void testUpdate() {
        // character.move() has already been tested at the characterTest class.

        // Check if coin is in game.
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));

        assertEquals(0, game.getProjectiles().size());
        // Check 1st update, coin not in starting position of character.
        game.update();
        //assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(1, game.getCoin().size());

        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertEquals(0, game.getCoinAmount());
        assertFalse(game.isEnded());

        // Check 2nd update, this time character collided with coin.
        //game.getCharacter().setDirection(Direction.NONE);
        game.getCharacter().setCharacterPos(game.getCoinPosStart());
        game.update();
        assertFalse(game.getCoin().contains(game.getCoinPosStart())); // Shows coin is not in set anymore

        assertTrue(game.getCoin().contains(game.getCoinPos())); // Spawn coin added a new coin into the set.
        assertEquals(1, game.getCoin().size());
        assertFalse(game.getCoinPosStart() == game.getCoinPos());
        assertEquals(1, game.getCoinAmount());

        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(game.isEnded());

        // Check 3rd update, this time character collided with the spawned coin.
        game.getCharacter().setCharacterPos(game.getCoinPos());
        game.update();
        assertFalse(game.getCoin().contains(game.getCheckCoinPos())); // Update changes coinPos to newSpawn CoinPos.
        assertEquals(1, game.getCoin().size());

        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.getCoinPos() == game.getCheckCoinPos()); // Shows that it's now a different position.
        assertEquals(2, game.getCoinAmount());
        assertFalse(game.isEnded());

        // Check 4th update, this time character collided with treasure.
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        game.update();
        assertFalse(game.getTreasures().contains(game.getCheckTreasurePos())); // Collided with treasure.

        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos())); // new treasure was spawned.
        assertEquals(1, game.getTreasures().size());
        assertFalse(game.getCheckTreasurePos() == game.getSpawnTreasurePos());
        assertFalse(game.isEnded());

        // Check 4th update, nothing happens.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        game.update();
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(game.isEnded());

        // Check 5th update, game has ended.
        assertFalse(game.isEnded());
        game.getCharacter().setHp(0);
        game.update();
        assertTrue(game.isEnded());

    }

    @Test
    void testCheckNextLevel() {
        // Nothing happen
        game.setNextLevelBoss(false);
        game.checkNextLevel();

        game.setNextLevelBoss(true);
        List<Enemy> boss = new ArrayList<>();
        Position pos = new Position(10, 100);
        boss.add(new Enemy(pos));
        game.setBoss(boss);

        game.checkNextLevel();
        assertEquals(1, game.getBoss().size());
        assertEquals(1, game.getBossProjectiles().size());

        boss.get(0).setHp(0);
        game.checkNextLevel();
        assertEquals(0, game.getBoss().size());
        assertFalse(game.getBoss().contains(boss));
        assertEquals(0, game.getBossProjectiles().size());
        assertFalse(game.getNextLevelBoss());
    }

    @Test
    void testCheckCooldownHit() {
        // Nothing happens
        assertFalse(game.gethitByProjectile());
        assertFalse(game.getOnCooldown());
        game.checkCooldownHit();

        // Set hitByProjectile true false
        game.setHitByProjectile(true);
        game.checkCooldownHit();
        assertEquals(95, game.getCharacter().getHp());
        assertTrue(game.getOnCooldown());

        // false true
        game.setHitByProjectile(false);
        game.setOnCooldown(true);
        game.checkCooldownHit();
        assertEquals(95, game.getCharacter().getHp());
        assertTrue(game.getOnCooldown());

        game.setHitByProjectile(true);
        game.checkCooldownHit();
        assertEquals(95, game.getCharacter().getHp());
        assertTrue(game.gethitByProjectile());
        assertTrue(game.getOnCooldown());
    }

    @Test
    void testCheckEnemy() {
        // Nothing happens
        game.checkEnemy();

        // Something happens
        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkEnemy();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());
        assertEquals(5, game.getEnemies().size());
        assertEquals(0, game.getBoss().size());
        assertEquals(0, game.getBossProjectiles().size());
        assertTrue(game.getNextLevelBoss());
    }

    @Test
    void testCheckEnemy2() {
        game.getCharacter().setCharacterPos(new Position(590, 369));
        game.checkEnemy();
        assertEquals(new Position(590, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());
        assertEquals(5, game.getEnemies().size());
        assertEquals(0, game.getBoss().size());
        assertEquals(0, game.getBossProjectiles().size());
        assertTrue(game.getNextLevelBoss());
    }

    @Test
    void testCheckEnemy3() {
        game.getCharacter().setCharacterPos(new Position(609, 350));
        game.checkEnemy();
        assertEquals(new Position(609, 350), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());
        assertEquals(5, game.getEnemies().size());
        assertEquals(0, game.getBoss().size());
        assertEquals(0, game.getBossProjectiles().size());
        assertTrue(game.getNextLevelBoss());
    }

    @Test
    void testCheckEnemy4() {
        game.getCharacter().setCharacterPos(new Position(609, 290));
        game.checkEnemy();
        assertEquals(new Position(609, 290), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());
        assertEquals(5, game.getEnemies().size());
        assertEquals(0, game.getBoss().size());
        assertEquals(0, game.getBossProjectiles().size());
        assertTrue(game.getNextLevelBoss());
    }

    @Test
    void testCheckEnemy5() {
        game.setNextLevelBoss(false);
        game.getCharacter().setCharacterPos(new Position(620, 369));
        game.checkEnemy();
        assertEquals(new Position(620, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkEnemy();
        assertEquals(new Position(620, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(620, 260));
        game.checkEnemy();
        assertEquals(new Position(620, 260), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(400, 240));
        game.checkEnemy();
        assertEquals(new Position(400, 240), game.getCharacter().getCharacterPos());


        game.getCharacter().setCharacterPos(new Position(609, 300));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 370));
        game.checkEnemy();
        assertEquals(new Position(609, 370), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 270));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 450));
        game.checkEnemy();
        assertEquals(new Position(609, 450), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 280));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 371));
        game.checkEnemy();
        assertEquals(new Position(609, 371), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 271));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 269));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 249));
        game.checkEnemy();
        assertEquals(new Position(609, 249), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 251));
        game.checkEnemy();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.getEnemies().clear();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(new Position(100, 100)));
        game.setEnemies(enemies);

        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkEnemy();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());
    }

    @Test
    void testCheckBoss() {
        // Nothing happens
        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.setNextLevelBoss(false);
        game.checkBoss();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());

        // Something happens
        game.setNextLevelBoss(true);
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());
        assertTrue(game.getNextLevelBoss());
    }

    @Test
    void testCheckBoss2() {
        game.setNextLevelBoss(true);
        game.getCharacter().setCharacterPos(new Position(620, 369));
        game.checkBoss();
        assertEquals(new Position(620, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.checkBoss();
        assertEquals(new Position(620, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(620, 260));
        game.checkBoss();
        assertEquals(new Position(620, 260), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(400, 240));
        game.checkBoss();
        assertEquals(new Position(400, 240), game.getCharacter().getCharacterPos());


        game.getCharacter().setCharacterPos(new Position(609, 300));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 370));
        game.checkBoss();
        assertEquals(new Position(609, 370), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 270));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 450));
        game.checkBoss();
        assertEquals(new Position(609, 450), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 280));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 371));
        game.checkBoss();
        assertEquals(new Position(609, 371), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 271));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 269));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        game.getCharacter().setCharacterPos(new Position(609, 249));
        game.checkBoss();
        assertEquals(new Position(609, 249), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 251));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);
        game.getEnemies().clear();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(new Position(100, 100)));
        game.setEnemies(enemies);

        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkBoss();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(false);
        game.checkBoss();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());
    }

    @Test
    void testCheckBoss3() {
        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.setNextLevelBoss(false);
        game.checkBoss();
        assertEquals(new Position(609, 369), game.getCharacter().getCharacterPos());

        game.setNextLevelBoss(true);

        game.getCharacter().setCharacterPos(new Position(609, 369));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(566, 369));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(611, 369));
        game.checkBoss();
        assertEquals(new Position(611, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(565, 369));
        game.checkBoss();
        assertEquals(new Position(565, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(564, 369));
        game.checkBoss();
        assertEquals(new Position(564, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(610, 369));
        game.checkBoss();
        assertEquals(new Position(610, 369), game.getCharacter().getCharacterPos());

        game.getCharacter().setCharacterPos(new Position(609, 340));
        game.checkBoss();
        assertEquals(new Position(30, 200), game.getCharacter().getCharacterPos());

    }

    @Test
    void testBossUpdate() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setBoss(enemies);
        assertEquals(1, enemyList.getEnemies().size());
        assertEquals(1, game.getBoss().size());

        for (Enemy enemy : game.getBoss()) {
            enemy.setEnemyPos(new Position(70, 150));
        }

        game.bossUpdate();

        assertTrue(65 <= game.getBoss().get(0).getEnemyPos().getPosX());
        assertTrue(145 <= game.getBoss().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);

    }

    @Test
    void testBossUpdate2() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> bossEnemy = enemyList.addEnemies(1);
        game.setBoss(bossEnemy);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getBoss()) {
            enemy.setEnemyPos(new Position(-10, 150));
        }

        game.bossUpdate();
        assertTrue(10 >= game.getBoss().get(0).getEnemyPos().getPosX());
        assertTrue(145 <= game.getBoss().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testBossUpdate3() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> bossEnemy = enemyList.addEnemies(1);
        game.setBoss(bossEnemy);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getBoss()) {
            enemy.setEnemyPos(new Position(100, -10));
        }

        game.bossUpdate();
        assertTrue(10 >= game.getBoss().get(0).getEnemyPos().getPosY());
        assertTrue(95 <= game.getBoss().get(0).getEnemyPos().getPosX());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testBossUpdate4() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> bossEnemy = enemyList.addEnemies(1);
        game.setBoss(bossEnemy);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getBoss()) {
            enemy.setEnemyPos(new Position(690, 100));
        }

        game.bossUpdate();
        assertTrue(570 >= game.getBoss().get(0).getEnemyPos().getPosX());
        assertTrue(95 <= game.getBoss().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testBossUpdate5() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> bossEnemy = enemyList.addEnemies(1);
        game.setBoss(bossEnemy);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getBoss()) {
            enemy.setEnemyPos(new Position(100, 560));
        }

        game.bossUpdate();
        assertTrue(550 >= game.getBoss().get(0).getEnemyPos().getPosY());
        assertTrue(95 <= game.getBoss().get(0).getEnemyPos().getPosX());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testCheckBossProjectiles() {
        EnemyList enemyList = new EnemyList();

        enemyList.addEnemies(1);
        List<Enemy> bossEnemy = enemyList.getEnemies();
        bossEnemy.get(0).setHp(200);
        game.setBoss(bossEnemy);

        Position pos = game.getBoss().get(0).getEnemyPos();

        Projectile projectile = new Projectile(pos);

        List<Projectile> projectiles = new ArrayList<>();

        projectiles.add(projectile);

        game.setProjectiles(projectiles);

        game.checkBossProjectiles();
        assertEquals(195, game.getBoss().get(0).getHp());
        assertEquals(0, game.getProjectiles().size());

        projectiles.add(projectile);
//        game.setProjectiles(projectiles);
        game.getBoss().get(0).setHp(5);
        game.checkBossProjectiles();
        assertEquals(0, game.getBoss().get(0).getHp());
    }

    @Test
    void testSeeWhichEnemy() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(new Position(100, 100)));
        game.setBoss(enemies);
        game.seeWhichEnemy();
        assertEquals(1, game.getBoss().size());

        game.getBoss().clear();
        game.seeWhichEnemy();
        assertEquals(new Position(375, 200), game.getBoss().get(0).getEnemyPos());
        assertEquals(200, game.getBoss().get(0).getHp());
    }

    @Test
    void testCheckBossHit() {
        //  do nothing
        Enemy enemy1 = new Enemy(new Position(1000, 1000));
        List<Projectile> lol = new ArrayList<>();
        Projectile projectile1 = new Projectile(new Position(10, 10));
        List<Projectile> projectileList = new ArrayList<>();
        projectileList.add(projectile1);
        game.setProjectiles(projectileList);
        assertFalse(game.checkBossHit(enemy1, lol));

        Enemy enemy = new Enemy(new Position(100, 100));
        Projectile projectile = new Projectile(new Position(100, 100));
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(projectile);
        game.setProjectiles(projectiles);
        List<Projectile> badProjectiles = new ArrayList<>();

        assertTrue(game.checkBossHit(enemy, badProjectiles));

        assertEquals(1, badProjectiles.size());
        assertEquals(15, enemy.getHp());
    }

    @Test
    void testBossFire() {
        long currentTime = System.currentTimeMillis();
        game.setEnemyLastFired(currentTime - 100);

        List<Enemy> boss = new ArrayList<>();
        boss.add(new Enemy(new Position(100, 100)));
        game.setBoss(boss);

        game.bossFire();
        assertEquals(0, game.getBossProjectiles().size());
    }

    @Test
    void testCheckCharHit() {
        //  do nothing
        Character character = new Character();
        List<Projectile> lol = new ArrayList<>();
        Projectile projectile1 = new Projectile(new Position(10, 10));
        List<Projectile> projectileList = new ArrayList<>();
        projectileList.add(projectile1);
        game.setBossProjectiles(projectileList);
        assertFalse(game.checkCharHit(character, lol));

        Character character1 = new Character();
        character1.setCharacterPos(new Position(100, 100));
        Projectile projectile = new Projectile(new Position(100, 100));
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(projectile);
        game.setBossProjectiles(projectiles);
        List<Projectile> badProjectiles = new ArrayList<>();

        assertTrue(game.checkCharHit(character1, badProjectiles));

        assertEquals(1, badProjectiles.size());
        assertTrue(game.gethitByProjectile());
    }

    @Test
    void testCheckFire() {
        // Test case where fire pressed is false
        keyHandler.setFirePressed(false);
        game.checkFire();
        assertEquals(0,game.getProjectiles().size());

        // Test case where fire is true
        keyHandler.setFirePressed(true);
        game.checkFire();
        assertEquals(1, game.getProjectiles().size());

        // test case where fire gives nothing
        game.checkFire();
        assertEquals(1, game.getProjectiles().size());

        // test case where it can do it again
        long currentTime = System.currentTimeMillis();
        long lastFired = currentTime - game.getInterval() - 5;
        game.setLastFired(lastFired);

        game.checkFire();

        assertEquals(2, game.getProjectiles().size());

        // last case where nothing happens
        game.checkFire();
        assertEquals(2, game.getProjectiles().size());
    }

    @Test
    public void moveProjectile() {
        game.moveProjectile();

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(new Position(5, 6)));
        game.setProjectiles(projectiles);

        game.moveProjectile();
        assertEquals(new Position(10, 6), game.getProjectiles().get(0).getPos());
    }

    @Test
    void testCheckProjectiles() {
        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(new Position(571, 300)));
        game.setProjectiles(projectiles);

        game.checkProjectiles();
        assertEquals(0, game.getProjectiles().size());

        projectiles.add(new Projectile(new Position(10, 300)));
        //game.setProjectiles(projectiles);
        game.checkProjectiles();
        assertEquals(1, game.getProjectiles().size());

        List<Projectile> bossProjectiles = new ArrayList<>();
        bossProjectiles.add(new Projectile(new Position(571, 300)));
        game.setBossProjectiles(bossProjectiles);

        game.checkProjectiles();
        assertEquals(0, game.getBossProjectiles().size());

        bossProjectiles.add(new Projectile(new Position(10, 300)));
        //game.setProjectiles(projectiles);
        game.checkProjectiles();
        assertEquals(1, game.getBossProjectiles().size());
    }

    @Test
    void testCheckEnemyFire() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(new Position(10, 100)));
        enemies.add(new Enemy(new Position(100, 200)));
        game.setEnemies(enemies);

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(new Position(1, 1)));
        game.setProjectiles(projectiles);

        // nothing happens
        game.checkEnemyFire();
        assertEquals(1, game.getProjectiles().size());
        assertEquals(2, game.getEnemies().size());

        projectiles.remove(0);
        assertEquals(0, game.getProjectiles().size());

        // Projectile hits an enemy
        projectiles.add(new Projectile(new Position(10, 100)));
        game.checkEnemyFire();
        assertEquals(0, game.getProjectiles().size());
        assertEquals(2, game.getEnemies().size());
        assertEquals(15, game.getEnemies().get(0).getHp());

        // projectile hits same enemy
        projectiles.add(new Projectile(new Position(10, 100)));
        assertEquals(1, game.getProjectiles().size());
        assertEquals(2, game.getEnemies().size());
        game.checkEnemyFire();
        assertEquals(0, game.getProjectiles().size());
        assertEquals(2, game.getEnemies().size());
        projectiles.add(new Projectile(new Position(10, 100)));
        game.checkEnemyFire();
        projectiles.add(new Projectile(new Position(10, 100)));
        game.checkEnemyFire();
        assertEquals(1, game.getEnemies().size());
    }

    @Test
    void testCheckHit() {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(new Position(10, 100)));
        enemies.add(new Enemy(new Position(100, 200)));
        game.setEnemies(enemies);

        List<Projectile> projectiles = new ArrayList<>();
        projectiles.add(new Projectile(new Position(1, 1)));
        game.setProjectiles(projectiles);

        assertFalse(game.checkHit(enemies.get(0), projectiles));

        projectiles.add(new Projectile(new Position(10, 100)));
        assertTrue(game.checkHit(enemies.get(0), projectiles));
        assertEquals(15, game.getEnemies().get(0).getHp());

        assertTrue(game.checkHit(enemies.get(0), projectiles));
        assertEquals(10, game.getEnemies().get(0).getHp());
    }



    @Test
    void testEnemyUpdateOnCooldown() {
        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(70, 150));
        }

        game.enemyUpdate();

        assertEquals(new Position(70, 150), game.getEnemies().get(0).getEnemyPos());
    }

    @Test
    void testEnemyUpdateNotOnCooldown() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(70, 150));
        }

        game.enemyUpdate();

        assertTrue(65 <= game.getEnemies().get(0).getEnemyPos().getPosX());
        assertTrue(145 <= game.getEnemies().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testEnemyUpdateNotOnCoolDown2() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(-10, 150));
        }

        game.enemyUpdate();
        assertTrue(10 >= game.getEnemies().get(0).getEnemyPos().getPosX());
        assertTrue(145 <= game.getEnemies().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testEnemyUpdateNotOnCooldown3() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(100, -10));
        }

        game.enemyUpdate();
        assertTrue(10 >= game.getEnemies().get(0).getEnemyPos().getPosY());
        assertTrue(95 <= game.getEnemies().get(0).getEnemyPos().getPosX());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testEnemyUpdateNotOnCooldown4() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(690, 100));
        }

        game.enemyUpdate();
        assertTrue(570 >= game.getEnemies().get(0).getEnemyPos().getPosX());
        assertTrue(95 <= game.getEnemies().get(0).getEnemyPos().getPosY());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);
    }

    @Test
    void testEnemyUpdateNotOnCooldown5() {
        long currentTime = System.currentTimeMillis();
        game.setLastEnemyUpdateTime(currentTime - 400);

        EnemyList enemyList = new EnemyList();
        List<Enemy> enemies = enemyList.addEnemies(1);
        game.setEnemies(enemies);
        assertEquals(1, enemyList.getEnemies().size());

        for (Enemy enemy : game.getEnemies()) {
            enemy.setEnemyPos(new Position(100, 560));
        }

        game.enemyUpdate();
        assertTrue(550 >= game.getEnemies().get(0).getEnemyPos().getPosY());
        assertTrue(95 <= game.getEnemies().get(0).getEnemyPos().getPosX());
        assertTrue(game.getLastEnemyUpdateTime() - 5 <= currentTime &&
                currentTime <= game.getLastEnemyUpdateTime() + 5);

        //assertEquals(currentTime, game.getLastEnemyUpdateTime());
    }

    @Test
    void testSpawnCoins() {
        // Checks whether first coin already in the game.
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertFalse(game.isValidPosition(game.getCoinPosStart())); // No longer a valid position due to coin

        // Tests 2nd spawnCoins
        game.spawnCoins();
        assertEquals(2, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.isValidPosition(game.getCoinPos()));

        // Tests spawning another coin.
        game.spawnCoins();
        assertEquals(3, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPos()));
        assertFalse(game.isValidPosition(game.getCoinPos()));
    }

    @Test
    void testSpawnTreasure() {
        // Check that there are no treasures at the very start
        assertEquals(0, game.getTreasures().size());

        // Spawns the first treasure.
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));

        // Spawns another treasure.
        game.spawnTreasure();
        assertEquals(2, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
    }

    @Test
    void testCheckCoin() {
        // Nothing happens at first, no collision. 1 coin in the screen right now due to initialization.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());
        game.checkCoin();
        assertEquals(1, game.getCoin().size());
        assertTrue(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(0, game.getCoinAmount());

        // Collision happens
        game.getCharacter().setCharacterPos(game.getCoinPosStart());
        assertEquals(1, game.getCoin().size());
        assertEquals(0, game.getCoinAmount());

        game.checkCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPosStart()));
        assertEquals(1, game.getCoinAmount());

        // Test collision again, this time with spawnCoins
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        game.spawnCoins();
        assertEquals(1, game.getCoin().size());
        assertEquals(1, game.getCoinAmount());

        game.getCharacter().setCharacterPos(game.getCoinPos());
        game.checkCoin();

        assertEquals(0, game.getCoin().size());
        assertFalse(game.getCoin().contains(game.getCoinPos()));
        assertEquals(2, game.getCoinAmount());

    }

    @Test
    void testCheckTreasure() {
        // Nothing happens at first, no collision.
        game.getCharacter().setCharacterPos(game.generateRandomPosition());
        assertEquals(0, game.getTreasures().size());
        game.checkTreasure();
        assertEquals(0, game.getTreasures().size());

        // Collides with treasure.
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        assertEquals(0, game.getInventory().getTreasures().size());

        game.checkTreasure();

        assertEquals(0, game.getTreasures().size());
        assertFalse(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertEquals(1, game.getInventory().getTreasures().size());

        // Spawn another treasure
        Position charPos = game.generateRandomPosition();
        game.getCharacter().setCharacterPos(charPos);
        game.spawnTreasure();
        assertEquals(1, game.getTreasures().size());
        assertTrue(game.getTreasures().contains(game.getSpawnTreasurePos()));
        assertFalse(charPos == game.getSpawnTreasurePos());

        game.getCharacter().setCharacterPos(game.getSpawnTreasurePos());
        game.checkTreasure();

        assertEquals(0, game.getTreasures().size());
        assertFalse(game.getTreasures().contains(game.getSpawnTreasurePos()));
        //   assertEquals(1, game.getInventory().getTreasures().size()); BOTH OPTIONS WORK DEPENDING ON WHAT
        //   assertEquals(2, game.getInventory().getTreasures().size()); TREASURE YOU GET.

    }

    @Test
    void testGenerateRandomPosition() {
        // Test whether random position gives a position between the boundaries.
        Position randomPos = game.generateRandomPosition();
        assertTrue(randomPos.getPosX() >= 0 && randomPos.getPosX() <= 570
                && randomPos.getPosY() >= 55 && randomPos.getPosY() <= 535);
    }

    @Test
    void testIsValidPosition() {
        // Test #1: Check is valid position normally
        Position randomPos = game.generateRandomPosition();
        while (!game.isValidPosition(randomPos)) {
            randomPos = game.generateRandomPosition();
        }
        assertTrue(game.isValidPosition(randomPos));

        // Test #2: Coin is colliding
        Position inCoinPos = game.getCoinPosStart(); // Uses the initial coin at the start to check if it's valid.
        assertFalse(game.isValidPosition(inCoinPos));

        // Add another coin to board, check if valid.
        game.spawnCoins();
        Position anotherCoinPos = game.getCoinPos();
        assertFalse(game.isValidPosition(anotherCoinPos));

        // Test #3: Treasure is colliding.
        game.spawnTreasure();
        Position treasurePos = game.getSpawnTreasurePos();
        assertFalse(game.isValidPosition(treasurePos));

        // Test #4: Character is colliding.
        assertFalse(game.isValidPosition(game.getCharacter().getCharacterPos()));

        // Test 5: Boundary checking
        game.getCoin().remove(inCoinPos);
        game.getCoin().remove(anotherCoinPos);
        game.getTreasures().remove(treasurePos);

        Position boundaryPos1 = new Position(0, 55);
        assertTrue(game.isValidPosition(boundaryPos1));

        Position boundaryPos2 = new Position(-1, 0);
        assertFalse(game.isValidPosition(boundaryPos2));

        Position boundaryPos3 = new Position(0, 54);
        assertFalse(game.isValidPosition(boundaryPos3));

        Position boundaryPos4 = new Position(4, 60);
        assertTrue(game.isValidPosition(boundaryPos4));

        Position boundaryPos5 = new Position(570, 65);
        assertTrue(game.isValidPosition(boundaryPos5));

        Position boundaryPos6 = new Position(571, 9);
        assertFalse(game.isValidPosition(boundaryPos6));

        Position boundaryPos7 = new Position(9, 535);
        assertTrue(game.isValidPosition(boundaryPos7));

        Position boundaryPos8 = new Position(10, 536);
        assertFalse(game.isValidPosition(boundaryPos8));
    }

    @Test
    void testMakeValidRandomPosition() {
        Position pos = new Position(30, 80);
        assertFalse(game.isValidPosition(pos));
        Position newPos = game.makeValidRandomPos(pos);
        assertTrue(game.isValidPosition(newPos));

    }

    @Test
    void testOutOfBoundary() {
        // Test #1
        assertFalse(game.outOfBoundary(new Position(0, 55)));

        // Test #2
        assertFalse(game.outOfBoundary(new Position(1, 56)));

        // Test #3
        assertFalse(game.outOfBoundary(new Position(0, 535)));

        // Test #4
        assertFalse(game.outOfBoundary(new Position(570, 55)));

        // Test #5
        assertTrue(game.outOfBoundary(new Position(571, 0)));

        // Test #6
        assertTrue(game.outOfBoundary(new Position(0, 536)));

        // Test #7
        assertTrue(game.outOfBoundary(new Position(670, 5)));

        // Test #8
        assertTrue(game.outOfBoundary(new Position(7, 539)));

        // Test #9
        assertFalse(game.outOfBoundary(new Position(game.getMaxX(), (game.getMaxY() + 54))));

        // Test #10
        assertFalse(game.outOfBoundary(new Position(9, 60)));

        // More Tests
        assertTrue(game.outOfBoundary(new Position(-1, -1)));
        assertTrue(game.outOfBoundary(new Position(10, 54)));
        assertTrue(game.outOfBoundary(new Position(-1, 5)));

        assertTrue(game.outOfBoundary(new Position(571, 536)));
        assertTrue(game.outOfBoundary(new Position(36, 540)));
        assertTrue(game.outOfBoundary(new Position(690, 54)));
        assertFalse(game.outOfBoundary(new Position(5, 80)));

    }

    @Test
    void testSetInventory() {
        assertEquals(0, game.getInventory().getTreasures().size());
        Inventory inventory = new Inventory();
        inventory.addTreasure(new Treasure("lol"));
        assertEquals(1, inventory.getTreasures().size());

        game.setInventory(inventory);
        assertEquals(1, game.getInventory().getTreasures().size());
    }

    @Test
    void testSetTreasure() {
        Set<Position> treasures = new HashSet<>();
        treasures.add(new Position(2, 3));
        game.setTreasures(treasures);

        assertEquals(new Position(2, 3), game.getTreasures().stream().findFirst().get());
    }

    @Test
    void testSetTreasurePos() {
        game.getTreasures().remove(game.getSpawnTreasurePos());

        // Set Spawn Treasure:
        game.setSpawnTreasurePos(new Position(4, 5));
        assertEquals(new Position(4, 5), game.getSpawnTreasurePos());

        // Set Check Treasure
        game.setCheckTreasurePos(new Position(2, 3));
        assertEquals(new Position(2, 3), game.getCheckTreasurePos());
    }

    @Test
    void testSetCoin() {
        game.getCoin().remove(game.getCoinPosStart());

        // Set Spawn Coin
        game.setCoinPosStart(new Position(5, 6));
        assertEquals(new Position(5, 6), game.getCoinPosStart());

        // Set Coin Pos
        game.setCoinPos(new Position(8, 7));
        assertEquals(new Position(8, 7), game.getCoinPos());

        // Set Check Coin Pos
        game.setCheckCoinPos(new Position(12, 8));
        assertEquals(new Position(12, 8), game.getCheckCoinPos());

        // Set Coin Amount
        assertEquals(0, game.getCoinAmount());
        game.setCoinAmount(2);
        assertEquals(2, game.getCoinAmount());
    }
}