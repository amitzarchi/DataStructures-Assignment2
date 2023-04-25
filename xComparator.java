import java.util.Comparator;

public class xComparator implements Comparator<Point> {
    public xComparator() {
        super();
    }
    public int compare(Point p1, Point p2) {
        if (p1.getX() > p2.getX())
            return 1;
        else if(p1.getX() < p2.getX())
            return -1;
        else
            return 0;
    }
}