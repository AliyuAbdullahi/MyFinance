package latproject.com.myfinance.core.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

class JsonProcessorFromAsset {

    fun loadJSONFromAsset(context: Context, path: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(path)

            val size = inputStream.available()

            val buffer = ByteArray(size)

            val read = inputStream.read(buffer)

            inputStream.close()

            json = String(buffer, Charset.forName("UTF-8"))


        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }
}
