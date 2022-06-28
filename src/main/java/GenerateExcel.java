import Beans.Folder;
import Beans.Symbol;
import SESFileReader.GetSymbols2d;
import SESFileReader.GetSymbols3d;
import SESFileReader.SESFileReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GenerateExcel
{

    private final ArrayList<Symbol> symbols = new ArrayList<>();
    private final ArrayList<Symbol> symbols3d = new ArrayList<>();
    private final String path;

    public GenerateExcel(String path)
    {
        this.path = path;
    }

    //Main function to generate an Excel for all symbols in a folder
    public void generate()
    {
        GetSymbols2d getSymbols2d = new GetSymbols2d(path);
        GetSymbols3d getSymbols3d = new GetSymbols3d(path);

        symbols.addAll(importSymbols(getSymbols2d));

        symbols3d.addAll(importSymbols(getSymbols3d));

        createXls();
        showMessage();
    }

    private ArrayList<Symbol> importSymbols(SESFileReader sesFileReader){
        //for each 3d database gets the symbols
        ArrayList<Symbol> returnSymbols = new ArrayList<>();
        for(File file: sesFileReader.getFiles()){
            String fileName = file.getName();
            ArrayList<Folder> folders = GetFolderSES.getFolderSES(path + "/" + fileName);
            returnSymbols.addAll(sesFileReader.getSymbols(path + "\\" + fileName, folders));
            System.out.println(fileName);
        }
        return returnSymbols;
    }

    //creates exel from all symbols
    public void createXls()
    {
        // workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        // spreadsheet object
        XSSFSheet spreadsheet = workbook.createSheet("SEEable Symbols");
        XSSFSheet spreadsheet1 = workbook.createSheet("SEEable 3D Symbols");

        // writing the data into the sheets
        spreadsheet = getSheet(symbols, spreadsheet);
        spreadsheet1 = getSheet(symbols3d, spreadsheet1);

        createFile(workbook);
    }

    private void createFile(XSSFWorkbook workbook){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        // writing the workbook into exel file
        FileOutputStream out;
        try {
            out = new FileOutputStream(path + "/SEEableSymbolDb" + date + ".xlsx");
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XSSFSheet getSheet(ArrayList<Symbol> symbols, XSSFSheet spreadsheet){
        Row row;
        for (int i = 0; i < symbols.size(); i++)
        {
            row = spreadsheet.createRow(i);
            Cell cell = row.createCell(0);
            Symbol symbol = symbols.get(i);
            String  fullSymbolPath = String.format("%s\\%s\\%s", symbol.getFileSES().substring(0, symbol.getFileSES().lastIndexOf(".")), symbol.getFolderName(), symbol.getSymbolName());
            cell.setCellValue(fullSymbolPath);
        }
        return spreadsheet;
    }

    private void showMessage(){
        String message = getMessage(symbols);

        JOptionPane.showMessageDialog(null,message + (symbols.size() + symbols3d.size()));
    }

    //returns the message for the pop-up message
    public String getMessage(ArrayList<Symbol> symbols){
        int symbolsSize = symbols.size();
        if(symbolsSize < 200){
            return "This is a safe amount of symbols ";
        }
        if(symbolsSize < 400){
            return "This could be a dangerous amount of symbols ";
        }
        return "This is a dangerous amount of symbols ";
    }
}
