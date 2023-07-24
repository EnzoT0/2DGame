package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import ui.Inventory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testNoFile() {
        JsonReader reader = new JsonReader("./data/lmaonofilemoment.json");
        try {
            Game game = reader.loadGame();
            fail("Did not except for this to pass");
        } catch (IOException e) {
            ///
        }
    }

    @Test
    void testReaderGameSpawnTreasure() {
        JsonReader jsonReader = new JsonReader("./data/testReaderSpawnTreasure.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getSpawnTreasurePos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }



    @Test
    void testReaderGameCheckTreasure() {
        JsonReader jsonReader = new JsonReader("./data/testReaderGameCheckTreasure.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getCheckTreasurePos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }


    @Test
    void testReaderGameInventory() {
        JsonReader jsonReader = new JsonReader("./data/testReaderGameInventory.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getSpawnTreasurePos());

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
    void testReaderCoinHandlingStart() {
        JsonReader jsonReader = new JsonReader("./data/testReaderCoinHandlingStart.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getCheckTreasurePos());
            assertEquals(new Position(1, 0), game.getCoinPosStart());


        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testReaderCoinHandling() {
        JsonReader jsonReader = new JsonReader("./data/testReaderCoinHandling.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getCheckTreasurePos());
            assertEquals(new Position(2, 3), game.getCoinPos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testReaderCoinHandlingCheck() {
        JsonReader jsonReader = new JsonReader("./data/testReaderCoinHandlingCheck.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getCheckTreasurePos());
            assertEquals(new Position(2, 3), game.getCheckCoinPos());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }

    @Test
    void testReaderEnemies() {
        JsonReader jsonReader = new JsonReader("./data/testReaderEnemies.json");
        try {
            Game game = jsonReader.loadGame();
            assertEquals(new Position(1, 3), game.getSpawnTreasurePos());
            assertEquals(2, game.getEnemies().size());

        } catch (FileNotFoundException e) {
            fail("did not expect this exception.");
        } catch (IOException e) {
            fail("did not expect this exception.");
        }
    }
}
