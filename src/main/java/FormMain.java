import Beans.Folder;
import Beans.Symbol;
import SESFileReader.GetSymbols2d;
import SESFileReader.GetSymbols3d;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FormMain
{
    private JPanel JPanel1;
    private JTextField textField1;
    private JButton button1;
    private JTextPane textPane1;
    public static String path = "";
    private final ArrayList<Symbol> symbols = new ArrayList<>();
    private final ArrayList<Symbol> symbols3d = new ArrayList<>();

    public FormMain()
    {
        button1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                path = textField1.getText();
                GenerateExel();
            }
        });
    }

    //Main function to generate an Excel for all symbols in a folder
    public void GenerateExel()
    {
        symbols.clear();
        symbols3d.clear();

        GetSymbols2d getSymbols2d = new GetSymbols2d(path);
        GetSymbols3d getSymbols3d = new GetSymbols3d(path);

        //for each 2d database gets the symbols
        for(File file: getSymbols2d.getFiles()){
            String fileName = file.getName();
            ArrayList<Folder> folders = getFolderSES(path + "/" + fileName, false);
            symbols.addAll(getSymbols2d.getSymbols(fileName, path + "/" + fileName, folders));
            System.out.println(fileName);
        }

        //for each 3d database gets the symbols
        for(File file: getSymbols3d.getFiles()){
            String fileName = file.getName();
            ArrayList<Folder> folders = getFolderSES(path + "/" + fileName, true);
            symbols3d.addAll(getSymbols3d.getSymbols(fileName, path + "/" + fileName, folders));
            System.out.println(fileName);
        }
        createXls();
        JOptionPane.showMessageDialog(null,"Process voltooid\nAantal symbolen: " + (symbols.size() + symbols3d.size()));
    }

    //creates exel from all symbols
    public void createXls()
    {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object
        XSSFSheet spreadsheet = workbook.createSheet("SEEable Symbols");
        XSSFSheet spreadsheet1 = workbook.createSheet("SEEable 3D Symbols");

        // creating a row object
        XSSFRow row;

        // writing the data into the sheets
        String fullSymbolPath;
        for (int i = 0; i < symbols.size(); i++)
        {
            row = spreadsheet.createRow(i);
            Cell cell = row.createCell(0);
            Symbol symbol = symbols.get(i);
            fullSymbolPath = String.format("%s\\%s\\%s", symbol.getFileSES().substring(0, symbol.getFileSES().length() - 4), symbol.getFolderName(), symbol.getSymboolName());
            cell.setCellValue(fullSymbolPath);
        }
        for (int i = 0; i < symbols3d.size(); i++)
        {
            row = spreadsheet1.createRow(i);
            Cell cell = row.createCell(0);
            Symbol symbol = symbols3d.get(i);
            fullSymbolPath = String.format("%s\\%s\\%s", symbol.getFileSES().substring(0, symbol.getFileSES().length() - 6), symbol.getFolderName(), symbol.getSymboolName());
            cell.setCellValue(fullSymbolPath);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        // writing the workbook into exel file
        FileOutputStream out;
        try
        {
            out = new FileOutputStream(path + "/SEEableSymbolDb" + date + ".xlsx");
            workbook.write(out);
            out.close();
        } catch (IOException e)
        {
            errorHandler(e, "Location : createXLS");
            e.printStackTrace();
        }
    }

    //gets all folders in a database
    public static ArrayList<Folder> getFolderSES(String location, boolean d3)
    {
        ArrayList<Folder> folders = new ArrayList<>();
        String databaseURL = "jdbc:ucanaccess://" + location;
        try
        {
            //connect to database
            Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM [Folder]";
            ResultSet rs = statement.executeQuery(query);
            String folder3dPath;
            int parentCounter;
            int i;
            while (rs.next())
            {
                //if it is a 3d database there are also subfolders
                //this if statement retrieves those subfolders with a parentCounter
                if(d3){
                    folder3dPath = rs.getString("Folder");
                    parentCounter = rs.getInt("ParentCounter");
                    if(parentCounter != 0){
                        i = 0;
                        for (Folder folder : folders)
                        {
                            if(folder.getFolderNumber() == parentCounter){
                                parentCounter = i;
                                break;
                            }
                            i++;
                        }
                        folder3dPath = folders.get(parentCounter).getFolderFullPath() + "\\" + folder3dPath;
                    }
                    folders.add(new Folder(rs.getString("Folder"), folder3dPath, rs.getInt("Counter")));
                }else
                {
                    //if it is not a 3d database the folder wil be added without subfolders
                    folders.add(new Folder(rs.getInt("Counter"), rs.getString("Folder")));
                }
            }
        } catch (SQLException e)
        {
            errorHandler(e, "Location : getFolderSES");
            e.printStackTrace();
        }
        return folders;
    }

    //creates an error message and stores saves message to a file
    public static void errorHandler(Exception e, String message){
        JOptionPane.showMessageDialog(null,String.format("%s\n%s", message, e));
        File file = getfile( "\\log.txt");
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);

            Writer output = new BufferedWriter(new FileWriter(file, true));
            output.append(String.format("%s -> %s -> %s\n", date, message, e));
            output.close();
        } catch (IOException ioException)
        {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(null,String.format("Warning : errorHandler not working!!\n%s", e));
        }
    }

    public static File getfile(String path){
        String localPath = new File("").getPath();
        File file = new File(localPath + path);
        if(!file.exists()) file.mkdirs();

        return file;
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("FormMain");
        frame.setContentPane(new FormMain().JPanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
