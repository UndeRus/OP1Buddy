package org.jugregator.op1buddy

import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.ProjectsRepositoryImpl
import org.jugregator.op1buddy.features.drumkit.DrumKitScreenViewModel
import org.jugregator.op1buddy.features.drumkit.data.DrumkitRepository
import org.jugregator.op1buddy.features.drumkit.media.ExoPlayerProvider
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.projects.ProjectsScreenViewModel
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

    single<LocalFileRepository> { LocalFileRepositoryImpl(androidContext()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(androidContext()) }

    single<DrumkitRepository> {
        DrumkitRepository(androidContext())
    }

    single<ExoPlayerProvider> {
        ExoPlayerProvider(androidContext())
    }

    viewModel {
        OP1SyncViewModel(
            savedStateHandle = get(),
            usbFileRepository = get(),
            backupRepository = get(),
            localFileRepository = get(),
            projectsRepository = get(),
        )
    }

    viewModel<ProjectsScreenViewModel> {
        ProjectsScreenViewModel(savedStateHandle = get(), projectsRepository = get())
    }

    viewModel<ProjectScreenViewModel> {
        ProjectScreenViewModel(
            savedStateHandle = get(),
            projectsRepository = get(),
            localFileRepository = get(),
        )
    }

    viewModel<DrumKitScreenViewModel> {
        DrumKitScreenViewModel(get(), get(), get())
    }
}
