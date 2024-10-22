package org.jugregator.op1buddy

interface ProjectsRepository {
    suspend fun readProject(id: String): Project?
    suspend fun readAllProjects(): List<Project>
    suspend fun createProject(project: Project): Boolean
    suspend fun removeProject(id: String): Boolean
}