package org.jugregator.op1buddy

import kotlinx.serialization.Serializable

@Serializable
data class Project(val id: String, val title: String, val backupDir: String)