package com.nordic.fairymine.dagger.module


import android.support.v4.app.Fragment
import com.nordic.fairymine.account.HoldingFragment
import com.nordic.fairymine.dagger.subcomponent.MyAccountFragmentSubcomponent
import com.nordic.fairymine.dagger.subcomponent.ProductsFragmentSubcomponent
import com.nordic.fairymine.product.OffersFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector

import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap


/**
 * Created by Sam22 on 2/4/18.
 */
@Module(subcomponents = [MyAccountFragmentSubcomponent::class, ProductsFragmentSubcomponent::class])
abstract class FragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(HoldingFragment::class)
    internal abstract fun bindMyAccountFragmentInjectorFactory(builder: MyAccountFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(OffersFragment::class)
    internal abstract fun bindProductsFragmentInjectorFactory(builder: ProductsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}