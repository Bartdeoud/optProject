import java.io.File;
import java.sql.*;
import java.util.ArrayList;

class GetSymbols2d extends SESFileReader implements SESfunctions {

    private final ArrayList<File> files2d = new ArrayList<>();

    public ArrayList<File> getFiles(){
        return files2d;
    }

    public GetSymbols2d(String path)
    {
        super(path);
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
        return GetSymbols3d.validate(symbols);
    }
}