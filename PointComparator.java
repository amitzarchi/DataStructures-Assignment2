import java.util.Comparator;

public abstract class PointComparator implements Comparator<Point>{

    abstract public int compare(Point p1, Point p2);

    abstract public int compareByInt(Point p, int n);

}