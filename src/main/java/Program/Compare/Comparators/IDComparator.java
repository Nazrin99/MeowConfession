package Program.Compare.Comparators;

import Program.Confession.ConfessionPost;

import java.util.Comparator;



public class IDComparator implements Comparator<ConfessionPost> {
    @Override
    public int compare(ConfessionPost o1, ConfessionPost o2) {
        return o1.getConfessionID().compareTo(o2.getConfessionID());
    }
}
