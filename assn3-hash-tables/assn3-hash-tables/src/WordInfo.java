public class WordInfo {

    public WordInfo(String word) {
        this.word = word;
        this.timesUsed = 0;
    }

    final String word;
    private int timesUsed;

    // Getter Methods
    public String getWord() { return word; }
    public int getTimesUsed() { return timesUsed; }

    // Setter Methods
    public void incrementTimesUsed() { timesUsed++; }

    // Overridden Methods
    @Override
    public String toString() {
        int count = this.getTimesUsed() + 1;
        return ("Word: \"" + this.getWord() + "\"  Count: " + count);
    }

    @Override
    public int hashCode()
    {
        // Return -1 since null we do not want to store null values in our hashtable.
        if (this.word == null) return -1;
        else return this.word.hashCode();

    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        // Cast the object as a WordInfo object
        WordInfo castObj = (WordInfo) obj;

        return this.word.equals(castObj.word);

    }
}
