import java.util.Arrays;
import java.util.Scanner;

public class Tests {
    private DataStructure dataStructure;
    private Scanner scanner;

    public static void main(String[] args) {
        Tests test = new Tests();
        test.runMenu();
    }

    public Tests() {
        dataStructure = new DataStructure();
        scanner = new Scanner(System.in);
        setPoints();
    }

    public void runMenu() {
        boolean quit = false;

        while (!quit) {
            System.out.println("Press ENTER to continue...");
            scanner.nextLine(); // consume any leftover newline characters from previous input
            
            // set the points to the initial set every time the menu is displayed
            setPoints();
            
            System.out.println("Select a method to test:");
            System.out.println("1. testGetDensity");
            System.out.println("2. testNarrowRange");
            System.out.println("3. testGetLargestAxis");
            System.out.println("4. testNearestPairAndNearestPairInStrip");
            System.out.println("0. Quit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character after user input

            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    testGetDensity();
                    break;
                case 2:
                    testNarrowRange();
                    break;
                case 3:
                    testGetLargestAxis();
                    break;
                case 4:
                    testNearestPairAndNearestPairInStrip();
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

        scanner.close();
    }
   
    private void setPoints() {
        dataStructure = new DataStructure();
        dataStructure.addPoint(new Point(1, 2));
        dataStructure.addPoint(new Point(3, 5));
        dataStructure.addPoint(new Point(2, 8));
        dataStructure.addPoint(new Point(6, 3));
        dataStructure.addPoint(new Point(10, 7));
        dataStructure.addPoint(new Point(12, 1));
        dataStructure.addPoint(new Point(15, 6));
        dataStructure.addPoint(new Point(18, 3));
        dataStructure.addPoint(new Point(20, 9));
        dataStructure.addPoint(new Point(22, 5));
        dataStructure.addPoint(new Point(25, 2));
        dataStructure.addPoint(new Point(28, 8));
        dataStructure.addPoint(new Point(30, 4));
        dataStructure.addPoint(new Point(32, 6));
        dataStructure.addPoint(new Point(35, 9));
    }

    public void testGetDensity() {
        // Calculate expected densities for the whole range and a narrow range
        double xMax = 35;
        double xMin = 1;
        double yMax = 9;
        double yMin = 1;
        double n = 15;
        double density1 = n / ((xMax - xMin) * (yMax - yMin));
        double narrowXMax = 30;
        double narrowXMin = 2;
        double narrowYMax = 9;
        double narrowYMin = 1;
        double narrowN = 12;
        double density2 = narrowN / ((narrowXMax - narrowXMin) * (narrowYMax - narrowYMin));

        // Calculate actual densities using the data structure
        double actualDensity1 = dataStructure.getDensity();
        dataStructure.narrowRange(2, 30, true);
        double actualDensity2 = dataStructure.getDensity();

        // Print expected and actual densities
        System.out.println("Expected density1: " + density1 + ", Actual density1: " + actualDensity1);
        System.out.println("Expected density2: " + density2 + ", Actual density2: " + actualDensity2);
    }
   
    public void testGetLargestAxis() {
        // check the largest axis for the existing points
        Boolean largestAxis1 = dataStructure.getLargestAxis();
        System.out.print("Expected largestAxis1: true, ");
        System.out.println("Actual largestAxis1: " + largestAxis1);

        // narrow the range to change the largest axis to the Y-axis
        dataStructure.narrowRange(1, 3, true);
        dataStructure.narrowRange(2, 5, false);
        Boolean largestAxis2 = dataStructure.getLargestAxis();
        System.out.print("Expected largestAxis2: false, ");
        System.out.println("Actual largestAxis2: " + largestAxis2);
    }
   	
    public void testNarrowRange() {
        // Narrow the range [3, 25) on the X-axis
        dataStructure.narrowRange(3, 25, true);

        // Check that only the expected points are returned
        Point[] pointsInRangeX = dataStructure.getPointsInRangeRegAxis(0,100,true);
        Point[] expectedPointsInRangeX = {
            new Point(3, 5),
            new Point(6, 3),
            new Point(10, 7),
            new Point(12, 1),
            new Point(15, 6),
            new Point(18, 3),
            new Point(20, 9),
            new Point(22, 5),
            new Point(25, 2),
        };
        System.out.println("Expected pointsInRangeX: " + Arrays.toString(expectedPointsInRangeX));
        System.out.println("Actual pointsInRangeX: " + Arrays.toString(pointsInRangeX));
        System.out.println("Points in range X match: " + Arrays.equals(pointsInRangeX, expectedPointsInRangeX));

        // Narrow the range [2, 7) on the Y-axis
        dataStructure.narrowRange(2, 7, false);

        // Check that only the expected points are returned
        Point[] pointsInRangeY = dataStructure.getPointsInRangeRegAxis(0, 100, false);
        Point[] expectedPointsInRangeY = {
            new Point(25, 2),
            new Point(18, 3),
            new Point(6, 3),
            new Point(22, 5),
            new Point(3, 5),
            new Point(15, 6),
            new Point(10, 7)
            
        };
        System.out.println("Expected pointsInRangeY: " + Arrays.toString(expectedPointsInRangeY));
        System.out.println("Actual pointsInRangeY: " + Arrays.toString(pointsInRangeY));
        System.out.println("Points in range Y match: " + Arrays.equals(pointsInRangeY, expectedPointsInRangeY));
    }

    public void testNearestPairAndNearestPairInStrip() {
        // Check nearest pair for the initial set of points
        Point[] expected = { new Point(30, 4), new Point(32, 6) };
        Point[] result = dataStructure.nearestPairInStrip(new Container(new Point(30, 4)), 6, true);
        System.out.println("Expected nearest pair for initial set of points: " + Arrays.toString(expected));
        System.out.println("Actual nearest pair for initial set of points  : " + Arrays.toString(result));
        
        // Narrow the range to 2 points and check nearest pair
        dataStructure.narrowRange(14, 18, true);
        expected = new Point[] { new Point(15, 6), new Point(18, 3) };
        result = dataStructure.nearestPair();
        System.out.println("Expected nearest pair for range [14, 18]: " + Arrays.toString(expected));
        System.out.println("Actual nearest pair for range [14, 18]  : " + Arrays.toString(result));
        
        // Narrow the range to 1 point and check nearest pair
        dataStructure.narrowRange(17, 17, true);
        expected = new Point[] { new Point(18, 3), new Point(20, 9) };
        result = dataStructure.nearestPair();
        System.out.println("Expected nearest pair for range [17, 17]: " + Arrays.toString(expected));
        System.out.println("Actual nearest pair for range [17, 17]  : " + Arrays.toString(result));
    }



	}