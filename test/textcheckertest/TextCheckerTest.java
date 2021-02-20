package textcheckertest;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Text;
import spellchecker.TextChecker;

import static org.junit.Assert.*;

public class TextCheckerTest {

    private TextChecker textChecker;

    @Before
    public void setUp() throws Exception {
        textChecker = new TextChecker();
    }

    @Test
    public void itShouldHandleNulls(){
        assertEquals(0, textChecker.getMisspeltWords(null).size());
    }

    @Test
    public void itShouldHandleEmptyStrings(){
        assertEquals(0, textChecker.getMisspeltWords("").size());
    }

}