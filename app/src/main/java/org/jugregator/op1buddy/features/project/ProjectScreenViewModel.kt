package org.jugregator.op1buddy.features.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.features.projects.ProjectRoute
import java.util.UUID

class ProjectScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ProjectRoute>()

    private val _mutableState =
        MutableStateFlow(ProjectUiState(projectId = route.projectId ?: UUID.randomUUID().toString()))
    val state: StateFlow<ProjectUiState> = _mutableState

    private var project: Project? = null

    fun loadProject() {
        viewModelScope.launch {
            val projectInfo = projectsRepository.readProject(state.value.projectId)
            if (projectInfo != null) {
                _mutableState.update { it.copy(title = projectInfo.title, path = projectInfo.backupDir) }
                project = projectInfo
            } else {
                // create project
                val newProject = Project(
                    id = state.value.projectId,
                    title = "",
                    backupDir = state.value.projectId
                )
                projectsRepository.createProject(
                    newProject
                )
                project = newProject
                _mutableState.update { it.copy(settingDialogOpened = true) }
            }
        }
    }

    fun saveProject() {
        viewModelScope.launch {
            val localProject = project
            if (localProject != null) {
                val newProject = localProject.copy(title = state.value.title)
                if (projectsRepository.updateProject(newProject.id, newProject)) {
                    project = newProject
                    _mutableState.update { it.copy(title = newProject.title, settingDialogOpened = false) }
                } else {
                    //TODO: failed to save, show error
                    Firebase.crashlytics.log("Failed to save project")
                }
            }
        }
    }

    fun onMoreClicked() {
        _mutableState.update {
            it.copy(settingDialogOpened = true)
        }
    }

    fun onSettingsDialogDismiss() {
        _mutableState.update {
            it.copy(settingDialogOpened = false)
        }
    }

    fun onSettingsDialogConfirm(projectTitle: String) {
        _mutableState.update {
            it.copy(settingDialogOpened = false, title = projectTitle)
        }
        saveProject()
    }
}

data class ProjectUiState(
    val projectId: String = "",
    val title: String = "",
    val settingDialogOpened: Boolean = false,
    val path: String = "",
)

data class TapeData(
    val id: Int,
    val name: String,
    val path: String,
)
