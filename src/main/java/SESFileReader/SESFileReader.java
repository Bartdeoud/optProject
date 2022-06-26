package SESFileReader;

import java.io.File;


public abstract class SESFileReader
{
    protected static File[] files;

    public SESFileReader(String path){
        if (path.length() != 0 & !path.equals("Voer een locatie in"))
        {
            files = new File(path).listFiles();
        }
    }
}

