package Beans;

public class Symbol
{
    private final String folderName;
    private final String symboolName;
    private final String fileSES;

    public Symbol(String fileSES, String folderName, String symboolName){
        this.fileSES = fileSES;
        this.folderName = folderName;
        this.symboolName = symboolName;
    }

    public String getFileSES()
    {
        return fileSES;
    }

    public String getSymboolName()
    {
        return symboolName;
    }

    public String getFolderName()
    {
        return folderName;
    }
}
