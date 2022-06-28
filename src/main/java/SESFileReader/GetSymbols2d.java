package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

/*
 * class om 2d symbolen te importeren
 */

public class GetSymbols2d extends SESFileReader
{

    private final ArrayList<File> files2d = new ArrayList<>();

    @Override
    public ArrayList<File> getFiles()
    {
        return files2d;
    }

    //filterd op 2d bestanden
    public GetSymbols2d(String path)
    {
        super(path);
        for (File file : files)
        {
            String fileName = file.getName();
            if (fileName.endsWith(".ses"))
            {
                files2d.add(file);
            }
        }
    }

    @Override
    public ArrayList<Symbol> getSymbols(String location, ArrayList<Folder> folders)
    {
        ArrayList<Symbol> symbols = new ArrayList<>();
        try
        {
            //foreach folder get symbols
            for (Folder folder : folders)
            {
                symbols.addAll(getDataFromResultSet(location, folder));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return validate(symbols);
    }

    public ArrayList<Symbol> getDataFromResultSet(String location, Folder folder) throws SQLException{
        Statement statement = DriverManager.getConnection("jdbc:ucanaccess://" + location).createStatement();
        String query = "SELECT * FROM [Group]";
        ResultSet rs = statement.executeQuery(query);
        ArrayList<Symbol> symbols = new ArrayList<>();
        while (rs.next())
        {
            //puts query output in folder
            if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
            {
                String symbolName = rs.getString("GroupName");
                symbols.add(new Symbol(location.substring(location.lastIndexOf("\\") + 1), folder.getFolderName(), symbolName));
            }
        }
        return symbols;
    }
}
