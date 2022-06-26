package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import static SESFileReader.GetSymbols2d.validate;

public class GetSymbols3d extends SESFileReader implements SESFunctions
{

    private final ArrayList<File> files3d = new ArrayList<>();

    public ArrayList<File> getFiles()
    {
        return files3d;
    }

    public GetSymbols3d(String path)
    {
        super(path);
        try
        {
            for (File file : files)
            {
                String fileName = file.getName();
                    if (fileName.length() >= 6)
                    {
                        if (fileName.substring((fileName.length() - 6)).equals(".ses3d"))
                        {
                            files3d.add(file);
                        }
                    }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders)
    {
        //connect to SES database
        String databaseURL = "jdbc:ucanaccess://" + location;

        folders = getGroupCounterFromDatabase(databaseURL,folders);

        ArrayList<Symbol> symbols = getSymbolsFromDatabase(databaseURL, folders, fileSES);

        return validate(symbols);
    }

    public ArrayList<Folder> getGroupCounterFromDatabase(String databaseURL, ArrayList<Folder> folders){
        try
        {
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM [GroupFolder]";
            for (Folder folder : folders)
            {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next())
                {
                    //puts query output in folder
                    if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
                    {
                        folders.get(folders.indexOf(folder)).addGroupCounter(rs.getInt("GroupCounter"));
                    }
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return folders;
    }

    public ArrayList<Symbol> getSymbolsFromDatabase(String databaseURL, ArrayList<Folder> folders, String fileSES){
        ArrayList<Symbol> returnSymbols = new ArrayList<>();
        try
        {
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement1 = connection.createStatement();
            String query1 = "SELECT * FROM [Group]";
            //for each folder -> get symbols
            for (Folder folder : folders)
            {
                for (int groupcounter : folder.getGroupCounter())
                {
                    ResultSet rs = statement1.executeQuery(query1);
                    returnSymbols.addAll(addSymbols(rs,groupcounter,folder,fileSES));
                }
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return returnSymbols;
    }

    public ArrayList<Symbol> addSymbols(ResultSet rs, int groupcounter, Folder folder, String fileSES) throws SQLException
    {
        ArrayList<Symbol> returnSymbols = new ArrayList<>();
        while (rs.next())
        {
            if (groupcounter == rs.getInt("Counter"))
            {
                returnSymbols.add(new Symbol(fileSES,  folder.getFolderFullPath(), rs.getString("GroupName")));
            }
        }
        return  returnSymbols;
    }
}
