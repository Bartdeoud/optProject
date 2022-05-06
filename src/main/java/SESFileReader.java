import java.io.File;
import java.sql.*;
import java.util.ArrayList;

interface SESfunctions{
    ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders);
    ArrayList<File> getFiles();
}

abstract class SESFileReader
{
    protected static File[] files;

    //gets all files from a folder
    public SESFileReader(String path){
        if (path.length() != 0 & !path.equals("Voer een locatie in"))
        {
            files = new File(path).listFiles();
        }
    }
}

class GetSymbols2d extends SESFileReader implements SESfunctions {

    private final ArrayList<File> files2d = new ArrayList<>();

    public ArrayList<File> getFiles(){
        return files2d;
    }

    //gets all 2d databases from folder
    public GetSymbols2d(String path)
    {
        super(path);
        for(File file: files){
            String fileName = file.getName();
            try
            {
                if (fileName.substring((fileName.length() - 4)).equals(".ses"))
                {
                    files2d.add(file);
                }
            } catch (Exception e)
            {
                FormMain.errorHandler(e, "Location : GetSymbols2d");
                e.printStackTrace();
            }
        }
    }

    //gets all symbols of database
    @Override
    public ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders)
    {
        ArrayList<Symbol> symbols = new ArrayList<>();
        //connect to SES database
        String databaseURL = "jdbc:ucanaccess://" + location;
        try
        {
            //connect to the database
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
                    //converts result -> symbol
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
            FormMain.errorHandler(e, "Location : getSymbols");
        }
        return symbols;
    }
}

class GetSymbols3d extends SESFileReader implements SESfunctions {

    private final ArrayList<File> files3d = new ArrayList<>();

    public ArrayList<File> getFiles(){
        return files3d;
    }

    //gets all 3d databases from folder
    public GetSymbols3d(String path)
    {
        super(path);
        for(File file: files){
            String fileName = file.getName();
            try
            {
                if(fileName.length() >= 6){
                    if (fileName.substring((fileName.length() - 6)).equals(".ses3d"))
                    {
                        files3d.add(file);
                    }
                }
            } catch (Exception e)
            {
                FormMain.errorHandler(e, "Location : GetSymbols3d");
                e.printStackTrace();
            }
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
            String query = "SELECT * FROM [GroupFolder]";
            for (Folder folder : folders)
            {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next())
                {
                    if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
                    {
                        folders.get(folders.indexOf(folder)).addGroupCounter(rs.getInt("GroupCounter"));
                    }
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            FormMain.errorHandler(e, "Location : getSymbols");
        }
        try
        {
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement1 = connection.createStatement();
            String query1 = "SELECT * FROM [Group]";
            //for each folder -> get symbols
            String symbolName;
            for (Folder folder : folders)
            {
                for (int groupcounter : folder.getGroupCounter())
                {
                    ResultSet rs = statement1.executeQuery(query1);
                    while (rs.next())
                    {
                        if (groupcounter == rs.getInt("Counter"))
                        {
                            symbolName = rs.getString("GroupName");
                            symbols.add(new Symbol(fileSES, folder.getFolderFullPath(), symbolName));
                        }
                    }
                }
            }
        } catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
        return symbols;
    }

    public ArrayList<Symbol> getSymbolsSES3d(String fileSES, String location, ArrayList<Folder> folders3d)
    {
        ArrayList<Symbol> symbols3d = new ArrayList<>();
        //connect to SES database
        String databaseURL = "jdbc:ucanaccess://" + location;
        try (Connection connection = DriverManager.getConnection(databaseURL))
        {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM [GroupFolder]";
            for(Folder folder: folders3d){
                ResultSet rs = statement.executeQuery(query);
                while (rs.next())
                {
                    if (folder.getFolderNumber() == rs.getInt("FolderCounter"))
                    {
                        folders3d.get(folders3d.indexOf(folder)).addGroupCounter(rs.getInt("GroupCounter"));
                    }
                }
            }

            Statement statement1 = connection.createStatement();
            String query1 = "SELECT * FROM [Group]";
            //foreach folder get symbols
            String symbolName;
            for (Folder folder : folders3d)
            {
                for(int groupcounter: folder.getGroupCounter()){
                    ResultSet rs = statement1.executeQuery(query1);

                    while (rs.next()){
                        if(groupcounter == rs.getInt("Counter")){
                            symbolName = rs.getString("GroupName");

                            symbols3d.add(new Symbol(fileSES, folder.getFolderFullPath(), symbolName));
                        }
                    }
                }
            }
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return symbols3d;
    }
}