package edu.berkeley.cs186.database.table.stats;

import edu.berkeley.cs186.database.StudentTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class StringHistogramTest {
  private static String alphaNumeric = " abcdefghijklmnopqrstuvwxyz0123456789";

  @Test
  public void testStringHistogram() {
    StringHistogram histogram = new StringHistogram();

    for (int i = 0; i < alphaNumeric.length(); i++) {
      String iString = alphaNumeric.substring(i, i+1);
      histogram.addValue(iString + iString + iString);
    }

    for (Bucket<String> bucket : histogram.getAllBuckets()) {
      assertEquals(1, bucket.getCount());
    }
  }
}
