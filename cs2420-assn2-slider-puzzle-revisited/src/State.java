/*
Very simple object to contain steps and id. The board does the heavy lifting.
 */

public class State implements Comparable<State> {
    private String steps;
    private String id;

    State(String id, String steps) {
        this.steps = steps;
        this.id = id;
    }

    /**
     * @return last move made
     */
    public char getLast() {
        if (steps.equals("")) return '*';
        int last = steps.length();
        return steps.charAt(last-1);
    }

    public String getId() {
        return id;
    }

    public String getSteps() {
        return steps;
    }

    public int getNumSteps() {
        return steps.length();
    }

    public String toString(){
        return "State " + id + " steps: " + steps;

    }

    /**
     ******* Manhattan Strategy *******
     * @return an integer that gives the Manhattan distance. The lower the Manhattan distance the better.
     *
     * $$$ After a few tests it seems the Manhattan Strategy is the best (1st best) of the three $$$
     */
    public int getPriority3() {
        int priority = 0;
        String correctID = "123456780";

        for (int i = 0; i < getId().length(); i++) {
            int column = (i % 3);
            int row = (int)(i / 3);
            int currentValue = getId().charAt(i);
            int x = correctID.indexOf(currentValue);
            int correctColumn = (x % 3);
            int correctRow = (int)(x / 3);

            priority += (Math.abs(correctColumn - column) + Math.abs(correctRow - row));
        }

        return priority;
    }

    /**
     ******* Hamming Strategy *******
     * @return an integer that gives the Hamming distance. The lower the hamming distance the better.
     *
     * $$$ After a few tests it seems the Hamming Strategy is the worst (3rd best) of the three. $$$
     */
    public int getPriority2() {
        int wrongPlacement = 0;
        String correctID = "123456780";

        for (int i = 0; i < getId().length(); i++) {
            if (getId().charAt(i) != correctID.charAt(i)) wrongPlacement++;
        }

        return wrongPlacement;
    }

    /**
     ******* Inversion Strategy *******
     * @return an integer that gives the inversion count. The lower the count, the closer it is to being solved.
     *
     * $$$ After a few tests it seems the Inversion Strategy is the 2nd best of the three. $$$
     */
    public int getPriority() {
        int inversions = 0;

        for (int i = 0; i < getId().length() - 1; i++) {

            if ((getId().charAt(i)) == '0') {
                continue;
            }

            for (int j = i+1; j < getId().length(); j++) {

                if ((getId().charAt(j)) == '0') {
                    continue;
                }

                if ((int)(getId().charAt(i)) > (int)(getId().charAt(j))) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    @Override
    public int compareTo(State o) {
        return Integer.compare(getPriority3()+getNumSteps(), o.getPriority3()+o.getNumSteps());
    }
}
