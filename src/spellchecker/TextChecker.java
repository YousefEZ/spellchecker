package spellchecker;

public class TextChecker extends SpellChecker{

    //ensures that multiple spaces are changed to one for splitOnSpace.
    private String ensureSingleSpaceDelimiter(String string){
        return string.replaceAll("[ ]+", " ");
    }

    //removes any punctuation and numbers.
    private String filterPunctuation(String string) {
        return string.replaceAll("[^a-zA-Z ]*", "").trim();
    }

    //changes any newlines or tabs into spaces.
    private String changeEscapes(String string){
        return string.replaceAll("[\n\t]", " ");
    }

    //splits the sentence on whitespaces.
    private StringArray splitOnSpace(String sentence){
        StringArray sentenceWords = new StringArray();
        if (sentence.length() == 0) return sentenceWords;

        int startIndexOfCurrentWord = 0;
        int currentIndex;
        for (currentIndex = 0; currentIndex < sentence.length(); currentIndex++){
            if (sentence.charAt(currentIndex) == ' ') {
                sentenceWords.add(sentence.substring(startIndexOfCurrentWord, currentIndex));
                startIndexOfCurrentWord = currentIndex + 1;
            }
        }
        sentenceWords.add(sentence.substring(startIndexOfCurrentWord, currentIndex));
        return sentenceWords;
    }

    /**
     * checks whether the sentence contains any misspelt words.
     * @param sentence String containing a sequence of words with a space as a delimiter.
     * @return StringArray containing the misspelt words.
     */
    public StringArray getMisspeltWords(String sentence){
        if (sentence == null) return new StringArray();

        StringArray givenWords = splitOnSpace(ensureSingleSpaceDelimiter(filterPunctuation(changeEscapes(sentence))));
        StringArray misspeltWords = new StringArray();
        for (int index=0; index < givenWords.size(); index++){

            if (isMisspelt(givenWords.get(index))){
                misspeltWords.add(givenWords.get(index));
            }
        }
        return misspeltWords;
    }

}
