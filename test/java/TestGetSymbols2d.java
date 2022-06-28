import org.junit.Test;

import static SESFileReader.GetSymbols2d.checkIfIllegalName;
import static org.junit.Assert.*;

public class TestGetSymbols2d
{
    //modified condition/decision coverage
    @Test
    public void testValidate(){
        assertTrue(checkIfIllegalName("Symbool1"));
        assertFalse(checkIfIllegalName("Symbool1\""));
        assertFalse(checkIfIllegalName("Symbool1#"));
        assertFalse(checkIfIllegalName("Symbool1\\"));
    }
}