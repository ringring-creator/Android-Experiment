package com.ring.ring.todo.feature.list.viewmodel

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
internal interface TodoListViewModelModules {
    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    fun provideTodoListViewModel(factory: TodoListViewModel.Factory): AssistedViewModelFactory<*, *>
}