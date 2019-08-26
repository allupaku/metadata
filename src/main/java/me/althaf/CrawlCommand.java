package me.althaf;

import lombok.Getter;
import lombok.Setter;
import me.althaf.exceptions.UnknownMimeTypeException;
import me.althaf.models.FieldDefinition;
import me.althaf.models.FieldSummaryObserver;
import me.althaf.models.IEntryObserver;
import me.althaf.parsers.CSVFileParser;
import me.althaf.parsers.JSONFileParser;
import me.althaf.parsers.AbstractFileParser;

import java.util.List;
import java.util.Objects;

public class CrawlCommand implements ICommand{

    @Getter @Setter
    AbstractFileParser fileParser;

    @Getter @Setter
    private String fileName;

    FieldSummaryObserver observer;

    @Getter
    CrawlCommandResult result;

    public CrawlCommand(String fileName) throws Exception{

        String mimetype = getFileMimeType(fileName);

        observer = new FieldSummaryObserver(fileName);

        if(mimetype.equals("csv")){
            this.fileParser = new CSVFileParser(fileName,observer);
        } else if(mimetype.equals("json")){
            this.fileParser = new JSONFileParser(fileName,observer);
        } else {
            throw new UnknownMimeTypeException();
        }

        this.fileName = fileName;
    }


    public IResult execute(){

        if(Objects.isNull(fileParser)) return null;

        this.fileParser.parse();

        List<FieldDefinition> resultList = this.observer.getResult();

        result = new CrawlCommandResult(this.getFileName(),resultList);

        return result;
    }


    // Just a helper method, in future this can be extended to advanced mime detection.
    public static String getFileMimeType(String filename){

        if(filename.endsWith("csv"))
            return "csv";
        if(filename.endsWith("json"))
            return "json";

        return "octet";

    }

}
