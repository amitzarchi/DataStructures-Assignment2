
public class DataStructure implements DT {

	private Axis xAxis;
	private Axis yAxis;
	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure()
	{
		xAxis = new Axis(new xComparator());
		yAxis = new Axis(new yComparator());
	}

	@Override
	public void addPoint(Point point) {
		// create 2 containers, set as twins, insert to lists
		
	}

	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDensity() {
		int xMax = xAxis.getLast().getData().getX();
		int xMin = xAxis.getFirst().getData().getX();
		int yMax = yAxis.getLast().getData().getY();
		int yMin = yAxis.getFirst().getData().getY();
		double density = xAxis.getSize() / (xMax - xMin) * (yMax - yMin);
		return density;
	}

	@Override
	public void narrowRange(int min, int max, Boolean axis) {
		removeUntil(min, axis);
		removeFrom(max, axis);
	}

	@Override
	public Boolean getLargestAxis() {
		int xMax = xAxis.getLast().getData().getX();
		int yMax = xAxis.getLast().getData().getY();
		return (xMax > yMax);
	}

	@Override
	public Container getMedian(Boolean axis) {
		if (axis)
			return xAxis.getMedian();
		else
			return yAxis.getMedian();
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width,
			Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	private void removeUntil(int n, boolean axis) {
		PointComparator comparator;
		Container current;
		if (axis) {
			comparator = new xComparator();
			current = xAxis.getFirst();
		}
		else {
			comparator = new yComparator();
			current = yAxis.getFirst();
		}
		while (comparator.compareByInt(current.getData(), n) < 0) {
			Container toRemove = current;
			xAxis.remove(toRemove.twin);
			xAxis.remove(toRemove);
			current = current.next;
		}
	}
	private void removeFrom(int n, boolean axis) {
		PointComparator comparator;
		Container current;
		if (axis) {
			comparator = new xComparator();
			current = xAxis.getLast();
		}
		else {
			comparator = new yComparator();
			current = yAxis.getLast();
		}
		while (comparator.compareByInt(current.getData(), n) < 0) {
			Container toRemove = current;
			xAxis.remove(toRemove.twin);
			xAxis.remove(toRemove);
			current = current.prev;
		}
	}
}

