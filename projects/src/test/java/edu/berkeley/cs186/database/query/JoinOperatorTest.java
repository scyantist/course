package edu.berkeley.cs186.database.query;

import edu.berkeley.cs186.database.databox.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;
import static org.junit.Assert.*;

public class JoinOperatorTest {

  @Test
  public void testOperatorSchema() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    JoinOperator joinOperator = new JoinOperator(sourceOperator, sourceOperator, "int", "int");

    List<String> expectedSchemaNames = new ArrayList<String>();
    expectedSchemaNames.add("bool");
    expectedSchemaNames.add("int");
    expectedSchemaNames.add("string");
    expectedSchemaNames.add("float");
    expectedSchemaNames.add("bool");
    expectedSchemaNames.add("int");
    expectedSchemaNames.add("string");
    expectedSchemaNames.add("float");

    List<DataBox> expectedSchemaTypes = new ArrayList<DataBox>();
    expectedSchemaTypes.add(new BoolDataBox());
    expectedSchemaTypes.add(new IntDataBox());
    expectedSchemaTypes.add(new StringDataBox(5));
    expectedSchemaTypes.add(new FloatDataBox());
    expectedSchemaTypes.add(new BoolDataBox());
    expectedSchemaTypes.add(new IntDataBox());
    expectedSchemaTypes.add(new StringDataBox(5));
    expectedSchemaTypes.add(new FloatDataBox());

    Schema expectedSchema = new Schema(expectedSchemaNames, expectedSchemaTypes);

    assertEquals(expectedSchema, joinOperator.getOutputSchema());
  }


  @Test
  public void testSimpleJoin() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    JoinOperator joinOperator = new JoinOperator(sourceOperator, sourceOperator, "int", "int");

    Iterator<Record> outputIterator = joinOperator.execute();
    int numRecords = 0;
    List<DataBox> expectedRecordValues = new ArrayList<DataBox>();
    expectedRecordValues.add(new BoolDataBox(true));
    expectedRecordValues.add(new IntDataBox(1));
    expectedRecordValues.add(new StringDataBox("abcde", 5));
    expectedRecordValues.add(new FloatDataBox(1.2f));
    expectedRecordValues.add(new BoolDataBox(true));
    expectedRecordValues.add(new IntDataBox(1));
    expectedRecordValues.add(new StringDataBox("abcde", 5));
    expectedRecordValues.add(new FloatDataBox(1.2f));
    Record expectedRecord = new Record(expectedRecordValues);

    while (outputIterator.hasNext()) {
      assertEquals(expectedRecord, outputIterator.next());
      numRecords++;
    }

    assertEquals(100*100, numRecords);
  }

  @Test
  public void testEmptyJoin() throws QueryPlanException, DatabaseException {
    TestSourceOperator leftSourceOperator = new TestSourceOperator();

    List<Integer> values = new ArrayList<Integer>();
    TestSourceOperator rightSourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    JoinOperator joinOperator = new JoinOperator(leftSourceOperator, rightSourceOperator, "int", "int");
    Iterator<Record> outputIterator = joinOperator.execute();

    assertFalse(outputIterator.hasNext());
  }

  @Test(expected = QueryPlanException.class)
  public void testJoinOnInvalidColumn() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();

    new JoinOperator(sourceOperator, sourceOperator, "notAColumn", "int");
  }

  @Test(expected = QueryPlanException.class)
  public void testJoinOnNonMatchingColumn() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();

    new JoinOperator(sourceOperator, sourceOperator, "string", "int");
  }
}
