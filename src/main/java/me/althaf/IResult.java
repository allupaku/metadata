package me.althaf;

import java.sql.Connection;

public interface IResult {

    public void persist();

    public void display();

    public String getIdentifier();

    public boolean canPersist();

    public void setConnection(Connection connection);

}
