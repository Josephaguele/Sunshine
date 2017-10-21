package com.example.android.sunshine.app.data;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.TestSuite;

public class FullTestSuite extends TestSuite {
    public static junit.framework.Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite() {
        super();
    }
}