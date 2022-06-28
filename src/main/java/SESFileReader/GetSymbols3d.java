package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GetSymbols3d extends SESFileReader
{

    private final ArrayList<File> files3d = new ArrayList<>();

    @Override
    public ArrayList<File> getFiles()
    {
        return files3d;
    }

    public GetSymbols3d(String path)
    {
        super(path);
        for (File file : files)
        {
            String fileName = file.getName();
            if (fileName.length() >= 6)
            {
                if (fileName.endsWith(".ses3d"))
                {
                    files3d.add(file);
                }
            }
        }
    }

    @Override
    public ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders)
    {
        //connect to SES database
        String databaseURL = "jdbc:ucanaccess://" + location;

        folders = getGroupCounterFromDatabase(databaseURL,folders);

        ArrayList<Symbol> returnSymbols = new ArrayList<>();

        ArrayList<Symbol> symbols = getSymbolsFromDatabase(databaseURL, folders, fileSES, returnSymbols);

        return validate(symbols);
    }

    private ArrayList<Folder> getGroupCounterFromDatabase(String databaseURL, ArrayList<Folder> folders){
        try
        {
            ResultSet rs = getResultSet(databaseURL);
            for (Folder folder : folders)
                while (rs.next())
                    if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
                        folders.get(folders.indexOf(folder)).addGroupCounter(rs.getInt("GroupCounter"));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return folders;
    }

    public ResultSet getResultSet(String databaseURL) throws SQLException{
        Connection connection = DriverManager.getConnection(databaseURL);
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM [GroupFolder]";
        return statement.executeQuery(query);
    }

    //Get symbols for each folder
    private ArrayList<Symbol> getSymbolsFromDatabase(String databaseURL, ArrayList<Folder> folders, String fileSES, ArrayList<Symbol> returnSymbols){
        if(!validated(databaseURL, folders, fileSES, returnSymbols)) return returnSymbols;
        try {
            String query1 = "SELECT * FROM [Group]";
            for (Folder folder : folders)
                for (int groupcounter : folder.getGroupCounter()) {
                    ResultSet rs = DriverManager.getConnection(databaseURL).createStatement().executeQuery(query1);
                    returnSymbols.addAll(addSymbols(rs, groupcounter, folder, fileSES));
                }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return returnSymbols;
    }

    public static boolean validated(String databaseURL, ArrayList<Folder> folders, String fileSES, ArrayList<Symbol> symbols){
        if(databaseURL.equals("jdbc:ucanaccess://")) return false;
        if(folders.size() == 0) return false;
        if(symbols.size() > 0) return false;
        return fileSES.endsWith(".ses3d") || fileSES.endsWith(".ses") || fileSES.endsWith(".SeeAble");
    }

    private ArrayList<Symbol> addSymbols(ResultSet rs, int groupcounter, Folder folder, String fileSES) throws SQLException
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
