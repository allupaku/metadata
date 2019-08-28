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
    List<FieldDefinition> listOfFields = new ArrayList<>();

    @Getter @Setter
    protected int totalNumberOfRecords = 0;

    public DescribeCommandResult(String fileName){
        this.identifier = fileName;
    }

    public void addFieldToResult(FieldDefinition fd){
        this.listOfFields.add(fd);
    }

    @Override
    public void persist() {
        // Do nothing / or later log how many times we have seen this.
    }

    @Override
    public void display() {

        System.out.println("Total number of records : " + this.getTotalNumberOfRecords()+ "\r\n");

        System.out.println("Total Number of fields : " + this.listOfFields.size());

        System.out.printf("%-40s  %-20s %-20s  %-20s%n","Field Name", "Data  Type","Null values", "Non Null Values");
        if(listOfFields.size() > 0) {
            this.listOfFields.forEach((fd) -> {
                System.out.println(fd.toString());
            });
        }else{
            System.out.println("There are not fields discovered yet for this file! Please run crawl command.");
        }
    }

    @Override
    public boolean canPersist() {
        return false;
    }

    @Override
    public void setConnection(Connection connection) {
        // Do nothing as this implementation doensnt need a database
    }

    @Override
    public int getNumberOfFields() {
        return this.listOfFields.size();
    }
}
