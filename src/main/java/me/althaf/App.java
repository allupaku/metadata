package me.althaf;

import com.google.common.base.Strings;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Hello world!
 *
 */
public class App 
{
    private final static String CRAWL_COMMAND = "crawl";
    private final static String DESCRIBE_COMMAND = "describe";

    private Connection dbConnection;

    private String filename;

    private String command;

    ICommand commandToExecute = null;
    IResult resultOfCommand = null;

    public App(String command, String filename){
        this.command = command;
        this.filename = this.normalizePath(filename);

    }

    public String normalizePath(String filename){
        Path filepath = Paths.get(filename);

        String normalFilePath = filename;

        if(!filepath.isAbsolute()){
            filepath.toAbsolutePath();
            normalFilePath = filepath.toFile().getAbsolutePath();
        }
        return normalFilePath;
    }

    public App run(){

        commandToExecute = getiCommand(command, filename);

        this.dbConnection  = getDBConnection();

        if(commandToExecute != null) {

            commandToExecute.setConnection(this.dbConnection);

            resultOfCommand = commandToExecute.execute();

        }
        return this;
    }

    public App showResult(){
        if(resultOfCommand != null){
            resultOfCommand.display();
        }
        return this;
    }

    public App persistResult(){
        if(resultOfCommand != null)
            resultOfCommand.persist();

        return this;
    }

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

        App instance = new App(command,filename);

        instance.run().persistResult().showResult().closeConnection();
    }

    private Connection getDBConnection(){

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

    private void closeConnection(){
        if(this.dbConnection != null){
            try {
                this.dbConnection.close();
            }catch(Exception ex){
                System.err.println("Unable to close db connection : " + ex.getMessage());
            }
        }
    }

    private ICommand getiCommand(String command, String filename) {

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
            System.err.println("Unknown file extension : " + ex.getMessage());

        }
        return commandToExecute;
    }

}
