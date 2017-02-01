package edu.berkeley.cs186.database.databox;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestStringDataBox {

 	@Test
  public void TestStringDataBoxConstructor() {
    DataBox first = new StringDataBox();
    assertEquals(first.getString(), "");

    DataBox sec = new StringDataBox("hello", 5);
    assertEquals(sec.getString(), "hello");

    DataBox third = new StringDataBox("hello", 3);
    assertEquals(third.getString(), "hel");

    DataBox fourth = new StringDataBox("hello", 10);
    assertEquals(fourth.getString(), "hello     ");
  }

	@Test
  public void TestStringDataBoxSetters() {
    DataBox first = new StringDataBox();
    assertEquals("", first.getString());

		first.setString("test1234", 8);
    assertEquals("test1234", first.getString());

		first.setString("test1234", 6);
    assertEquals("test12", first.getString());

		first.setString("test1234", 10);
    assertEquals("test1234  ", first.getString());
	}

  @Test
  public void TestStringDataBoxType() {
    DataBox first = new StringDataBox("LOL", 3);
    assertEquals(DataBox.Types.STRING, first.type());
  }

  @Test
  public void TestStringDataBoxEquals() {
    DataBox first = new StringDataBox("1234", 4);
    DataBox second = new StringDataBox("1234",4);
    assertEquals(first, second);
  }

  @Test
  public void TestStringDataBoxCompare() {
    DataBox first = new StringDataBox("ABCC", 4);
    DataBox second = new StringDataBox("ABCD", 4);
    assertTrue(first.compareTo(second) == -1);
    first.setString("ABCD", 4);
    assertTrue(first.compareTo(second) == 0);
    first.setString("ABCE", 4);
    assertTrue(first.compareTo(second) == 1);
  }

  @Test
  public void TestStringDataBoxSerialize() {
		String testString = "Test Serialize";
    DataBox first = new StringDataBox(testString, testString.length());
    byte[] b = first.getBytes();
    assertEquals(b.length, testString.length());
    assertEquals(b.length, first.getSize());
    DataBox sec = new StringDataBox(b);
    assertEquals(first, sec);
    assertEquals(testString, sec.getString());
  }

  @Test
  public void TestStringDataBoxSerialize2() {
		String testString = "Test Serialize";
    DataBox first = new StringDataBox(testString, testString.length() + 10);
    byte[] b = first.getBytes();
    assertEquals(b.length, testString.length() + 10);
    assertEquals(b.length, first.getSize());
    DataBox sec = new StringDataBox(b);
    assertEquals(first, sec);
  }

  @Test(expected = DataBoxException.class)
  public void TestStringDataBoxString() {
    DataBox first = new StringDataBox("hello", 3);
    first.getInt();
  }

  @Test(expected = DataBoxException.class)
  public void TestStringDataBoxString2() {
    DataBox first = new StringDataBox("test ", 5);
    first.setFloat(89.9f);
  }
}
