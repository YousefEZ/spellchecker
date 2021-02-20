package spellchecker;

public class Main {
    private TextChecker textChecker;
    private Input input;

    public Main(){
        textChecker = new TextChecker();
        input = new Input();
    }

    private String getInput(){
        return input.nextLine().trim();
    }

    private void flushBuffer(){
        input.nextLine();
    }

    private String correctWord(String word){
        while (textChecker.isMisspelt(word)){
            System.out.println("❎ Spelling Error: "+ word);
            System.out.println(textChecker.getPossibleSpellings(word).join("\n"));
            System.out.println("Closest Solution: " + textChecker.getClosestMatch(word));
            System.out.print("Please enter the correct word (or enter to ignore): ");
            String newWord = getInput();
            if (newWord.length() == 0) {
                return word; //if it's an empty string, then ignore.
            }
            word = newWord;
        }
        return word;
    }

    /**
     * checks whether the sentence contains any misspelled words, & returns the correct version.
     * @param sentence String containing a sequence of words with a space as a delimiter.
     * @return string with all it's words having a correct spelling.
     */
    private String correctSentence(String sentence){
        StringArray misspeltWords = textChecker.getMisspeltWords(sentence);

        for (int index=0; index < misspeltWords.size(); index++){
            String newWord = correctWord(misspeltWords.get(index));
            sentence = sentence.replace(misspeltWords.get(index), newWord);
        }
        return sentence;
    }

    private String getFileText(){
        System.out.print("Enter the file name: ");
        FileInput reader = new FileInput(getInput());
        StringArray text = new StringArray();
        while (reader.hasNextLine()){
            text.add(reader.nextLine());
        }
        reader.close();
        return text.join("\n");
    }

    private void writeToFile(String fileName, String text){
        FileOutput writer = new FileOutput(fileName);
        writer.writeString(text);
        writer.close();
    }

    private void saveText(String text){
        while (true) {
            System.out.print("Would you like to save the text? y/n: ");
            String response = getInput().toLowerCase();
            if (response.length() > 0  && (response.charAt(0) == 'y' || response.charAt(0) == 'n')) {
                if (response.charAt(0) == 'y') {
                    System.out.print("Destination address: ");
                    writeToFile(getInput(), text);
                }
                return;
            }
            System.out.println("Invalid response.");
        }
    }

    private void changeAllowedDifference(){
        while (true){
            System.out.print("Enter the new number of allowed differences: ");

            if (input.hasNextInt()){
                int newAllowedDifference = input.nextInt();

                if (newAllowedDifference > 0){
                    textChecker.setAllowedDifference(newAllowedDifference);
                    return;
                }
                System.out.println("Please enter a positive value.");
            }
            System.out.println("Please enter a integer.");
        }
    }

    private void displayMenu(){
        System.out.println("Text Checker");
        System.out.println("-------------");
        System.out.println("1. Change the allowed difference (Current: " + textChecker.getAllowedDifference() + ") ");
        System.out.println("2. Spell check a file");
        System.out.println("3. Input text for spell checking");
        System.out.println("4. Exit");
        System.out.print("Please enter the option's corresponding number to select it: ");
    }

    private int getOption(){
        if (!input.hasNextInt()){
            System.out.println("Invalid Input");
            flushBuffer();
            return -1;
        }
        return input.nextInt();
    }

    private void processOption(int option){
        switch (option){
            case 1:
                changeAllowedDifference();
                break;
            case 2:
                saveText(correctSentence(getFileText()));
                break;
            case 3:
                System.out.println("\n\nEnter the text that you would like to spell check:");
                saveText(correctSentence(getInput()));
            case 4:
                break;
            default:
                System.out.println("Invalid Option, Please enter the corresponding option's integer.");
        }
    }

    private void run(){
        int option = 0;
        while (option != 4){
            displayMenu();
            option = getOption();
            flushBuffer();
            processOption(option);
        }
        System.out.println("Exiting...");
    }

    public static void main(String[] args){
        new Main().run();
    }

}
