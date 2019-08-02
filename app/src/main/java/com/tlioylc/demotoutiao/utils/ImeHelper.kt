package com.tlioylc.demotoutiao.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/166:21 PM
 *    desc   :
 */
class ImeHelper{
    companion object {
        /**
         * 显示输入法。
         *
         * @param context context。
         */
        fun showIme(context: Context) {
            val timer = Timer()
            timer.schedule(object : TimerTask() {

                override fun run() {
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    imm?.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
                }

            }, 200)
        }

        /**
         * 隐藏输入法。
         *
         * @param context context。
         * @param view    view。
         */
        fun hideIme(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}