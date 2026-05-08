package dev.sethdegay.sequence.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.sethdegay.sequence.core.navigation.SequenceNavigator
import dev.sethdegay.sequence.feature.home.api.HomeNav
import javax.inject.Inject

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class SequenceNavigatorModule {
    @Binds
    @ActivityRetainedScoped
    abstract fun bindNavigator(impl: SequenceNavigatorImpl): SequenceNavigator
}

@ActivityRetainedScoped
class SequenceNavigatorImpl @Inject constructor() : SequenceNavigator {
    override val backStack: MutableList<NavKey> = mutableStateListOf(HomeNav)

    override fun navigate(key: NavKey) {
        backStack.add(key)
    }

    override fun navigateUp() {
        backStack.removeLastOrNull()
    }

    override fun replaceLast(key: NavKey) {
        if (backStack.isNotEmpty()) {
            backStack[backStack.lastIndex] = key
        } else {
            backStack.add(key)
        }
    }
}