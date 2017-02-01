package edu.berkeley.cs186.database.concurrency;

/**
 * The LockManager provides a basic locking implementation that ensures that only one transaction runs at a time.
 * We will cover locking later in the semester; for now, you don't need to change anything here.
 *
 */
public class LockManager {
  private boolean databaseLocked;
  private long databaseTransactionOwner;

  public enum LockType {SHARED, EXCLUSIVE};

  public LockManager() {
    this.databaseLocked = false;
    this.databaseTransactionOwner = -1;
  }
  /**
   * Acquires a lock on tableNum of type lockType for transaction transNum.
   *
   * @param tableName the database to lock on
   * @param transNum the transactions id
   * @param lockType the type of lock
   */
  public synchronized void acquireLock(String tableName, long transNum, LockType lockType) {
    while (this.databaseLocked) {
      if (this.databaseTransactionOwner == transNum) {
        break;
      }
      try {
        wait();
      } catch (InterruptedException e) {

      }
    }

    this.databaseTransactionOwner = transNum;
    this.databaseLocked = true;
  }

  /**
   * Releases transNum's lock on tableName.
   *
   * @param tableName the table that was locked
   * @param transNum the transaction that held the lock
   */
  public synchronized void releaseLock(String tableName, long transNum) {
    if (this.databaseLocked && this.databaseTransactionOwner == transNum) {
      this.databaseLocked = false;
      this.databaseTransactionOwner = -1;

      notifyAll();
    }
  }

  /**
   * Returns a boolean indicating whether or not transNum holds a lock of type lockType on tableName.
   *
   * @param tableName the table that we're checking
   * @param transNum the transaction that we're checking for
   * @param lockType the lock type
   * @return whether the lock is held or not
   */
  public synchronized boolean holdsLock(String tableName, long transNum, LockType lockType) {
    return this.databaseLocked && this.databaseTransactionOwner == transNum;
  }
}
