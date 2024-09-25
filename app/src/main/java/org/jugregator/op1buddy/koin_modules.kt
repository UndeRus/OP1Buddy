package org.jugregator.op1buddy

import androidx.compose.ui.text.intl.LocaleList
import org.jugregator.op1buddy.features.sync.BackupRepository
import org.jugregator.op1buddy.features.sync.BackupRepositoryImpl
import org.jugregator.op1buddy.features.sync.LocalFileRepository
import org.jugregator.op1buddy.features.sync.LocalFileRepositoryImpl
import org.jugregator.op1buddy.features.sync.OP1SyncViewModel
import org.jugregator.op1buddy.features.sync.UsbFileRepository
import org.jugregator.op1buddy.features.sync.UsbFileRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UsbFileRepository> { UsbFileRepositoryImpl() }

    single<BackupRepository> { BackupRepositoryImpl() }

    single<LocalFileRepository> { LocalFileRepositoryImpl() }

    viewModel { OP1SyncViewModel(get(), get(), get()) }
}