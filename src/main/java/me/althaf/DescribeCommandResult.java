package me.althaf;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;

public class DescribeCommandResult implements IResult {

    @Getter @Setter
    String identifier;

    public DescribeCommandResult(String fileName){
        this.identifier = fileName;
    }


    @Override
    public void persist() {
        // Do nothing / or later log how many times we have seen this.
    }

    @Override
    public void display() {

    }

    @Override
    public boolean canPersist() {
        return false;
    }

    @Override
    public void setConnection(Connection connection) {

    }


}
