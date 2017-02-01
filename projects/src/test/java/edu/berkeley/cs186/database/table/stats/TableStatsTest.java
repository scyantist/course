package edu.berkeley.cs186.database.table.stats;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.StudentTest;
import edu.berkeley.cs186.database.table.Schema;
import static org.junit.Assert.*;

public class TableStatsTest {

  @Test
  public void testTableStats() {
    Schema schema = TestUtils.createSchemaWithAllTypes();
    TableStats stats = new TableStats(schema);

    for (int i = 0; i < 100; i++) {
      stats.addRecord(TestUtils.createRecordWithAllTypes());
    }

    assertEquals(100, stats.getNumRecords());

    Histogram histOne = stats.getHistogram(0);
    assertTrue(histOne instanceof BoolHistogram);
    assertEquals(100, histOne.getEntriesInRange(true, null));

    Histogram histTwo = stats.getHistogram(1);
    assertTrue(histTwo instanceof IntHistogram);
    assertEquals(100, histTwo.getEntriesInRange(0, 5));

    Histogram histThree = stats.getHistogram(2);
    assertTrue(histThree instanceof StringHistogram);
    assertEquals(100, histThree.getEntriesInRange("a", "b"));

    Histogram histFour = stats.getHistogram(3);
    assertTrue(histFour instanceof FloatHistogram);
    assertEquals(100, histFour.getEntriesInRange(0f, 5f));
  }
}
