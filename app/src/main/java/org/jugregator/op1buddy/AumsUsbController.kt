package org.jugregator.op1buddy

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import me.jahnen.libaums.core.UsbMassStorageDevice
import me.jahnen.libaums.core.fs.FileSystem
import me.jahnen.libaums.core.fs.FileSystemFactory.registerFileSystem
import me.jahnen.libaums.core.fs.UsbFile
import me.jahnen.libaums.javafs.JavaFsFileSystemCreator
import java.io.IOException

private const val ACTION_USB_PERMISSION = "org.jugregator.op1buddy.USB_PERMISSION"
private const val TAG = "LibAUMSDemo"

class AumsUsbController(
    private val context: Context,
    val connectedCallback: (FileSystem) -> Unit,
    val disconnectedCallback: () -> Unit,
    val errorCallback: (String) -> Unit,
    val fsInitCallback: (OP1State) -> Unit,
) {
    private var foundDevices = listOf<UsbMassStorageDevice>()

    init {
        registerFileSystem(JavaFsFileSystemCreator())
//        registerFileSystem(BridgeFileSystemCreator())
    }

    fun initReceiver() {
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        ContextCompat.registerReceiver(context, usbReceiver, filter, ContextCompat.RECEIVER_EXPORTED)
    }

    fun destroyReceiver() {
        context.unregisterReceiver(usbReceiver)
    }

    fun closeDevices() {
        foundDevices.map { device ->
            try {
                device.close()
            } catch (e: IOException) {
                Log.e(TAG, "Failed to close device", e)
            }
        }
    }

    private fun getDevices() {
        val devices = UsbMassStorageDevice.getMassStorageDevices(context)
        foundDevices = devices.toList()
    }

    fun discoverDevice(device: UsbDevice) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val massStorageDevices = UsbMassStorageDevice.getMassStorageDevices(context)
        if (massStorageDevices.isEmpty()) {
            //NO devices found, update state
            return
        }

        if (usbManager.hasPermission(device)) {
            // Device found, init
            setupDevice(device)
        } else {
            // Request permission
            val intentForPermission = Intent(
                ACTION_USB_PERMISSION
            )
            intentForPermission.setPackage(context.packageName)
            val permissionIntent = PendingIntent.getBroadcast(
                context, 0, intentForPermission, PendingIntent.FLAG_MUTABLE
            )
            usbManager.requestPermission(device, permissionIntent)
        }
    }

    private fun setupDevice(usbDevice: UsbDevice) {
        try {
            getDevices()
            foundDevices.filter { it.usbDevice == usbDevice }.forEach {
                it.init()
//                currentFs = foundDevices[currentDevice].partitions[0].fileSystem.also {

                val currentFs = it.partitions[0].fileSystem.also {
                    Log.d(TAG, "Capacity: " + it.capacity)
                    Log.d(TAG, "Occupied Space: " + it.occupiedSpace)
                    Log.d(TAG, "Free Space: " + it.freeSpace)
                    Log.d(TAG, "Chunk size: " + it.chunkSize)
                }
                connectedCallback(currentFs)

                try {
                    val op1State = processOP1FS(currentFs)
                    fsInitCallback(op1State)
                } catch (e: IllegalStateException) {
                    errorCallback("Failed to check device integrity")
                }

                //TODO: currentFs is our device, work with it
            }

        } catch (e: IOException) {
            //TODO: failed to init device, show error
            Log.e("OP1Buddy", "Failed to init device", e)
            errorCallback("Failed to init device")
        }
    }

    private val usbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    // After permission granted
                    val device =
                        intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice?

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        device?.let {
                            setupDevice(it)
                        }
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED == action) {
                // After device connected
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                } else {
                    intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice?
                }
                Log.d(TAG, "USB device attached")

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    discoverDevice(device)
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                // After device disconnected
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                } else {
                    intent.getParcelableExtra(UsbManager.EXTRA_DEVICE) as UsbDevice?
                }
                Log.d(TAG, "USB device detached")

                // determine if connected device is a mass storage devuce
                if (device != null) {
                    foundDevices.filter { it.usbDevice == device }.forEach { it.close() }
                    // check if there are other devices or set action bar title
                    // to no device if not
                    discoverDevice(device)
                    disconnectedCallback()
                }
            }
        }
    }
}

