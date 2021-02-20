package spellchecker;

public class SpellChecker {

    private StringArray words = new StringArray();
    private StringArray commonWords = new StringArray();
    private int allowedDifference = 1; // the larger the value, the more tolerant the possible spellings are.

    // letterIndices indicate when words begins with a new letter, in alphabetical order (A -> Z)
    private int[] letterIndices = new int[26];

    public SpellChecker() {
        loadDictionary();
        loadCommonWords();
    }

    // pulled from: https://github.com/dwyl/english-words/blob/master/words_alpha.txt & then sorted.
    private void loadDictionary() {
        // Dictionary MUST NOT contain duplicate single letters.
        FileInput dictionary = new FileInput("../Coursework/src/spellchecker/newDictionary.txt");
        int letterIndex = 0;

        for (int index = 0; dictionary.hasNextLine(); index++) {
            String word = dictionary.nextLine().toLowerCase();
            if (word.length() == 1) {
                letterIndices[letterIndex] = index;
                letterIndex++;
            }
            words.add(word);
        }
        dictionary.close();
    }

    // pulled from: https://gist.github.com/deekayen/4148741
    private void loadCommonWords() {
        FileInput listOfCommonWords = new FileInput("../Coursework/src/spellchecker/commonWords.txt");
        while (listOfCommonWords.hasNextLine()) {
            commonWords.add(listOfCommonWords.nextLine().toLowerCase());
        }
        listOfCommonWords.close();
    }

    public void setAllowedDifference(int allowedDifference) {
        this.allowedDifference = allowedDifference;
    }

    public int getAllowedDifference() {
        return this.allowedDifference;
    }

    private int getNextLetterIndex(int letterIndex) {
        if (letterIndex != letterIndices.length - 1) {
            // if not letter z.
            return letterIndices[letterIndex + 1];
        } else {
            return words.size();
        }
    }

    private static String sanitiseWord(String word) {
        if (word == null) {
            return null;
        }
        word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

        if (word.length() == 0) {
            return null;
        }
        return word;
    }

    // inserts a character at the specified index, and returns it.
    private static String insertCharIntoString(String string, char character, int index) {
        return string.substring(0, index) + character + string.substring(index);
    }

    // removes character at specified index from the string, and returns it.
    private static String removeCharFromString(String string, int index) {
        return string.substring(0, index) + string.substring(index + 1);
    }

    // swaps the character at specified index with the character after it.
    private static String swapWithNextCharacter(String string, int index) {
        return string.substring(0, index) + string.charAt(index + 1) + string.charAt(index) + string.substring(index + 2);
    }

    // returns a boolean, indicating whether the current letter, and the letter after it has been swapped. ae, ea
    private static boolean isCurrentAndNextCharacterSwapped(String s1, String s2, int index) {
        return s1.charAt(index) == s2.charAt(index + 1) && s1.charAt(index + 1) == s2.charAt(index);
    }

    // checks if the first (index) letters are the same for both strings.
    private static boolean isStartStringEqual(String s1, String s2, int index) {
        return s1.substring(0, index).compareTo(s2.substring(0, index)) == 0;
    }

    /**
     * checks to see if there is a missing/extra character, and attempts to repair the word by adding/removing the char.
     *
     * @param validWord    a word that is spelt correctly, and is going to be used to check misspeltWord against.
     * @param misspeltWord an incorrectly spelt word
     * @param index        the index as to where to repair the word by inserting/removing a character.
     * @return if the word matches, and can be repaired then return the repaired word, otherwise return misspeltWord.
     */
    private static String repairCharacterDifference(String validWord, String misspeltWord, int index) {
        if (index > 0 && isStartStringEqual(validWord, misspeltWord, index)) {
            // ensure that there is a substring before this index, and that both strings have the same start substrings.
            if (misspeltWord.length() < validWord.length()) {
                // missing a character in misspeltWord.
                return insertCharIntoString(misspeltWord, validWord.charAt(index), index);
            }

            if (misspeltWord.length() > validWord.length()) {
                // extra character exists in misspeltWord
                return removeCharFromString(misspeltWord, index);
            }
        }
        return misspeltWord;

    }

    /**
     * fetches the number of differences in two strings, by comparing the characters in both strings at the same index.
     *
     * @param validWord    the String that is going to be compared with misspeltWord.
     * @param misspeltWord the String that is going to be compared with validWord.
     * @return an integer representing the number of differences between the characters of both strings.
     */
    private static int getNumberOfDifferences(String validWord, String misspeltWord) {
        int differences = Math.abs(validWord.length() - misspeltWord.length()); // difference in length.
        int shortestLength = Math.min(validWord.length(), misspeltWord.length());

        for (int i = 0; i < shortestLength; i++) {
            if (validWord.charAt(i) != misspeltWord.charAt(i)) {
                differences++;

                if (i < shortestLength - 1 && isCurrentAndNextCharacterSwapped(validWord, misspeltWord, i)) {
                    misspeltWord = swapWithNextCharacter(misspeltWord, i);
                }

                if (validWord.compareTo(repairCharacterDifference(validWord, misspeltWord, i)) == 0) {
                    return differences - 1;
                }

            }
        }
        return differences;
    }

    /**
     * checks whether the string given is spelled correctly. Using First Letter indexing for faster searching.
     *
     * @param word: the word that is to be checked by the spell checker.
     * @return true/false indicating whether or not the word is spelt correctly.
     */
    public boolean isMisspelt(String word) {
        word = sanitiseWord(word);
        if (word == null) return true;

        int firstCharLetterIndex = (int) word.charAt(0) - 97; //maps it onto the letterIndices array.
        int finalIndex = getNextLetterIndex(firstCharLetterIndex);

        for (int Index = letterIndices[firstCharLetterIndex]; Index < finalIndex; Index++) {
            // checks every word starting from the section with the same first letter as the given word.
            if (words.get(Index).compareTo(word) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * fetches the closest match to the given word. Returns ? if word is null or an empty string.
     *
     * @param word: String containing the word that needs a close match.
     * @return String containing the closest matching string to the given word.
     */
    public String getClosestMatch(String word) {
        word = sanitiseWord(word);
        if (word == null) return "?";

        String closestMatch = words.get(0);
        int lowestDifference = getNumberOfDifferences(sanitiseWord(words.get(0)), word);
        for (int index = 1; index < words.size(); index++) {

            // this part is to ensure that at least the best possible solution is presented.
            String validWord = sanitiseWord(words.get(index));
            int difference = getNumberOfDifferences(validWord, word);
            if (difference < lowestDifference || (difference == lowestDifference && commonWords.contains(validWord))) {
                closestMatch = validWord;
                lowestDifference = difference;
            }
        }
        return closestMatch;
    }

    /**
     * fetches the possible spellings of the incorrectly spelt word that is contained in the argument "word".
     *
     * @param word argument containing misspelled word
     * @return An array of possible correct spellings of the incorrectly spelt word that was given.
     */
    public StringArray getPossibleSpellings(String word) {
        word = sanitiseWord(word);
        StringArray possibleStrings = new StringArray();

        if (word == null) return possibleStrings;

        for (int index = 0; index < words.size(); index++) {
            String validWord = sanitiseWord(words.get(index));

            if (getNumberOfDifferences(validWord, word) <= this.allowedDifference) {
                possibleStrings.add(validWord);
            }
        }
        return possibleStrings;
    }


}
