package spellchecker;

public class StringArray {

    private String[] strings;
    private int emptyIndex;


    public StringArray(){
        strings = new String[100];
        emptyIndex = 0;
    }

    public StringArray(StringArray a){
        strings = a.strings.clone();
        emptyIndex = a.emptyIndex;
    }

    public int size() {
        return emptyIndex;
    }

    public boolean isEmpty(){
        return emptyIndex == 0;
    }

    public String get(int index){
        return strings[index];
    }

    private boolean validIndex(int index){
        return index >= 0 && index < emptyIndex;
    }

    public void set(int index, String s){
        if (validIndex(index)){
            strings[index] = s;
        }
    }

    public void add(String s){
        ensureEmptySpace();
        strings[emptyIndex] = s;
        emptyIndex++;
    }

    private void ensureEmptySpace(){
        // if there are no more free elements.
        if (emptyIndex == strings.length){
            String[] newStrings = new String[strings.length*2]; // double the size of the array.
            System.arraycopy(strings, 0, newStrings, 0, emptyIndex); // copy contents of the array.
            strings = newStrings; // assign the new larger array into the strings array.
        }
    }

    private void pushFrom(int index){
        if (validIndex(index)) {
            ensureEmptySpace();
            if (emptyIndex - index >= 0) {
                System.arraycopy(strings, index, strings, index + 1, emptyIndex - index);
            }
            strings[index] = null;
            emptyIndex++;
        }
    }

    public void insert(int index, String s){
        if (index == 0 && isEmpty()){
            add(s);
        } else if (index < emptyIndex){
            pushFrom(index);
            strings[index] = s;
        }
    }

    public void remove(int index){
        if (validIndex(index)) {
            int i;
            for (i = index; i < emptyIndex - 1; i++) {
                strings[i] = strings[i + 1];
            }
            strings[i + 1] = null; // last element should be deleted.
            emptyIndex--; // decrement by one.
        }
    }

    public boolean contains(String s){
        return indexOf(s) != -1;
    }

    public boolean containsMatchingCase(String s){
        return indexOfMatchingCase(s) != -1;
    }

    public int indexOf(String s){
        if (s == null){
            return indexOfNull();
        }
        s = s.toLowerCase();
        for (int i=0; i< emptyIndex; i++){
            String string = strings[i];
            if (string == null){
                continue;
            }
            if (s.equals(string.toLowerCase())){
                return i;
            }
        }
        return -1;
    }

    private int indexOfNull(){
        for (int i=0; i< emptyIndex; i++) {
            if (strings[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfMatchingCase(String s){
        if (s == null) {
            return indexOfNull();
        }
        for (int i=0; i< emptyIndex; i++){
            if (s.equals(strings[i])){
                return i;
            }
        }
        return -1;
    }

    public String join(String delimiter){
        if (isEmpty()){
            // nothing in the array.
            return "";
        }

        String joinedString = get(0);
        for (int i=1; i<emptyIndex; i++){
            joinedString = joinedString.concat(delimiter + get(i));
        }

        return joinedString;
    }

}
