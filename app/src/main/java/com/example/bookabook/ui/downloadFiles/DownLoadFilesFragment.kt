package com.example.bookabook.ui.downloadFiles

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bookabook.BuildConfig
import com.example.bookabook.adapter.BookFileAdapter
import com.example.bookabook.adapter.BookFileListener
import com.example.bookabook.databinding.DownloadFragmentBinding
import com.example.bookabook.utils.isPermissionGranted
import java.io.File

class DownLoadFilesFragment : Fragment() {

    companion object {
        fun newInstance() = DownLoadFilesFragment()
    }

    private val viewModel: DownLoadFilesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DownloadFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this


        checkForPermission()

        binding.viewModel = viewModel


        binding.fileListRecyclerView.adapter = BookFileAdapter(BookFileListener {
            checkForPermission()

            var loadFile = it.fileDirection
            if (loadFile.exists()) {

                val bookUri = FileProvider.getUriForFile(
                    requireActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    loadFile
                )

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(bookUri, "application/pdf")
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "No Application to open the file",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        })


        viewModel.isEmpty.observe(viewLifecycleOwner, Observer {
            if (it == true)
            {
                binding.downloadFilesNotFound.visibility = View.VISIBLE
            }else
            {
                binding.downloadFilesNotFound.visibility = View.GONE
            }
        })

        // createChannel("bookChannelId", "channelNameBook")
        return binding.root
    }

    private fun checkForPermission() {
        if (isPermissionGranted(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            getAllFiles()

        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 4545)
        }
    }

    private fun getAllFiles() {
        val rootPath =
            File(Environment.getExternalStorageDirectory(), "Book A Book DOWNLOADS")
        viewModel.search_Dir(rootPath)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            4545 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getAllFiles()

                }else
                {
                    findNavController().popBackStack()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()

        checkForPermission()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "one way not"

            val notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }


}