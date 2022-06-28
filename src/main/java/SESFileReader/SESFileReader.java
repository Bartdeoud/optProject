package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.util.ArrayList;

/*
 * class om symbolen uit een database te importeren
 */

public abstract class SESFileReader
{
    protected static File[] files;

    //importeerd alle betanden uit een map
    public SESFileReader(String path){
        if (path.length() != 0 & !path.equals("Voer een locatie in"))
        {
            files = new File(path).listFiles();
        }
    }

    //importeerd alle symbolen uit een database
    public abstract ArrayList<Symbol> getSymbols(String location, ArrayList<Folder> folders);

    //importeerd alle mappen uit een database
    public abstract ArrayList<File> getFiles();

    public static boolean checkIfIllegalName(String name){
        return !name.contains("\"") & !name.contains("#") & !name.contains("\\");
    }

    //controleerd of de library ucanaccess de String kan processen
    protected ArrayList<Symbol> validate(ArrayList<Symbol> symbols)
    {
        ArrayList<Symbol> returnSymbols = new ArrayList<>();
        for (Symbol symbol : symbols)
        {
            if (checkIfIllegalName(symbol.getSymbolName()))
                returnSymbols.add(symbol);
        }
        return returnSymbols;
    }
}

