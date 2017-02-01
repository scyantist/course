package edu.berkeley.cs186.database.table;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.StudentTest;
import edu.berkeley.cs186.database.databox.*;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.IOException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.DEFAULT)
public class TestTable {
  public static final String TABLENAME = "testtable";
  private Schema schema;
  private Table table;

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  @Before
  public void beforeEach() throws Exception {
    this.schema = TestUtils.createSchemaWithAllTypes();
    this.table = createTestTable(this.schema, TABLENAME);
  }

  @After
  public void afterEach() {
    table.close();
  }

  private Table createTestTable(Schema s, String tableName) throws DatabaseException {
    try {
      tempFolder.newFile(tableName);
      String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
      return new Table(s, tableName, tempFolderPath);
    } catch (IOException e) {
      throw new DatabaseException(e.getMessage());
    }
  }

  /**
   * Test sample, do not modify.
   */
  @Test
  @Category(StudentTest.class)
  public void testSample() {
    assertEquals(true, true); // Do not actually write a test like this!
  }

  @Test
  public void testTableNumEntries() throws DatabaseException {
    assertEquals("NumEntries per page is incorrect", 288, this.table.getNumEntriesPerPage());
  }

  @Test
  public void testTableNumEntriesBool() throws DatabaseException {

    Schema boolSchema = TestUtils.createSchemaOfBool();
    Table boolTable = createTestTable(boolSchema, "boolTable");
    int numEntries = boolTable.getNumEntriesPerPage();
    boolTable.close();
    assertEquals("NumEntries per page is incorrect", 3640, numEntries);
  }

  @Test
  public void testTableNumEntriesString() throws DatabaseException {

    Schema stringSchema = TestUtils.createSchemaOfString(100);
    Table stringTable = createTestTable(stringSchema, "stringTable");
    int numEntries = stringTable.getNumEntriesPerPage();
    stringTable.close();
    assertEquals("NumEntries per page is incorrect", 40, numEntries);
  }

  @Test
  public void testTableSimpleInsert() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();

    RecordID rid = table.addRecord(input.getValues());

    // This is a new table, so it should be put into the first slot of the first page.
    assertEquals(1, rid.getPageNum());
    assertEquals(0, rid.getEntryNumber());

