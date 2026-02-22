package com.example.stikerrli

import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RequestHandler {

    fun sendPostRequest(requestURL: String, postDataParams: HashMap<String, String>): String {
        val sb = StringBuilder()
        try {
            val url = URL(requestURL)
            val conn = url.openConnection() as HttpURLConnection

            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")

            val jsonObject = JSONObject()
            for ((key, value) in postDataParams) {
                jsonObject.put(key, value)
            }

            val os: OutputStream = conn.outputStream
            BufferedWriter(OutputStreamWriter(os, "UTF-8")).use { writer ->
                writer.write(jsonObject.toString())
                writer.flush()
            }
            os.close()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                    var response: String?
                    while (br.readLine().also { response = it } != null) {
                        sb.append(response)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    fun sendGetRequest(requestURL: String): String {
        val sb = StringBuilder()
        try {
            val url = URL(requestURL)
            val conn = url.openConnection() as HttpURLConnection
            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    sb.append(line).append("\n")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sb.toString()
    }

    fun sendGetRequestParam(requestURL: String, id: String): String {
        return sendGetRequest("$requestURL$id")
    }
}