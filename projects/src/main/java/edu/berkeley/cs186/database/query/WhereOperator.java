package edu.berkeley.cs186.database.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.table.MarkerRecord;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

public class WhereOperator extends QueryOperator {
  private int columnIndex;
  private QueryPlan.PredicateOperator operator;
  private DataBox value;

  /**
   * Creates a new WhereOperator that pulls from source and only returns tuples for which the
   * predicate is satisfied.
   *
   * @param source the source of this operator
   * @param columnName the name of the column to evaluate the predicate on
   * @param operator the actual comparator
   * @param value the value to compare against
   * @throws QueryPlanException
   */
  public WhereOperator(QueryOperator source,
                       String columnName,
                       QueryPlan.PredicateOperator operator,
                       DataBox value) throws QueryPlanException {
    super(OperatorType.WHERE, source);
    this.operator = operator;
    this.value = value;

    columnName = this.checkSchemaForColumn(source.getOutputSchema(), columnName);
    this.columnIndex = this.getOutputSchema().getFieldNames().indexOf(columnName);
  }

  public Iterator<Record> execute() throws QueryPlanException, DatabaseException {
    Iterator<Record> sourceIterator = this.getSource().execute();
    List<Record> validRecords = new ArrayList<Record>();
    MarkerRecord markerRecord = MarkerRecord.getMarker();

    while (sourceIterator.hasNext()) {
      Record r = sourceIterator.next();

      if (r == markerRecord) {
        validRecords.add(r);
      } else {

        switch (this.operator) {
          case EQUALS:
            if (r.getValues().get(this.columnIndex).equals(value)) {
              validRecords.add(r);
            }
            break;

          case NOT_EQUALS:
            if (!r.getValues().get(this.columnIndex).equals(value)) {
              validRecords.add(r);
            }
            break;

          case LESS_THAN:
            if (r.getValues().get(this.columnIndex).compareTo(value) == -1) {
              validRecords.add(r);
            }
            break;

          case LESS_THAN_EQUALS:
            if (r.getValues().get(this.columnIndex).compareTo(value) == -1) {
              validRecords.add(r);
            } else if (r.getValues().get(this.columnIndex).compareTo(value) == 0) {
              validRecords.add(r);
            }
            break;
          case GREATER_THAN:
            if (r.getValues().get(this.columnIndex).compareTo(value) == 1) {
              validRecords.add(r);
            }
            break;

          case GREATER_THAN_EQUALS:
            if (r.getValues().get(this.columnIndex).compareTo(value) == 1) {
              validRecords.add(r);
            } else if (r.getValues().get(this.columnIndex).compareTo(value) == 0) {
              validRecords.add(r);
            }
            break;

          default:
            break;
        }
      }

    }

    return validRecords.iterator();
  }

  public Schema computeSchema() throws QueryPlanException {
    return this.getSource().getOutputSchema();
  }
}
