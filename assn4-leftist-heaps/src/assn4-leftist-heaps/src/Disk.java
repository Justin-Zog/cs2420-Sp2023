public class Disk implements Comparable<Disk> {
    String contents;
    int remainingSpace;
    int id;

    int idTracker = 0;

    Disk(int maxSize){
       contents="";
       remainingSpace = maxSize;
       this.id = idTracker;
       idTracker++;
     }
     public boolean add(int oneFileSize){
        if (remainingSpace<oneFileSize) return false;
        remainingSpace-=oneFileSize;
        contents+= " "  + oneFileSize;
        return true;
     }
     public String toString(){
        return id + "(" + remainingSpace + ")  : " + contents;
     }

     @Override
     public int compareTo(Disk disk2) {
         return Integer.compare(this.remainingSpace, disk2.remainingSpace);
     }
}
