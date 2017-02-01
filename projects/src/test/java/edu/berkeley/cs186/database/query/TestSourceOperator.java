package edu.berkeley.cs186.database.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.TestUtils;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

public class TestSourceOperator extends QueryOperator {
  private List<Record> recordList;
  private Schema setSchema;

  public TestSourceOperator() throws QueryPlanException {
    super(OperatorType.SEQSCAN, null);
    this.recordList = null;
    this.setSchema = null;
  }

  public TestSourceOperator(List<Record> recordIterator, Schema schema) throws QueryPlanException {
    super(OperatorType.SEQSCAN);
    this.recordList = recordIterator;

    this.setOutputSchema(schema);
    this.setSchema = schema;
  }


  public Iterator<Record> execute() {
    if (this.recordList == null) {
      ArrayList<Record> recordList = new ArrayList<Record>();
      for (int i = 0; i < 100; i++) {
        recordList.add(TestUtils.createRecordWithAllTypes());
      }

      return recordList.iterator();
    }

    return this.recordList.iterator();
  }

  protected Schema computeSchema() {
    if (this.setSchema == null) {
      return TestUtils.createSchemaWithAllTypes();
    }

    return this.setSchema;
  }
}
