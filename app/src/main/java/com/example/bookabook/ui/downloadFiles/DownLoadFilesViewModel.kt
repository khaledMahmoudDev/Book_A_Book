package com.example.bookabook.ui.downloadFiles

import android.app.Application
import android.app.NotificationManager
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.bookabook.model.FileModel
import com.example.bookabook.utils.sendBookNotification
import java.io.File

class DownLoadFilesViewModel(val app: Application) : AndroidViewModel(app) {

    val fileList = MutableLiveData<ArrayList<FileModel>>()

    val isEmpty = Transformations.map(fileList){
        it.isNullOrEmpty()
    }


     fun search_Dir(dir: File) {

        val listFile = ArrayList<FileModel>()
        val pdfPattern = ".pdf"

        val mFileList = dir.listFiles()

        for (i in mFileList.indices) {

            if (mFileList[i].isDirectory) {
                search_Dir(mFileList[i])
            } else {
                if (mFileList[i].name.endsWith(pdfPattern)) {
                    val file = FileModel(mFileList[i].name, mFileList[i].absoluteFile)
                    listFile.add(file)

                    fileList.value = listFile

                }
            }
        }
    }

}