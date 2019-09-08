package org.imaginativeworld.apianalysis.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import retrofit2.Response

class Utils {

    companion object {

        fun getResponseTime(response: Response<*>): Long {
            return response.raw().receivedResponseAtMillis() - response.raw().sentRequestAtMillis()
        }

        fun hideKeyboard(activity: Activity) {
            val view = activity.currentFocus
            view?.let { v ->
                v.clearFocus()

                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.let { it.hideSoftInputFromWindow(v.windowToken, 0) }
            }
        }

    }

}