import diffprocessor.Processor;
import diffprocessor.SortedLimitedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by VavilauA on 6/19/2015.
 */
public class SimpleTest {
    static final int LIMIT = 10;
    static Processor diffProcessor = new Processor(LIMIT);
    static SortedLimitedList<Double> sortedList1 = new SortedLimitedList<Double>(LIMIT);
    static SortedLimitedList<Double> sortedList2 = new SortedLimitedList<Double>(LIMIT);
    static SortedLimitedList<Double> sortedList1_ = new SortedLimitedList<Double>(LIMIT);
    static SortedLimitedList<Double> sortedList2_ = new SortedLimitedList<Double>(LIMIT);
    static int test = 0;
    static void doTest(int operations) {
        try {
            diffProcessor.doProcess(sortedList1, sortedList2);
            if (!sortedList1.equals(sortedList2) || !sortedList1.equals(sortedList2_) || sortedList1.getPerformedOperations() != operations) {
                System.out.println("Test case: " + ++test + " Failed. Inputs: mustBeEqual: [" + sortedList1_ + "] expected: [" + sortedList2_ + "]");
                System.out.println("Your output " + sortedList1);
                System.out.println("Performed operations: " + sortedList1.getPerformedOperations() + ", expected " + operations);
                System.out.println();
            } else {
                System.out.println("Test case: " + ++test + " Succeed. Inputs: mustBeEqual: [" + sortedList1_ + "] expected: [" + sortedList2_ + "]");
                System.out.println();
            }
        } catch (Throwable e) {
            System.out.println("Test case: " + ++test + " Exception. Inputs: mustBeEqual: [" + sortedList1_ + "] expected: [" + sortedList2_ + "]");
            System.out.println();
            diffProcessor.doProcess(sortedList1_, sortedList2_);
        }
    }

    static void test(Double[] array1, Double[] array2, int operations) {
        sortedList1.fromArray(array1);
        sortedList2.fromArray(array2);
        sortedList1_.fromArray(array1);
        sortedList2_.fromArray(array2);
        doTest(operations);
    }

    static void test(List<Double> list1, List<Double> list2, int operations) {
        sortedList1.fromList(list1);
        sortedList2.fromList(list2);
        sortedList1_.fromList(list1);
        sortedList2_.fromList(list2);
        doTest(operations);
    }

    static void doManyRandomTests(int count, int seed) {

        Random random = new Random(seed);

        for (int testIdx = 0; testIdx < count; testIdx++) {
            int itemCount1 = random.nextInt(20);
            List<Double> list1 = new ArrayList<Double>();
            for (int i = 0; i < itemCount1; i++) {
                list1.add((double) random.nextInt(10));
            }
            list1.sort(Comparator.naturalOrder());

            int itemCount2 = random.nextInt(20);
            List<Double> list2 = new ArrayList<Double>();
            for (int i = 0; i < itemCount2; i++) {
                list2.add((double) random.nextInt(10));
            }
            list2.sort(Comparator.naturalOrder());

            int limit = Math.max(itemCount1, itemCount2);
            SortedLimitedList<Double> sList1 = new SortedLimitedList<Double>(limit);
            sList1.fromList(list1);
            SortedLimitedList<Double> sList2 = new SortedLimitedList<Double>(limit);
            sList2.fromList(list2);
            diffProcessor.doProcess(sList1, sList2);

            if (!sList1.equals(sList2)) {
                System.out.println("Test fail " + testIdx + ": original " + list1 + " expected [" + sList2 + "] found [" + sList1 + "]");
            }
        }
        System.out.println("Performed " + count + " random tests");
    }


    public static void main(String[] args) {
        Double[] needToBeEqual_0 = { 0.0, 1.0, 2.0, 3.0, 4.0, 6.0, 7.0 };
        Double[] expected_0 = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};
        test(needToBeEqual_0, expected_0, 1);

        Double[] needToBeEqual_1 = { 0.0, 1.0, 2.0, 3.0, 4.0, 6.0, 7.0};
        Double[] expected_1 = { 1.0, 2.0, 3.0, 3.0};
        test(needToBeEqual_1, expected_1, 5);

        Double[] needToBeEqual_2 = { 0.0 };
        Double[] expected_2 = { 7.0 };
        test(needToBeEqual_2, expected_2, 2);

        Double[] needToBeEqual_3 = { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
        Double[] expected_3 = { 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0 };
        test(needToBeEqual_3, expected_3, 17);

        Double[] needToBeEqual_4 = { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 3.0};
        Double[] expected_4 = { 0.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 2.0, 3.0};
        test(needToBeEqual_4, expected_4, 16);

        Double[] needToBeEqual_5 = { 0.0, 1.0, 2.0, 3.0, 4.0, 6.0, 7.0};
        Double[] expected_5 = { };
        test(needToBeEqual_5, expected_5, 7);

        Double[] needToBeEqual_6 = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
        Double[] expected_6 = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
        test(needToBeEqual_6, expected_6, 2);

        Double[] needToBeEqual_7 = { 0.0, 0.0};
        Double[] expected_7 = { 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
        test(needToBeEqual_7, expected_7, 10);

        Double[] needToBeEqual_8 = {0.0, 1.0, 1.0, 6.0, 6.0, 7.0};
        Double[] expected_8 = {2.0, 2.0, 3.0, 3.0, 3.0, 4.0, 5.0, 5.0, 7.0, 7.0};
        test(needToBeEqual_8, expected_8, 14);

        doManyRandomTests(1 * 1000 * 1000, 12942);
    }
}
