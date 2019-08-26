package me.althaf;

import lombok.Getter;
import lombok.Setter;
import me.althaf.models.FieldDefinition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CrawlCommandResult implements IResult {

    List<FieldDefinition> listOfFields;

    private final static String FIELD_DEF_TABLE_NAME = "field_definitions";

    @Getter @Setter
    Connection connection;

    @Getter @Setter
    private String identifier;

    public CrawlCommandResult(String fileName, List<FieldDefinition> listOfFields){

        this.identifier = fileName;
        this.listOfFields = listOfFields;
    }


    @Override
    public void persist() {

        try {
            this.deleteExistingRecords();

            this.writeFieldDefinitionsToTable();

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    protected void deleteExistingRecords() throws SQLException {

        PreparedStatement prepStatement =
                connection.prepareStatement("delete from "+ FIELD_DEF_TABLE_NAME+ " where identifier=?");

        prepStatement.setString(1,this.identifier);

        prepStatement.execute();
    }

    protected void writeFieldDefinitionsToTable()  {

        this.listOfFields.forEach(this::insertFieldDefinition);

    }

    private void insertFieldDefinition(FieldDefinition fd){

        try {
            PreparedStatement preparedStatement = getPreparedStatementForField(fd);

            if (preparedStatement != null) {

                preparedStatement.executeQuery();
            }

        }catch(SQLException sqex){
            sqex.printStackTrace();
        }

    }

    private PreparedStatement getPreparedStatementForField( FieldDefinition fd) {
        try {

            PreparedStatement prepInsertStatement =
                    connection.prepareStatement(
                            "INSERT INTO " +
                                    FIELD_DEF_TABLE_NAME +
                                    " VALUES( ? , ?, ?, ?, ? ) ");

            prepInsertStatement.setString(1,this.identifier);

            prepInsertStatement.setString(2,fd.getFieldName());

            prepInsertStatement.setString(3,fd.getDataType());

            prepInsertStatement.setInt(4,fd.getCountNullValues());

            prepInsertStatement.setInt(5,fd.getCountNonNullValues());

            return prepInsertStatement;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }


    }

    @Override
    public void display() {

        this.listOfFields.forEach((fd) -> {
            System.out.println(fd.toString());
        });

    }

    public boolean canPersist() {
        try{
            if(connection != null)
                return connection.isValid(10);
            else
                return false;
        }catch(Exception ex){
            return false;
        }
    }

}
