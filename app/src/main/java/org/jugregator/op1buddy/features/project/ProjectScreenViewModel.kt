package org.jugregator.op1buddy.features.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import org.jugregator.op1buddy.features.projects.ProjectRoute

class ProjectScreenViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    val route = savedStateHandle.toRoute<ProjectRoute>()

    init {
        println(route)
    }
}