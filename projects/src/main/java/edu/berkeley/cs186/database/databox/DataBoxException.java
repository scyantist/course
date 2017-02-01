package edu.berkeley.cs186.database.databox;

/**
* Exception that is thrown for DataBox errors such as type mismatches
*/
public class DataBoxException extends RuntimeException {

  public DataBoxException() {
    super();
  }

  public DataBoxException(String message) {
    super(message);
  }
}
