package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes the JSON representation of the game to a file.

public class JsonWriter {

    private String sourceFile;
    private PrintWriter writer;

    // EFFECTS: Constructs a writer to write the file to the source file.
    public JsonWriter(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer and throws an exception if file cannot be opened for writing.
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(sourceFile));
    }

    // MODIFIES: this
    // EFFECTS: writes the JSON representation of the game to the source file.
    public void write(Game game) {
        JSONArray jsonEnemies = new JSONArray();
        JSONArray jsonInventory = new JSONArray();
        for (Enemy enemy : game.getEnemies()) {
            JSONObject jsonEnemy = getJsonEnemy(enemy);
            jsonEnemies.put(jsonEnemy);
        }

        for (Treasure treasure : game.getInventory().getTreasures()) {
            jsonInventory.put(getJsonTreasure(treasure));
        }


        JSONObject json = new JSONObject();
        json.put("playerPos", getJsonPos(game.getCharacter().getCharacterPos()));
        json.put("enemies", jsonEnemies);
        json.put("inventory", jsonInventory);
        checkers(game, json);
        hpCoinCheck(game, json);
        writer.print(json.toString());
    }

    // MODIFIES: this
    // EFFECTS: checkers used to see what state game is in. Writes the state into the JSOBObject.
    public void checkers(Game game, JSONObject json) {
        if (game.getTreasures().contains(game.getSpawnTreasurePos())) {
            json.put("spawnTreasurePos", getJsonPos(game.getSpawnTreasurePos()));
        } else {
            json.put("checkTreasurePos", getJsonPos(game.getCheckTreasurePos()));
        }
        if (game.getCoin().contains(game.getCoinPosStart())) {
            json.put("coinPosStart", getJsonPos(game.getCoinPosStart()));
        } else if (game.getCoin().contains(game.getCoinPos())) {
            json.put("coinPos", getJsonPos(game.getCoinPos()));
        } else {
            json.put("checkCoinPos", getJsonPos(game.getCheckCoinPos()));
        }
    }

    // MODIFIES: this
    // EFFECTS: check used to put the hp and coin amount into the JSON representation of it into the file.
    public void hpCoinCheck(Game game, JSONObject json) {
        json.put("hp", game.getCharacter().getHp());
        json.put("coinAmount", game.getCoinAmount());
    }

    // MODIFIES: this
    // EFFECTS: puts the key, x and y, representing the position into the json representation of it.
    // This will be added to the json file which will later read and decoded.
    public JSONObject getJsonPos(Position pos) {
        JSONObject json = new JSONObject();
        json.put("x", pos.getPosX());
        json.put("y", pos.getPosY());
        return json;
    }

    // MODIFIES: this
    // EFFECTS: puts the key, x and y, representing the enemy position into the json representation of it.
    // This will be added to the json file which will later read and decoded.
    public JSONObject getJsonEnemy(Enemy enemy) {
        JSONObject json = new JSONObject();
        json.put("x", enemy.getEnemyPos().getPosX());
        json.put("y", enemy.getEnemyPos().getPosY());
        json.put("enemyHp", enemy.getHp());
        return json;
    }

    // MODIFIES: this
    // EFFECTS: puts the key, name and quantity, representing the treasure into the json representation of it.
    // This will be added to the json file which will be later read and decoded.
    public JSONObject getJsonTreasure(Treasure treasure) {
        JSONObject json = new JSONObject();
        json.put("name", treasure.getName());
        json.put("quantity", treasure.getQuantity());
        return json;
    }

    // MODIFIES: this
    // EFFECTS: closes the writer.
    public void close() {
        writer.close();
    }

}
