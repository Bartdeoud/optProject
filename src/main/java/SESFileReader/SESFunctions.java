package SESFileReader;

import Beans.Folder;
import Beans.Symbol;

import java.util.ArrayList;

public interface SESFunctions
{
    ArrayList<Symbol> getSymbols(String fileSES, String location, ArrayList<Folder> folders);
}
