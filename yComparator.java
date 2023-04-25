import java.util.Comparator;

public class yComparator extends PointComparator {
    public yComparator() {
        super();
    }
    public int compare(Point p1, Point p2) {
        if (p1.getY() > p2.getY())
            return 1;
        else if(p1.getY() < p2.getY())
            return -1;
        else
            return 0;
    }
    public int compareByInt(Point p, int n) {
        if (p.getY() > n)
            return 1;
        else if (p.getY() < n)
            return -1;
        else
            return 0;
    }

}