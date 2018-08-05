package com.nordic.fairymine.dagger.subcomponent

import com.nordic.fairymine.product.OffersFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Sam22 on 2/4/18.
 */
@Subcomponent
interface ProductsFragmentSubcomponent: AndroidInjector<OffersFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<OffersFragment>()
}