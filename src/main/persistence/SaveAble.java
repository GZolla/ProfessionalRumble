package persistence;

import model.Player;
import org.json.JSONArray;

import java.io.FileNotFoundException;

public interface SaveAble extends Writable {
    default void save(int currentIndex, Player p, String type) {
        SaveAble[] playerStored = p.loadAll(type);

        JSONArray jsonItems = new JSONArray();
        for (SaveAble s : playerStored) {
            jsonItems.put(s.toJson());
        }
        jsonItems.put(currentIndex,this.toJson());

        JsonWriter writer = new JsonWriter(type + "/" + p.getId() + ".json");
        try {
            writer.open();
            writer.writeArray(jsonItems);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
