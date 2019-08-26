package me.althaf.models;

import jdk.internal.joptsimple.internal.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FieldSummaryObserver implements
        IEntryObserver<Map<String,String>, List<FieldDefinition>>{

    Map<String, FieldDefinition> fieldsDiscovered = new HashMap<>();

    @Getter @Setter
    AtomicInteger countRecords = new AtomicInteger();

    // Identifier will usually be the file name, which can be used to quicly identify the file
    @Getter @Setter
    protected String identifier;

    public FieldSummaryObserver(String identifier){
        if(identifier==null || identifier.isEmpty()){
            throw new RuntimeException("Invalid identifier passed in for field summary");
        }
        this.identifier = identifier;
    }

    @Override
    public void observe(Map<String, String> data) {

        int prevCount = this.countRecords.getAndIncrement();

        Set<String> alreadyDiscovered = new HashSet<>(fieldsDiscovered.keySet());
        // loop through all the fields and process.
        // In future, if heavy processing need to done, this can be changed to a different thread.
        data.forEach((key,value) -> {
            if(fieldsDiscovered.containsKey(key)){
                // If we already came across this field from previous records
                fieldsDiscovered.get(key).handle( value );
                alreadyDiscovered.remove(key);
            }else{
                // If we never came across this field
                FieldDefinition fd = new FieldDefinition(key,prevCount);
                fd.handle(value);
                fieldsDiscovered.put( key, fd );
            }
        });

        alreadyDiscovered.forEach((key) -> { fieldsDiscovered.get(key).handle(null); });
    }

    @Override
    public List<FieldDefinition> getResult() {

        return new ArrayList<FieldDefinition>(fieldsDiscovered.values());
    }

    @Override
    public String toString(){

        StringBuffer sb = new StringBuffer();

        fieldsDiscovered.values().forEach((fd) -> { sb.append(fd.toString()); } );

        return sb.toString();
    }
}
