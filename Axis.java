public class Axis {

    private Container First;
    private Container Last;
    private int Size;
    private Container Median;
    private PointComparator Comparator;

    // Constructor initialize an empty linked list and set the input PointComparator
    public Axis(PointComparator comparatorAxis) {
        this.First = null;
        this.Last = null;
        this.Median = null;
        this.Size = 0;
        this.Comparator = comparatorAxis;
    }

    Container getFirst() {
        return First;
    }

    Container getLast() {
        return Last;
    }

    int getSize() {
        return Size;
    }

    Container getMedian() {
        return Median;
    }
    PointComparator getComparator() { return Comparator; }


    public void add(Container toAdd) {
        if (this.Size == 0) {
            this.First = toAdd;
            this.Last = toAdd;
            this.Median = toAdd;
            this.Size = this.Size+1;
        }
        else {
            Container current = this.First;
            Container prev = null;
            // Iteraiting the list to find the right sorted place
            while (current != null && Comparator.compare(current.getData(), toAdd.getData()) < 0) {
                prev = current;
                current = current.getNext();
            }
            // handle the case its the first Container
            if (prev == null) {
                toAdd.next = this.First;
                this.First.prev = toAdd;
                this.First = toAdd;
            }
            // handle the case its the last Container
            else if (current == null) {
                this.Last.next = toAdd;
                toAdd.prev = this.Last;
                this.Last = toAdd;
            }
            // handle the case its somewhere in the middle
            else {
                prev.next = toAdd;
                current.prev = toAdd;
                toAdd.next = current;
                toAdd.prev = prev;
            }
            this.Size = this.Size+1;
            PreserveMedianAdd(toAdd);
        }
        }
    private void PreserveMedianAdd(Container toAdd){
        if ((Size % 2) == 0 && (Comparator.compare(toAdd.getData(), Median.getData()) > 0 )) {
            Median = Median.next;
        }
        else if ((Size % 2 == 1) && (Comparator.compare(toAdd.getData(), Median.getData()) < 0 )) {
            Median = Median.prev;
        }
    }
    private void PreserveMedianDelete(Container toRemove) {
        if ((Size % 2) == 0 && (Comparator.compare(toRemove.getData(), Median.getData()) <= 0 )) {
            Median = Median.next;
        }
        else if ((Size % 2 == 1) && (Comparator.compare(toRemove.getData(), Median.getData()) >= 0 )) {
            Median = Median.prev;
        }
    }

    public void remove(Container toRemove) {
        if (toRemove == null) {
            return;
        }
        //handle the case its removing the first element
        if (toRemove == First) {
            First = toRemove.next;
        } 
        else {
            toRemove.prev.next = toRemove.next;
        }
        //handle the case its removing the last element
        if (toRemove == Last) {
            Last = toRemove.prev;
        } 
        else {
            toRemove.next.prev = toRemove.prev;
        }
        Size --;
        PreserveMedianDelete(toRemove);
        toRemove.prev = null;
        toRemove.next = null;
    }

    //O(n)
    public Axis getRange(int min, int max) {
        PointComparator comp = this.getComparator();
        Axis output = new Axis(this.Comparator);
        Container current =this.Last;
        Container toAdd = null;
        //iterating thrugh the whole list, and adding the relevant elements to "output"
        while (current != null) {
            if (comp.compareByInt(current.getData(), min) >= 0 & comp.compareByInt(current.getData(), max) <= 0) {
                toAdd = new Container(current.getData());
                output.add(toAdd);
                current = current.prev;
            }
        }
        return output;
    }

    public Axis getFitRange(int min, int max ,PointComparator comp) {
        Axis output = new Axis(this.getComparator());
        Container current =this.Last;
        Container toAdd = null;
        while (current != null) {
            if (comp.compareByInt(current.getData(), min) >= 0 & comp.compareByInt(current.getData(), max) <= 0) {
                toAdd = new Container(current.getData());
                output.add(toAdd);
                current = current.prev;
            }
        }
        return output;
    }


}