    Record output = table.getRecord(rid);
    assertEquals(input, output);
  }

  @Test
  public void testTableMultiplePages() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();

    int numEntriesPerPage = table.getNumEntriesPerPage();

    // create one page's worth of entries
    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());

      // ensure that records are created in sequential slot order on the sam page
      assertEquals(1, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    // add one more to make sure the next page is created
    RecordID rid = table.addRecord(input.getValues());
    assertEquals(2, rid.getPageNum());
    assertEquals(0, rid.getEntryNumber());
  }

  @Test(expected = DatabaseException.class)
  public void testInvalidRetrieve() throws DatabaseException {
    table.getRecord(new RecordID(1, 1));
  }

  @Test
  public void testSchemaSerialization() throws DatabaseException {
    // open another reference to the same file
    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    Table table = new Table(TABLENAME, tempFolderPath);

    assertEquals(table.getSchema(), this.table.getSchema());
  }

  @Test
  public void testTableIterator() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();

    for (int i = 0; i < 1000; i++) {
      RecordID rid = table.addRecord(input.getValues());
    }
    Iterator<Record> iRec = table.iterator();
    for (int i = 0; i < 1000; i++) {
      assertTrue(iRec.hasNext());
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());
  }

  @Test
  public void testTableIteratorGap() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();
    RecordID[] recordIds = new RecordID[1000];

    for (int i = 0; i < 1000; i++) {
      recordIds[i] = table.addRecord(input.getValues());
    }

    for (int i = 0; i < 1000; i += 2) {
      table.deleteRecord(recordIds[i]);
    }

    Iterator<Record> iRec = table.iterator();
    for (int i = 0; i < 1000; i += 2) {
      assertTrue(iRec.hasNext());
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());
  }

  @Test
  public void testTableIteratorGapFront() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();
    RecordID[] recordIds = new RecordID[1000];

    for (int i = 0; i < 1000; i++) {
      recordIds[i] = table.addRecord(input.getValues());
    }

    for (int i = 0; i < 500; i++) {
      table.deleteRecord(recordIds[i]);
    }

    Iterator<Record> iRec = table.iterator();
    for (int i = 500; i < 1000; i++) {
      assertTrue(iRec.hasNext());
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());
  }

  @Test
  public void testTableIteratorGapBack() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();
    RecordID[] recordIds = new RecordID[1000];
    for (int i = 0; i < 1000; i++) {
      recordIds[i] = table.addRecord(input.getValues());
    }

    for (int i = 500; i < 1000; i++) {
      table.deleteRecord(recordIds[i]);
    }

    Iterator<Record> iRec = table.iterator();
    for (int i = 0; i < 500; i++) {
      assertTrue(iRec.hasNext());
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());
  }

  @Test
  public void testTableDurable() throws Exception {
    Record input = TestUtils.createRecordWithAllTypes();

    int numEntriesPerPage = table.getNumEntriesPerPage();

    // create one page's worth of entries
    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());

      // ensure that records are created in sequential slot order on the sam page
      assertEquals(1, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    // add one more to make sure the next page is created
    RecordID rid = table.addRecord(input.getValues());
    assertEquals(2, rid.getPageNum());
    assertEquals(0, rid.getEntryNumber());
    // close table and reopen
    table.close();

    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    this.table = new Table(TABLENAME, tempFolderPath);

    for (int i = 0; i < numEntriesPerPage; i++) {
      Record rec = table.getRecord(new RecordID(1, i));
      assertEquals(input, rec);
    }
    Record rec = table.getRecord(new RecordID(2, 0));
    assertEquals(input, rec);
  }

  @Test
  public void testTableDurableAppends() throws Exception {
    Record input = TestUtils.createRecordWithAllTypes();

    int numEntriesPerPage = table.getNumEntriesPerPage();

    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());
      assertEquals(1, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());
      assertEquals(2, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());
      assertEquals(3, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    // close table and reopen
    table.close();
    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    this.table = new Table(TABLENAME, tempFolderPath);

    for (int i = 0; i < numEntriesPerPage; i++) {
      RecordID rid = table.addRecord(input.getValues());
      assertEquals(4, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

  }
  @Test
  public void testTableDurablePartialAppend() throws Exception {
    Record input = TestUtils.createRecordWithAllTypes();

    int numEntriesPerPage = table.getNumEntriesPerPage();

    // create one page's worth of entries
    for (int i = 0; i < numEntriesPerPage; i++) {
      input.getValues().get(1).setInt(i);
      RecordID rid = table.addRecord(input.getValues());

      // ensure that records are created in sequential slot order on the sam page
      assertEquals(1, rid.getPageNum());
      assertEquals(i, rid.getEntryNumber());
    }

    // add one more to make sure the next page is created
    input.getValues().get(1).setInt(0);
    RecordID rid = table.addRecord(input.getValues());
    assertEquals(2, rid.getPageNum());
    assertEquals(0, rid.getEntryNumber());
    // close table and reopen
    table.close();

    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    this.table = new Table(TABLENAME, tempFolderPath);

    for (int i = 0; i < numEntriesPerPage; i++) {
      Record rec = table.getRecord(new RecordID(1, i));
      input.getValues().get(1).setInt(i);
      assertEquals(input, rec);
    }
    Record rec = table.getRecord(new RecordID(2, 0));
    input.getValues().get(1).setInt(0);

    assertEquals(input, rec);

    rid = table.addRecord(input.getValues());
    assertEquals(2, rid.getPageNum());
    assertEquals(1, rid.getEntryNumber());
  }

  @Test
  public void testTableIteratorGapBackDurable() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();
    RecordID[] recordIds = new RecordID[1000];
    for (int i = 0; i < 1000; i++) {
      input.getValues().get(1).setInt(i);
      recordIds[i] = table.addRecord(input.getValues());
    }

    for (int i = 500; i < 1000; i++) {
      table.deleteRecord(recordIds[i]);
    }

    Iterator<Record> iRec = table.iterator();
    for (int i = 0; i < 500; i++) {
      assertTrue(iRec.hasNext());
      input.getValues().get(1).setInt(i);
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());

    table.close();
    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    this.table = new Table(TABLENAME, tempFolderPath);

    iRec = table.iterator();
    for (int i = 0; i < 500; i++) {
      assertTrue(iRec.hasNext());
      input.getValues().get(1).setInt(i);
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());

  }
  @Test
  public void testTableIteratorGapFrontDurable() throws DatabaseException {
    Record input = TestUtils.createRecordWithAllTypes();
    RecordID[] recordIds = new RecordID[1000];
    for (int i = 0; i < 1000; i++) {
      input.getValues().get(1).setInt(i);
      recordIds[i] = table.addRecord(input.getValues());
    }

    for (int i = 0; i < 500; i++) {
      table.deleteRecord(recordIds[i]);
    }

    Iterator<Record> iRec = table.iterator();
    for (int i = 500; i < 1000; i++) {
      assertTrue(iRec.hasNext());
      input.getValues().get(1).setInt(i);
      assertEquals(input, iRec.next());
    }
    assertFalse(iRec.hasNext());

    table.close();
    String tempFolderPath = tempFolder.getRoot().getAbsolutePath();
    this.table = new Table(TABLENAME, tempFolderPath);

    iRec = table.iterator();
    for (int i = 500; i < 1000; i++) {
      assertTrue(iRec.hasNext());
      Record r = iRec.next();
      input.getValues().get(1).setInt(i);
      assertEquals(input, r);
    }
    assertFalse(iRec.hasNext());
  }

}
