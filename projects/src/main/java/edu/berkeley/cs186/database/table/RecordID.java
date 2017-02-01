package edu.berkeley.cs186.database.table;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Represents the ID of a single record. Stores the id of a page and the slot number where this
 * record lives within that page.
 */
public class RecordID {
  private int pageNum;
  private short entryNumber;

  public RecordID(int pageNum, int entryNumber) {
    this.pageNum = pageNum;
    this.entryNumber = (short) entryNumber;
  }

  public RecordID(byte[] buff) {
    ByteBuffer bb = ByteBuffer.wrap(buff);
    this.pageNum = bb.getInt();
    this.entryNumber = bb.getShort();
  }

  public int getPageNum() {
    return this.pageNum;
  }

  public int getEntryNumber() {
    return (int) this.entryNumber;
  }

  @Override
  public String toString() {
    return "(PageNumber: " + this.pageNum + ", EntryNumber: " + this.entryNumber + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof RecordID)) {
      return false;
    }

    RecordID otherRecord = (RecordID) other;
    return otherRecord.getPageNum() == this.getPageNum() &&
          otherRecord.getEntryNumber() == this.getEntryNumber();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getPageNum(), getEntryNumber());
  }

  public byte[] getBytes() {
    return ByteBuffer.allocate(6).putInt(pageNum).putShort(entryNumber).array();
  }

  static public int getSize() {
    return 6;
  }

  public int compareTo(Object obj) {
    RecordID other = (RecordID) obj;
    int pageCompVal = Integer.compare(this.getPageNum(), other.getPageNum());

    if (pageCompVal == 0) {
      return Integer.compare(this.getEntryNumber(), other.getEntryNumber());
    }

    return pageCompVal;
  }

}
