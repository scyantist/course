package edu.berkeley.cs186.database.databox;
import java.lang.Integer;
import java.nio.ByteBuffer;

/**
 * Integer data type which serializes to 4 bytes
 */
public class IntDataBox extends DataBox {
  private int i;

  /**
   * Construct an empty IntDataBox with value 0.
   */
  public IntDataBox() {
    this.i = 0;
  }

  /**
   * Constructs an IntDataBox with value i.
   *
   * @param i the value of the IntDataBox
   */
  public IntDataBox(int i) {
    this.i = i;
  }

  /**
   * Construct an IntDataBox from the bytes in buf.
   *
   * @param buf the byte buffer source
   */
  public IntDataBox(byte[] buf) {
    if (buf.length != this.getSize()) {
      throw new DataBoxException("Wrong size buffer for int");
    }
    this.i = ByteBuffer.wrap(buf).getInt();
  }

  @Override
  public int getInt() {
    return this.i;
  }

  @Override
  public void setInt(int i) {
    this.i = i;
  }

  @Override
  public Types type() {
    return DataBox.Types.INT;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (this == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    IntDataBox other = (IntDataBox) obj;
    return this.getInt() == other.getInt();
  }

  public int compareTo(Object obj) {
    if (this.getClass() != obj.getClass()) {
      throw new DataBoxException("Invalid Comparsion");
    }
    IntDataBox other = (IntDataBox) obj;
    return Integer.compare(this.getInt(), other.getInt());
  }

  @Override
  public byte[] getBytes() {
    return ByteBuffer.allocate(4).putInt(this.i).array();
  }

  @Override
  public int getSize() {
    return 4;
  }

  @Override
  public String toString() {
    return "" + this.i;
  }
}
