package org.jugregator.op1buddy.data

import kotlinx.coroutines.flow.Flow
import org.jugregator.op1buddy.data.project.Project

interface ProjectsRepository {
    suspend fun readProject(id: String): Project?
    fun readAllProjects(): Flow<List<Project>>
    suspend fun createProject(project: Project): Boolean
    suspend fun removeProject(id: String): Boolean
    suspend fun updateProject(projectId: String, project: Project): Boolean
}
