package lab10;

/**
 * COMP 3021
 * <p>
 * This is a class that prints the maximum value of a given array of 90 elements
 * <p>
 * This is a single threaded version.
 * <p>
 * Create a multi-thread version with 3 threads:
 * <p>
 * one thread finds the max among the cells [0,29]
 * another thread the max among the cells [30,59]
 * another thread the max among the cells [60,89]
 * <p>
 * Compare the results of the three threads and print at console the max value.
 *
 * @author valerio
 */
public class FindMax {
    // this is an array of 90 elements
    // the max value of this array is 9999
    static int[] array = {1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
            234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
            3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
            3, 4543, 234, 3, 454, 1, 2, 3};

    public static void main(String[] args) {
        new FindMax().printMax();
    }

    public void printMax() {
        // this is a single threaded version
        int max = findMax2(0, array.length - 1);
        System.out.println("the max value is " + max);
    }

    /**
     * returns the max value in the array within a give range [begin,range]
     *
     * @param begin
     * @param end
     * @return
     */
    private int findMax(int begin, int end) {
        // you should NOT change this function
        int max = array[begin];
        for (int i = begin + 1; i <= end; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    private int findMax2(int begin, int end) {
        FindMax2 t1 = new FindMax2(0, 29),
                t2 = new FindMax2(30, 59),
                t3 = new FindMax2(60, 89);
        Thread first = new Thread(t1),
                second = new Thread(t2),
                third = new Thread(t3);
        first.start();
        second.start();
        third.start();
        try {
            first.join();
            second.join();
            third.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Math.max(t1.getMax(), Math.max(t2.getMax(), t3.getMax()));
    }

    public class FindMax2 implements Runnable {
        private int begin, end, max;

        public FindMax2(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public int getMax() {
            return max;
        }

        @Override
        public void run() {
            max = findMax(begin, end);
        }
    }
}
