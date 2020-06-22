package com.example.bookabook.ui.addingBook

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.bookabook.R
import com.example.bookabook.databinding.AddingBooksFragmentBinding
import com.example.bookabook.utils.Utils.PICK_IMAGE_REQUEST
import com.google.android.material.chip.Chip

class AddingBooksFragment : Fragment() {

    companion object {
        fun newInstance() = AddingBooksFragment()
    }

    private lateinit var viewModel: AddingBooksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = AddingBooksFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this


        viewModel = ViewModelProviders.of(this).get(AddingBooksViewModel::class.java)
        binding.addBookViewModel = viewModel

        val chipGroup = binding.AddBookHomeFragmentChipGroup
        val genres: ArrayList<String> = ArrayList()

        viewModel.categoryList.observe(viewLifecycleOwner, Observer { data ->
            //chipGroup.removeAllViews()
            data?.forEach {
                if (!genres.contains(it)) {
                    genres.add(it)
                    val chip = Chip(chipGroup.context)
                    chip.text = it
                    chipGroup.addView(chip)
                }
            }
        })

        viewModel.startActivityToPickImage.observe(viewLifecycleOwner, Observer {

            if (it) {
                selectImage()
                viewModel.pickingImageCompleted()
            }

        })
        viewModel.addBookState.observe(viewLifecycleOwner, Observer { uploadState ->
            var toastMessage : String = ""
            when(uploadState)
            {
                UploadBookState.ErrorBookTitle->
                {
                    toastMessage = "Please enter valid Book Title"

                }
                UploadBookState.ErrorBookWriter ->
                {
                    toastMessage = "Please enter valid Book Writer"
                }
                UploadBookState.ErrorBookDescription ->
                {
                    toastMessage = "Please enter valid Book Description"
                }
                UploadBookState.ErrorBookImage ->
                {
                    toastMessage = "Please Pick an Image"
                }
                UploadBookState.FailedToUploadImage ->
                {
                    toastMessage = "Failed to upload the Image"
                }
                UploadBookState.FailedToUploadBook ->
                {
                    toastMessage = "Failed To upload The Book"
                }
                UploadBookState.Uploaded ->
                {
                    toastMessage = "Book Uploaded Successfully"
                    this.findNavController().navigate(R.id.action_addingBooksFragment_to_homeFragment)
                }
                else ->{
                    toastMessage = "UnExpected Error Try Again"
                }
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()

        })

        return binding.root
    }

    private fun selectImage() {

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
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
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}