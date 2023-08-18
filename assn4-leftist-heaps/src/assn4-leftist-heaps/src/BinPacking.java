import java.util.Arrays;
import java.util.Random;

public class BinPacking {
    static int BINSIZE=100;
    Integer [] requests;
    BinPacking(int size){
        Random rand  = new Random(size); //Seed will cause the same sequence of numbers to be generated each test
        requests = new Integer[size];
        for (int i=0; i < size; i++){
            requests[i] =rand.nextInt(BINSIZE)+1;
        }
        if (size <= 20 ) System.out.println("Size " + size + " " +Arrays.toString(requests));
    }


    public int scheduleWorstFit() {
        // We make a maxLeftistHeap that holds all the disks and sorts them based on largest space remaining
        // From there we process the requests as they come in
        // If they can fit in the largest bin (the Root of the LeftistHeap), we put it in that one,
        // otherwise we make a new bin and store it there.
        int sumOfFileSizes = 0;
        MaxLeftistHeap<Disk> disks = new MaxLeftistHeap<>();
        for (int i=0; i < requests.length; i++) {
            if (disks.isEmpty()) {
                disks.insert(new Disk(BINSIZE));
            }
            sumOfFileSizes += requests[i];
            if (requests[i] < disks.findMax().remainingSpace) {
                Disk disk = disks.deleteMax();
                disk.add(requests[i]);
                MaxLeftistHeap<Disk> tempTree = new MaxLeftistHeap<>();
                tempTree.insert(disk);
                disks.merge(tempTree);
            }
            else {
                Disk disk = new Disk(BINSIZE);
                disk.add(requests[i]);
                disks.insert(disk);
            }
        }
        int usedBins = disks.getSize();
        System.out.println("Online Worst Fit Bin Packing Yields " + usedBins + " (requestCt=" + requests.length + ")");
        System.out.println("Minimum number of bins " + (sumOfFileSizes / 100));
        if (disks.getSize() <= 20) {
            disks.toString2();
        }
        System.out.println();

        return usedBins;
    }

    public int scheduleOfflineWorstFit() {
        // This is us 'looking' at everything before placing.
        HeapSort<Integer> heap = new HeapSort<>();
        heap.sort(requests);
        int sumOfFileSizes = 0;
        MaxLeftistHeap<Disk> disks = new MaxLeftistHeap<>();
        for (int i=0; i < requests.length; i++) {
            if (disks.isEmpty()) {
                disks.insert(new Disk(BINSIZE));
            }
            sumOfFileSizes += requests[i];
            if (requests[i] < disks.findMax().remainingSpace) {
                Disk disk = disks.deleteMax();
                disk.add(requests[i]);
                MaxLeftistHeap<Disk> tempTree = new MaxLeftistHeap<>();
                tempTree.insert(disk);
                disks.merge(tempTree);
            }
            else {
                Disk disk = new Disk(BINSIZE);
                disk.add(requests[i]);
                disks.insert(disk);
            }
        }
        int usedBins = disks.getSize();
        System.out.println("Decreasing Worst Fit Bin Packing Yields " + usedBins + " (requestCt=" + requests.length + ")");
        System.out.println("Minimum number of bins " + (sumOfFileSizes / 100));
        if (disks.getSize() <= 20) {
            disks.toString2();
        }
        System.out.println();

        return usedBins;
    }


    public static void main (String[] args)
    {
       int [] fileSizes = {10, 20, 100, 500,10000,100000};
        for (int size :fileSizes){
            BinPacking b = new BinPacking(size);
            // Online means that we decide to place the item as we encounter it.
            // Offline means we can look at all the items before placing anything.
            int worstFitBins = b.scheduleWorstFit();
            int offlineWorstFitBins = b.scheduleOfflineWorstFit();
            if (worstFitBins == offlineWorstFitBins) {
                System.out.println("BOTH are the same");
            }
            else {
                System.out.println("**Sorted is better by " + (worstFitBins - offlineWorstFitBins) + " "
                        + (int)(((double)offlineWorstFitBins / (double)worstFitBins) * 100) + "%");
            }
            System.out.println();
        }
    }
}
