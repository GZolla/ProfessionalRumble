package persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = "./data/" + source;
    }

    // EFFECTS: reads file and returns it as JSONObject;
    // throws IOException if an error occurs reading data from file
    public JSONObject read() throws IOException {
        String jsonData = readFile(source);
        return new JSONObject(jsonData);
    }

    // EFFECTS: reads file and returns it as JSONArray;
    // throws IOException if an error occurs reading data from file
    public JSONArray arrayRead() throws IOException {
        String jsonData = readFile(source);
        return new JSONArray(jsonData);
    }



    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }


}
