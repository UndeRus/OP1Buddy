package org.jugregator.op1buddy

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class ProjectsRepositoryImpl(private val context: Context) : ProjectsRepository {

    private val fileName = "projects.json"
    private val backupRootDir = File(context.filesDir, "op1backups")

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
}
