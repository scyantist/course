package edu.berkeley.cs186.database.databox;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestIntDataBox {
  @Test
  public void TestIntDataBoxConstructor() {
    DataBox first = new IntDataBox(99);
    assertEquals(first.getInt(), 99);

    DataBox sec = new IntDataBox(-99);
    assertEquals(sec.getInt(), -99);
  }

  @Test
  public void TestIntDataBoxSetters() {
    DataBox first = new IntDataBox();
    first.setInt(13);
    assertEquals(first.getInt(), 13);
    first.setInt(-13);
    assertEquals(first.getInt(), -13);
  }

  @Test
  public void TestIntDataBoxType() {
    DataBox first = new IntDataBox();
    assertEquals(first.type(), DataBox.Types.INT);
  }

  @Test
  public void TestIntDataBoxEquals() {
    DataBox first = new IntDataBox(11);
    DataBox second = new IntDataBox(11);
    assertEquals(first, second);
  }

  @Test
  public void TestIntDataBoxCompare() {
    DataBox first = new IntDataBox(11);
    DataBox second = new IntDataBox(12);
    assertTrue(first.compareTo(second) == -1);
    first.setInt(12);
    assertTrue(first.compareTo(second) == 0);
    first.setInt(13);
    assertTrue(first.compareTo(second) == 1);
  }

  @Test
  public void TestIntDataBoxSerialize() {
    DataBox first = new IntDataBox(11);
    byte[] b = first.getBytes();
    assertEquals(b.length, 4);
    assertEquals(b.length, first.getSize());
    DataBox sec = new IntDataBox(b);
    assertEquals(first, sec);
  }

  @Test
  public void TestIntDataBoxSerialize2() {
    DataBox first = new IntDataBox(-11);
    byte[] b = first.getBytes();
    assertEquals(b.length, 4);
    assertEquals(b.length, first.getSize());
    DataBox sec = new IntDataBox(b);
    assertEquals(first, sec);
  }

  @Test(expected = DataBoxException.class)
  public void TestIntDataBoxString() {
    DataBox first = new IntDataBox(11);
    first.getString();
  }

  @Test(expected = DataBoxException.class)
  public void TestIntDataBoxString2() {
    DataBox first = new IntDataBox(11);
    String s = "LOL";
    first.setString(s,s.length());
  }

  @Test(expected = DataBoxException.class)
  public void TestIntDataBoxFloat() {
    DataBox first = new IntDataBox(11);
    first.getFloat();
  }

  @Test(expected = DataBoxException.class)
  public void TestIntDataBoxFloat2() {
    DataBox first = new IntDataBox(11);
    first.setFloat(1.1f);
  }
}
