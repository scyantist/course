package edu.berkeley.cs186.database.table.stats;

import java.util.ArrayList;
import java.util.List;

import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.table.Record;
import edu.berkeley.cs186.database.table.Schema;

/**
 * A wrapper class to represent the statistics for a single table.
 */
public class TableStats {
  private int numRecords;
  private List<Histogram> histograms;

  public TableStats(Schema tableSchema) {
    this.histograms = new ArrayList<Histogram>();

    for (DataBox dataBox : tableSchema.getFieldTypes()) {
      switch(dataBox.type()) {
        case INT:
          this.histograms.add(new IntHistogram());
          break;
        case FLOAT:
          this.histograms.add(new FloatHistogram());
          break;
        case BOOL:
          this.histograms.add(new BoolHistogram());
          break;
        case STRING:
          this.histograms.add(new StringHistogram());
          break;
        default:
          break;
      }
    }
  }

  /**
   * Adds the stats for a new record.
   *
   * @param record the new record
   */
  public void addRecord(Record record) {
    this.numRecords++;
    int count = 0;

    for (DataBox value : record.getValues()) {
      switch(value.type()) {
        case INT:
          this.histograms.get(count).addValue(value.getInt());
          break;
        case STRING:
          this.histograms.get(count).addValue(value.getString());
          break;
        case BOOL:
          this.histograms.get(count).addValue(value.getBool());
          break;
        case FLOAT:
          this.histograms.get(count).addValue(value.getFloat());
          break;
        default:
          break;
      }

      count++;
    }
  }

  /**
   * Remove the stats for an existing record.
   *
   * @param record the new record
   */
  public void removeRecord(Record record) {
    this.numRecords--;

    int count = 0;
    for (DataBox value : record.getValues()) {
      switch(value.type()) {
        case INT:
          this.histograms.get(count).removeValue(value.getInt());
          break;
        case STRING:
          this.histograms.get(count).removeValue(value.getString());
          break;
        case BOOL:
          this.histograms.get(count).removeValue(value.getBool());
          break;
        case FLOAT:
          this.histograms.get(count).removeValue(value.getFloat());
          break;
        default:
          break;
      }

      count++;
    }
  }

  public int getNumRecords() {
    return this.numRecords;
  }

  /**
   * Get the histogram for a particular column.
   *
   * @param index the index of the column
   * @return the histogram corresponding to index
   */
  public Histogram getHistogram(int index) {
    return this.histograms.get(index);
  }
}
