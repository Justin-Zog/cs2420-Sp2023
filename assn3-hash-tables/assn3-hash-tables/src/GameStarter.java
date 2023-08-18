
import java.io.*;
import java.util.*;

public class GameStarter {
    public GameStarter() {
        H = new HashTable<WordInfo>();
        totalScore = 0;
    }

    private int totalScore;
    static HashTable<String> legalWords = getLegalWords();
    private String fileName;
    private HashTable<WordInfo> H;

    public int getTotalScore() { return totalScore; }
    public void incrementTotalScore(int i) { totalScore += i; }

    public int computeScore(WordInfo wi) {
        if (!legalWords.contains(wi.word)) {
            System.out.println(wi.word + " is an illegal word and scored 0 points.");
            return 0;
        }

        int[] letterValue = new int[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        int[] reuseValues = new int[]{5, 4, 3, 2};
        int valueOfLetters = 0;

        for (int i=0; i < wi.word.length(); i++) {
            valueOfLetters += letterValue[wi.word.charAt(i)-'a'];
        }

        int lengthValue = (wi.word.length() < 8) ? (wi.word.length() - 2) : 6;
        if (lengthValue < 0) lengthValue = 0;
        int bonus = (wi.getTimesUsed() <= 15) ? reuseValues[((wi.getTimesUsed() + 4) / 5)] : 1;
        return (lengthValue * valueOfLetters * bonus);
    }

    public void playGame(String filename) {
        fileName = filename;
        System.out.println("FILE " + filename);
        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {
                WordInfo word = new WordInfo(sc.next());
                WordInfo foundWord = H.find(word);
                if (foundWord == null) {
                    H.insert(word);
                    incrementTotalScore(computeScore(word));
                }
                else {
                    foundWord.incrementTimesUsed();
                    incrementTotalScore(computeScore(foundWord));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static HashTable<String> getLegalWords() {
        HashTable<String> legalWords = new HashTable<>();
        try {
            Scanner sc = new Scanner(new File("dictionary.txt"));
            while (sc.hasNext()) {
                String word = sc.next();
                legalWords.insert(word);
            }

        } catch (Exception e) {
            System.out.println("Something went wrong with dictionary.txt");
            e.printStackTrace();
        }

        return legalWords;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("This games score is: " + this.getTotalScore() + "\n");
        sb.append("Hash Table Statistics:\n");
        sb.append("\tTotal finds: " + H.getTotalFinds() + "\n");
        sb.append("\tTotal probes: " + H.getTotalProbes() + "\n");
        sb.append("\tItems stored in table: " + H.size() + "\n");
        sb.append("\tPhysical Length of Hash Table: " + H.capacity() + "\n");
        sb.append("\tContents of the first 20 elements in the hash table:\n" + H.toString(20));

        return sb.toString();
    }


    public static void main(String[] args) {
         String[] games = {"game0.txt", "game1.txt", "game2.txt", "game3.txt", "game4.txt"};
         for (String filename : games) {
            GameStarter g = new GameStarter();
            g.playGame(filename);
            System.out.println(g);
        }

    }

}
