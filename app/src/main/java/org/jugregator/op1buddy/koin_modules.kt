package org.jugregator.op1buddy

import kotlinx.serialization.json.Json
import org.jugregator.op1buddy.data.ProjectsRepository
import org.jugregator.op1buddy.data.ProjectsRepositoryImpl
import org.jugregator.op1buddy.data.project.BackupRepository
import org.jugregator.op1buddy.data.project.BackupRepositoryImpl
import org.jugregator.op1buddy.data.project.LocalFileRepository
import org.jugregator.op1buddy.data.project.LocalFileRepositoryImpl
import org.jugregator.op1buddy.data.project.ProjectRepository
import org.jugregator.op1buddy.data.project.ProjectRepositoryImpl
import org.jugregator.op1buddy.features.drumkit.DrumKitScreenViewModel
import org.jugregator.op1buddy.features.drumkit.data.DrumkitRepository
import org.jugregator.op1buddy.features.drumkit.data.DrumkitRepositoryImpl
import org.jugregator.op1buddy.features.project.DrumkitListScreenViewModel
import org.jugregator.op1buddy.features.project.ProjectScreenViewModel
import org.jugregator.op1buddy.features.project.SynthListScreenViewModel
import org.jugregator.op1buddy.features.project.TapePlayerScreenViewModel
import org.jugregator.op1buddy.features.projects.ProjectsScreenViewModel
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.data.UsbFileRepository
import org.jugregator.op1buddy.features.sync.data.UsbFileRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UsbFileRepository> { UsbFileRepositoryImpl() }

    single<BackupRepository> { BackupRepositoryImpl() }

    single<LocalFileRepository> { LocalFileRepositoryImpl(androidContext()) }

    single<ProjectsRepository> { ProjectsRepositoryImpl(androidContext()) }

    single<DrumkitRepository> {
        DrumkitRepositoryImpl(androidContext(), get())
    }

    single<ProjectRepository> {
        ProjectRepositoryImpl(androidContext(), get())
    }

    single<Json> {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    viewModel<OP1SyncViewModel> {
        OP1SyncViewModel(
            savedStateHandle = get(),
            usbFileRepository = get(),
            backupRepository = get(),
            localFileRepository = get(),
            projectsRepository = get(),
        )
    }

    viewModel<ProjectsScreenViewModel> {
        ProjectsScreenViewModel(
            projectsRepository = get(),
            backupRepository = get()
        )
    }

    viewModel<ProjectScreenViewModel> {
        ProjectScreenViewModel(
            savedStateHandle = get(),
            projectsRepository = get(),
        )
    }

    viewModel<DrumKitScreenViewModel> {
        DrumKitScreenViewModel(
            savedStateHandle = get(),
            projectsRepository = get(),
            drumkitRepository = get(),
        )
    }

    viewModel<DrumkitListScreenViewModel> {
        DrumkitListScreenViewModel(get(), get(), get())
    }

    viewModel<SynthListScreenViewModel> {
        SynthListScreenViewModel(get(), get(), get())
    }

    viewModel<TapePlayerScreenViewModel> {
        TapePlayerScreenViewModel(get(), get(), get())
    }
}
