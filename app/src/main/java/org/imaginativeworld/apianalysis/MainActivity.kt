package org.imaginativeworld.apianalysis

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.imaginativeworld.apianalysis.client.ApiClient
import org.imaginativeworld.apianalysis.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_report_status.text = "System is ready."

        initClickEvents()
    }

    var totalRequest = 0
    var totalFailedRequest = 0
    var totalResponse = 0
    var totalSuccessfulResponse = 0
    var totalErrorResponse = 0

    var totalResponseTime = 0L

    var totalRequestTry = 0

    fun initClickEvents() {
        btn_start.setOnClickListener {
            startAnalysis()
        }

        scroll_container.setOnClickListener {
            Utils.hideKeyboard(this)
        }

        tv_report_status.setOnClickListener {
            Utils.hideKeyboard(this)
        }
    }

    private fun startAnalysis() {

        if (!isInputsValid()) {
            return
        }

        updateStartBtnPref(true)

        totalRequestTry = Integer.valueOf(et_request_number.text.toString())

        updateOutput("Total request: ${totalRequestTry}")

        AsyncTask.execute {

            // Test api 1
            for (i in 1..totalRequestTry) {

                totalRequest += 1

                ApiClient.getClient().doUserLogin(
                    "lasc1993@yahoo.com",
                    "1234567"
                ).enqueue(object : Callback<ResponseBody> {

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        val currentResponse = Utils.getResponseTime(response)

                        updateOutput("Request #${i} response time: ${currentResponse}ms")

                        totalResponseTime += currentResponse

                        totalResponse += 1

                        if (response.isSuccessful && response.code() == 200) {

//                        updateOutput("===========[RESPONSE]===========")
//                        updateOutput("${response.body()?.string()}")
//                        updateOutput("================================")

                            totalSuccessfulResponse += 1

                        } else {

                            totalErrorResponse += 1

                        }

                        checkAndUpdateFinalResult()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        updateOutput("Request #${i} failed: ${t.localizedMessage}")

                        totalFailedRequest += 1

                        checkAndUpdateFinalResult()
                    }

                })

            }

        }

    }

    private fun isInputsValid(): Boolean {
        et_request_number.error = null

        if (et_request_number.text.toString().isEmpty()) {
            et_request_number.error = "Enter concurrent request number!"
            et_request_number.requestFocus()

            return false
        }

        return true
    }

    @SuppressLint("SetTextI18n")
    fun updateOutput(text: String) {
        tv_report_status.text = "${tv_report_status.text}\n${text}"

        scroll_container.post {
            scroll_container.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    fun checkAndUpdateFinalResult() {
        if ((totalResponse + totalFailedRequest) == totalRequestTry) {

            updateOutput("Total request: ${totalRequest}")
            updateOutput("Total failed request: ${totalFailedRequest}")
            updateOutput("Total successful response: ${totalSuccessfulResponse}")
            updateOutput("Total error response: ${totalErrorResponse}")

            if (totalResponseTime == 0L) {
                updateOutput("Response time average: 0ms\n\n")
            } else {
                updateOutput("Response time average: ${totalResponseTime / totalResponse}ms\n\n")
            }

            // Reset vars
            totalRequest = 0
            totalFailedRequest = 0
            totalResponse = 0
            totalSuccessfulResponse = 0
            totalErrorResponse = 0
            totalResponseTime = 0L

            updateStartBtnPref(false)
        }
    }

    private fun updateStartBtnPref(isStart: Boolean) {
        if (isStart) {

            btn_start.isEnabled = false
            btn_start.text = "Analysing..."
            btn_start.icon = ContextCompat.getDrawable(this, R.drawable.ic_sync_black_24dp)

            et_request_number.isEnabled = false

        } else {

            btn_start.isEnabled = true
            btn_start.text = "Start Analysis"
            btn_start.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_play_circle_outline_black_24dp)

            et_request_number.isEnabled = true

        }
    }
}
