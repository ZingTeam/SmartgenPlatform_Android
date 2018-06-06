package com.example.txjju.smartgenplatform_android;

import android.support.test.filters.SmallTest;
import android.test.InstrumentationTestCase;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class ApplicationTest extends InstrumentationTestCase {

    @SmallTest
    public void test_case(){
        final int expected =5;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
