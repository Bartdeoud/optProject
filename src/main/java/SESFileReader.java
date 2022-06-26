 import java.io.File;
import java.util.ArrayList;

interface SESfunctions{
    ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders);
    ArrayList<File> getFiles();
}

abstract class SESFileReader
{
    protected static File[] files;

    public SESFileReader(String path){
        if (path.length() != 0 & !path.equals("Voer een locatie in"))
        {
            files = new File(path).listFiles();
        }
    }
}