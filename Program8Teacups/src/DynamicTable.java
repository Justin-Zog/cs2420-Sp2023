public class DynamicTable {

    final int maxSize;
    final int amount;


    public DynamicTable(int amount, int maxSize) {
        this.maxSize = maxSize;
        this.amount = amount;
    }


     // This function will recursively print all the possible groups that we can split different sets of teacups into.
    public void printChoices() {
        // Base cases.
        if (amount == 0) return;
        if (maxSize == 0) return;

        int[] maxSizeCounter = new int[amount];
        maxSizeCounter[amount-1] = amount;

        System.out.println("Teaset Size is " + amount + " and Maximum group size is " + maxSize);
        printChoices(1, maxSizeCounter, maxSize, amount);

    }


    private void printChoices(int maxSize, int[] maxSizeCounter, int initialMaxSize, int initialAmt) {
        //Base cases.
        if (maxSize > initialMaxSize) return;

        // !!! THIS PRINTS OFF OUR CURRENT maxSizeCounter ARRAY !!! \\
        StringBuilder sb = new StringBuilder();
        int howManyOnes = initialAmt;
        // Loops through the maxSizeCounter array
        for (int i=maxSizeCounter.length-maxSize; i<maxSizeCounter.length; i++) {
            // Checks if we are printing ones and breaks out of the loop.
            if (i == initialAmt-1) {
                for (int j=0; j<howManyOnes; j++) {
                    sb.append("1 ");
                }
                break;
            }

            // Adds everything that isn't a one.
            for (int j=0; j<maxSizeCounter[i]; j++) {
                sb.append((initialAmt - i) + " ");
                howManyOnes -= (initialAmt - i);
            }
        }
        System.out.println(sb);

        //  !!! THIS UPDATES OUR maxSizeCounter ARRAY !!! \\
        int index = initialAmt-2;
        while (true) {
            // Checks to see if we went too far
            // If we've gone to far it is time to increase maxSize.
            if (index < initialAmt-maxSize) {
                maxSize++;
                maxSizeCounter[index]++;
                break;
            }

            maxSizeCounter[index]++;
            int total = 0;
            for (int i=(initialAmt-maxSize); i<(initialAmt-1); i++) {
                total += maxSizeCounter[i] * (initialAmt - i);
            }

            // Everything is good.
            if (total <= initialAmt) break;
            // Our total is too much, reset the current index to zero and "increase" the index.
            if (index != 0) {
                maxSizeCounter[index] = 0;
                index--;
            }
            else {
                maxSize++;
                break;
            }
        }
        // Now that everything is updated, call the method again.
        printChoices(maxSize, maxSizeCounter, initialMaxSize, initialAmt);
    }


    public void printMaxProfit(int[] profitChart) {
        int[][] profitMatrix = new int[maxSize][amount];
        // This loops through all rows and columns of our table.
        for (int i=0; i<maxSize; i++) {
            for (int j=0; j<amount; j++) {
                // Pull from the row above
                if (i>j) {
                    profitMatrix[i][j] = profitMatrix[i-1][j];
                    continue;
                }
                // Sees if a new 'use it' is available.
                int usedIt = 0;
                if ((j+1)%(i+1) == 0) {
                    usedIt = ((j+1)/(i+1))*profitChart[i];
                }
                // Now we loop through our current row to find the max profit for the current row and column.
                int savedMax = 0;
                for (int k=0; ((j+1)-(k+1)>=k); k++) {
                    if (((j+1)-(k+1)) > maxSize) continue;
                    int result;
                    if (j==0) {
                        result = profitMatrix[i][j-k] + profitMatrix[i][k];
                    }
                    else {
                        result = profitMatrix[i][j-(k+1)] + profitMatrix[i][k];
                    }
                    if (result > savedMax) savedMax = result;
                }
                // Now we have our potential maxes and put the best one in the matrix.
                profitMatrix[i][j] = Math.max(usedIt, savedMax);
            }
        }
        printProfitMatrix(profitMatrix);
        System.out.println();
        printBestSum(profitMatrix);
    }


    public void printProfitMatrix(int[][] matrix) {
        // Get the maximum number of digits in any element of the matrix
        int maxDigits = 0;
        for (int[] row : matrix) {
            for (int element : row) {
                int digits = String.valueOf(element).length();
                if (digits > maxDigits) {
                    maxDigits = digits;
                }
            }
        }

        // Print column indices
        System.out.print("\t");
        for (int j = 0; j < matrix[0].length; j++) {
            System.out.printf("%-" + (maxDigits + 2) + "d", j+1);
        }
        System.out.println();

        // Print the matrix with row indices
        for (int i = 0; i < matrix.length; i++) {
            // Print the row index
            System.out.printf("%-" + (maxDigits + 2) + "d", i+1);

            // Print the matrix elements
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%-" + (maxDigits + 2) + "d", matrix[i][j]);
            }
            System.out.println();
        }
    }


    public void printBestSum(int[][] matrix) {
        for (int j=0; j<amount; j++) {
            // Gets how we got to the best sum.
            String howToGetMaxSum = "";
            int tracker = j;
            while (tracker >= 0) {
                // Find the correct row.
                int rowCounter = maxSize-1;
                while (rowCounter>0) {
                    // Find the right row.
                    if (matrix[rowCounter-1][tracker] == matrix[rowCounter][tracker]) rowCounter--;
                    else break;
                }
                // Update the tracker and how we got there.
                howToGetMaxSum += (" " + (rowCounter+1));
                tracker -= (rowCounter+1);
            }

            System.out.println("Best Sum for (" + (j+1) + " teacup): $" + matrix[maxSize-1][j] + howToGetMaxSum);
        }
    }


    public static void main(String[] args) {
        DynamicTable dt = new DynamicTable(24, 12);
        // dt.printChoices();

        int[] profitChart1 = new int[] {1, 3, 5, 9, 10, 15, 17, 18, 19, 22, 25, 27};
        int[] profitChart2 = new int[] {2, 5, 8, 9, 10, 15, 19, 23, 24, 29, 30, 32};

        dt.printMaxProfit(profitChart1);
        System.out.println("\n");
        dt.printMaxProfit(profitChart2);

    }

}


