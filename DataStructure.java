
public class DataStructure implements DT {

	private Axis xAxis;
	private Axis yAxis;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure() {
		xAxis = new Axis(new xComparator());
		yAxis = new Axis(new yComparator());
	}
	// using Axis's constructor to generate both X and Y Axis. Each O(1), Total O(1)

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
	// Generating new Containers and setting as twins costs O(1), adding them to the lists using Axis's add method which sort them in the right place, which costs O(n). O(n) total 
	public void addPoint(Point point) {
		// create 2 containers
		Container twin1 = new Container(point);
		Container twin2 = new Container(point);
		//set each others as twins
		twin1.setTwin(twin2);
		twin2.setTwin(twin1);
		// adding to both lists
		xAxis.add(twin1);
		yAxis.add(twin2);
	}

	@Override //O(n)
	// Using Axis's "getRange" method which costs O(n), iterating thrugh them and adding to the Array costs O(n). O(n) total
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		Axis rangeAxis;
		// using Axis's "getRange" method
		if (axis) rangeAxis = this.xAxis.getRange(min, max);
		else rangeAxis = this.yAxis.getRange(min, max);
		Container curr = rangeAxis.getFirst();
		Point[] arr = new Point[rangeAxis.getSize()];
		int index = 0;
		//adding the points to the output array
		while (index < arr.length && curr != null){
			arr[index] = curr.getData();
			curr = curr.getNext();
			index++;
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
			curr = curr.next;
			index++;
		}
		return arr;
	}

	@Override //O(1)
	// Using only methods which costs O(1), calculating the density costs O(1). O(1) total
	public double getDensity() {
		double xMax = xAxis.getLast().getData().getX();
		double xMin = xAxis.getFirst().getData().getX();
		double yMax = yAxis.getLast().getData().getY();
		double yMin = yAxis.getFirst().getData().getY();
		double size = xAxis.getSize();
		double density = size / ((xMax - xMin) * (yMax - yMin));
		return density;
	}

	@Override //O(|A|)
	//iterating only thrugh the elements which we need to remove, one time from the head of the list to the min value, once more from the tail of the list to the max, costs O(|A|).  O(|A|) total
	public void narrowRange(int min, int max, Boolean axis) {
		Axis currAxis;
		Axis secondAxis;
		Container toRemove;
		Container current;
		//setting the input Axis and Comparator
		if (axis) {
			currAxis = xAxis;
			secondAxis = yAxis;
		} else {
			currAxis = yAxis;
			secondAxis = xAxis;
		}
		PointComparator comp = currAxis.getComparator();
		current = currAxis.getFirst();
		//Iterating thrugh the first elements and removing them
		while (current != null && comp.compareByInt(current.getData(), min) < 0) {
			toRemove = current;
			current = current.next;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
		}
		current = currAxis.getLast();
		//Iterating thrugh the last elements and removing them
		while (current != null && comp.compareByInt(current.getData(), max) > 0) {
			toRemove = current;
			current = current.prev;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
		}
	}

	@Override //O(1)
	//using only methods and operations which costs O(1). O(1) total
	public Boolean getLargestAxis() {
		int xMax = xAxis.getLast().getData().getX() - xAxis.getFirst().getData().getX();
		int yMax = yAxis.getLast().getData().getY() - yAxis.getFirst().getData().getY();
		return (xMax > yMax);
	}

	@Override//O(1)
	//median value is maintained by the Axis add and remove methods, so getting it is just getting the field value. O(1) total
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
		Point[] arr = getPointsInRangeOppAxis((int)(mid-(width/2)), (int)(mid+(width/2)), axis); //O(n)
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

