package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.io.File;
import java.util.ArrayList;


public abstract class SESFileReader
{
    protected static File[] files;

    public SESFileReader(String path){
        if (path.length() != 0 & !path.equals("Voer een locatie in"))
        {
            files = new File(path).listFiles();
        }
    }

    public abstract ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders);

    public abstract ArrayList<File> getFiles();

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

    public static boolean checkIfIllegalName(String name){
        return !name.contains("\"") & !name.contains("#") & !name.contains("\\");
    }
}

