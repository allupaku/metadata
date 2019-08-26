package me.althaf;

public interface ICommand {

    public IResult execute();

    public void setFileName(String fileName
    );
}
