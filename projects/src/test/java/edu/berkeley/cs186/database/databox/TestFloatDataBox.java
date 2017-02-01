package edu.berkeley.cs186.database.databox;

import static org.junit.Assert.*;
import org.junit.Test;

/**
* @author  Sammy Sidhu
* @version 1.0
*/

public class TestFloatDataBox {
  @Test
  public void TestFloatDataBoxConstructor() {
    DataBox first = new FloatDataBox(9.9f);
    assertEquals(first.getFloat(), 9.9f, 1e-9f);

    DataBox sec = new FloatDataBox(-9.9f);
    assertEquals(sec.getFloat(), -9.9f, 1e-9f);
  }

  @Test
  public void TestFloatDataBoxSetters() {
    DataBox first = new FloatDataBox();
    first.setFloat(1.3f);
    assertEquals(first.getFloat(), 1.3f, 1e-9f);
    first.setFloat(-1.3f);
    assertEquals(first.getFloat(), -1.3f, 1e-9f);
  }

  @Test
  public void TestFloatDataBoxType() {
    DataBox first = new FloatDataBox();
    assertEquals(first.type(), DataBox.Types.FLOAT);
  }

  @Test
  public void TestFloatDataBoxEquals() {
    DataBox first = new FloatDataBox(1.1f);
    DataBox second = new FloatDataBox(1.1f);
    assertEquals(first, second);
  }

  @Test
  public void TestFloatDataBoxCompare() {
    DataBox first = new FloatDataBox(1.1f);
    DataBox second = new FloatDataBox(1.2f);
    assertTrue(first.compareTo(second) == -1);
    first.setFloat(1.2f);
    assertTrue(first.compareTo(second) == 0);
    first.setFloat(1.3f);
    assertTrue(first.compareTo(second) == 1);
  }

  @Test
  public void TestFloatDataBoxSerialize() {
    DataBox first = new FloatDataBox(11);
    byte[] b = first.getBytes();
    assertEquals(b.length, 4);
    assertEquals(b.length, first.getSize());
    DataBox sec = new FloatDataBox(b);
    assertEquals(first, sec);
  }

  @Test
  public void TestFloatDataBoxSerialize2() {
    DataBox first = new FloatDataBox(-11);
    byte[] b = first.getBytes();
    assertEquals(b.length, 4);
    assertEquals(b.length, first.getSize());
    DataBox sec = new FloatDataBox(b);
    assertEquals(first, sec);
  }

  @Test(expected = DataBoxException.class)
  public void TestFloatDataBoxString() {
    DataBox first = new FloatDataBox(1.1f);
    first.getString();
  }

  @Test(expected = DataBoxException.class)
  public void TestFloatDataBoxString2() {
    DataBox first = new FloatDataBox(1.1f);
    String s = "LOL";
    first.setString(s,s.length());
  }

  @Test(expected = DataBoxException.class)
  public void TestFloatDataBoxInt() {
    DataBox first = new FloatDataBox(1.1f);
    first.getInt();
  }

  @Test(expected = DataBoxException.class)
  public void TestFloatDataBoxInt2() {
    DataBox first = new FloatDataBox(1.1f);
    first.setInt(1);
  }
}
