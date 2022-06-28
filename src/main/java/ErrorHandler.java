import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * class om error berichten op te slaan en weer te geven
 */

public class ErrorHandler
{

    private static ErrorHandler instance;
    private final File loglocation;

    private ErrorHandler(){
        this.loglocation = getfile("\\log.txt");
    }

    public static ErrorHandler getInstance(){
        if(instance == null){
            instance = new ErrorHandler();
        }
        return instance;
    }

    //creates an error message and stores saves message to a file
    public void errorMessage(Exception e, String message){
        JOptionPane.showMessageDialog(null,String.format("%s\n%s", message, e));
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);

            Writer output = new BufferedWriter(new FileWriter(loglocation, true));
            output.append(String.format("%s -> %s -> %s\n", date, message, e));
            output.close();
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    public File getfile(String path){
        String localPath = new File("").getPath();
        File file = new File(localPath + path);
        if(!file.exists()) file.mkdirs();

        return file;
    }
}
