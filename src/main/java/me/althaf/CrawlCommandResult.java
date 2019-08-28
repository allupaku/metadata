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

    @Getter @Setter
    List<FieldDefinition> listOfFields;

    @Getter @Setter
    String fieldsTableName;

    @Getter @Setter
    String totalsTableName;

    @Getter @Setter
    Connection connection;

    @Getter @Setter
    private String identifier;

    @Getter @Setter
    private int totalNumberOfRecords = 0;

    public CrawlCommandResult(String fileName, List<FieldDefinition> listOfFields, int numRecords){

        this.identifier = fileName;
        this.listOfFields = listOfFields;

        this.totalNumberOfRecords = numRecords;
    }


    @Override
    public void persist() {
        if(canPersist() && connection!=null){
            try {
                this.deleteExistingRecords();

                this.writeFieldDefinitionsToTable();

                this.writeTotalsForFile();

            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    protected void deleteExistingRecords() throws SQLException {

        this.deleteExistingFieldDefinitions();

        this.deleteTotalsForFile();
    }

    private void deleteTotalsForFile() throws SQLException{
        PreparedStatement prepStatement =
                connection.prepareStatement("delete from "+ totalsTableName + " where filename=?");

        prepStatement.setString(1,this.identifier);

        prepStatement.execute();

        prepStatement.close();
    }

    private void deleteExistingFieldDefinitions() throws SQLException{

        PreparedStatement prepStatement =
                connection.prepareStatement("delete from "+ fieldsTableName + " where filename=?");

        prepStatement.setString(1,this.identifier);

        prepStatement.execute();

        prepStatement.close();
    }

    protected void writeFieldDefinitionsToTable()  {

        this.listOfFields.forEach(this::insertFieldDefinition);

    }

    protected void writeTotalsForFile() {
        try {
            PreparedStatement prepInsertStatement =
                    connection.prepareStatement(
                            "INSERT INTO " +
                                    totalsTableName +
                                    " VALUES( ? , ? ) ");

            prepInsertStatement.setString(1, this.identifier);

            prepInsertStatement.setInt(2, this.totalNumberOfRecords);

            prepInsertStatement.execute();

            prepInsertStatement.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void insertFieldDefinition(FieldDefinition fd){

        try {
            PreparedStatement preparedStatement = getPreparedStatementForField(fd);

            if (preparedStatement != null) {

                preparedStatement.execute();
            }

            preparedStatement.close();

        }catch(SQLException sqex){
            sqex.printStackTrace();
        }

    }

    private PreparedStatement getPreparedStatementForField( FieldDefinition fd) {
        try {

            PreparedStatement prepInsertStatement =
                    connection.prepareStatement(
                            "INSERT INTO " +
                                    fieldsTableName +
                                    " VALUES( ? , ?, ?, ?, ? ) ");

            prepInsertStatement.setString(1,this.identifier);

            prepInsertStatement.setString(2,fd.getFieldName());

            prepInsertStatement.setString(3,fd.getDataType());

            prepInsertStatement.setInt(4,fd.getCountNonNullValues());

            prepInsertStatement.setInt(5,fd.getCountNullValues());

            return prepInsertStatement;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }


    }

    @Override
    public void display() {

        System.out.println("Total number of records : " + this.getTotalNumberOfRecords()+ "\r\n");

        System.out.println("Total Number of fields : " + this.listOfFields.size());
        System.out.printf("%-40s  %-20s %-20s  %-20s%n","Field Name", "Data  Type","Null values", "Non Null Values");
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

    @Override
    public int getNumberOfFields() {
        return this.listOfFields.size();
    }

}
