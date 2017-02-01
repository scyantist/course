package edu.berkeley.cs186.database.table;

import edu.berkeley.cs186.database.databox.DataBox;

import java.util.List;
import java.lang.StringBuilder;

/**
 * A wrapper class for an individual record. Simply stores a list of DataBoxes.
 */
public class Record {
  private List<DataBox> values;

  public Record(List<DataBox> values) {
    this.values = values;
  }

  public List<DataBox> getValues() {
    return this.values;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Record)) {
      return false;
    }

    Record otherRecord = (Record) other;

    if (values.size() != otherRecord.values.size()) {
      return false;
    }

    for (int i = 0; i < values.size(); i++) {
      if (!(values.get(i).equals(otherRecord.values.get(i)))) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (DataBox d : values) {
      s.append(d.toString().trim());
      s.append(", ");
    }
    return s.substring(0, s.length() -2);
  }
}
