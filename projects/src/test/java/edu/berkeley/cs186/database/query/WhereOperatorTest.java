package edu.berkeley.cs186.database.query;

import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.databox.IntDataBox;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.table.Record;

import static org.junit.Assert.*;

public class WhereOperatorTest {

  @Test
  public void testOperatorSchema() throws QueryPlanException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.EQUALS, new IntDataBox(1));

    assertEquals(TestUtils.createSchemaWithAllTypes(), whereOperator.getOutputSchema());
  }

  @Test
  public void testWhereFiltersCorrectRecords() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.EQUALS, new IntDataBox(1));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(100, outputList.size());

    Record inputRecord = TestUtils.createRecordWithAllTypes();
    for (Record record : outputList) {
      assertEquals(inputRecord, record);
    }
  }

  @Test
  public void testWhereRemovesIncorrectRecords() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.EQUALS, new IntDataBox(10));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(0, outputList.size());
  }

  @Test
  public void testWhereRemovesSomeRecords() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    List<DataBox> dataValues = new ArrayList<DataBox>();
    dataValues.add(new IntDataBox(1));
    Record keptRecord = new Record(dataValues);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.EQUALS, new IntDataBox(1));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(1, outputList.size());

    assertEquals(keptRecord, outputList.get(0));
  }

  @Test(expected = QueryPlanException.class)
  public void testWhereFailsOnInvalidField() throws QueryPlanException, DatabaseException {
    TestSourceOperator sourceOperator = new TestSourceOperator();
    new WhereOperator(sourceOperator, "nonexistentField",
        QueryPlan.PredicateOperator.EQUALS, new IntDataBox(10));
  }

  @Test
  public void testWhereNotEquals() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.NOT_EQUALS, new IntDataBox(1));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(2, outputList.size());

    Set<Integer> keptValues = new HashSet<Integer>();
    keptValues.add(2);
    keptValues.add(3);

    for (Record record : outputList) {
      int val = record.getValues().get(0).getInt();
      assert(keptValues).contains(val);
      keptValues.remove(val);
    }

    assertEquals(0, keptValues.size());
  }

  @Test
  public void testWhereLessThan() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.LESS_THAN, new IntDataBox(3));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(2, outputList.size());

    Set<Integer> keptValues = new HashSet<Integer>();
    keptValues.add(1);
    keptValues.add(2);

    for (Record record : outputList) {
      int val = record.getValues().get(0).getInt();
      assert(keptValues).contains(val);
      keptValues.remove(val);
    }

    assertEquals(0, keptValues.size());
  }

  @Test
  public void testWhereGreaterThan() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.GREATER_THAN, new IntDataBox(3));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(0, outputList.size());
  }

  @Test
  public void testWhereLessThanEquals() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.LESS_THAN_EQUALS, new IntDataBox(3));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(3, outputList.size());

    Set<Integer> keptValues = new HashSet<Integer>();
    keptValues.add(1);
    keptValues.add(2);
    keptValues.add(3);

    for (Record record : outputList) {
      int val = record.getValues().get(0).getInt();
      assert(keptValues).contains(val);
      keptValues.remove(val);
    }

    assertEquals(0, keptValues.size());
  }

  @Test
  public void testWhereGreaterThanEquals() throws QueryPlanException, DatabaseException {
    List<Integer> values = new ArrayList<Integer>();
    values.add(1);
    values.add(2);
    values.add(3);
    TestSourceOperator sourceOperator = TestUtils.createTestSourceOperatorWithInts(values);

    WhereOperator whereOperator = new WhereOperator(sourceOperator, "int",
        QueryPlan.PredicateOperator.GREATER_THAN_EQUALS, new IntDataBox(2));

    Iterator<Record> output = whereOperator.execute();
    List<Record> outputList = new ArrayList<Record>();

    while (output.hasNext()) {
      outputList.add(output.next());
    }

    assertEquals(2, outputList.size());

    Set<Integer> keptValues = new HashSet<Integer>();
    keptValues.add(2);
    keptValues.add(3);

    for (Record record : outputList) {
      int val = record.getValues().get(0).getInt();
      assert(keptValues).contains(val);
      keptValues.remove(val);
    }

    assertEquals(0, keptValues.size());
  }
}
