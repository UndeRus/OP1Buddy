package org.jugregator.op1buddy.features.projects

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.project.BackupRepository
import org.jugregator.op1buddy.data.project.Project
import java.io.File
import java.util.Date
import java.util.UUID

class ProjectsScreenViewModel(
    private val projectsRepository: ProjectsRepository,
    private val backupRepository: BackupRepository,
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

    fun onBackupPathSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            // Check for the freshest data.
            contentResolver.takePersistableUriPermission(uri, takeFlags)

            val selectedFile = DocumentFile.fromSingleUri(context, uri)
            if (selectedFile == null) {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val filename = contentResolver.queryFileName(uri)
            filename?.let {
                if (!it.endsWith(".op1.zip")) {
                    Toast.makeText(context, "Wrong project format", Toast.LENGTH_SHORT).show()
                    return@launch
                }
            }

            _mutableUiState.update {
                it.copy(projectImporting = true)
            }

            withContext(Dispatchers.IO) {
                val id = UUID.randomUUID().toString()
                val backupDir = File(context.filesDir, "op1backup/${id}")
                val project = Project(
                    id = id,
                    title = filename ?: Date().toString(),
                    backupDir = backupDir.name,
                )
                projectsRepository.createProject(project)
                val inputStreamLambda = { contentResolver.openInputStream(selectedFile.uri) }
                backupRepository.importBackup(backupDir, inputStreamLambda)
            }

            _mutableUiState.update {
                it.copy(projectImporting = false)
            }
        }
    }
}

fun ContentResolver.queryFileName(uri: Uri): String? {
    val cursor: Cursor = query(uri, null, null, null, null) ?: return null
    val nameIndex: Int = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    cursor.moveToFirst()
    val name: String = cursor.getString(nameIndex)
    cursor.close()
    return appendExtensionIfNeeded(name, uri)
}

private fun ContentResolver.appendExtensionIfNeeded(name: String, uri: Uri): String {
    return if (hasKnownExtension(name)) {
        name
    } else {
        val type = getType(uri)
        if (type != null && allSupportedDocumentsTypesToExtensions.containsKey(type)) {
            return name + allSupportedDocumentsTypesToExtensions[type]
        }
        Log.e("SAFUtils", "unknown file type: $type, for file: $name")
        name
    }
}

private fun hasKnownExtension(filename: String): Boolean {
    val lastDotPosition = filename.indexOfLast { it == '.' }
    if (lastDotPosition == -1) {
        return false
    }
    val extension = filename.substring(lastDotPosition)
    return extensionsToTypes.containsKey(extension)
}

val allSupportedDocumentsTypesToExtensions = mapOf(
    "application/msword" to ".doc",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" to ".docx",
    "application/pdf" to ".pdf",
    "text/rtf" to ".rtf",
    "application/rtf" to ".rtf",
    "application/x-rtf" to ".rtf",
    "text/richtext" to ".rtf",
    "text/plain" to ".txt"
)
private val extensionsToTypes = allSupportedDocumentsTypesToExtensions.invert()

private fun <K, V> Map<K, V>.invert(): Map<V, K> {
    val inverted = mutableMapOf<V, K>()
    for (item in this) {
        inverted[item.value] = item.key
    }
    return inverted
}

data class ProjectsScreenUiState(
    val projects: List<Project> = listOf(),
    val projectImporting: Boolean = false,
)
