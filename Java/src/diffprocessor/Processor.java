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
            } else {
                // Just skip
                toEqualEntry = toEqualEntry.getNext();
            }
            expectedEntry = expectedEntry.getNext();
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

                // A little hack here - if we hit the limit, just replace the value
                // This should work because it happens only at the last item of the lists
                if (mustBeEqualTo.getCount() == mustBeEqualTo.getLimit()) {
                    SortedLimitedList.Entry<Double> prevEntry = toEqualEntry.getPrevious();
                    mustBeEqualTo.remove(toEqualEntry);
                    mustBeEqualTo.addAfter(prevEntry, expectedEntry.getValue());
                } else {
                    mustBeEqualTo.addBefore(toEqualEntry, expectedEntry.getValue());
                }
            } else {
                // Just skip
                toEqualEntry = toEqualEntry.getNext();
            }
            expectedEntry = expectedEntry.getNext();
        }
    }

    public void doProcess(SortedLimitedList<Double> mustBeEqualTo, SortedLimitedList<Double> expectedOutput) {

        // Delete items not present in expectedOutput
        performRemovePass(mustBeEqualTo, expectedOutput);

        // Insert items not present in mustBeEqualTo
        performInsertPass(mustBeEqualTo, expectedOutput);

        // Process the remaining items
        int itemCountDelta = mustBeEqualTo.getCount() - expectedOutput.getCount();

        // First list has more items than needed, remove remaining
        if (itemCountDelta > 0) {
            for (int i = 0; i < itemCountDelta; i++) {
                mustBeEqualTo.remove(mustBeEqualTo.getLast());
            }
        }

        // First list has less items than needed, append remaining
        if (itemCountDelta < 0) {
            SortedLimitedList.Entry<Double> expectedEntry = expectedOutput.getLast();
            SortedLimitedList.Entry<Double> insertAfterEntry = mustBeEqualTo.getLast();
            for (int i = 0; i < -itemCountDelta; i++) {
                mustBeEqualTo.addAfter(insertAfterEntry, expectedEntry.getValue());
                expectedEntry = expectedEntry.getPrevious();
            }
        }
    }
}
