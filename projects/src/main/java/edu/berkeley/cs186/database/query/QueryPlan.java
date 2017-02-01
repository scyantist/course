package edu.berkeley.cs186.database.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs186.database.Database;
import edu.berkeley.cs186.database.DatabaseException;
import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.table.Record;

/**
 * QueryPlan provides a set of functions to generate simple queries. Calling the methods corresponding
 * to SQL syntax stores the information in the QueryPlan, and calling execute generates and executes
 * a QueryPlan DAG.
 */
public class QueryPlan {
  public enum PredicateOperator {
    EQUALS,
    NOT_EQUALS,
    LESS_THAN,
    LESS_THAN_EQUALS,
    GREATER_THAN,
    GREATER_THAN_EQUALS
  }

  private Database.Transaction transaction;
  private QueryOperator finalOperator;
  private String startTableName;

  private List<String> joinTableNames;
  private List<String> joinLeftColumnNames;
  private List<String> joinRightColumnNames;
  private List<String> whereColumnNames;
  private List<PredicateOperator> whereOperators;
  private List<DataBox> whereDataBoxes;
  private List<String> selectColumns;
  private String groupByColumn;
  private boolean hasCount;
  private String averageColumnName;
  private String sumColumnName;

  /**
   * Creates a new QueryPlan within transaction. The base table is startTableName.
   *
   * @param transaction the transaction containing this query
   * @param startTableName the source table for this query
   */
  public QueryPlan(Database.Transaction transaction, String startTableName) {
    this.transaction = transaction;
    this.startTableName = startTableName;

    this.selectColumns = new ArrayList<String>();
    this.joinTableNames = new ArrayList<String>();
    this.joinLeftColumnNames = new ArrayList<String>();
    this.joinRightColumnNames = new ArrayList<String>();

    this.whereColumnNames = new ArrayList<String>();
    this.whereOperators = new ArrayList<PredicateOperator>();
    this.whereDataBoxes = new ArrayList<DataBox>();

    this.hasCount = false;
    this.averageColumnName = null;
    this.sumColumnName = null;

    this.groupByColumn = null;

    this.finalOperator = null;
  }

  /**
   * Add a select operator to the QueryPlan with a list of column names. Can only specify one set
   * of selections.
   *
   * @param columnNames the columns to select
   * @throws QueryPlanException
   */
  public void select(List<String> columnNames) throws QueryPlanException {
    if (!this.selectColumns.isEmpty()) {
      throw new QueryPlanException("Cannot add more than one select operator to this query.");
    }

    if (columnNames.isEmpty()) {
      throw new QueryPlanException("Cannot select no columns.");
    }

    this.selectColumns = columnNames;
  }

  /**
   * Add a where operator. Only returns columns in which the column fulfills the predicate relative
   * to value.
   *
   * @param column the column to specify the predicate on
   * @param comparison the comparator
   * @param value the value to compare against
   * @throws QueryPlanException
   */
  public void where(String column, PredicateOperator comparison, DataBox value) throws QueryPlanException {
    this.whereColumnNames.add(column);
    this.whereOperators.add(comparison);
    this.whereDataBoxes.add(value);
  }

  /**
   * Set the group by column for this query.
   *
   * @param column the column to group by
   * @throws QueryPlanException
   */
  public void groupBy(String column) throws QueryPlanException {
    this.groupByColumn = column;
  }

  /**
   * Add a count aggregate to this query. Only can specify count(*).
   *
   * @throws QueryPlanException
   */
  public void count() throws QueryPlanException {
    this.hasCount = true;
  }

  /**
   * Add an average on column. Can only average over integer or float columns.
   *
   * @param column the column to average
   * @throws QueryPlanException
   */
  public void average(String column) throws QueryPlanException {
    this.averageColumnName = column;
  }

  /**
   * Add a sum on column. Can only sum integer or float columns
   *
   * @param column the column to sum
   * @throws QueryPlanException
   */
  public void sum(String column) throws QueryPlanException {
    this.sumColumnName = column;
  }

  /**
   * Join the leftColumnName column of the existing queryplan against the rightColumnName column
   * of tableName.
   *
   * @param tableName the table to join against
   * @param leftColumnName the join column in the existing QueryPlan
   * @param rightColumnName the join column in tableName
   */
  public void join(String tableName, String leftColumnName, String rightColumnName) {
    this.joinTableNames.add(tableName);
    this.joinLeftColumnNames.add(leftColumnName);
    this.joinRightColumnNames.add(rightColumnName);
  }

  /**
   * Generates a naïve QueryPlan in which all joins are at the bottom of the DAG followed by all where
   * predicates, an optional group by operator, and a set of selects (in that order).
   *
   * @return an iterator of records that is the result of this query
   * @throws DatabaseException
   * @throws QueryPlanException
   */
  public Iterator<Record> execute() throws DatabaseException, QueryPlanException {

    // start off with the start table scan as the source
    this.finalOperator = new SequentialScanOperator(this.transaction, this.startTableName);

    this.addJoins();
    this.addWheres();
    this.addGroupBy();
    this.addSelects();

    return this.finalOperator.execute();
  }

  private void addJoins() throws QueryPlanException, DatabaseException {
    int index = 0;

    for (String joinTable : this.joinTableNames) {
      SequentialScanOperator scanOperator = new SequentialScanOperator(this.transaction, joinTable);

      JoinOperator joinOperator = new JoinOperator(finalOperator, scanOperator,
          this.joinLeftColumnNames.get(index), this.joinRightColumnNames.get(index));

      this.finalOperator = joinOperator;
      index++;
    }
  }

  private void addWheres() throws QueryPlanException, DatabaseException {
    int index = 0;

    for (String whereColumn : this.whereColumnNames) {
      PredicateOperator operator = this.whereOperators.get(index);
      DataBox value = this.whereDataBoxes.get(index);

      WhereOperator whereOperator = new WhereOperator(this.finalOperator, whereColumn,
          operator, value);

      this.finalOperator = whereOperator;
      index++;
    }
  }

  private void addGroupBy() throws QueryPlanException, DatabaseException {
    if (this.groupByColumn != null) {
      if (this.selectColumns.size() > 2 || (this.selectColumns.size() == 1 &&
          !this.selectColumns.get(0).equals(this.groupByColumn))) {
        throw new QueryPlanException("Can only select columns specified in the GROUP BY clause.");
      }

      GroupByOperator groupByOperator = new GroupByOperator(this.finalOperator, this.transaction,
          this.groupByColumn);

      this.finalOperator = groupByOperator;
    }
  }

  private void addSelects() throws QueryPlanException, DatabaseException {
    if (!this.selectColumns.isEmpty() || this.hasCount || this.sumColumnName != null
        || this.averageColumnName != null) {
      SelectOperator selectOperator = new SelectOperator(this.finalOperator, this.selectColumns,
          this.hasCount, this.averageColumnName, this.sumColumnName);

      this.finalOperator = selectOperator;
    }
  }
}
