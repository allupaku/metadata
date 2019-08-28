package me.althaf;

import java.sql.Connection;

public interface ICommand {

    public final static String FIELD_DEF_TABLE_NAME = "field_definitions";

    public final static String TOTALS_TABLE_NAME = "records_total";

    public IResult execute();

    public void setFileName(String fileName);

    public void setConnection(Connection conn);
}
