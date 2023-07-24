package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import ui.Inventory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:savepath.json");
            writer.open();
            fail("FileNotFoundException expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testGameSpawnTreasure() {
        try {
            Game game = new Game(39, 21);
/*            EnemyList enemyList = new EnemyList();
            List<Enemy> enemies = enemyList.addEnemies(1);
            game.setEnemies(enemies);*/

            Position pos = new Position(1, 3);
            game.setSpawnTreasurePos(pos);
            game.getTreasures().add(pos);

            JsonWriter jsonWriter = new JsonWriter("./data/testNewGame.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testNewGame.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getSpawnTreasurePos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testGameCheckTreasure() {
        try {
            Game game = new Game(39, 21);

            Position pos = new Position(1, 3);
            game.setCheckTreasurePos(pos);
            game.getTreasures().add(pos);

            JsonWriter jsonWriter = new JsonWriter("./data/testNewGame2.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testNewGame2.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getCheckTreasurePos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testGameInventory() {
        try {
            Game game = new Game(39, 21);

            Position pos = new Position(1, 3);
            game.setSpawnTreasurePos(pos);
            game.getTreasures().add(pos);

            Inventory inventory = new Inventory();
            inventory.addTreasure(new Treasure("Aponia"));
            inventory.addTreasure(new Treasure("Elysia"));
            game.setInventory(inventory);

            JsonWriter jsonWriter = new JsonWriter("./data/testNewGame3.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testNewGame3.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getSpawnTreasurePos());

            assertEquals(2, game.getInventory().getTreasures().size());
            assertEquals("Aponia", game.getInventory().getTreasures().get(0).getName());
            assertEquals("Elysia", game.getInventory().getTreasures().get(1).getName());
            assertEquals(1, game.getInventory().getTreasures().get(0).getQuantity());
            assertEquals(1, game.getInventory().getTreasures().get(1).getQuantity());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testCoinHandlingStart() {
        try {
            Game game = new Game(39, 21);

            Position pos = new Position(1, 3);
            game.setCheckTreasurePos(pos);
            game.getTreasures().add(pos);
            Position coinSpawnPos = game.getCoinPosStart();

            JsonWriter jsonWriter = new JsonWriter("./data/testCoinHandling.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testCoinHandling.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getCheckTreasurePos());
            assertEquals(coinSpawnPos, game.getCoinPosStart());


        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testCoinHandling() {
        try {
            Game game = new Game(39, 21);

            Position pos = new Position(1, 3);
            game.setCheckTreasurePos(pos);
            game.getTreasures().add(pos);

            game.getCoin().remove(game.getCoinPosStart());
            Position coinPos = new Position(2, 3);
            game.setCoinPos(coinPos);
            game.getCoin().add(coinPos);

            JsonWriter jsonWriter = new JsonWriter("./data/testCoinHandling2.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testCoinHandling2.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getCheckTreasurePos());
            assertEquals(coinPos, game.getCoinPos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testCoinHandlingCheck() {
        try {
            Game game = new Game(39, 21);

            Position pos = new Position(1, 3);
            game.setCheckTreasurePos(pos);
            game.getTreasures().add(pos);

            game.getCoin().remove(game.getCoinPosStart());
            Position coinPos = new Position(2, 3);
            game.setCheckCoinPos(coinPos);
            game.getCoin().add(coinPos);

            JsonWriter jsonWriter = new JsonWriter("./data/testCoinHandling3.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testCoinHandling3.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getCheckTreasurePos());
            assertEquals(coinPos, game.getCheckCoinPos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testEnemies() {
        try {
            Game game = new Game(39, 21);
            EnemyList enemyList = new EnemyList();
            List<Enemy> enemies = enemyList.addEnemies(2);
            game.setEnemies(enemies);

            Position pos = new Position(1, 3);
            game.setSpawnTreasurePos(pos);
            game.getTreasures().add(pos);

            JsonWriter jsonWriter = new JsonWriter("./data/testEnemies.json");
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();

            JsonReader jsonReader = new JsonReader("./data/testEnemies.json");
            game = jsonReader.loadGame();
            assertEquals(pos, game.getSpawnTreasurePos());
            assertEquals(2, game.getEnemies().size());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }
}
