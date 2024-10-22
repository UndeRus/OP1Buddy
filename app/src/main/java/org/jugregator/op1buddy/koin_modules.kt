package org.jugregator.op1buddy

import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.sync.BackupRepository
import org.jugregator.op1buddy.features.sync.BackupRepositoryImpl
import org.jugregator.op1buddy.features.sync.LocalFileRepository
import org.jugregator.op1buddy.features.sync.LocalFileRepositoryImpl
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.UsbFileRepository
import org.jugregator.op1buddy.features.sync.UsbFileRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UsbFileRepository> { UsbFileRepositoryImpl() }

    single<BackupRepository> { BackupRepositoryImpl() }

    single<LocalFileRepository> { LocalFileRepositoryImpl() }

    single<ProjectsRepository> { ProjectsRepositoryImpl(androidContext()) }

    viewModel {
        OP1SyncViewModel(
            savedStateHandle = get(),
            usbFileRepository = get(),
            backupRepository = get(),
            localFileRepository = get(),
            projectsRepository = get()
        )
    }

    viewModel<ProjectScreenViewModel> {
        ProjectScreenViewModel(savedStateHandle = get())
    }
}