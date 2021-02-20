package stringarraytest;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import spellchecker.StringArray;

import static org.junit.Assert.*;

/*
 * Tests for ensuring that the StringArray class is functioning correctly.
 * Made by SN:20018291 @ UCL
 */

public class StringArrayTest {

    private StringArray aStringArray;
    private Random random = new Random();
    private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Before
    public void setUp() throws Exception{
        aStringArray = new StringArray();
    }

    // builds unique strings. Possibility of getting 2 equal strings is maximum: 1/26,000.
    private String createString(){
        int length = random.nextInt(1000);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++){
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()-1)));
        }
        return builder.toString();
    }

    // adds a random amount of strings to the array, and returns the size.
    private int addRandomStrings(){
        int amount = Math.abs(random.nextInt(50000));
        for (int i = 0; i < amount; i++){
            aStringArray.add(createString());
        }
        return amount;
    }

    @Test //Empty Test
    public void aEmptyStringArrayShouldBeEmpty(){
        assertTrue(aStringArray.isEmpty());
    }

    @Test // Size Test
    public void ifIAddAStringToAnEmptyArrayThenItShouldNotBeEmpty(){
        aStringArray.add(createString());
        assertFalse(aStringArray.isEmpty());
    }

    @Test //Size Test
    public void aEmptyStringArrayShouldHaveSize0(){
        assertEquals(0, aStringArray.size());
    }

    @Test //Size Test
    public void ifIAddXAmountOfStringsThenTheSizeShouldBeEqualToX(){
        int size = addRandomStrings();
        assertEquals(size, aStringArray.size());
    }


    @Test //Add Test
    public void ifIAddStringYToAnEmptyArrayThenIndex0ShouldContainStringY(){
        String newString = createString();
        aStringArray.add(newString);
        assertEquals(0, newString.compareTo(aStringArray.get(0)));
    }

    @Test //Cloning Test
    public void ifIConstructTheStringArrayWithAStringArrayArgumentItShouldCloneTheContents(){
        int size = addRandomStrings();
        StringArray newStringArray = new StringArray(aStringArray);

        for (int i = 0; i < size; i++){
            assertEquals(0, aStringArray.get(i).compareTo(newStringArray.get(i)));
        }
    }

    @Test //Insert Test
    public void ifIInsertStringYAtXThenGetXShouldGiveY(){
        int size = addRandomStrings();

        int insertAt = Math.abs(random.nextInt(size));
        String newString = createString();

        aStringArray.insert(insertAt, newString);
        assertEquals(0, aStringArray.get(insertAt).compareTo(newString));
        assertEquals(size+1, aStringArray.size());
    }

    @Test //Insert test
    public void insertingIntoAnEmptyArrayShouldBeLegal(){
        String newString = createString();

        aStringArray.insert(0, newString);
        assertEquals(1, aStringArray.size());
        assertEquals(0, aStringArray.get(0).compareTo(newString));
    }

    @Test //Insert test
    public void insertingIntoAInvalidIndexShouldBeIllegal(){
        String newString = createString();

        int size = addRandomStrings();
        aStringArray.insert(size, newString);
        assertEquals(size, aStringArray.size());
    }

    @Test //Set test
    public void ifISetIndexXToStringYThenTheElementAtXShouldBeY(){
        String newString = createString();

        int size = addRandomStrings();
        int setIndex = Math.abs(random.nextInt(size-1));
        aStringArray.set(setIndex, newString);

        assertEquals(0, newString.compareTo(aStringArray.get(setIndex)));
    }

    @Test //Contain & IndexOf test
    public void ifISetIndexXtoStringYThenArrayShouldContainStringY(){
        String newString = createString();

        int size = addRandomStrings();
        int setIndex = Math.abs(random.nextInt(size-1));
        aStringArray.set(setIndex, newString);

        assertEquals(setIndex, aStringArray.indexOfMatchingCase(newString));
        assert(aStringArray.containsMatchingCase(newString));
    }

    @Test //Contain & IndexOf test
    public void ifISetIndexXtoStringYThenArrayShouldDetectLowerCaseStringY(){
        String newString = createString(); // ALL UPPERCASE

        int size = addRandomStrings();
        int setIndex = Math.abs(random.nextInt(size-1));
        aStringArray.set(setIndex, newString);

        assertEquals(setIndex, aStringArray.indexOf(newString.toLowerCase()));
        assert(aStringArray.contains(newString.toLowerCase()));
    }

    @Test //Remove & isEmpty Test
    public void ifIRemoveAnElementInAnArrayContaining1ElementThenItShouldBeEmpty(){
        aStringArray.add(createString());
        assertFalse(aStringArray.isEmpty());
        aStringArray.remove(0);
        assert(aStringArray.isEmpty());
    }

    @Test // Remove Test
    public void IfIRemoveAnElementThenTheSizeShouldDecreaseBy1(){
        int size = addRandomStrings();
        aStringArray.remove(0);

        assertEquals(size-1, aStringArray.size());
    }

    @Test // Remove Test
    public void IfIRemoveAnElementAtXThenThatElementShouldNotBeAtX(){
        int size = addRandomStrings();
        String newString = createString();

        int atIndex = Math.abs(random.nextInt(size-1));
        aStringArray.set(atIndex, newString);

        assertEquals(0, newString.compareTo(aStringArray.get(atIndex)));
        aStringArray.remove(atIndex);
        assertNotEquals(0, newString.compareTo(aStringArray.get(atIndex)));
        assertFalse(aStringArray.containsMatchingCase(newString));
    }

    @Test // Null Handling
    public void invalidIndexOnGetShouldReturnNull(){
        assertEquals(null, aStringArray.get(0));
    }

    @Test // Null Handling
    public void shouldBeAbleToCheckIfNullExists(){
        aStringArray.add(null);

        assertEquals(0, aStringArray.indexOf(null));
        assertEquals(0, aStringArray.indexOfMatchingCase(null));
    }

    @Test // Null Handling
    public void shouldbeAbleToCheckIfNullExists(){
        int size = addRandomStrings();

        aStringArray.insert(Math.abs(random.nextInt(size-1)), null);
        assertTrue(aStringArray.contains(null));
        assertTrue(aStringArray.containsMatchingCase(null));
    }

    @Test // Null Handling
    public void shouldBeAbleToHandleNull(){
        aStringArray.add(null);
        String newString = createString();
        aStringArray.add(newString);

        assertTrue(aStringArray.contains(newString));
        assertTrue(aStringArray.containsMatchingCase(newString));
    }

    @Test // Join test
    public void joiningAnEmptyArrayShouldGiveAnEmptyString(){
        assertEquals(0, "".compareTo(aStringArray.join(",")));
    }

    @Test // Join test
    public void joiningASingleElementArrayShouldResultInNoDelimiter(){
        String randomString = createString();
        aStringArray.add(randomString);
        assertEquals(0, randomString.compareTo(aStringArray.join(",")));
    }

    @Test // Join test
    public void joiningAnArrayContainingTwoElementsShouldHaveADelimiter(){
        String randomString1 = createString();
        String randomString2 = createString();
        String delimiter = ",";
        aStringArray.add(randomString1);
        aStringArray.add(randomString2);
        assertEquals(0, (randomString1+delimiter+randomString2).compareTo(aStringArray.join(delimiter)));

    }

}