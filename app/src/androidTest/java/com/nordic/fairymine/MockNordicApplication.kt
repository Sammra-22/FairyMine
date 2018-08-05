package com.nordic.fairymine

import com.nordic.fairymine.dagger.TestAppComponent

/**
 * Created by Sam22 on 4/3/18.
 */
class MockNordicApplication : NordicApplication() {

    override val appComponent by lazy { TestAppComponent.init(this) }

}