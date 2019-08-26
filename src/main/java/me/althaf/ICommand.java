package me.althaf;

import java.sql.Connection;

public interface ICommand {

    public IResult execute();

    public void setFileName(String fileName);

    public void setConnection(Connection conn);
}
