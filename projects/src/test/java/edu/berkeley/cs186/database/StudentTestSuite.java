package edu.berkeley.cs186.database;

import edu.berkeley.cs186.database.databox.*;
import edu.berkeley.cs186.database.io.*;
import edu.berkeley.cs186.database.query.*;
import edu.berkeley.cs186.database.table.stats.*;
import edu.berkeley.cs186.database.table.*;

import org.junit.runner.RunWith;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runners.Suite.SuiteClasses;

import edu.berkeley.cs186.database.table.TestSchema;

/**
 * A test suite for student tests.
 *
 * DO NOT CHANGE ANY OF THIS CODE.
 */
@RunWith(Categories.class)
@IncludeCategory(StudentTest.class)
@SuiteClasses({
    TestBoolDataBox.class,
    TestFloatDataBox.class,
        TestIntDataBox.class,
        TestStringDataBox.class,
        TestLRUCache.class,
        TestPage.class,
        TestPageAllocator.class,
        GroupByOperatorTest.class,
        JoinOperatorTest.class,
        QueryPlanTest.class,
        SelectOperatorTest.class,
        WhereOperatorTest.class,
        BoolHistogramTest.class,
        FloatHistogramTest.class,
        IntHistogramTest.class,
        StringHistogramTest.class,
        TableStatsTest.class,
        TestSchema.class,
        TestTable.class,
        TestDatabase.class,
        TestDatabaseQueries.class
})
public class StudentTestSuite {}
