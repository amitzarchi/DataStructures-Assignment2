import java.util.Arrays;

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
			index ++;
		}
		return arr;
	}

	@Override //O(n)
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		Axis rangeAxis;
		// using Axis's "getFitRange" method with the other Axis Comparator
		if (axis) rangeAxis = this.yAxis.getFitRange(min, max, this.xAxis.getComparator());
		else rangeAxis = this.xAxis.getFitRange(min, max, this.yAxis.getComparator());
		Container curr = rangeAxis.getFirst();
		Point[] arr = new Point[rangeAxis.getSize()];
		int index = 0;
		//adding the points to the output array
		while (index < arr.length && curr != null){
			arr[index] = curr.getData();
			curr = curr.next;
			index++;
		}
		return arr;
	}

	@Override //O(1)
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
		// Iterating through the elements from the first one and removing them
		while (current != null && comp.compareByInt(current.getData(), min) < 0) {
			toRemove = current;
			current = current.next;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
		}
		current = currAxis.getLast();
		//Iterating through the elements from the lsat one and removing them
		while (current != null && comp.compareByInt(current.getData(), max) > 0) {
			toRemove = current;
			current = current.prev;
			secondAxis.remove(toRemove.twin);
			currAxis.remove(toRemove);
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


	// return an array with all B Points
	private Point[] GetB(Container middle, int max, int min, Boolean axis) {
		int index = 0;
		PointComparator comp = getComparator(axis);
		Axis range = new Axis(comp);
		Container addLast = middle;
		Container addFirst = middle;
		Container addLastN = middle;
		Container addFirstN = middle;
		// find all the points on the left side and set the last one as "First" to the new range Axis
		while (addFirstN != null && comp.compareByInt(addFirstN.getData(), min) >= 0) {
			index ++;
			addFirst = addFirstN;
			addFirstN = addFirstN.prev;
		}
		range.setFirst(addFirst);
		// find all the points on the right side and set the last one as "Last" to the new range Axis
		if (addLastN.next != null) {
			addLastN = addLastN.next;
			while (addLastN != null && comp.compareByInt(addLastN.getData(), max) <= 0) {
				index ++;
				addLast = addLastN;
				addLastN = addLastN.next;
			}
		}
		range.setLast(addLast);
		range.setSize(index);
		// add all the Point from range Axis into array and return array
		Point[] arr = new Point[index];
		Container toAdd = range.getLast();
		while (index > 0) {
			arr[index-1] =  toAdd.getData();
			toAdd = toAdd.prev;
			index --;
		}
		return arr;
	}

	// the function gets median by axis and width, and return the closest pair in distance less than width/2, if there isnt return empty array
	// the function checks the size of B(how many points are in the strip) and choose algorithm between min(O(n) / O(B*log(B)) to sort B array point according to the second axis
	// after having all B points sorted according to the second axis, we can check only points that their second axis's values is less than width/2 according to the assumption
	// mathematical proof shows that in that way two "for loops" is only O(B) intend of O(B^2)
	@Override
	public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
		Point[] ans = new Point[0];
		double minDist = (width/2);
		// get comparator according to the second axis
		PointComparator comp = getComparator(!axis);
		int mid = getPointValue(container.getData(), axis);
		int sizeB;
		Point[] arrB;
		// find B, and B len O(B)
		arrB = GetB(container,(int)(mid-minDist), (int)(mid+minDist), axis);
		sizeB = arrB.length;
		// get arrB sorted according to the second axis
		// O(n) if n < B*log(B)
		if ((sizeB*Math.log(sizeB)) > this.xAxis.getSize()) {
			arrB = getPointsInRangeOppAxis((int)(mid-minDist), (int)(mid+minDist), axis);
		}
		// O(B*log(B)) if B*log(B) < n
		else {
			Arrays.sort(arrB, comp);
		}
		// check all distances between two possible points (max 7 in the inner for loop)
		// O(B)
		for (int i = 0; i < sizeB-1; i++) {	//O(B)
				for(int j = i+1; j < sizeB && (getPointValue(arrB[j],!axis) - getPointValue(arrB[i],!axis)) < minDist; j++) {	//O(7)
					if (getDistance(arrB[j], arrB[i]) < minDist) {
						minDist = getDistance(arrB[j], arrB[i]);
						ans = new Point[]{new Point(arrB[i]), new Point(arrB[j])};
					}
				}
		}
			return ans;
	}

	// recursive function that uses SplitByMedian & nearest PairInStrip to find the nearest pair in the DS.
	// splitting the DS into two new DS according to the biggest Axis, to find the nearest pair in each of them (recursive)
	// after that, find the nearest in Strip in the current DS according to the min between the results of the splits (to determine width of the strip)
	// eventually choose the closest pair from the split or from the strip.
	@Override
	public Point[] nearestPair() {
		Point[] ans;
		// handles the case there are only 2 points in the DS
		if (xAxis.getSize() == 2) {
			return  new Point[]{xAxis.getFirst().getData(), xAxis.getLast().getData()};
		// handles the case there less than 2 points in the DS
		} else if (xAxis.getSize() < 2) {
			return  new Point[0];
		} else {
			// find the biggest Axis and return two new DS split according to the median of the biggest Axis
			boolean biggerAxis = getLargestAxis();                  	      //O(1)
			DataStructure[] arrDS = SplitByMedian(biggerAxis);          	  //O(n)
			// recursively find the nearest pair in each new DS
			Point[] left = arrDS[0].nearestPair();                 		      //(T(n/2))
			Point[] right = arrDS[1].nearestPair();                    		  //(T(n/2))
			// calculate each result (for left/right) into distance
			double leftDistance;
			double rightDistance;
			leftDistance = getDistance(left[0], left[1]);                     //O(1)
			//there are at least two points in the DS, so in the left there will always be 2 points, so we check the case that right does not have 2 points
			if (right.length != 2) {
				rightDistance = leftDistance + 1;
			}
			else rightDistance = getDistance(right[0], right[1]);             //O(1)
			// choose the min between them
			double minDist;
			if (leftDistance < rightDistance) {
				ans = left;
				minDist = leftDistance;
			} else {
				ans = right;
				minDist = rightDistance;
			}
			// find the nearest in the strip of the current DS with the width according to the recursive results
			Point[] middle = nearestPairInStrip(this.getMedian(biggerAxis), 2 * minDist, biggerAxis);	//O(n)
			//choose the final closest to points (from the left / right / strip in the middle)
			if (middle.length == 2 && getDistance(middle[0], middle[1]) < minDist) {
				ans = middle;
			}
		}
		return ans;
	}

	// build new DS according to min & max values of Axis X or Y
	// use getRange and getFitRange to create new X Axis and Y Axis
	// example: if the Range is according to X values so getRange for X   &   getFitRange for Y
	// O(n)
	private DataStructure GetRangeDS(int min, int max, boolean X_Y) {
		Axis newAxisX;
		Axis newAxisY;
		// if Range is according to X values
		if (X_Y) {
			newAxisX = this.getAxisX().getRange(min, max);
			newAxisY = this.getAxisY().getFitRange(min, max, newAxisX.getComparator());
			// if Range is according to Y values
		} else {
			newAxisY = this.getAxisY().getRange(min, max);
			newAxisX = this.getAxisX().getFitRange(min, max, newAxisY.getComparator());
		}
		return new DataStructure(newAxisX, newAxisY);
	}


	// create and return two new DS (left & right) according to x/y median
	// median goes to the left, so when DS with 2 or more points call this method, it always  gives back at least one DS with 2 points, the other one might be empty.
	// uses the private function GetRangeDS to build and return the result
	// O(n)
	private DataStructure[] SplitByMedian(boolean X_Y) {
		DataStructure[] ans = new DataStructure[2];
		Axis currAxis;
		int min;
		int mid;
		int max;
		// take values according to X Axis
		if(X_Y) {
			currAxis = this.getAxisX();
			min = currAxis.getFirst().getData().getX();
			max = currAxis.getLast().getData().getX();
			mid = currAxis.getMedian().getData().getX();
		// take values according to Y Axis
		} else {
			currAxis = this.getAxisY();
			min = currAxis.getFirst().getData().getY();
			max = currAxis.getLast().getData().getY();
			mid = currAxis.getMedian().getData().getY();
		}
		// build and return left + right new DS
		ans [0] = GetRangeDS(min, mid, X_Y);           			//O(n)
		ans [1] = GetRangeDS(mid+1, max, X_Y);             //O(n)
		return ans;
	}

	// calculate distance between two points
	private double getDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2));
	}

	// get point (x/y) value
	private int getPointValue(Point point, boolean axis) {
		if (axis) return point.getX();
		else return point.getY();
	}

	// get Point Comparator (x/y)
	private PointComparator getComparator(boolean axis) {
		if (axis) return this.getAxisX().getComparator();
		else return this.getAxisY().getComparator();
	}

}

