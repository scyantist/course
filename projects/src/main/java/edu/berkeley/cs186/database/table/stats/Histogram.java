package edu.berkeley.cs186.database.table.stats;

import java.util.List;

/**
 * A parametrized type that stores histograms for a given value.
 *
 * @param <T> the type of the histogram
 */
public interface Histogram<T> {

  /**
   * Add a new value to the Histogram.
   *
   * @param value the value to add
   */
  void addValue(T value);

  /**
   * Removes a value from the Histogram
   *
   * @param value the value to remove
   */
  void removeValue(T value);

  /**
   * Get the number of values within a given range, including start and up to but not including end.
   *
   * @param start the inclusive start of the range
   * @param end the non-inclusive end of the range
   * @return the number of values in this range
   */
  int getEntriesInRange(T start, T end);

  /**
   * Return all of the buckets in the histogram
   *
   * @return the list of buckets
   */
  List<Bucket<T>> getAllBuckets();
}
