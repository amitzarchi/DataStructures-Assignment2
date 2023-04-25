
public class xComparator extends PointComparator {
    public xComparator() {
        super();
    }
    @Override
    public int compare(Point p1, Point p2) {
        if (p1.getX() > p2.getX())
            return 1;
        else if(p1.getX() < p2.getX())
            return -1;
        else
            return 0;
    }
    @Override
    public int compareByInt(Point p, int n) {
        if (p.getX() > n)
            return 1;
        else if (p.getX() < n)
            return -1;
        else
            return 0;
    }
}