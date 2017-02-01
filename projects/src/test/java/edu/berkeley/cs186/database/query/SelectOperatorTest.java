package edu.berkeley.cs186.database.query;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.databox.FloatDataBox;
import edu.berkeley.cs186.database.databox.IntDataBox;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

import static org.junit.Assert.*;

public class SelectOperatorTest {

  @Test
  public void testOperatorSchema() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("int");

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, false, null, null);

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("int");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new IntDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());
  }

  @Test
  public void testSelectCorrectColumns() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("int");

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, false, null, null);

    Iterator<Record> outputRecords = selectOperator.execute();
    List<Record> recordList = new ArrayList<Record>();

    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    for (int i = 0; i < 100; i++) {
      assertEquals(1, recordList.get(i).getValues().get(0).getInt());
    }
  }

  @Test
  public void testSelectMultipleColumns() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("int");
    columnNames.add("string");

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, false, null, null);

    Iterator<Record> outputRecords = selectOperator.execute();
    List<Record> recordList = new ArrayList<Record>();

    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    for (int i = 0; i < 100; i++) {
      assertEquals(1, recordList.get(i).getValues().get(0).getInt());
      assertEquals("abcde", recordList.get(i).getValues().get(1).getString());
    }
  }

  @Test(expected = QueryPlanException.class)
  public void testSelectNonexistentColumn() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("nonexistentColumn");

    new SelectOperator(sourceOperator, columnNames, false, null, null);
  }

  @Test
  public void testSelectCount() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, true, null, null);

    Iterator<Record> outputRecords = selectOperator.execute();
    List<Record> recordList = new ArrayList<Record>();

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("sumAgg");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new IntDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());

    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    assertEquals(1, recordList.size());
    assertEquals(100, recordList.get(0).getValues().get(0).getInt());
  }

  @Test
  public void testSelectIntegerSum() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, false, null, "int");

    Iterator<Record> outputRecords = selectOperator.execute();
    List<Record> recordList = new ArrayList<Record>();

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("countAgg");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new IntDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());

    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    assertEquals(1, recordList.size());
    assertEquals(100, recordList.get(0).getValues().get(0).getInt());
  }

  @Test
  public void testSelectAverage() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    int sum = 0;

    for (int i = 0; i < 100; i++) {
      sum += i;
      values.add(i);
    }

    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);
    SelectOperator selectOperator =  new SelectOperator(sourceOperator, new ArrayList<String>(), false, "int", null);
    Iterator<Record> outputRecords = selectOperator.execute();

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("averageAgg");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new FloatDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());

    List<Record> recordList = new ArrayList<Record>();
    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    assertEquals(1, recordList.size());

    // the last argument specifies the precision to which the answer should be accurate
    assertEquals(((float) sum)/ 100, recordList.get(0).getValues().get(0).getFloat(), 0.01);
  }

  @Test
  public void testSelectFloatSum() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();

    SelectOperator selectOperator = new SelectOperator(sourceOperator, columnNames, false, null, "float");

    Iterator<Record> outputRecords = selectOperator.execute();
    List<Record> recordList = new ArrayList<Record>();

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("sumAgg");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new FloatDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());

    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    assertEquals(1, recordList.size());
    assertEquals(1.2 * 100f, recordList.get(0).getValues().get(0).getFloat(), 0.01);
  }

  @Test
  public void testSelectAllAggregates() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    int sum = 0;

    for (int i = 0; i < 100; i++) {
      sum += i;
      values.add(i);
    }

    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);
    SelectOperator selectOperator =  new SelectOperator(sourceOperator, new ArrayList<String>(), true, "int", "int");
    Iterator<Record> outputRecords = selectOperator.execute();

    List<String> outputSchemaNames = new ArrayList<String>();
    outputSchemaNames.add("countAgg");
    outputSchemaNames.add("sumAgg");
    outputSchemaNames.add("averageAgg");
    List<DataBox> outputSchemaTypes = new ArrayList<DataBox>();
    outputSchemaTypes.add(new IntDataBox());
    outputSchemaTypes.add(new IntDataBox());
    outputSchemaTypes.add(new FloatDataBox());
    Schema expectedSchema = new Schema(outputSchemaNames, outputSchemaTypes);

    assertEquals(expectedSchema, selectOperator.getOutputSchema());

    List<Record> recordList = new ArrayList<Record>();
    while (outputRecords.hasNext()) {
      recordList.add(outputRecords.next());
    }

    assertEquals(1, recordList.size());

    // the last argument specifies the precision to which the answer should be accurate
    assertEquals(100, recordList.get(0).getValues().get(0).getInt());
    assertEquals(sum, recordList.get(0).getValues().get(1).getInt());
    assertEquals(((float) sum)/ 100, recordList.get(0).getValues().get(2).getFloat(), 0.01);
  }

  @Test(expected = QueryPlanException.class)
  public void testSelectNonexistentColumnAggregate() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    List<String> columnNames = new ArrayList<String>();

    new SelectOperator(sourceOperator, columnNames, false, null, "nonexistentColumn");
  }
}
