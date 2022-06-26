package Beans;

import java.util.ArrayList;

public class Folder
{
    private final int folderNumber;
    private final String folderName;
    private String folderFullPath;
    private final ArrayList<Integer> groupCounters = new ArrayList<>();

    public Folder(int folderNumber, String folderName)
    {
        this.folderNumber = folderNumber;
        this.folderName = folderName;
    }

    public Folder(String folderName, String folderFullPath, int folderNumber){
        this.folderName = folderName;
        this. folderFullPath = folderFullPath;
        this.folderNumber = folderNumber;
    }

    public String getFolderFullPath()
    {
        return folderFullPath;
    }

    public ArrayList<Integer> getGroupCounter()
    {
        return groupCounters;
    }

    public void addGroupCounter(int groupCounter)
    {
        this.groupCounters.add(groupCounter);
    }

    public int getFolderNumber()
    {
        return folderNumber;
    }

    public String getFolderName()
    {
        return folderName;
    }
}
