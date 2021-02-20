package spellcheckertest;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import spellchecker.FileInput;
import spellchecker.SpellChecker;
import spellchecker.StringArray;

import static org.junit.Assert.*;

/*
 * Tests for ensuring that the StringArray class is functioning correctly.
 * Made by SN:20018291 @ UCL
 */

public class SpellCheckerTest {

    private SpellChecker spellchecker;
    private Random random = new Random();
    private String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Before
    public void setUp() throws Exception{
        spellchecker = new SpellChecker();
    }

    // builds unique strings. Possibility of getting 2 equal strings is (maximum): 1/26,000.
    private String createString(){
        int length = random.nextInt(1000);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++){
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length()-1)));
        }
        return builder.toString();
    }

    @Test
    public void aWordInTheDictionaryShouldAlwaysBeSpeltCorrectly(){
        FileInput reader = new FileInput("../Coursework/src/spellchecker/newDictionary.txt");
        StringArray dictionary = new StringArray();
        while (reader.hasNextLine()){
            dictionary.add(reader.nextLine());
        }
        for (int i = 0; i<10000; i++){
            String validWord = dictionary.get(Math.abs(random.nextInt(dictionary.size())));
            assertFalse(spellchecker.isMisspelt(validWord));
        }
    }

    @Test
    public void aRandomlyGeneratedWordShouldNotBeInTheDictionary(){
        assertTrue(spellchecker.isMisspelt(createString()));
    }

    @Test
    public void whatIfIPutNullInIsMisspelt(){
        spellchecker.isMisspelt(null);
    }

    @Test
    public void whatIfIPutEmptyStringInIsMisspelt(){
        spellchecker.isMisspelt("");
    }

    @Test
    public void whatIfIPutNullInGetPossibleSpellings(){
        spellchecker.getPossibleSpellings(null);
    }

    @Test
    public void whatIfIPutEmptyStringInGetPossibleSpellings(){
        spellchecker.getPossibleSpellings("");
    }

    @Test
    public void whatIfIPutNullInGetClosestMatch(){
        spellchecker.getClosestMatch(null);
    }

    @Test
    public void whatIfIPutEmptyStringInGetClosestMatch(){
        spellchecker.getClosestMatch("");
    }

    @Test
    public void canIChangeTheAllowedDifference(){
        int currentDifference = spellchecker.getAllowedDifference();
        currentDifference++;

        spellchecker.setAllowedDifference(currentDifference);
        assertEquals(currentDifference, spellchecker.getAllowedDifference());
    }

    @Test
    public void increasingAllowedDifferenceShouldIncreaseTolerance(){
        int currentDifference = spellchecker.getAllowedDifference();
        String testString = "heab";
        StringArray strictPossibleSpellings = spellchecker.getPossibleSpellings(testString);
        currentDifference++;

        spellchecker.setAllowedDifference(currentDifference);
        StringArray tolerantPossibleSpellings = spellchecker.getPossibleSpellings(testString);
        assert(tolerantPossibleSpellings.size() > strictPossibleSpellings.size());

    }

}