package edu.berkeley.cs186.database.databox;
import java.lang.Float;
import java.nio.ByteBuffer;

/**
 * Float data type which serializes to 14 bytes.
 */
public class FloatDataBox extends DataBox {
  private float f;

  /**
   * Construct an empty FloatDataBox with value 0.
   */
  public FloatDataBox() {
    this.f = 0.0f;
  }

  /**
   * Construct an empty FloatDataBox with value f.
   *
   * @param f the value of the FloatDataBox
   */
  public FloatDataBox(float f) {
    this.f = f;
  }

  /**
   * Construct a FloatDataBox from the bytes in buf
   *
   * @param buf the bytes to construct the FloatDataBox from
   */
  public FloatDataBox(byte[] buf) {
    if (buf.length != this.getSize()) {
      throw new DataBoxException("Wrong size buffer for float");
    }
    this.f = ByteBuffer.wrap(buf).getFloat();
  }

  @Override
  public float getFloat() {
    return this.f;
  }

  @Override
  public void setFloat(float f) {
    this.f = f;
  }

  @Override
  public Types type() {
    return DataBox.Types.FLOAT;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (this == null)
      return false;
    if (this.getClass() != obj.getClass())
      return false;
    FloatDataBox other = (FloatDataBox) obj;
    return this.getFloat() == other.getFloat();
  }

  public int compareTo(Object obj) {
    if (this.getClass() != obj.getClass()) {
      throw new DataBoxException("Invalid Comparsion");
    }
    FloatDataBox other = (FloatDataBox) obj;
    return Float.compare(this.getFloat(), other.getFloat());
  }

  @Override
  public byte[] getBytes() {
    return ByteBuffer.allocate(4).putFloat(this.f).array();
  }

  @Override
  public int getSize() {
    return 4;
  }

  @Override
  public String toString() {
    return "" + this.f;
  }
}

