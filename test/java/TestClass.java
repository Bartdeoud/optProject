import Beans.Folder;
import Beans.Symbol;
import SESFileReader.GetSymbols2d;
import SESFileReader.GetSymbols3d;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


//this class test the most important functions
//test databases are in the folder test
public class TestClass
{
    String localPath = new File("").getAbsolutePath() + "\\test\\databases";
    Symbol testSymbool = new Symbol("SymbolenVerzamelBestand.ses", "Folder1", "Symbool1");
    Symbol testSymbool3d = new Symbol("Test", "Tekst\\Kast\\Relais", "SEEable");
    ArrayList<Folder> testFolders = new ArrayList<>(Arrays.asList(
            new Folder("Tekst", "Tekst", 1),
            new Folder("Kast", "Tekst\\Kast", 2),
            new Folder("Relais", "Tekst\\Kast\\Relais", 3),
            new Folder("Peter", "Tekst\\Kast\\Relais\\Peter", 4)
    ));

    //tests if the function gets the right folders
    @Test
    public void getFolderSESTest(){
        ArrayList<Folder> folders = GetFolderSES.getFolderSES(localPath + "\\Test.ses3d");

        for(int i = 0; i < folders.size(); i++){
            Folder testfolder = testFolders.get(i);
            Folder folder = folders.get(i);
            assertEquals(folder.getFolderFullPath(),testfolder.getFolderFullPath());
            assertEquals(folder.getFolderName(),testfolder.getFolderName());
            assertEquals(folder.getFolderNumber(),testfolder.getFolderNumber());
        }
    }

    //test if functions gets the right 2d symbols from the database
    @Test
    public void getSymbolsTest2d(){
        GetSymbols2d getSymbols2d = new GetSymbols2d(localPath);

        ArrayList<Symbol> symbols = getSymbols2d.getSymbols(localPath + "\\SymbolenVerzamelBestand.ses", GetFolderSES.getFolderSES(localPath + "\\SymbolenVerzamelBestand.ses"));

        assertEquals(1, symbols.size());
        assertEquals(symbols.get(0).getFileSES(), testSymbool.getFileSES());
        assertEquals(symbols.get(0).getSymbolName(), testSymbool.getSymbolName());
        assertEquals(symbols.get(0).getFolderName(), testSymbool.getFolderName());
    }

    //test if functions gets the right 3d symbols from the database
    @Test
    public void getSymbolsTest3d(){
        GetSymbols3d getSymbols3d = new GetSymbols3d(localPath);

        ArrayList<Folder> folders = GetFolderSES.getFolderSES(localPath + "\\Test.ses3d");

        ArrayList<Symbol> symbols = getSymbols3d.getSymbols(localPath + "\\Test.ses3d", folders);

        assertEquals(symbols.get(1).getSymbolName(), testSymbool3d.getSymbolName());
    }
}
