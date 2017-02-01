package edu.berkeley.cs186.database;

import edu.berkeley.cs186.database.databox.DataBox;
import edu.berkeley.cs186.database.query.QueryPlan;
import edu.berkeley.cs186.database.table.*;
import edu.berkeley.cs186.database.concurrency.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class Database {
  private Map<String, Table> tableLookup;
  private long numTransactions;
  private String fileDir;
  private LockManager lockMan;

  /**
   * Creates a new database.
   *
   * @param fileDir the directory to put the table files in
   * @throws DatabaseException
   */
  public Database(String fileDir) throws DatabaseException {
    this.fileDir = fileDir;
    numTransactions = 0;
    tableLookup = new ConcurrentHashMap<String, Table>();

    File dir = new File(fileDir);
    lockMan = new LockManager();

    if (!dir.exists()) {
      dir.mkdirs();
    }

    File[] files = dir.listFiles();

    for (File f : files) {
      String fName = f.getName();
      if (fName.endsWith(Table.FILENAME_EXTENSION)) {
        int lastIndex = fName.lastIndexOf(Table.FILENAME_EXTENSION);
        String tableName = fName.substring(0, lastIndex);
        tableLookup.put(tableName, new Table(tableName, this.fileDir));
      }
    }
  }

  /**
   * Create a new table in this database.
   *
   * @param s the table schema
   * @param tableName the name of the table
   * @throws DatabaseException
   */
  public synchronized void createTable(Schema s, String tableName) throws DatabaseException {
    if (this.tableLookup.containsKey(tableName)) {
      throw new DatabaseException("Table name already exists");
    }

    this.tableLookup.put(tableName, new Table(s, tableName, this.fileDir));
  }

  /**
   * Delete a table in this database.
   *
   * @param tableName the name of the table
   * @return true if the database was successfully deleted
   */
  public synchronized boolean deleteTable(String tableName) {
    if (!this.tableLookup.containsKey(tableName)) {
      return false;
    }

    this.tableLookup.get(tableName).close();
    this.tableLookup.remove(tableName);

    File f = new File(fileDir + tableName + Table.FILENAME_EXTENSION);
    f.delete();

    return true;
  }

  /**
   * Delete all tables from this database.
   */
  public synchronized void deleteAllTables() {
    List<String> tableNames = new ArrayList<String>(tableLookup.keySet());

    for (String s : tableNames) {
      deleteTable(s);
    }
  }

  /**
   * Close this database.
   */
  public synchronized void close() {
    for (Table t : this.tableLookup.values()) {
      t.close();
    }

    this.tableLookup.clear();
  }

  /**
   * Start a new transaction.
   *
   * @return the new Transaction
   */
  public synchronized Transaction beginTransaction() {
    Transaction t = new Transaction(this.numTransactions);

    this.numTransactions++;
    return t;
  }

  public class Transaction {
    long transNum;
    boolean active;
    HashMap<String, LockManager.LockType> locksHeld;
    HashMap<String, Table> tempTables;
    HashMap<String, String> aliasMaps;
    private Transaction(long tNum) {
      this.transNum = tNum;
      this.active = true;
      this.locksHeld = new HashMap<String, LockManager.LockType>();
      this.tempTables = new HashMap<String, Table>();
      this.aliasMaps = new HashMap<String, String>();
    }

    public boolean isActive() {
      return this.active;
    }

    public void end() {
      assert(this.active);

      releaseAllLocks();
      deleteAllTempTables();
      this.active = false;
    }

    /**
     * Allows the user to query a table. See query#QueryPlan
     *
     * @param tableName The name/alias of the table wished to be queried.
     * @throws DatabaseException if table does not exist
     */
    public QueryPlan query(String tableName) throws DatabaseException {
      assert(this.active);
      checkAndGrabSharedLock(tableName);
      return new QueryPlan(this, tableName);
    }

    /**
     * Allows the user to provide an alias for a particular table. That alias is valid for the
     * remainder of the transaction. For a particular QueryPlan, once you specify an alias, you
     * must use that alias for the rest of the query.
     *
     * @param tableName The original name of the table.
     * @param alias The new Aliased name.
     * @throws DatabaseException if the alias already exists or the table does not.
     */
    public void queryAs(String tableName, String alias) throws DatabaseException {
      assert(this.active);

      if (Database.this.tableLookup.containsKey(alias)
        || this.tempTables.containsKey(alias)
        || this.aliasMaps.containsKey(alias)) {
          throw new DatabaseException("Table name already exists");
      }
      checkAndGrabSharedLock(tableName);
      if (Database.this.tableLookup.containsKey(tableName)) {
        this.aliasMaps.put(alias, tableName);
      } else if (tempTables.containsKey(tableName)) {
        this.aliasMaps.put(alias, tableName);
      } else {
        throw new DatabaseException("Table name not found");
      }
    }

    /**
     * Create a temporary table within this transaction.
     *
     * @param schema the table schema
     * @param tempTableName the name of the table
     * @throws DatabaseException
     */
    public void createTempTable(Schema schema, String tempTableName) throws DatabaseException {
      assert(this.active);

      if (Database.this.tableLookup.containsKey(tempTableName)
        || this.tempTables.containsKey(tempTableName))  {
        throw new DatabaseException("Table name already exists");
      }
      File f = new File(Database.this.fileDir + "temp/");
      if (!f.exists()) {
        f.mkdirs();
      }

      this.tempTables.put(tempTableName, new Table(schema, tempTableName, Database.this.fileDir + "temp/"));
      this.locksHeld.put(tempTableName, LockManager.LockType.EXCLUSIVE);
    }

    public RecordID addRecord(String tableName, List<DataBox> values) throws DatabaseException {
      assert(this.active);

      checkAndGrabExclusiveLock(tableName);
      Table tab = getTable(tableName);
      RecordID rid = tab.addRecord(values);
      return rid;
    }

    public void deleteRecord(String tableName, RecordID rid) throws DatabaseException {
      assert(active);

      checkAndGrabExclusiveLock(tableName);
      Table tab = getTable(tableName);
      Record rec = tab.deleteRecord(rid);
    }

    public Record getRecord(String tableName, RecordID rid) throws DatabaseException {
      assert(active);

      checkAndGrabSharedLock(tableName);
      return getTable(tableName).getRecord(rid);
    }

    public Iterator<Record> getRecordIterator(String tableName) throws DatabaseException {
      assert(this.active);

      checkAndGrabSharedLock(tableName);
      return getTable(tableName).iterator();
    }

    public void updateRecord(String tableName, List<DataBox> values, RecordID rid) throws DatabaseException {
      assert(this.active);
      checkAndGrabExclusiveLock(tableName);
      Table tab = getTable(tableName);
      Record rec = tab.updateRecord(values, rid);
    }

    public Schema getSchema(String tableName) throws DatabaseException {
      assert(this.active);

      checkAndGrabSharedLock(tableName);
      return getTable(tableName).getSchema();
    }

    public Schema getFullyQualifiedSchema(String tableName) throws DatabaseException {
      assert(this.active);

      checkAndGrabSharedLock(tableName);

      Schema schema = getTable(tableName).getSchema();

      List<String> newColumnNames = new ArrayList<String>();

      for (String oldName : schema.getFieldNames()) {
        newColumnNames.add(tableName + "." + oldName);
      }

      return new Schema(newColumnNames, schema.getFieldTypes());
    }

    private Table getTable(String tableName) throws DatabaseException {
      if (this.tempTables.containsKey(tableName)) {
        return this.tempTables.get(tableName);
      }

      while (aliasMaps.containsKey(tableName)) {
        tableName = aliasMaps.get(tableName);
      }

      if (!Database.this.tableLookup.containsKey(tableName)) {
        throw new DatabaseException("Table: " + tableName + "does not exist");
      }
      checkAndGrabSharedLock(tableName);
      return Database.this.tableLookup.get(tableName);
    }

    private void checkAndGrabSharedLock(String tableName) throws DatabaseException{
      if (this.locksHeld.containsKey(tableName)) {
        return;
      }

      while (aliasMaps.containsKey(tableName)) {
        tableName = aliasMaps.get(tableName);
      }

      if (!this.tempTables.containsKey(tableName) && !Database.this.tableLookup.containsKey(tableName)) {
        throw new DatabaseException("Table: " + tableName + " Does not exist");
      }

      LockManager lockMan = Database.this.lockMan;
      if (lockMan.holdsLock(tableName, this.transNum, LockManager.LockType.SHARED)) {
        this.locksHeld.put(tableName, LockManager.LockType.SHARED);
      } else {
        lockMan.acquireLock(tableName, this.transNum, LockManager.LockType.SHARED);
      }
    }

    private void checkAndGrabExclusiveLock(String tableName) throws DatabaseException {
      while (aliasMaps.containsKey(tableName)) {
        tableName = aliasMaps.get(tableName);
      }

      if (this.locksHeld.containsKey(tableName) && this.locksHeld.get(tableName).equals(LockManager.LockType.EXCLUSIVE)) {
        return;
      }

      if (!this.tempTables.containsKey(tableName) && !Database.this.tableLookup.containsKey(tableName)) {
        throw new DatabaseException("Table: " + tableName + " Does not exist");
      }

      LockManager lockMan = Database.this.lockMan;

      if (lockMan.holdsLock(tableName, this.transNum, LockManager.LockType.EXCLUSIVE)) {
        this.locksHeld.put(tableName, LockManager.LockType.EXCLUSIVE);
      } else {
        lockMan.acquireLock(tableName, this.transNum, LockManager.LockType.EXCLUSIVE);
      }
    }

    private void releaseAllLocks() {
      LockManager lockMan = Database.this.lockMan;

      for (String tableName : this.locksHeld.keySet()) {
        lockMan.releaseLock(tableName, this.transNum);
      }
    }

    public void deleteTempTable(String tempTableName) {
      assert(this.active);

      if (!this.tempTables.containsKey(tempTableName)) {
        return;
      }

      this.tempTables.get(tempTableName).close();
      Database.this.tableLookup.remove(tempTableName);

      File f = new File(Database.this.fileDir + "temp/" + tempTableName + Table.FILENAME_EXTENSION);
      f.delete();
    }

    private void deleteAllTempTables() {
      Set<String> keys = tempTables.keySet();

      for (String tableName : keys) {
        deleteTempTable(tableName);
      }
    }
  }
}

