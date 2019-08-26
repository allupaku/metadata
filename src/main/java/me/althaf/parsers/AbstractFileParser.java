package me.althaf.parsers;

import lombok.Getter;
import lombok.Setter;
import me.althaf.models.FieldDefinition;
import me.althaf.models.IEntryObserver;
import me.althaf.models.FieldSummaryObserver;
import me.althaf.models.ResultCounter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractFileParser<T> implements IFileParser, IRecordParser<T>, IRecordObserver<T> {

    @Getter
    protected String filename;

    @Getter @Setter
    protected FileReader fileReader;

    protected IEntryObserver<Map<String,String>, Integer> resultCounter = new ResultCounter();

    protected List<IEntryObserver> observers = new LinkedList<>();

    public AbstractFileParser(String filename) throws Exception{
        this.filename = filename;

        if(!this.validateFile(filename)) {
            throw new RuntimeException("Parser specific validation failed!");
        }

        this.fileReader = new FileReader(filename);

        this.observers.add(new ResultCounter());
    }

    public void addObserver(IEntryObserver observer){
        this.observers.add(observer);
    }

    protected boolean validateFile(String filename) throws Exception{

        Path filepath = Paths.get(filename);
        // Checking basic qualities required for a file to be read by our application
        if(!filepath.isAbsolute()){
            throw new RuntimeException("Please provide absolute path");
        }
        if(!Files.exists(filepath)){
            throw new FileNotFoundException("File not found " + filepath.toAbsolutePath());
        }
        if(!Files.isReadable(filepath)){
            throw new RuntimeException("File not readable " + filepath.toAbsolutePath());
        }
        // any further parser specific validation, like format checking or checks for encoding
        // etc can be done here.
        return doValidate();
    }

    public void observe(T data){
        // This can be converted to a separate thread for parellel processing.
        //        new Thread(() -> {  }).start();
        observers.forEach((observer) -> { observer.observe(data); });
    }

    // This can be overridden, if a more efficient implementation is required.
    public void parse(){
        T data = null;
        do{
            try {
                data = doParse();
                if(Objects.nonNull(data))
                    this.observe(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }while(Objects.nonNull(data));
    }


    public int getCountOfRecords(){
        return resultCounter.getResult().intValue();
    }

    // For the validation of file from the implementation.
    public abstract boolean doValidate();

    // For each call of doParse, i am expecting one record to be returned for processing
    // The type of that record will be configurable by T.
    // If we know the specific type of the record, and can be bound to a bean , we can use that bean type.
    public abstract T doParse() throws Exception;


}
