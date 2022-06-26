import java.io.File;
import java.sql.*;
import java.util.ArrayList;

class GetSymbols3d extends SESFileReader implements SESfunctions {

    private final ArrayList<File> files3d = new ArrayList<>();

    public ArrayList<File> getFiles(){
        return files3d;
    }

    public GetSymbols3d(String path)
    {
        super(path);
        for (File file : files)
        {
            String fileName = file.getName();
            try
            {
                if (fileName.length() >= 6)
                {
                    if (fileName.substring((fileName.length() - 6)).equals(".ses3d"))
                    {
                        files3d.add(file);
                    }
                }
            } catch (Exception e)
            {
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