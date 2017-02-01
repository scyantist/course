package edu.berkeley.cs186.database.query;

import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

public abstract class QueryOperator {
  private QueryOperator source;
  private QueryOperator destination;
  private Schema operatorSchema;

  public enum OperatorType {
    JOIN,
    SELECT,
    WHERE,
    GROUPBY,
    SEQSCAN,
    INDEXSCAN
  }

  private OperatorType type;

  public QueryOperator(OperatorType type) {
    this.type = type;
    this.source = null;
    this.operatorSchema = null;
    this.destination = null;
  }

  protected QueryOperator(OperatorType type, QueryOperator source) throws QueryPlanException {
    this.source = source;
    this.type = type;
    this.operatorSchema = this.computeSchema();
    this.destination = null;
  }

  public OperatorType getType() {
    return this.type;
  }

  public boolean isJoin() {
    return this.type.equals(OperatorType.JOIN);
  }

  public boolean isWhere() {
    return this.type.equals(OperatorType.WHERE);
  }

  public boolean isSelect() {
    return this.type.equals(OperatorType.SELECT);
  }

  public boolean isGroupBy() {
    return this.type.equals(OperatorType.GROUPBY);
  }

  public boolean isSequentialScan() {
    return this.type.equals(OperatorType.SEQSCAN);
  }

  public boolean isIndexScan() {
    return this.type.equals(OperatorType.INDEXSCAN);
  }

  public QueryOperator getSource() throws QueryPlanException {
    return this.source;
  }

  public QueryOperator getDestination() throws QueryPlanException {
    return this.destination;
  }

  public void setSource(QueryOperator source) throws QueryPlanException {
    this.source = source;
    this.operatorSchema = this.computeSchema();
  }

  public void setDestination(QueryOperator destination) throws QueryPlanException {
    this.destination = destination;
  }

  public Schema getOutputSchema() {
    return this.operatorSchema;
  }

  protected void setOutputSchema(Schema schema) {
    this.operatorSchema = schema;
  }

  protected abstract Schema computeSchema() throws QueryPlanException;

  public abstract Iterator<Record> execute() throws QueryPlanException, DatabaseException;

  /**
   * Utility method that checks to see if a column is found in a schema using dot notation.
   *
   * @param fromSchema the schema to search in
   * @param specified the column name to search for
   * @return
   */
  public boolean checkColumnNameEquality(String fromSchema, String specified) {
    if (fromSchema.equals(specified)) {
      return true;
    }

    if (fromSchema.contains(".")) {
      String[] splits = fromSchema.split("\\.");
      String schemaColName = splits[1];

      return schemaColName.equals(specified);
    }

    return false;
  }

  /**
   * Utility method to determine whether or not a specified column name is valid with a given schema.
   *
   * @param schema
   * @param columnName
   * @return
   * @throws QueryPlanException
   */
  public String checkSchemaForColumn(Schema schema, String columnName) throws QueryPlanException {
    List<String> schemaColumnNames = schema.getFieldNames();
    boolean found = false;
    String foundName = null;

    for (String sourceColumnName : schemaColumnNames) {
      if (this.checkColumnNameEquality(sourceColumnName, columnName)) {
        if (found) {
          throw new QueryPlanException("Column " + columnName + " specified twice without disambiguation.");
        }

        found = true;
        foundName = sourceColumnName;
      }
    }

    if (!found) {
      throw new QueryPlanException("No column " + columnName + " found.");
    }

    return foundName;
  }
}
