package com.nordic.fairymine.dagger.subcomponent

import com.nordic.fairymine.account.HoldingFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by Sam22 on 2/4/18.
 */
@Subcomponent
interface MyAccountFragmentSubcomponent: AndroidInjector<HoldingFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HoldingFragment>()
}