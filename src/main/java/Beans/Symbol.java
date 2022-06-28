package Beans;

public class Symbol
{
    private final String fileSES;
    private final String folderName;
    private final String symbolName;

    public Symbol(String fileSES, String folderName, String symbolName)
    {
        this.fileSES = fileSES;
        this.folderName = folderName;
        this.symbolName = symbolName;
    }

    public String getFileSES()
    {
        return fileSES;
    }

    public String getSymbolName()
    {
        return symbolName;
    }

    public String getFolderName()
    {
        return folderName;
    }
}