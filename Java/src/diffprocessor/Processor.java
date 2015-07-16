package diffprocessor;

/**
 * Created by VavilauA on 6/19/2015.
 */
public class Processor {

    public Processor(long limit) {
        // Do nothing
    }

    private void performRemovePass(SortedLimitedList<Double> mustBeEqualTo, SortedLimitedList<Double> expectedOutput) {
        SortedLimitedList.Entry<Double> toEqualEntry = mustBeEqualTo.getFirst();
        SortedLimitedList.Entry<Double> expectedEntry = expectedOutput.getFirst();

        // Just do removals to not hit the limit
        while (expectedEntry != null) {
            // The first list is exhausted, leave now
            if (toEqualEntry == null) {
                break;
            }
            if (toEqualEntry.getValue() < expectedEntry.getValue()) {
                // Lower then target - remove
                SortedLimitedList.Entry<Double> nextEntry = toEqualEntry.getNext();
                mustBeEqualTo.remove(toEqualEntry);
                toEqualEntry = nextEntry;
                continue;
            } else if (toEqualEntry.getValue() > expectedEntry.getValue()) {
                // Don't skip greater items, skip the expected list item instead
            } else {
                // Skip equal items
                toEqualEntry = toEqualEntry.getNext();
            }
            expectedEntry = expectedEntry.getNext();
        }

        // Remove remaining items
        while (toEqualEntry != null) {
            SortedLimitedList.Entry<Double> nextEntry = toEqualEntry.getNext();
            mustBeEqualTo.remove(toEqualEntry);
            toEqualEntry = nextEntry;
        }
    }

    private void performInsertPass(SortedLimitedList<Double> mustBeEqualTo, SortedLimitedList<Double> expectedOutput) {
        SortedLimitedList.Entry<Double> toEqualEntry = mustBeEqualTo.getFirst();
        SortedLimitedList.Entry<Double> expectedEntry = expectedOutput.getFirst();

        // Just insertions here, as we've already done removals
        while (expectedEntry != null) {
            // The first list is exhausted, leave now
            if (toEqualEntry == null) {
                break;
            }
            if (toEqualEntry.getValue() > expectedEntry.getValue()) {
                // Greater then target - add before
                mustBeEqualTo.addBefore(toEqualEntry, expectedEntry.getValue());
            } else {
                // Just skip
                toEqualEntry = toEqualEntry.getNext();
            }
            expectedEntry = expectedEntry.getNext();
        }

        // Append remaining items
        while (expectedEntry != null) {
            mustBeEqualTo.addLast(expectedEntry.getValue());
            expectedEntry = expectedEntry.getNext();
        }
    }

    public void doProcess(SortedLimitedList<Double> mustBeEqualTo, SortedLimitedList<Double> expectedOutput) {

        // Delete items not present in expectedOutput
        performRemovePass(mustBeEqualTo, expectedOutput);

        // Insert items not present in mustBeEqualTo
        performInsertPass(mustBeEqualTo, expectedOutput);
    }
}
