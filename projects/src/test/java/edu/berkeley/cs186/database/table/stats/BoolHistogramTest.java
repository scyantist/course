package edu.berkeley.cs186.database.table.stats;

import edu.berkeley.cs186.database.StudentTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;
import static org.junit.Assert.*;

public class BoolHistogramTest {

  @Test
  public void testBoolHistogram() {
    BoolHistogram histogram = new BoolHistogram();

    for (int i = 0; i < 100; i++) {
      histogram.addValue(true);
      histogram.addValue(false);
    }

    List<Bucket<Boolean>> buckets = histogram.getAllBuckets();
    assertEquals(2, buckets.size());

    assertEquals(100, buckets.get(0).getCount());
    assertEquals(100, buckets.get(1).getCount());
  }
}
