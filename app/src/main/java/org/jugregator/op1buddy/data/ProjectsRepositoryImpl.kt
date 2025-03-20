package org.jugregator.op1buddy.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.project.Project
import java.io.File

/*
class ProjectsRepositoryImpl(private val context: Context) : ProjectsRepository {

    private val fileName = "projects.json"
    private val backupRootDir = File(context.filesDir, "op1backup")

    // Helper function to get the projects file
    private fun getFile(): File = File(context.filesDir, fileName)

    // Helper function to get the backup directory for a project
    private fun getBackupDir(project: Project): File = File(backupRootDir, project.backupDir)

    // Read a single project by ID
    override suspend fun readProject(id: String): Project? {
        return withContext(Dispatchers.IO) {
            val projects = readAllProjects()
            projects.find { it.id == id }
        }
    }

    // Read all projects from the JSON file
    override suspend fun readAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            val file = getFile()
            if (!file.exists()) {
                return@withContext emptyList<Project>()
            }

            val jsonData = file.readText()
            Json.decodeFromString(jsonData)
        }
    }

    // Add a new project and create its backup directory
    override suspend fun createProject(project: Project): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = readAllProjects().toMutableList()
            if (projects.any { it.id == project.id }) {
                return@withContext false // Project with the same ID already exists
            }

            // Add the new project
            projects.add(project)
            val updatedJson = Json.encodeToString(projects)
            getFile().writeText(updatedJson)

            // Create the backup directory for the project
            val backupDir = getBackupDir(project)
            if (!backupDir.exists()) {
                backupDir.mkdirs()
            }

            return@withContext true
        }
    }

    // Remove a project by ID and delete its backup directory
    override suspend fun removeProject(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = readAllProjects().toMutableList()
            val project = projects.find { it.id == id } ?: return@withContext false // Project not found

            // Remove the project from the list
            projects.remove(project)
            val updatedJson = Json.encodeToString(projects)
            getFile().writeText(updatedJson)

            // Delete the project's backup directory
            val backupDir = getBackupDir(project)
            if (backupDir.exists()) {
                backupDir.deleteRecursively() // Recursively delete all files
            }

            return@withContext true
        }
    }

    override suspend fun updateProject(projectId: String, project: Project): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = readAllProjects().toMutableList()
            val projectIndex = projects.indexOfFirst { it.id == projectId }

            if (projectIndex == -1) {
                return@withContext false // Project not found
            }

            // Update the project in the list
            projects[projectIndex] = project
            val updatedJson = Json.encodeToString(projects)
            getFile().writeText(updatedJson)

            // Optionally, handle backup directory if backupDir was changed
            val oldProject = projects[projectIndex]
            if (oldProject.backupDir != project.backupDir) {
                val oldBackupDir = getBackupDir(oldProject)
                val newBackupDir = getBackupDir(project)

                // Move or rename the backup directory if it has changed
                if (oldBackupDir.exists() && !newBackupDir.exists()) {
                    oldBackupDir.renameTo(newBackupDir)
                }
            }

            return@withContext true
        }
    }
}
*/

class ProjectsRepositoryImpl(private val context: Context) : ProjectsRepository {

    private val fileName = "projects.json"
    private val backupRootDir = File(context.filesDir, "op1backup")

    // StateFlow to hold the list of projects
    private val projectFlow = MutableStateFlow<List<Project>>(emptyList())

    init {
        // Initialize the flow with the current list of projects from the file
        projectFlow.value = loadProjectsFromFile()
    }

    private fun loadProjectsFromFile(): List<Project> {
        val file = getFile()
        return if (file.exists()) {
            val jsonData = file.readText()
            Json.decodeFromString(jsonData)
        } else {
            emptyList()
        }
    }

    private fun getFile(): File = File(context.filesDir, fileName)

    private fun getBackupDir(project: Project): File = File(backupRootDir, project.backupDir)

    override suspend fun readProject(id: String): Project? {
        return withContext(Dispatchers.IO) {
            projectFlow.value.find { it.id == id }
        }
    }

    override fun readAllProjects(): Flow<List<Project>> = projectFlow.asStateFlow()

    override suspend fun createProject(project: Project): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = projectFlow.value.toMutableList()
            if (projects.any { it.id == project.id }) return@withContext false // Project already exists

            projects.add(project)
            saveProjectsToFile(projects)
            projectFlow.value = projects // Emit the updated list

            // Create the backup directory
            val backupDir = getBackupDir(project)
            if (!backupDir.exists()) backupDir.mkdirs()

            true
        }
    }

    override suspend fun removeProject(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = projectFlow.value.toMutableList()
            val project = projects.find { it.id == id } ?: return@withContext false // Project not found

            projects.remove(project)
            saveProjectsToFile(projects)
            projectFlow.value = projects // Emit the updated list

            // Remove the backup directory
            val backupDir = getBackupDir(project)
            if (backupDir.exists()) backupDir.deleteRecursively()

            true
        }
    }

    override suspend fun updateProject(projectId: String, project: Project): Boolean {
        return withContext(Dispatchers.IO) {
            val projects = projectFlow.value.toMutableList()
            val projectIndex = projects.indexOfFirst { it.id == projectId }
            if (projectIndex == -1) return@withContext false // Project not found

            // Update the project in the list
            val oldProject = projects[projectIndex]
            projects[projectIndex] = project
            saveProjectsToFile(projects)
            projectFlow.value = projects // Emit the updated list

            // Handle backup directory change
            if (oldProject.backupDir != project.backupDir) {
                val oldBackupDir = getBackupDir(oldProject)
                val newBackupDir = getBackupDir(project)
                if (oldBackupDir.exists() && !newBackupDir.exists()) {
                    oldBackupDir.renameTo(newBackupDir)
                }
            }

            true
        }
    }

    private fun saveProjectsToFile(projects: List<Project>) {
        val updatedJson = Json.encodeToString(projects)
        getFile().writeText(updatedJson)
    }
}
