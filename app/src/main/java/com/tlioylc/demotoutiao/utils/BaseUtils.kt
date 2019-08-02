package com.tlioylc.demotoutiao.utils

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import java.util.*

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/2410:33 AM
 *    desc   :
 */
class BaseUtils{
    companion object {
        /**
         * 获取view所在的Activity
         * @return
         */
        fun getActivity(view: View?): Activity {
            var context = view?.context
            if (context is Activity) {
                return context
            }
            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }
            throw IllegalStateException("The view's Context is not an Activity.")
        }
    }
}