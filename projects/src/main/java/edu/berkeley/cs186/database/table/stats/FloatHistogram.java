package edu.berkeley.cs186.database.table.stats;

import java.util.ArrayList;
import java.util.List;

public class FloatHistogram implements Histogram<Float> {
  private static int NUM_BUCKETS = 10;

  private float max;
  private float min;
  private List<Bucket<Float>> buckets;

  public FloatHistogram() {
    this.buckets = new ArrayList<Bucket<Float>>();
    this.min = Float.MIN_VALUE;
    this.max = Float.MAX_VALUE;
  }

  public List<Bucket<Float>> getAllBuckets() {
    return this.buckets;
  }

  public void addValue(Float value) {
    if (this.buckets.size() == 0) {
      this.min = value - NUM_BUCKETS / 2;
      this.max = value + NUM_BUCKETS / 2;
      float bucketRange = (this.max - this.min) / NUM_BUCKETS;

      for (int i = 0; i < NUM_BUCKETS; i++) {
        float start = this.min + (i * bucketRange);
        float end = start + bucketRange;
        this.buckets.add(new Bucket<Float>(start, end));
      }
    }

    if (value > this.max || value < this.min) {
      this.refactorBuckets(value);
    }

    for (Bucket<Float> bucket : this.buckets) {
      if (value >= bucket.getStart() && value < bucket.getEnd()) {
        bucket.increment();
      }
    }
  }

  public void removeValue(Float value) {
    for (Bucket<Float> bucket : this.buckets) {
      if (value >= bucket.getStart() && value < bucket.getEnd()) {
        bucket.decrement();
      }
    }
  }

  public int getEntriesInRange(Float start, Float end) {
    int entries = 0;

    for (Bucket<Float> bucket : this.buckets) {
      float bucketStart = bucket.getStart();
      float bucketEnd = bucket.getEnd();
      float bucketRange = bucketEnd - bucketStart;

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

  private void refactorBuckets(float value) {
    float newMin = value < this.min ? value : this.min;
    float newMax = value > this.max ? value : this.max;

    float newRange = newMax - newMin;
    float newBucketSize = newRange / NUM_BUCKETS;

    List<Bucket<Float>> newBuckets = new ArrayList<Bucket<Float>>();
    for (int i = 0; i < NUM_BUCKETS; i++) {
      float newStart = newMin + i * newBucketSize;
      float newEnd = newStart + newBucketSize;

      if (i + 1 == NUM_BUCKETS) {
        newEnd++;
      }

      Bucket<Float> newBucket = new Bucket<Float>(newStart, newEnd);

      for (int j = 0; j < NUM_BUCKETS; j++) {
        Bucket<Float> oldBucket = this.buckets.get(j);
        float oldStart = oldBucket.getStart();
        float oldEnd = oldBucket.getEnd();

        if (newStart >= oldEnd) {
          break;
        }

        if (oldStart >= newStart && oldEnd < newEnd) {
          newBucket.increment(oldBucket.getCount());
        } else if (oldStart >= newStart && oldStart < newEnd) {
          float oldBucketRange = oldEnd - oldStart;

          int count = oldBucket.getCount() * (int) ((newEnd - oldStart) / oldBucketRange);
          newBucket.increment(count);
        } else if (oldEnd < newEnd && oldEnd >= newStart){
          float oldBucketRange = oldEnd - oldStart;

          int count = oldBucket.getCount() * (int) ((oldEnd - newStart) / oldBucketRange);
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
