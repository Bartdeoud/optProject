package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class GetSymbols2d extends SESFileReader
{

    private final ArrayList<File> files2d = new ArrayList<>();

    @Override
    public ArrayList<File> getFiles()
    {
        return files2d;
    }

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
    public ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders)
    {
        ArrayList<Symbol> symbols = new ArrayList<>();
        try
        {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + location);
            Statement statement = connection.createStatement();
            //foreach folder get symbols
            for (Folder folder : folders)
            {
                symbols.addAll(getDataFromResultSet(statement, folder, fileSES));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return validate(symbols);
    }

    public ArrayList<Symbol> getDataFromResultSet(Statement statement, Folder folder, String fileSES) throws SQLException{
        String query = "SELECT * FROM [Group]";
        ResultSet rs = statement.executeQuery(query);
        ArrayList<Symbol> symbols = new ArrayList<>();
        while (rs.next())
        {
            //puts query output in folder
            if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
            {
                String symbolName = rs.getString("GroupName");
                symbols.add(new Symbol(fileSES, folder.getFolderName(), symbolName));
            }
        }
        return symbols;
    }
}