@Throws(IllegalStateException::class)
private fun processOP1FS(currentFs: FileSystem): OP1State {
    val stateBuilder = OP1State.OP1StateBuilder()

    val root = currentFs.rootDirectory

    val files = root.listFiles()

    val tapeDir = files.find { it.name == "tape" }

    if (tapeDir != null) {
        val tapeDirFiles = tapeDir.listFiles()
        for (tapeFile in tapeDirFiles) {
            if (tapeFile.name == "track_1.aif") {
                stateBuilder.addTrack(tapeFile)
            }

            if (tapeFile.name == "track_2.aif") {
                stateBuilder.addTrack(tapeFile)
            }

            if (tapeFile.name == "track_3.aif") {
                stateBuilder.addTrack(tapeFile)
            }

            if (tapeFile.name == "track_4.aif") {
                stateBuilder.addTrack(tapeFile)
            }
        }
    } else {
        //TODO: no tape found
        println("No tapes found")
        error("No tracks found")
    }

    val synthDir = files.find { it.name == "synth" }
    if (synthDir != null) {
        val userSynthDir = synthDir.listFiles().find { it.name == "user" }
        if (userSynthDir != null) {
            val userSynths = userSynthDir.listFiles()
            for (synthFile in userSynths) {
                if (synthFile.name == "1.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "2.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "3.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "4.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "5.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "6.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "7.aif") {
                    stateBuilder.addSynth(synthFile)
                }

                if (synthFile.name == "8.aif") {
                    stateBuilder.addSynth(synthFile)
                }
            }
        } else {
            println("Synths not found")
            error("No synths found")
        }
    } else {
        println("Synths dir not found")
        error("No synths dir found")
    }

    val drumDir = files.find { it.name == "drum" }
    if (drumDir != null) {
        val userDrumDir = drumDir.listFiles().find { it.name == "user" }
        if (userDrumDir != null) {
            var userDrums = userDrumDir.listFiles().filter { it.name.endsWith(".aif") }

            for (drumFile in userDrums) {
                if (drumFile.name == "1.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "2.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "3.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "4.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "5.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "6.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "7.aif") {
                    stateBuilder.addDrum(drumFile)
                }

                if (drumFile.name == "8.aif") {
                    stateBuilder.addDrum(drumFile)
                }
            }
        } else {
            error("No drums found")
        }
    } else {
        error("No drums dir found")
    }

    val albumDir = files.find { it.name == "album" }
    if (albumDir != null) {
        val albumFiles = albumDir.listFiles()
        for (albumFile in albumFiles) {
            if (albumFile.name == "side_a.aif") {
                stateBuilder.addAlbum(albumFile)
            }

            if (albumFile.name == "side_b.aif") {
                stateBuilder.addAlbum(albumFile)
            }
        }
    } else {
        error("Albums dir not found")
    }

    return stateBuilder.build()
}

class OP1State private constructor(
    val tracks: List<UsbFile>,
    val albums: List<UsbFile>,
    val synths: List<UsbFile>,
    val drums: List<UsbFile>,
) {

    class OP1StateBuilder {
        private var tracks: MutableList<UsbFile> = mutableListOf()
        private var albums: MutableList<UsbFile> = mutableListOf()
        private var synths: MutableList<UsbFile> = mutableListOf()
        private var drums: MutableList<UsbFile> = mutableListOf()

        fun addTrack(track: UsbFile) {
            tracks.add(track)
        }

        fun addAlbum(album: UsbFile) {
            albums.add(album)
        }

        fun addSynth(synth: UsbFile) {
            synths.add(synth)
        }

        fun addDrum(drum: UsbFile) {
            drums.add(drum)
        }


        @Throws(IllegalStateException::class)
        fun build(): OP1State {
            if (tracks.size != 4) {
                throw IllegalStateException("Only ${tracks.size} of 4 tracks found")
            }

            if (albums.size != 2) {
                throw IllegalStateException("Only ${albums.size} of 2 albums found")
            }

            if (synths.size != 8) {
                throw IllegalStateException("Only ${synths.size} of 8 synths found")
            }

            if (drums.size != 8) {
                throw IllegalStateException("Only ${synths.size} of 8 drums found")
            }
            return OP1State(
                tracks,
                albums,
                synths,
                drums
            )
        }
    }
}