package edu.berkeley.cs186.database.table.stats;

import java.util.ArrayList;
import java.util.List;

public class BoolHistogram implements Histogram<Boolean> {
  private List<Bucket<Boolean>> buckets;

  public BoolHistogram() {
    this.buckets = new ArrayList<Bucket<Boolean>>();
    this.buckets.add(new Bucket<Boolean>(true, null));
    this.buckets.add(new Bucket<Boolean>(false, null));
  }

  public List<Bucket<Boolean>> getAllBuckets() {
    return this.buckets;
  }

  public int getEntriesInRange(Boolean start, Boolean end) {
    if (start) {
      return this.buckets.get(0).getCount();
    }

    return this.buckets.get(1).getCount();
  }

  public void removeValue(Boolean value) {
    if (value) {
      this.buckets.get(0).decrement();
    } else {
      this.buckets.get(1).decrement();
    }
  }

  public void addValue(Boolean value) {
    if(value) {
      this.buckets.get(0).increment();
    } else {
      this.buckets.get(1).increment();
    }
  }
}
