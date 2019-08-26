package me.althaf.parsers;
import me.althaf.models.FieldSummaryObserver;
import me.althaf.models.IEntryObserver;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JSONFileParser extends AbstractFileParser<Map<String,String>> implements IFileParser {

    JSONParser parserObject = new JSONParser();

    JSONArray rootArray;

    AtomicInteger currentIndex = new AtomicInteger();

    public JSONFileParser(String filename, IEntryObserver observer) throws Exception{
        super(filename);

        this.addObserver(observer);

        Object obj = parserObject.parse(this.fileReader);
        // I am ignoring other formats, which are not starting with an array.
        // Different strategies can be taken for handling such data.
        if(obj instanceof JSONArray){
            rootArray = (JSONArray) obj;
        }else{
            throw new RuntimeException("No root array found in the file");
        }
    }

    @Override
    public boolean doValidate() {
        return true;
    }

    @Override
    public Map<String, String> doParse() throws Exception {

        if(rootArray.size() > currentIndex.get()) {

            Map<String,String> returnObject = new HashMap<>();

            JSONObject obj = (JSONObject) rootArray.get(currentIndex.getAndIncrement());

            obj.forEach( (key, val) -> {
                String strVal;

                if(val instanceof String){
                    strVal = (String) val;
                }else{
                    strVal = "Object OR Array";
                }

                returnObject.put((String)key, strVal);
            });
            return returnObject;
        }
        return null;
    }
}
