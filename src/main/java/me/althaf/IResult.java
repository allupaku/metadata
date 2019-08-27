package me.althaf;

import java.sql.Connection;
import java.util.List;

public interface IResult {

    public void persist();

    public void display();

    public List getListOfFields();

    public String getIdentifier();

    public boolean canPersist();

    public void setConnection(Connection connection);

    public int getNumberOfFields();

}
