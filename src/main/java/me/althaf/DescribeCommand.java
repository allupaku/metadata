package me.althaf;

import lombok.Getter;
import lombok.Setter;

public class DescribeCommand implements ICommand {

    @Getter @Setter
    protected String fileName;

    public DescribeCommand(String fileName){
        this.fileName = fileName;
    }

    @Override
    public IResult execute() {

        return null;
    }
}
