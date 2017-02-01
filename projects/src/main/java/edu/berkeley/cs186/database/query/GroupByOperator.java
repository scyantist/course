package edu.berkeley.cs186.database.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.berkeley.cs186.database.Database;
import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.table.MarkerRecord;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

public class GroupByOperator extends QueryOperator {
  private int groupByColumnIndex;
  private String groupByColumn;
  private Database.Transaction transaction;

  /**
   * Create a new GroupByOperator that pulls from source and groups by groupByColumn.
   *
   * @param source the source operator of this operator
   * @param transaction the transaction containing this operator
   * @param groupByColumn the column to group on
   * @throws QueryPlanException
   */
  public GroupByOperator(QueryOperator source,
                         Database.Transaction transaction,
                         String groupByColumn) throws QueryPlanException {
    super(OperatorType.GROUPBY, source);
    Schema sourceSchema = this.getSource().getOutputSchema();
    this.transaction = transaction;
    this.groupByColumn = this.checkSchemaForColumn(sourceSchema, groupByColumn);

    this.groupByColumnIndex = sourceSchema.getFieldNames().indexOf(this.groupByColumn);
  }

  /**
   * Read input tuples from source, group by groupByColumn, and return an iterator. Inserts
   * MarkerRecord between each group.
   *
   * @return
   * @throws QueryPlanException
   * @throws DatabaseException
   */
  public Iterator<Record> execute() throws QueryPlanException, DatabaseException {
    Map<String, String> hashGroupTempTables = new HashMap<String, String>();
    Iterator<Record> inputRecords = this.getSource().execute();

    while (inputRecords.hasNext()) {
      Record record = inputRecords.next();
      DataBox groupByColumn = record.getValues().get(this.groupByColumnIndex);

      String tableName;
      if (!hashGroupTempTables.containsKey(groupByColumn.toString())) {
        tableName = "Temp" + this.groupByColumn + "GroupBy" + hashGroupTempTables.size();

        this.transaction.createTempTable(this.getSource().getOutputSchema(), tableName);
        hashGroupTempTables.put(groupByColumn.toString(), tableName);
      } else {
        tableName = hashGroupTempTables.get(groupByColumn.toString());
      }

      this.transaction.addRecord(tableName, record.getValues());
    }

    MarkerRecord markerRecord = MarkerRecord.getMarker();
    List<Record> recordList = new ArrayList<Record>();

    int count = 0;
    for (String key : hashGroupTempTables.keySet()) {
      String tableName = hashGroupTempTables.get(key);
      Iterator<Record> recordIterator = this.transaction.getRecordIterator(tableName);

      while (recordIterator.hasNext()) {
        recordList.add(recordIterator.next());
      }

      if (++count < hashGroupTempTables.size()) {
        recordList.add(markerRecord);
      }
    }

    return recordList.iterator();
  }

  protected Schema computeSchema() throws QueryPlanException {
    return this.getSource().getOutputSchema();
  }
}
