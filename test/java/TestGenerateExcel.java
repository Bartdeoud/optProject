import Beans.Symbol;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestGenerateExcel
{

    private final GenerateExcel generateExcel = new GenerateExcel("");

    @Test
    public void TestGetMessage(){
        assertEquals("This is a safe amount of symbols ", generateExcel.getMessage(fillSymbolsArray(199)));
        assertEquals("This could be a dangerous amount of symbols ", generateExcel.getMessage(fillSymbolsArray(200)));
        assertEquals("This could be a dangerous amount of symbols ", generateExcel.getMessage(fillSymbolsArray(399)));
        assertEquals("This is a dangerous amount of symbols ", generateExcel.getMessage(fillSymbolsArray(400)));
    }

    public ArrayList<Symbol> fillSymbolsArray(int amount){
        ArrayList<Symbol> symbols = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            symbols.add(new Symbol("test","test","test"));
        }
        return symbols;
    }
}