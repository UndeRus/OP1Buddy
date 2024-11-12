package org.jugregator.op1buddy.features.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.drumkit.DrumkitInfo
import org.jugregator.op1buddy.data.project.LocalFileRepository
import org.jugregator.op1buddy.data.project.Project
import org.jugregator.op1buddy.data.project.ProjectRepository
import org.jugregator.op1buddy.data.synth.SynthInfo
import org.jugregator.op1buddy.features.project.ui.screens.ProjectResource
import org.jugregator.op1buddy.features.project.ui.screens.ProjectTab
import org.jugregator.op1buddy.features.projects.ProjectRoute
import java.util.UUID

class ProjectScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val projectsRepository: ProjectsRepository,
    private val localFileRepository: LocalFileRepository,
    private val projectRepository: ProjectRepository,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<ProjectRoute>()

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

                val synths = if (backupInfo.synthsEnabled) projectRepository.readSynths(projectInfo) else emptyList()

                val drumkits =
                    if (backupInfo.drumkitsEnabled) projectRepository.readDrumKits(projectInfo) else emptyList()

                _mutableState.update {
                    it.copy(
                        synths = synths,
                        drumkits = drumkits,
                        tapes = backupInfo.tapes.filter { tape -> tape.first.enabled }.map {
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
            ProjectTab.Synth -> state.value.synths.mapIndexed { index, synthInfo ->
                ProjectResource.Synth(
                    index = index,
                    filename = synthInfo.filename,
                    name = synthInfo.name,
                    engine = synthInfo.synthEngine
                )
            }

            ProjectTab.Drumkit -> state.value.drumkits.mapIndexed { index, drumkitInfo ->
                ProjectResource.Drumkit(
                    index = index,
                    filename = drumkitInfo.filename,
                    name = drumkitInfo.name
                )
            }

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
    val synths: List<SynthInfo> = listOf(),
    val drumkits: List<DrumkitInfo> = listOf(),
    val tapes: List<TapeData> = listOf(),
    val tabOpened: ProjectTab = ProjectTab.Synth,
    val items: List<ProjectResource> = listOf()
)

data class TapeData(
    val id: Int,
    val name: String,
    val path: String,
)
