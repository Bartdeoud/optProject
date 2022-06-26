package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class GetSymbols2d extends SESFileReader implements SESFunctions
{

    private final ArrayList<File> files2d = new ArrayList<>();

    public ArrayList<File> getFiles()
    {
        return files2d;
    }

    public GetSymbols2d(String path)
    {
        super(path);
        try
        {
            for (File file : files)
            {
                String fileName = file.getName();
                try
                {
                    if (fileName.substring((fileName.length() - 4)).equals(".ses"))
                    {
                        files2d.add(file);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
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
        ArrayList<Symbol> symbols = new ArrayList<>();
        //connect to SES database
        String databaseURL = "jdbc:ucanaccess://" + location;
        try
        {
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM [Group]";
            //foreach folder get symbols
            String symbolName;
            for (Folder folder : folders)
            {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next())
                {
                    //puts query output in folder
                    if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
                    {
                        symbolName = rs.getString("GroupName");
                        symbols.add(new Symbol(fileSES, folder.getFolderName(), symbolName));
                    }
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return validate(symbols);
    }

    public static ArrayList<Symbol> validate(ArrayList<Symbol> symbols)
    {
        ArrayList<Symbol> returnSymbols = new ArrayList<>();
        for (Symbol symbol : symbols)
        {
            if (!symbol.getSymboolName().contains("\"") & !symbol.getSymboolName().contains("#") & !symbol.getSymboolName().contains("\\") & !symbol.getSymboolName().contains(";"))
                returnSymbols.add(symbol);
        }
        return returnSymbols;
    }
}