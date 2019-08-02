package com.livelihood.client.module.widget.imagearticle

import android.content.Context
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import java.lang.Exception

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/226:15 PM
 *    desc   :
 */
class ImageVp : ViewPager{
    constructor( context : Context) : super(context)
    constructor( context : Context, attrs :AttributeSet) : super(context,attrs )

    /**
     * 避免疯狂切换导致crash
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try{
            return super.onInterceptTouchEvent(ev)
        }catch (e : Exception){
            e.printStackTrace()
        }
        return false
    }
}