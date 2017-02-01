package edu.berkeley.cs186.database.databox;

/**
 * Abstract DataBox for all database primitives Currently supported: integers, booleans, floats,
 * fixed-length strings.
 *
 * DataBoxes are also comparable allowing comparisons or sorting.
 *
 * Provides default functionality for all DataBox subclasses by assuming that the contained value is
 * not of the type specified.
 */
public abstract class DataBox implements Comparable {

  /**
   * An enum with the current supported types.
   */
  public enum Types {BOOL, INT, FLOAT, STRING}

  public DataBox() throws DataBoxException {
  }

  public DataBox(boolean b) throws DataBoxException {
    throw new DataBoxException("not boolean type");
  }

  public DataBox(int i) throws DataBoxException {
    throw new DataBoxException("not int type");
  }

  public DataBox(float f) throws DataBoxException {
    throw new DataBoxException("not float type");
  }

  public DataBox(String s, int len) throws DataBoxException {
    throw new DataBoxException("not String type");
  }

  public DataBox(byte[] buf) throws DataBoxException {
    throw new DataBoxException("Not Implemented");
  }

  public boolean getBool() throws DataBoxException {
    throw new DataBoxException("not boolean type");
  }

  public int getInt() throws DataBoxException {
    throw new DataBoxException("not int type");
  }

  public float getFloat() throws DataBoxException {
    throw new DataBoxException("not float type");
  }

  public String getString() throws DataBoxException {
    throw new DataBoxException("not String type");
  }

  public void setBool(boolean b) throws DataBoxException {
    throw new DataBoxException("not boolean type");
  }

  public void setInt(int i) throws DataBoxException {
    throw new DataBoxException("not int type");
  }

  public void setFloat(float f) throws DataBoxException {
    throw new DataBoxException("not float type");
  }

  public void setString(String s, int len) throws DataBoxException {
    throw new DataBoxException("not string type");
  }

  /**
   * Returns the type of the DataBox.
   *
   * @return the type from the Types enum
   * @throws DataBoxException
   */
  public Types type() throws DataBoxException {
    throw new DataBoxException("No type");
  }

  /**
   * Returns a byte array with the data contained by this DataBox.
   *
   * @return a byte array
   * @throws DataBoxException
   */
  public byte[] getBytes() throws DataBoxException {
    throw new DataBoxException("Not Implemented");
  }

  /**
   * Returns the fixed size of this DataBox.
   *
   * @return the size of the DataBox
   * @throws DataBoxException
   */
  public int getSize() throws DataBoxException {
    throw new DataBoxException("Not Implemented");
  }

  public int compareTo(Object obj) throws DataBoxException {
    throw new DataBoxException("Not Implemented");
  }

  @Override
  public String toString() throws DataBoxException {
    throw new DataBoxException("Not Implemented");
  }
}
