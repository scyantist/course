package edu.berkeley.cs186.database.table.stats;

import edu.berkeley.cs186.database.StudentTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class FloatHistogramTest {

  @Test
  public void testSimpleHistogram() {
    FloatHistogram histogram = new FloatHistogram();

    // this will create a histogram from 0 -> 10
    histogram.addValue(5f);

    for (float i = 0; i < 10; i++) {
      histogram.addValue(i);
    }

    for (float i = 0; i < 10; i++) {
      if (i != 5) {
        assertEquals(1, histogram.getEntriesInRange(i, i + 1));
      } else {
        assertEquals(2, histogram.getEntriesInRange(i, i + 1));
      }
    }
  }

  @Test
  public void testHistogramExpand() {
    FloatHistogram histogram = new FloatHistogram();

    // this will create a histogram from 0 -> 10
    histogram.addValue(5f);

    histogram.addValue(100f);

    float count = 0;
    for (Bucket<Float> bucket : histogram.getAllBuckets()) {
      assertEquals(count, bucket.getStart(), .0001);

      if (count < 90) {
        assertEquals(count + 10, bucket.getEnd(), .0001);
      } else {
        assertEquals(count + 11, bucket.getEnd(), .0001);
      }

      count += 10;
    }

    assertEquals(1, histogram.getAllBuckets().get(0).getCount());
    assertEquals(1, histogram.getAllBuckets().get(9).getCount());
  }
}
