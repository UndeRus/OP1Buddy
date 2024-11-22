package org.jugregator.op1buddy.features.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.project.Project

class ProjectsScreenViewModel(
    private val projectsRepository: ProjectsRepository,
) : ViewModel() {
    private val _mutableUiState = MutableStateFlow(ProjectsScreenUiState())
    val uiState: StateFlow<ProjectsScreenUiState> = _mutableUiState

    init {
        viewModelScope.launch {
            val projectsFlow = projectsRepository.readAllProjects()
            projectsFlow.collect { projects ->
                _mutableUiState.update { it.copy(projects = projects) }
            }
        }
    }

    fun removeProjectConfirmed(project: Project) {
        viewModelScope.launch {
            projectsRepository.removeProject(project.id)
        }
    }

    fun editProjectConfirmed(project: Project, title: String) {
        viewModelScope.launch {
            projectsRepository.updateProject(projectId = project.id, project = project.copy(title = title))
        }
    }
}

data class ProjectsScreenUiState(
    val projects: List<Project> = listOf()
)
