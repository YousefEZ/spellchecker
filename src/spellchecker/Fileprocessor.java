package spellchecker;

class Fileprocessor{
    private void displayFileContent(String filename) {
        FileInput fileIn = new FileInput(filename);
        while (fileIn.hasNextLine()) {
            String s = fileIn.nextLine();
            System.out.println(s);
        }
        fileIn.close(); // Always close a file after it has been used.}
    }

    private String getFileName(){
        Input in = new Input();
        System.out.print("Enter filename: ");
        return in.nextLine();
    }

    public void showFile(){
        displayFileContent(getFileName());
    }

    public static void main(String[] args){
    new Fileprocessor().showFile();
    }
}