package edu.berkeley.cs186.database.table.stats;

import edu.berkeley.cs186.database.StudentTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class IntHistogramTest {

  @Test
  public void testSimpleHistogram() {
    IntHistogram histogram = new IntHistogram();

    // this will create a histogram from 0 -> 10
    histogram.addValue(5);

    for (int i = 0; i < 10; i++) {
      histogram.addValue(i);
    }

    for (int i = 0; i < 10; i++) {
      if (i != 5) {
        assertEquals(1, histogram.getEntriesInRange(i, i + 1));
      } else {
        assertEquals(2, histogram.getEntriesInRange(i, i + 1));
      }
    }
  }

  @Test
  public void testHistogramExpand() {
    IntHistogram histogram = new IntHistogram();

    // this will create a histogram from 0 -> 10
    histogram.addValue(5);

    histogram.addValue(100);

    int count = 0;
    for (Bucket<Integer> bucket : histogram.getAllBuckets()) {
      assertEquals(count, (int) bucket.getStart());

      if (count < 90) {
        assertEquals(count + 10, (int) bucket.getEnd());
      } else {
        assertEquals(count + 11, (int) bucket.getEnd());
      }

      count += 10;
    }

    assertEquals(1, histogram.getAllBuckets().get(0).getCount());
    assertEquals(1, histogram.getAllBuckets().get(9).getCount());
  }
}
