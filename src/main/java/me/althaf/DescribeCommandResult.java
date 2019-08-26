package me.althaf;

import lombok.Getter;
import lombok.Setter;
import me.althaf.models.FieldDefinition;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DescribeCommandResult implements IResult {

    @Getter @Setter
    String identifier;

    @Getter @Setter
    List<FieldDefinition> fieldList = new ArrayList<>();

    public DescribeCommandResult(String fileName){
        this.identifier = fileName;
    }

    public void addFieldToResult(FieldDefinition fd){
        this.fieldList.add(fd);
    }

    @Override
    public void persist() {
        // Do nothing / or later log how many times we have seen this.
    }

    @Override
    public void display() {

        this.fieldList.forEach((fd) -> { System.out.println(fd.toString() );});
    }

    @Override
    public boolean canPersist() {
        return false;
    }

    @Override
    public void setConnection(Connection connection) {

    }


}
