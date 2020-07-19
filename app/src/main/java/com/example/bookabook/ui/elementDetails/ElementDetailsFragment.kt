package com.example.bookabook.ui.elementDetails

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bookabook.BuildConfig
import com.example.bookabook.R
import com.example.bookabook.databinding.ElementDetailsFragmentBinding
import com.google.android.material.chip.Chip

class ElementDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ElementDetailsFragment()
    }

    private lateinit var viewModel: ElementDetailsViewModel
    private lateinit var viewModelFactory: ElementDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ElementDetailsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val safeArgs: ElementDetailsFragmentArgs by navArgs()
        val book = safeArgs.clickedBook
        viewModelFactory = ElementDetailViewModelFactory(book)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ElementDetailsViewModel::class.java)
        binding.bookDetailViewModel = viewModel

        viewModel.navigateToLogIn.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                Toast.makeText(context, "please logIn to perform this action", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(R.id.logInFragment)
                viewModel.completeNavigateToLogIn()
            }
        })
        viewModel.openFileNow.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                var loadFile = viewModel.bookFile
                if (loadFile.exists()) {

                    val bookUri = FileProvider.getUriForFile(
                        requireActivity(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        loadFile)

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

                viewModel.completeOpenFile()

            }
        })

        viewModel.askForWritePermission.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                writePermission()
            }
        })


        val chipGroup = binding.bookDetailCategoryChipGroup

        val catList = book.bookCategory
        catList.forEach {
            val chip = Chip(chipGroup.context)
            chip.text = it
            chipGroup.addView(chip)
        }




        return binding.root

    }

    private fun writePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )) {
                PackageManager.PERMISSION_GRANTED -> {

                    Log.d("fileClicked", "granted")
                    viewModel.downLoadFile()

                }
                else -> {
                    Log.d("fileClicked", "Not granted")
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 56)
                }

            }
        } else {
            viewModel.downLoadFile()
        }
        viewModel.completeAskForPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            56 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Log.d("fileClicked", "give granted")
                    viewModel.downLoadFile()
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.checkLogging()
    }

}