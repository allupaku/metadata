package me.althaf.parsers;

import com.opencsv.CSVReaderHeaderAware;
import me.althaf.models.FieldSummaryObserver;
import me.althaf.models.IEntryObserver;

import java.util.Map;

public class CSVFileParser extends AbstractFileParser<Map<String,String>> implements IFileParser {

    CSVReaderHeaderAware csvreader;

    public CSVFileParser(String filename, IEntryObserver observer) throws Exception {
        super(filename);

        this.addObserver(observer);

        // all file validation should be done above this.
        csvreader = new CSVReaderHeaderAware(this.fileReader);
    }

    @Override
    public boolean doValidate() {
        return true;
    }

    @Override
    public Map doParse(){
        try {
            return csvreader.readMap();
        }catch(Exception ex){
            return null;
        }
    }

}
