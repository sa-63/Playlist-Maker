package com.project.playlistmaker.playlist.data.storage.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.project.playlistmaker.playlist.data.storage.PlaylistStorage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistStorageImpl(private val context: Context) : PlaylistStorage {

    override fun getSavedImageFromPrivateStorage(uriFile: String?): String? {
        if (uriFile != null && uriFile != "null") {
            val filePath =
                File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "album"
                )

            if (!filePath.exists()) {
                filePath.mkdir()
            }

            val timeStamp =
                SimpleDateFormat("dd.MM.yyyy_hh:mm:ss", Locale.getDefault()).format(Date().time)
            val file = File(filePath, "cover-$timeStamp")

            val inputStream = context.contentResolver.openInputStream(uriFile.toUri())
            val outputStream = FileOutputStream(file)

            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            return file.toUri().toString()
        }
        return null
    }
}