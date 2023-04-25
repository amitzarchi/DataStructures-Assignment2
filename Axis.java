
public class Axis {

    private Container first;
    private Container last;
    private int size;
    private Container median;
    private PointComparator comparator;

    public Axis(PointComparator comparatorAxis) {
        this.first = null;
        this.last = null;
        this.size = 0;
        this.median = null;
        this.comparator = comparatorAxis;
    }

    Container getFirst() {
        return first;
    }
	Container getLast() {
        return last;
    }
	int getSize() {
        return size;
    }
	Container getMedian(Container first) {
        return median;
    }
    public void add(Container toAdd) {
        size++;       
        if (size == 0) {
            first = toAdd;
            last = toAdd;
            median = toAdd;
        }
        else {
            Container current = first;
            Container prev = null;
            while (current != null && comparator.compare(current.getData(), toAdd.getData()) < 0) {
                prev = current;
                current = current.getNext();
            }
            if (prev == null) {
                toAdd.next = first;
                first.prev = toAdd;
                first = toAdd;
            }
            else if (current == null) {
                last.next = toAdd;
                toAdd.prev = last;
                last = toAdd;
            }
            else {
                prev.next = toAdd;
                current.prev = toAdd;
                toAdd.next = current;
                toAdd.prev = prev;
            }
            if ((size % 2) == 0 && (comparator.compare(toAdd.getData(), median.getData()) > 0 )) {
                median = median.next;
            }
            else if ((size % 2 == 1) && (comparator.compare(toAdd.getData(), median.getData()) < 0 )) {
                median = median.prev;
            }

            }  
        }

    public void remove(Container toRemove) {
        if (toRemove == null) {
            return;
        }

        if (toRemove == first) {
            first = toRemove.next;
        } 
        else {
            toRemove.prev.next = toRemove.next;
        }

        if (toRemove == last) {
            last = toRemove.prev;
        } 
        else {
            toRemove.next.prev = toRemove.prev;
        }

        size--;

        if ((size % 2) == 0 && (comparator.compare(toRemove.getData(), median.getData()) <= 0 )) {
            median = median.next;
        }
        else if ((size % 2 == 1) && (comparator.compare(toRemove.getData(), median.getData()) >= 0 )) {
            median = median.prev;
        }
        
        toRemove.prev = null;
        toRemove.next = null;
    }

    public Axis getRange(int min, int max) {
        Axis output = new Axis(comparator);
        Container current =first;
        while (comparator.compareByInt(current.getData(), min) < 0 ) {
            current = current.next;
        }
        while (comparator.compareByInt(current.getData(), max) <= 0 ) {
            Container toAdd = new Container(current.getData());
            output.add(toAdd);
            current = current.next;
        }
        return output;
    }
}

