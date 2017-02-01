package edu.berkeley.cs186.database.table.stats;

import java.util.ArrayList;
import java.util.List;

public class StringHistogram implements Histogram<String> {
  private List<Bucket<String>> buckets;
  private static String alphaNumeric = " abcdefghijklmnopqrstuvwxyz0123456789";

  public StringHistogram() {
    this.buckets = new ArrayList<Bucket<String>>();

    for (int i = 0; i < alphaNumeric.length(); i++) {
      this.buckets.add(new Bucket<String>(alphaNumeric.substring(i, i + 1)));
    }
  }

  public List<Bucket<String>> getAllBuckets() {
    return this.buckets;
  }

  public void addValue(String value) {
    value = value.toLowerCase();

    int index = alphaNumeric.indexOf(value.charAt(0));

    buckets.get(index).increment();
  }

  public void removeValue(String value) {
    value = value.toLowerCase();
    int index = alphaNumeric.indexOf(value.charAt(0));

    buckets.get(index).decrement();
  }

  public int getEntriesInRange(String start, String end) {
    start = start.toLowerCase();
    end = end.toLowerCase();

    int startIndex = alphaNumeric.indexOf(start.charAt(0));
    int endIndex = alphaNumeric.indexOf(end.charAt(0));

    int result = 0;
    for (int i = startIndex; i < endIndex; i++) {
      result += this.buckets.get(i).getCount();
    }

    return result;
  }
}
