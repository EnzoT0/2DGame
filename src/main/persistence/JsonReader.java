package persistence;

import model.Enemy;
import model.Game;
import model.Position;
import model.Treasure;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.GameKeyHandler;
import ui.Inventory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads the game from the JSON data stores in the file.

// Some parts of the code is referenced from the JsonReader class in the JsonSerializationDemo project (readFile):
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/JsonReader.java

public class JsonReader {
    private String fileSource;

    // EFFECTS: Constructs a reader to read the file source.
    public JsonReader(String fileSource) {
        this.fileSource = fileSource;
    }

    // MODIFIES: game
    // EFFECTS: read source files as string and parses together the information
    // needed to load the game. This includes getting the character position, enemy position, inventory,
    // treasure positions, coin positions, hp, etc.
    public Game loadGame(GameKeyHandler keyHandler) throws IOException {
        Game game = new Game(keyHandler);
        String jsonData = readFile(fileSource);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONObject jsonPlayerPos = jsonObject.getJSONObject("playerPos");
        game.getCharacter().setCharacterPos(getJsonPos(jsonPlayerPos));

        JSONArray jsonEnemies = jsonObject.getJSONArray("enemies");
        for (int i = 0; i < jsonEnemies.length(); i++) {
            JSONObject jsonEnemy = jsonEnemies.getJSONObject(i);
            Enemy enemy = getEnemy(jsonEnemy);
            game.getEnemies().add(enemy);
        }

        jsonInventory(game, jsonObject);
        checkTreasures(game, jsonObject);
        checkCoins(game, jsonObject);
        checkAmount(game, jsonObject);
        return game;

    }

    // EFFECTS: reads source files as string and returns it.
    // Note: Code referenced from JsonSerializationDemo
    public String readFile(String  source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: game
    // EFFECTS: reads the json file and gets the information of inventory from it. It then
    // adds it to the new inventory.
    public void jsonInventory(Game game, JSONObject jsonObject) {
        JSONArray jsonInventory = jsonObject.getJSONArray("inventory");
        Inventory inventory = new Inventory();
        for (int i = 0; i < jsonInventory.length(); i++) {
            JSONObject jsonTreasure = jsonInventory.getJSONObject(i);
            Treasure treasure = getTreasureObj(jsonTreasure);
            treasure.setQuantity(getTreasureQuantity(jsonTreasure));
            inventory.addSilentTreasure(treasure);
        }
        game.setInventory(inventory);
    }

    // MODIFIES: game
    // EFFECTS: reads the json file and gets the information of treasure positions from it. It then
    // adds it to the new treasure positions.
    public void checkTreasures(Game game, JSONObject jsonObject) {
        if (jsonObject.has("spawnTreasurePos")) {
            JSONObject jsonTreasurePos = jsonObject.getJSONObject("spawnTreasurePos");
            Position pos = getJsonPos(jsonTreasurePos);
            game.setSpawnTreasurePos(pos);
            game.getTreasures().add(pos);
        } else {
            JSONObject jsonTreasurePos = jsonObject.getJSONObject("checkTreasurePos");
            Position pos = getJsonPos(jsonTreasurePos);
            game.setCheckTreasurePos(pos);
            game.getTreasures().add(pos);
        }
    }

    // MODIFIES: game
    // EFFECTS: reads the json file and gets the information of the position of the coins from it. It then
    // adds it to the new coins position.
    public void checkCoins(Game game, JSONObject jsonObject) {
        game.getCoin().remove(game.getCoinPosStart());
        if (jsonObject.has("coinPosStart")) {
            JSONObject jsonCoinPos = jsonObject.getJSONObject("coinPosStart");
            Position pos = getJsonPos(jsonCoinPos);
            game.setCoinPosStart(pos);
            game.getCoin().add(pos);
        } else if (jsonObject.has("coinPos")) {
            JSONObject jsonCoinPos = jsonObject.getJSONObject("coinPos");
            Position pos = getJsonPos(jsonCoinPos);
            game.setCoinPos(pos);
            game.getCoin().add(pos);
        } else {
            JSONObject jsonCoinPos = jsonObject.getJSONObject("checkCoinPos");
            Position pos = getJsonPos(jsonCoinPos);
            game.setCheckCoinPos(pos);
            game.getCoin().add(pos);
        }
    }

    // MODIFIES: game
    // EFFECTS: reads the json file and gets the information of coinAmount from it. It then
    // sets the coinAmount of the game to said amount.
    public void checkAmount(Game game, JSONObject jsonObject) {
        int hp = jsonObject.getInt("hp");
        int coinAmount = jsonObject.getInt("coinAmount");
        int atk = jsonObject.getInt("charAtk");
        game.getCharacter().setHp(hp);
        game.setCoinAmount(coinAmount);
        game.getCharacter().setAtk(atk);
    }

    // EFFECTS: finds the key x and y from the jsonObject and returns a new position with the given information.
    public Position getJsonPos(JSONObject jsonObject) {
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");
        return new Position(x, y);
    }

    // EFFECTS: finds the key x and y from the jsonObject and returns a new enemy with said
    // position with the given information.
    public Enemy getEnemy(JSONObject jsonObject) {
        int x = jsonObject.getInt("x");
        int y = jsonObject.getInt("y");
        int enemyHp = jsonObject.getInt("enemyHp");
        Enemy enemy = new Enemy(new Position(x, y));
        enemy.setHp(enemyHp);
        return enemy;
    }

    // EFFECTS: finds the key name from the jsonObject and returns a new treasure with the given information.
    public Treasure getTreasureObj(JSONObject jsonObject) {
        String treasureName = jsonObject.getString("name");
        return new Treasure(treasureName);
    }

    // EFFECTS: finds the key quantity from the jsonObject and returns the quantity of the treasure.
    public int getTreasureQuantity(JSONObject jsonObject) {
        int treasureQuantity = jsonObject.getInt("quantity");
        return treasureQuantity;
    }
}
