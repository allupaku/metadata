package me.althaf;

import com.google.common.base.Strings;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static String CRAWL_COMMAND = "crawl";
    private final static String DESCRIBE_COMMAND = "describe";

    private static Connection dbConnection;


    public static void main( String[] args ) {

        if(args.length <=1 ){
            System.err.println("Please pass in command and a file name");
            return;
        }

        String command = args[0];
        String filename = args[1];

        if(command==null || filename==null || filename.isEmpty() || command.isEmpty() ){
            System.err.println("Please pass command to run and file name");
            return;
        }

        ICommand commandToExecute = getiCommand(command, filename);
        IResult resultOfCommand = null;

        if(commandToExecute != null)
            resultOfCommand = commandToExecute.execute();

        Connection dbConnection  = getDBConnection();

        if(resultOfCommand != null){

            resultOfCommand.setConnection(dbConnection);

            if(resultOfCommand.canPersist() && dbConnection!=null)
                resultOfCommand.persist();

            resultOfCommand.display();
        }
    }

    private static Connection getDBConnection(){

        if(dbConnection!=null){
            return dbConnection;
        }

        try {
             dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/filemeta","root","root");
        } catch (SQLException e) {
            System.out.println("Database connection failed : " + e.getMessage());
        }

        return dbConnection;
    }

    private static ICommand getiCommand(String command, String filename) {

        ICommand commandToExecute = null;
        try{
            switch(command){
                case CRAWL_COMMAND:
                    commandToExecute = new CrawlCommand(filename);
                    break;
                case DESCRIBE_COMMAND:
                    commandToExecute = new DescribeCommand(filename);
                    break;
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return commandToExecute;
    }

}
