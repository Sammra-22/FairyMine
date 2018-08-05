package com.nordic.fairymine.registration

import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.filters.SmallTest
import android.support.test.runner.AndroidJUnit4
import com.nordic.fairymine.RegisterActivity
import org.junit.runner.RunWith

/**
 * Created by Sam22 on 4/3/18.
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class TestRegisterActivity {


    val activityTestRule = IntentsTestRule<RegisterActivity>(RegisterActivity::class.java)
}