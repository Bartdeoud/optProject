import Beans.Folder;
import Beans.Symbol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static SESFileReader.GetSymbols3d.validated;
import static org.junit.Assert.*;

public class TestGetSymbols3d
{
    @Test
    public void testValidate(){
        assertFalse(validated("jdbc:ucanaccess://", new ArrayList<>(), "file.ses3d", new ArrayList<>(List.of(new Symbol("test", "test", "test")))));

        assertTrue(validated("jdbc:ucanaccess://database", new ArrayList<>(List.of(new Folder(1, "test"))), "file.ses3d", new ArrayList<>()));

        assertFalse(validated("jdbc:ucanaccess://", new ArrayList<>(List.of(new Folder(1, "test"))), "file.ses", new ArrayList<>(List.of(new Symbol("test", "test", "test")))));

        assertFalse(validated("jdbc:ucanaccess://database", new ArrayList<>(), "file.ses", new ArrayList<>()));

        assertFalse(validated("jdbc:ucanaccess://", new ArrayList<>(), "file.SeeAble", new ArrayList<>()));

        assertFalse(validated("jdbc:ucanaccess://database", new ArrayList<>(List.of(new Folder(1, "test"))), "file.Seeable", new ArrayList<>(List.of(new Symbol("test", "test", "test")))));
    }
}