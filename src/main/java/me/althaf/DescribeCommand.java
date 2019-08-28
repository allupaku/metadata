package me.althaf;

import lombok.Getter;
import lombok.Setter;
import me.althaf.models.FieldDefinition;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DescribeCommand implements ICommand {


    @Getter @Setter
    protected String fileName;



    @Getter @Setter
    Connection connection;

    public DescribeCommand(String fileName){
        this.fileName = fileName;
    }

    @Override
    public IResult execute() {

        DescribeCommandResult res = new DescribeCommandResult(this.fileName);

        this.populateFieldsResult(res);

        this.populateTotalsResult(res);

        return res;
    }

    protected void populateTotalsResult(DescribeCommandResult res) {

        try{
            PreparedStatement prepStatement =
                    connection.prepareStatement("select * from "+ TOTALS_TABLE_NAME+ " where filename=?");

            prepStatement.setString(1,this.fileName);

            ResultSet rs = prepStatement.executeQuery();

            rs.first();

            res.setTotalNumberOfRecords(rs.getInt("total"));

            rs.close();

        }catch(Exception e){
            System.out.println("Invalid sql : " + e.getMessage());
        }
    }

    protected void populateFieldsResult(DescribeCommandResult res) {

        try{
            PreparedStatement prepStatement =
                    connection.prepareStatement("select * from "+ FIELD_DEF_TABLE_NAME+ " where filename=?");

            prepStatement.setString(1,this.fileName);

            ResultSet rs = prepStatement.executeQuery();

            while(rs.next()){

                FieldDefinition fd = new FieldDefinition(rs.getString("fieldname"));

                fd.setCountNonNullValues(rs.getInt("numnonnull"));

                fd.setCountNullValues(rs.getInt("numnull"));

                fd.setDataType(rs.getString("datatype"));

                fd.setFileName(this.fileName);

                res.addFieldToResult(fd);
            }

            rs.close();

        }catch(Exception e){
            System.out.println("Invalid sql : " + e.getMessage());
        }
    }
}
