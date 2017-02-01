package edu.berkeley.cs186.database;

import edu.berkeley.cs186.database.databox.*;
import edu.berkeley.cs186.database.query.QueryPlanException;
import edu.berkeley.cs186.database.query.TestSourceOperator;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

 public static Schema createSchemaWithAllTypes() {
    List<DataBox> dataBoxes = new ArrayList<DataBox>();
    List<String> fieldNames = new ArrayList<String>();

    dataBoxes.add(new BoolDataBox());
    dataBoxes.add(new IntDataBox());
    dataBoxes.add(new StringDataBox(5));
    dataBoxes.add(new FloatDataBox());

    fieldNames.add("bool");
    fieldNames.add("int");
    fieldNames.add("string");
    fieldNames.add("float");

    return new Schema(fieldNames, dataBoxes);
  }

 public static Schema createSchemaWithTwoInts() {
    List<DataBox> dataBoxes = new ArrayList<DataBox>();
    List<String> fieldNames = new ArrayList<String>();

    dataBoxes.add(new IntDataBox());
    dataBoxes.add(new IntDataBox());

    fieldNames.add("int1");
    fieldNames.add("int2");

    return new Schema(fieldNames, dataBoxes);
  }

 public static Schema createSchemaOfBool() {
    List<DataBox> dataBoxes = new ArrayList<DataBox>();
    List<String> fieldNames = new ArrayList<String>();

    dataBoxes.add(new BoolDataBox());

    fieldNames.add("bool");

    return new Schema(fieldNames, dataBoxes);
  }

 public static Schema createSchemaOfString(int len) {
    List<DataBox> dataBoxes = new ArrayList<DataBox>();
    List<String> fieldNames = new ArrayList<String>();

    dataBoxes.add(new StringDataBox(len));
    fieldNames.add("string");

    return new Schema(fieldNames, dataBoxes);
  }


  public static Record createRecordWithAllTypes() {
    List<DataBox> dataValues = new ArrayList<DataBox>();
    dataValues.add(new BoolDataBox(true));
    dataValues.add(new IntDataBox(1));
    dataValues.add(new StringDataBox("abcde", 5));
    dataValues.add(new FloatDataBox((float) 1.2));

    return new Record(dataValues);
  }

  public static Record createRecordWithAllTypesWithValue(int val) {
    List<DataBox> dataValues = new ArrayList<DataBox>();
    dataValues.add(new BoolDataBox(true));
    dataValues.add(new IntDataBox(val));
    dataValues.add(new StringDataBox(String.format("%05d", val), 5));
    dataValues.add(new FloatDataBox((float) val));
    return new Record(dataValues);
  }


  public static TestSourceOperator createTestSourceOperatorWithInts(List<Integer> values)
      throws QueryPlanException {
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("int");
    List<DataBox> columnTypes = new ArrayList<DataBox>();
    columnTypes.add(new IntDataBox());
    Schema schema = new Schema(columnNames, columnTypes);

    List<Record> recordList = new ArrayList<Record>();

    for (int v : values) {
      List<DataBox> recordValues = new ArrayList<DataBox>();
      recordValues.add(new IntDataBox(v));
      recordList.add(new Record(recordValues));
    }


    return new TestSourceOperator(recordList, schema);
  }

  public static TestSourceOperator createTestSourceOperatorWithFloats(List<Float> values)
      throws QueryPlanException {
    List<String> columnNames = new ArrayList<String>();
    columnNames.add("float");
    List<DataBox> columnTypes = new ArrayList<DataBox>();
    columnTypes.add(new FloatDataBox());
    Schema schema = new Schema(columnNames, columnTypes);

    List<Record> recordList = new ArrayList<Record>();

    for (float v : values) {
      List<DataBox> recordValues = new ArrayList<DataBox>();
      recordValues.add(new FloatDataBox(v));
      recordList.add(new Record(recordValues));
    }


    return new TestSourceOperator(recordList, schema);
  }
}
