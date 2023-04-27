
public class DataStructure implements DT {

	private Axis xAxis;
	private Axis yAxis;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure() {
		xAxis = new Axis(new xComparator());
		yAxis = new Axis(new yComparator());
	}

	public DataStructure(Axis xAxis, Axis yAxis) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}

	public Axis getAxisX() {
		return this.xAxis;
	}

	public Axis getAxisY() {
		return this.yAxis;
	}


	@Override //O(n)
	public void addPoint(Point point) {
		// create 2 containers, set as twins, insert to lists
		Container twin1 = new Container(point);
		Container twin2 = new Container(point);
		twin1.setTwin(twin2);
		twin2.setTwin(twin1);
		xAxis.add(twin1);
		yAxis.add(twin2);
	}

	@Override //O(n)
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		Axis rangeAxis;
		if (axis) rangeAxis = this.xAxis.getRange(min, max);
		else rangeAxis = this.yAxis.getRange(min, max);
		Container curr = rangeAxis.getFirst();
		Point[] arr = new Point[rangeAxis.getSize()];
		int index = 0;
		while (index < arr.length && curr != null){
			arr[index] = curr.getData();
			curr = curr.getNext();
		}
		return arr;
	}

	@Override //O(n)
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		Axis rangeAxis;
		if (axis) rangeAxis = this.yAxis.getFitRange(min, max, this.xAxis.getComparator());
		else rangeAxis = this.xAxis.getFitRange(min, max, this.yAxis.getComparator());
		Container curr = rangeAxis.getFirst();
		Point[] arr = new Point[rangeAxis.getSize()];
		int index = 0;
		while (index < arr.length && curr != null){
			arr[index] = curr.getData();
			curr = curr.getNext();
		}
		return arr;
	}

	@Override //O(1)
	public double getDensity() {
		int xMax = xAxis.getLast().getData().getX();
		int xMin = xAxis.getFirst().getData().getX();
		int yMax = yAxis.getLast().getData().getY();
		int yMin = yAxis.getFirst().getData().getY();
		double density = xAxis.getSize() / (xMax - xMin) * (yMax - yMin);
		return density;
	}

	@Override //O(|A|)
	public void narrowRange(int min, int max, Boolean axis) {
		Axis currAxis;
		Axis secondAxis;
		Container toRemove;
		Container current;
		if (axis) {
			currAxis = xAxis;
			secondAxis = yAxis;
		} else {
			currAxis = yAxis;
			secondAxis = xAxis;
		}
		PointComparator comp = currAxis.getComparator();
		current = currAxis.getFirst();
		while (comp.compareByInt(current.getData(), min) < 0) {
			toRemove = current;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
			current = current.next;
		}
		current = currAxis.getLast();
		while (comp.compareByInt(current.getData(), max) > 0) {
			toRemove = current;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
			current = current.prev;
		}
	}

	@Override //O(1)
	public Boolean getLargestAxis() {
		int xMax = xAxis.getLast().getData().getX() - xAxis.getFirst().getData().getX();
		int yMax = yAxis.getLast().getData().getY() - yAxis.getFirst().getData().getY();
		return (xMax > yMax);
	}

	@Override//O(1)
	public Container getMedian(Boolean axis) {
		if (axis)
			return xAxis.getMedian();
		else
			return yAxis.getMedian();
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
		Point[] ans = new Point[2];
		int mid;
		double minDist = (width/2);
		if (axis) mid = container.getData().getX();
		else mid = container.getData().getY();
		Point[] arr =getPointsInRangeOppAxis((int)(mid-(width/2)), (int)(mid+(width/2)), axis); //O(n)
		int size = arr.length;
		for (int i = 0; i< size; i++) {															//O(|B|)
				for(int j = i+1; j<size && (getPointValue(arr[j],axis) - getPointValue(arr[i],axis)) < minDist; j++) {		//O(7)
					if (getDistance(arr[j], arr[i]) < width/2) {
						minDist = getDistance(arr[j], arr[i]);
						ans[0] = new Point(arr[i]);
						ans[1] = new Point(arr[j]);
					}
				}
		}
			return ans;
	}

	@Override
	public Point[] nearestPair() {
		Point[] ans;
		if (xAxis.getSize() == 2) {
			return ans = new Point[]{xAxis.getFirst().getData(), xAxis.getLast().getData()};
		} else if (xAxis.getSize() < 2) {
			return ans = new Point[0];
		} else {
			boolean biggerAxis = getLargestAxis();                        //O(1)
			DataStructure[] arr = SplitByMedian(biggerAxis);            //O(n)
			Point[] left = arr[0].nearestPair();                        //(T(n/2))
			Point[] right = arr[0].nearestPair();                        //T(n/2)
			double leftDistance = getDistance(left[0], left[1]);                    //O(1)
			double rightDistance = getDistance(right[0], right[1]);                    //O(1)
			double minDist;
			if (leftDistance < rightDistance) {
				ans = left;
				minDist = leftDistance;
			} else {
				ans = right;
				minDist = rightDistance;
			}
			Point[] middle = nearestPairInStrip(this.getMedian(biggerAxis), 2 * minDist, biggerAxis);	//O(n)
			if (getDistance(middle[0], middle[1]) < minDist) {
				ans = middle;
			}
		}
		return ans;
	}

	//O(n)
	private DataStructure GetRangeDS(int min, int max, boolean X_Y) {
		Axis newAxisX;
		Axis newAxisY;
		if (X_Y) {
			newAxisX = this.getAxisX().getRange(min, max);
			newAxisY = this.getAxisY().getFitRange(min, max, newAxisX.getComparator());
		} else {
			newAxisY = this.getAxisY().getRange(min, max);
			newAxisX = this.getAxisX().getFitRange(min, max, newAxisY.getComparator());
		}
		return new DataStructure(newAxisX, newAxisY);
	}

	//O(n)
	private DataStructure[] SplitByMedian(boolean X_Y) {
		DataStructure[] ans = new DataStructure[2];
		Axis currAxis;
		int min = 0;
		int mid = 0;
		int max = 0;
		if(X_Y) {
			currAxis = this.getAxisX();
			min = currAxis.getFirst().getData().getX();
			max = currAxis.getLast().getData().getX();
			mid = currAxis.getMedian().getData().getX();
		} else {
			currAxis = this.getAxisY();
			min = currAxis.getFirst().getData().getY();
			max = currAxis.getLast().getData().getY();
			mid = currAxis.getMedian().getData().getY();
		}
		ans [0] = GetRangeDS(min, mid-1, X_Y);           //O(n)
		ans [1] = GetRangeDS(mid+1, max, X_Y);            //O(n)
		return ans;
	}

	private double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
	}
	private int getPointValue(Point point, boolean axis) {
		if (axis) return point.getX();
		else return point.getY();
	}
}

