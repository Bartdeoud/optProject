import Beans.Folder;

import java.sql.*;
import java.util.ArrayList;

public class GetFolderSES
{

    //gets all folders in a database
    public static ArrayList<Folder> getFolderSES(String location)
    {
        ArrayList<Folder> folders = new ArrayList<>();
        String databaseURL = "jdbc:ucanaccess://" + location;
        try
        {
            //connect to database
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM [Folder]";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next())
            {
                //if it is a 3d database there are also subfolders
                //this if statement retrieves those subfolders with a parentCounter
                Folder folderToAdd;
                if(location.endsWith(".ses3d")){
                    folderToAdd = getFolder3d(rs, folders);
                }else
                {
                    //if it is not a 3d database the folder wil be added without subfolders
                    folderToAdd = new Folder(rs.getInt("Counter"), rs.getString("Folder"));
                }
                if(!folders.contains(folderToAdd))
                    folders.add(folderToAdd);
            }
        } catch (SQLException e)
        {
            ErrorHandler.getInstance().errorMessage(e, "Location : createXLS");
        }
        return folders;
    }

    public static Folder getFolder3d(ResultSet rs, ArrayList<Folder> folders){
        String folder3dPath;
        int parentCounter;
        Folder folderToAdd = new Folder(1, "initialise");
        try
        {
            folder3dPath = rs.getString("Folder");
            parentCounter = rs.getInt("ParentCounter");
            if (parentCounter != 0)
            {
                parentCounter = getParentCounter(folders, parentCounter);
                folder3dPath = folders.get(parentCounter).getFolderFullPath() + "\\" + folder3dPath;
            }
            folderToAdd = new Folder(rs.getString("Folder"), folder3dPath, rs.getInt("Counter"));
        } catch (SQLException e)
        {
            ErrorHandler.getInstance().errorMessage(e, "Location : createXLS");
        }
        return folderToAdd;
    }

    public static int getParentCounter(ArrayList<Folder> folders, int parentCounter){
        int i = 0;
        for (Folder folder : folders)
        {
            if (folder.getFolderNumber() == parentCounter)
            {
                parentCounter = i;
                break;
            }
            i++;
        }
        return parentCounter;
    }
}
