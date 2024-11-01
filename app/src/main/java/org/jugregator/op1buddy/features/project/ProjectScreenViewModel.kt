package org.jugregator.op1buddy.features.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.Project
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.features.project.ui.screens.ProjectTab
import org.jugregator.op1buddy.features.projects.ProjectRoute
import org.jugregator.op1buddy.features.sync.LocalFileRepository
import java.util.UUID

class ProjectScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val localFileRepository: LocalFileRepository,
) : ViewModel() {
    val route = savedStateHandle.toRoute<ProjectRoute>()

    private val _mutableState =
        MutableStateFlow(ProjectUiState(projectId = route.projectId ?: UUID.randomUUID().toString()))
    val state: StateFlow<ProjectUiState> = _mutableState

    private var project: Project? = null

    init {
        viewModelScope.launch {
            val projectInfo = projectsRepository.readProject(state.value.projectId)
            if (projectInfo != null) {
                _mutableState.update { it.copy(title = projectInfo.title, path = projectInfo.backupDir) }
                project = projectInfo
                // load dir state
                val backupInfo = localFileRepository.readBackupInfo(projectInfo.backupDir)
                _mutableState.update {
                    it.copy(
                        synths = if (backupInfo.synthsEnabled) {
                            (0..7).map {
                                SynthData(
                                    id = it,
                                    name = (it + 1).toString(),
                                    path = "${projectInfo.backupDir}/synth_${it + 1}.aif"
                                )
                            }
                        } else {
                            listOf()
                        },
                        drumkits = if (backupInfo.drumkitsEnabled) {
                            (0..7).map {
                                DrumData(
                                    id = it,
                                    name = (it + 1).toString(),
                                    path = "${projectInfo.backupDir}/drum_${it + 1}.aif"
                                )
                            }
                        } else {
                            listOf()
                        },
                        tapes = backupInfo.tapes.filter { it.first.enabled }.map {
                            TapeData(
                                id = it.first.index,
                                name = "Tape ${it.first.index + 1}",
                                path = "${projectInfo.backupDir}/tape_${it.first.index + 1}.aif"
                            )
                        }
                    )
                }
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
            }
            selectTab(ProjectTab.Synth)
        }
    }

    fun updateTitle(title: String) {
        _mutableState.update { it.copy(title = title) }
    }

    fun saveProject() {
        viewModelScope.launch {
            val localProject = project
            if (localProject != null) {
                val newProject = localProject.copy(title = state.value.title)
                if (projectsRepository.updateProject(newProject.id, newProject)) {
                    project = newProject
                    _mutableState.update { it.copy(title = newProject.title, titleEditEnabled = false) }
                } else {
                    //TODO: failed to save, show error
                }
            }
        }
    }

    fun onEditTitleClicked() {
        _mutableState.update { it.copy(titleEditEnabled = true) }
    }

    fun selectTab(resource: ProjectTab) {
        val items = when (resource) {
            ProjectTab.Synth -> state.value.synths.map { ProjectResource.Synth(it.id, it.path) }
            ProjectTab.Drumkit -> state.value.drumkits.map { ProjectResource.Drumkit(it.id, it.path) }
            ProjectTab.Tape -> state.value.tapes.map { ProjectResource.Tape(it.id, it.path) }
            else -> return
        }
        _mutableState.update {
            it.copy(
                tabOpened = resource,
                items = items,
            )
        }
    }
}

data class ProjectUiState(
    val titleEditEnabled: Boolean = false,
    val projectId: String = "",
    val title: String = "",
    val path: String = "",
    val synths: List<SynthData> = listOf(),
    val drumkits: List<DrumData> = listOf(),
    val tapes: List<TapeData> = listOf(),
    val tabOpened: ProjectTab = ProjectTab.Synth,
    val items: List<ProjectResource> = listOf()
)

data class SynthData(
    val id: Int,
    val name: String,
    val path: String,
)

data class DrumData(
    val id: Int,
    val name: String,
    val path: String,
)

data class TapeData(
    val id: Int,
    val name: String,
    val path: String,
)
