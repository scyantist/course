package edu.berkeley.cs186.database.databox;

import static org.junit.Assert.*;
import org.junit.Test;

/**
* @author  Sammy Sidhu
* @version 1.0
*/

public class TestBoolDataBox {
  @Test
  public void TestBoolDataBoxConstructor() {
    DataBox first = new BoolDataBox(true);
    assertEquals(first.getBool(), true);

    DataBox sec = new BoolDataBox(false);
    assertEquals(sec.getBool(), false);
  }

  @Test
  public void TestBoolDataBoxSetters() {
    DataBox first = new BoolDataBox();
    first.setBool(true);
    assertEquals(first.getBool(), true);
    first.setBool(false);
    assertEquals(first.getBool(), false);
  }

  @Test
  public void TestBoolDataBoxType() {
    DataBox first = new BoolDataBox(true);
    assertEquals(first.type(), DataBox.Types.BOOL);
  }

  @Test
  public void TestBoolDataBoxEquals() {
    DataBox first = new BoolDataBox(false);
    DataBox second = new BoolDataBox(false);
    assertEquals(first, second);
  }

  @Test
  public void TestBoolDataBoxCompare() {
    DataBox first = new BoolDataBox(false);
    DataBox second = new BoolDataBox(true);
    assertTrue(first.compareTo(second) == -1);
    first.setBool(true);
    assertTrue(first.compareTo(second) == 0);
  }

  @Test
  public void TestBoolDataBoxSerialize() {
    DataBox first = new BoolDataBox(true);
    byte[] b = first.getBytes();
    assertEquals(b.length, 1);
    assertEquals(b.length, first.getSize());
    DataBox sec = new BoolDataBox(b);
    assertEquals(first, sec);
  }

  @Test
  public void TestBoolDataBoxSerialize2() {
    DataBox first = new BoolDataBox(false);
    byte[] b = first.getBytes();
    assertEquals(b.length, 1);
    assertEquals(b.length, first.getSize());
    DataBox sec = new BoolDataBox(b);
    assertEquals(first, sec);
  }

  @Test(expected = DataBoxException.class)
  public void TestBoolDataBoxString() {
    DataBox first = new BoolDataBox(true);
    first.getString();
  }

  @Test(expected = DataBoxException.class)
  public void TestBoolDataBoxString2() {
    DataBox first = new BoolDataBox(true);
    String s = "LOL";
    first.setString(s,s.length());
  }

  @Test(expected = DataBoxException.class)
  public void TestBoolDataBoxFloat() {
    DataBox first = new BoolDataBox(false);
    first.getFloat();
  }

  @Test(expected = DataBoxException.class)
  public void TestBoolDataBoxFloat2() {
    DataBox first = new BoolDataBox(true);
    first.setFloat(1.1f);
  }
}
