package edu.berkeley.cs186.database.table.stats;

import java.util.ArrayList;
import java.util.List;

public class IntHistogram implements Histogram<Integer>{
  private static int NUM_BUCKETS = 10;

  private int max;
  private int min;
  private List<Bucket<Integer>> buckets;

  public IntHistogram() {
    this.buckets = new ArrayList<Bucket<Integer>>();
    this.min = Integer.MIN_VALUE;
    this.max = Integer.MAX_VALUE;
  }

  public List<Bucket<Integer>> getAllBuckets() {
    return this.buckets;
  }

  public void addValue(Integer value) {
    if (this.buckets.size() == 0) {
      this.min = value - NUM_BUCKETS / 2;
      this.max = value + NUM_BUCKETS / 2;
      int bucketRange = (this.max - this.min) / NUM_BUCKETS;

      for (int i = 0; i < NUM_BUCKETS; i++) {
        int start = this.min + (i * bucketRange);
        int end = start + bucketRange;
        this.buckets.add(new Bucket<Integer>(start, end));
      }
    }

    if (value > this.max || value < this.min) {
      this.refactorBuckets(value);
    }

    for (Bucket<Integer> bucket : this.buckets) {
      if (value >= bucket.getStart() && value < bucket.getEnd()) {
        bucket.increment();
      }
    }
  }

  public void removeValue(Integer value) {
    for (Bucket<Integer> bucket : this.buckets) {
      if (value >= bucket.getStart() && value < bucket.getEnd()) {
        bucket.decrement();
      }
    }
  }

  public int getEntriesInRange(Integer start, Integer end) {
    int entries = 0;

    for (Bucket<Integer> bucket : this.buckets) {
      int bucketStart = bucket.getStart();
      int bucketEnd = bucket.getEnd();
      int bucketRange = bucketEnd - bucketStart;

      if (bucketStart >= end) {
        break;
      }

      if (bucketStart >= start && bucketEnd < end) {
        entries += bucket.getCount();
      } else if (bucketStart >= start && bucketStart < end) {
        entries += bucket.getCount() * ((end - bucketStart) / bucketRange);
      } else if (bucketEnd < end && bucketEnd >= start) {
        entries += bucket.getCount() * ((bucketEnd - start) / bucketRange);
      }
    }

    return entries;
  }

  private void refactorBuckets(int value) {
    int newMin = value < this.min ? value : this.min;
    int newMax = value > this.max ? value : this.max;

    int newRange = newMax - newMin;
    int newBucketSize = newRange / NUM_BUCKETS;

    List<Bucket<Integer>> newBuckets = new ArrayList<Bucket<Integer>>();
    for (int i = 0; i < NUM_BUCKETS; i++) {
      int newStart = newMin + i * newBucketSize;
      int newEnd = newStart + newBucketSize;

      if (i + 1 == NUM_BUCKETS) {
        newEnd++;
      }

      Bucket<Integer> newBucket = new Bucket<Integer>(newStart, newEnd);

      for (int j = 0; j < NUM_BUCKETS; j++) {
        Bucket<Integer> oldBucket = this.buckets.get(j);
        int oldStart = oldBucket.getStart();
        int oldEnd = oldBucket.getEnd();

        if (newStart >= oldEnd) {
          break;
        }

        if (oldStart >= newStart && oldEnd < newEnd) {
          newBucket.increment(oldBucket.getCount());
        } else if (oldStart >= newStart && oldStart < newEnd) {
          int oldBucketRange = oldEnd - oldStart;

          int count = oldBucket.getCount() * ((newEnd - oldStart) / oldBucketRange);
          newBucket.increment(count);
        } else if (oldEnd < newEnd && oldEnd >= newStart){
          int oldBucketRange = oldEnd - oldStart;

          int count = oldBucket.getCount() * ((oldEnd - newStart) / oldBucketRange);
          newBucket.increment(count);
        }
      }

      newBuckets.add(newBucket);
    }

    this.min = newMin;
    this.max = newMax;
    this.buckets = newBuckets;
  }
}
