package com.nordic.fairymine.common.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.nordic.fairymine.common.form.Form
import com.nordic.fairymine.common.form.InfoRow
import com.nordic.fairymine.common.form.InfoRowType
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Sam22 on 4/2/18.
 */
class CommonAdapterTest {

    @Mock
    lateinit var fragment: Fragment

    @InjectMocks
    private
    lateinit var pagerAdapter: AppPagerAdapter

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testAppPagerAdapter() {
        Assert.assertEquals(0, pagerAdapter.count)
        pagerAdapter.addFragment(fragment, "TestFragment")
        Assert.assertEquals(1, pagerAdapter.count)
        Assert.assertEquals(1, pagerAdapter.getAllFragments().size)
        Assert.assertSame(fragment, pagerAdapter.getFragment("TestFragment"))
        Assert.assertSame(fragment, pagerAdapter.getItem(0))
    }

    @Test
    fun testFormAdapter() {
        var formAdapter = FormInfoAdapter()
        Assert.assertEquals(0, formAdapter.itemCount)

        formAdapter = FormInfoAdapter(Form(arrayListOf(InfoRow("", "", InfoRowType.SingleSelect(mapOf())))))
        Assert.assertEquals(1, formAdapter.itemCount)
        Assert.assertSame(FormInfoAdapter.VIEW_TYPE_SINGLE_CHOICE, formAdapter.getItemViewType(0))
        Assert.assertFalse(formAdapter.getForm().isValid)

        formAdapter = FormInfoAdapter(Form(arrayListOf(InfoRow("", "", InfoRowType.TextInput()))))
        Assert.assertSame(FormInfoAdapter.VIEW_TYPE_TEXT_INPUT, formAdapter.getItemViewType(0))
        Assert.assertFalse(formAdapter.getForm().isValid)
    }
}