package com.example.bookabook.ui.addingBook

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.data.storageRef
import com.example.bookabook.databinding.AddingBooksFragmentBinding
import com.example.bookabook.ui.userAuthentication.logIn.LogInViewModel
import com.example.bookabook.utils.Utils.PICK_IMAGE_REQUEST
import com.example.bookabook.utils.Utils.pickImageIntent
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.ArrayList


class AddingBooksFragment : Fragment() {

    companion object {
        fun newInstance() = AddingBooksFragment()
    }

    private lateinit var viewModel: AddingBooksViewModel
    private val logInViewModel: LogInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = AddingBooksFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        logInViewModel.authenticationState.observe(
            viewLifecycleOwner,
            Observer { authenticationState ->
                when (authenticationState) {
                    LogInViewModel.AuthenticationState.AUTHENTICATED -> showWelcomeMessage()
                    LogInViewModel.AuthenticationState.UNAUTHENTICATED -> this.findNavController()
                        .navigate(R.id.logInFragment)
                }
            })

        viewModel = ViewModelProviders.of(this).get(AddingBooksViewModel::class.java)
        binding.addBookViewModel = viewModel

        val chipGroup = binding.AddBookHomeFragmentChipGroup
        val genres: ArrayList<String> = ArrayList()

        viewModel.categoryList.observe(viewLifecycleOwner, Observer { data ->

            data?.forEach {
                if (!genres.contains(it)) {
                    genres.add(it)
                    val chip = Chip(chipGroup.context)
                    chip.text = it
                    chipGroup.addView(chip)
                }
            }
        })


        viewModel.eventToGetCategories.observe(viewLifecycleOwner, Observer {
            if (false != it) {
                val x = chipGroup.checkedChipIds
                val categoryList = ArrayList<String>()
                x.forEach {

                    val d = chipGroup.findViewById<Chip>(it)
                    categoryList.add(d.text.toString())
                }

                viewModel.checkedItemList = categoryList
            }
        })

        viewModel.eventToPickBookFile.observe(viewLifecycleOwner, Observer {
            if (it != false) {
                Log.d("fileClicked", "clicked")
                pickFileNow()
            }
        })

        viewModel.startActivityToPickImage.observe(viewLifecycleOwner, Observer {

            if (it) {
                selectImage()
                viewModel.pickingImageCompleted()
            }

        })
        viewModel.addBookState.observe(viewLifecycleOwner, Observer { uploadState ->
            var toastMessage: String = ""
            when (uploadState) {
                UploadBookState.ErrorBookTitle -> {
                    toastMessage = "Please enter valid Book Title"

                }
                UploadBookState.ErrorBookWriter -> {
                    toastMessage = "Please enter valid Book Writer"
                }
                UploadBookState.ErrorBookDescription -> {
                    toastMessage = "Please enter valid Book Description"
                }
                UploadBookState.ErrorBookImage -> {
                    toastMessage = "Please Pick an Image"
                }
                UploadBookState.FailedToUploadImage -> {
                    toastMessage = "Failed to upload the Image"
                }
                UploadBookState.FailedToUploadBook -> {
                    toastMessage = "Failed To upload The Book"
                }
                UploadBookState.FailedToUploadBookFile -> {
                    toastMessage = "Please Pick File"
                }
                UploadBookState.Uploaded -> {
                    toastMessage = "Book Uploaded Successfully"
                    this.findNavController()
                        .navigate(R.id.action_addingBooksFragment_to_homeFragment)
                }
                else -> {
                    toastMessage = "UnExpected Error Try Again"
                }
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

        })

        return binding.root
    }

    private fun pickFileNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            when (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )) {
                PackageManager.PERMISSION_GRANTED -> {
                    Log.d("fileClicked", "granted")
                    selectFile()
                }
                else -> {
                    Log.d("fileClicked", "Not granted")
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 53)
                }
            }
        } else {
            selectFile()
        }

    }

    private fun selectFile() {
        Log.d("fileClicked", "Select file fun")

        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1212)
    }

    private fun showWelcomeMessage() {
        Toast.makeText(context, "welcome", Toast.LENGTH_LONG).show()
    }

    private fun selectImage() {

        val intent = pickImageIntent()
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            val imgUri = data.data
            imgUri?.let { viewModel.setImageString(it) }
            viewModel.pickingImageCompleted()
            viewModel.setVisiabilityTrue()
        } else if (requestCode == 1212 && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            } else {
                val fileUrl = data.data
                uploadFileBook(fileUrl)



                viewModel.pickFileComplete()

            }

        }
    }
    private fun uploadFileBook(_pickFileString: Uri?)
    {

        val ref = storageRef.child("uploadsBooks/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(_pickFileString!!).addOnProgressListener {taskSnapshot ->

            val progress =((100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount).toInt()
            viewModel.setFileProgress("Upload is $progress% done")

        }.addOnCanceledListener {

        }
        val urlTask =
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    val fileUri = it.result
                    viewModel.setFileString(fileUri!!)
                }
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            53 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    pickFileNow()
                }
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}