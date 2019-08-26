package me.althaf.models;

import jdk.internal.joptsimple.internal.Strings;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class FieldDefinition {

    private  String STRING_TYPE = "STRING";

    protected String INT_TYPE = "INTEGER";

    protected String MIXED_TYPE = "MIXED";

    @Getter
    protected String fieldName;

    @Getter @Setter
    public String dataType = null;

    public AtomicInteger countNullValues = new AtomicInteger();

    public AtomicInteger countNonNullValues = new AtomicInteger();

    public int getCountNullValues() {
        return countNullValues.get();
    }

    public int getCountNonNullValues() {
        return countNonNullValues.get();
    }

    public FieldDefinition(String fieldName, int countNullValues) throws RuntimeException{
        if(fieldName == null || fieldName.isEmpty()){
            throw new RuntimeException("Invalid field name");
        }

        this.countNullValues.set(countNullValues);

        this.fieldName = fieldName;
    }

    public void handle(String value){

        if(value == null || value.isEmpty()){
            this.countNullValues.incrementAndGet();
        }else{
            this.countNonNullValues.incrementAndGet();

            try {
                // Relying on the exception thrown when parsing a string as an integer.
                Integer intVal = Integer.parseInt(value);

                if(dataType == null ) dataType = INT_TYPE;
                if(dataType == STRING_TYPE ) dataType = MIXED_TYPE;

            }catch( NumberFormatException nfe){
                if(dataType == null ) dataType = STRING_TYPE;
                if(dataType == INT_TYPE ) dataType = MIXED_TYPE;
            }

        }
    }

    public String toString(){
        return "FIELD NAME : \t\t" + this.fieldName + " \r\nTYPE : \t\t" + dataType + " \r\nNULL VALUES: \t\t" + getCountNullValues() + "\r\nNON NULL VALUES: \t\t" + getCountNonNullValues()+ "\r\n ============ \r\n";
    }
}
