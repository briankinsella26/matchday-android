package ie.wit.matchday.helpers

import android.content.Context
import timber.log.Timber.e
import java.io.*
import java.lang.StringBuilder

fun write(context: Context, fileName: String, data: String) {
    try {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.write(data)
        outputStreamWriter.close()
    } catch (e: Exception) {
        e("cannot read file: %s", e.toString())
    }
}

fun read(context: Context, filename: String): String {
    var str = ""
    try{
        val inputStream = context.openFileInput(filename)
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val partialStr = StringBuilder()
            var done = false
            while (!done) {
                val line = bufferedReader.readLine()
                done = (line == null)
                if(line != null) {
                    partialStr.append(line)
                }
            }
            inputStream.close()
            str = partialStr.toString()
        }
    } catch (e: FileNotFoundException) {
        e("file not found: %s", e.toString())
    } catch (e: IOException) {
        e("cannot read file: %s", e.toString())
    }
    return str
}

fun exists(context: Context, fileName: String): Boolean{
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}