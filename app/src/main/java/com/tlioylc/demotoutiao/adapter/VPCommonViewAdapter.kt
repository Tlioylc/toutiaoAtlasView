package com.tlioylc.demotoutiao.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.livelihood.client.module.widget.imagearticle.ImageArticleView
import com.tlioylc.demotoutiao.R
import com.tlioylc.demotoutiao.bean.ImageBean
import java.util.ArrayList

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/194:39 PM
 *    desc   :
 */
class VPCommonViewAdapter(private val mContext: Context
                          ,private val mImgList: List<ImageBean>
                          ,daggarListener : ImageArticleView.ImageDraggedListener?) : PagerAdapter() {

    private val viewList = ArrayList<ImageArticleView>(4)
    init {
        createImageViews(daggarListener)
    }

    private fun createImageViews(daggarListener : ImageArticleView.ImageDraggedListener?) {
        for (i in 0..3) {
            val imageView = ImageArticleView(mContext)
            imageView.setDaggerListener(daggarListener)
            viewList.add(imageView)
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
       return mImgList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is ImageArticleView) {
            `object`.setImageDrawable(null)
            viewList.add(`object`)
            container.removeView(`object`)
        }
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val currentView = viewList.removeAt(0)
        currentView.setPosition(position)
        val image = mImgList[position]
        container.addView(currentView)

            Glide.with(mContext).asBitmap()
                    .load(image.path).into(object : CustomViewTarget<ImageArticleView,Bitmap>(currentView){
                        override fun onResourceCleared(placeholder: Drawable?) {
                            currentView.setImageResource(R.mipmap.ic_default_img)
                        }
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                        }

                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            currentView.setBitmap(resource)
                        }

                    })

        return currentView
    }
}