package com.tlioylc.demotoutiao

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.ToastUtils
import com.github.chrisbanes.photoview.PhotoView
import com.livelihood.client.module.widget.imagearticle.ImageArticleView
import com.tlioylc.demotoutiao.adapter.VPCommonViewAdapter
import com.tlioylc.demotoutiao.bean.ImageBean
import com.tlioylc.demotoutiao.utils.BaseUtils
import kotlinx.android.synthetic.main.activity_news_atlas.*
import java.util.ArrayList


class NewsAtlasActivity : AppCompatActivity(), ImageArticleView.ImageDraggedListener {
    companion object {
        fun open(v: View?){
            open(BaseUtils.getActivity(v))
        }
        fun open(activity: Activity?){
            activity?.startActivity(Intent(activity, NewsAtlasActivity::class.java))
            activity?.overridePendingTransition(0,0)
        }
    }
    private var alpha = 0f
    override fun imageLongClickListener(iv: PhotoView, pos: Int) {
        ToastUtils.showShort("long click")
    }

    override fun imageClickListener(iv: PhotoView, pos: Int) {
        _news_atlas_activity_title.visibility = if(_news_atlas_activity_title.visibility == GONE) VISIBLE else GONE
        _news_atlas_activity_bottom_view.visibility = if(_news_atlas_activity_bottom_view.visibility == GONE) VISIBLE else GONE
        _text_article_view.visibility = if(_text_article_view.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    override fun onDragger(dx: Float, dy: Float) {
        var alpha = (1 - Math.abs(dy/(ScreenUtils.getScreenHeight()/5)))
        if(alpha < 0){
            alpha = 0f
        }
        if(alpha > 1){
            alpha = 1f
        }
        _news_atlas_activity_bottom_view.alpha = alpha
        _text_article_view.alpha = alpha
        _news_atlas_activity_title.alpha  = alpha
        this.alpha = alpha

        _news_atlas_activity_vp.setBackgroundColor(Color.argb( (alpha * 255).toInt(),0,0,0))
    }

    override fun fingerUpSpeed(xvel: Float, yvel: Float,dx: Float,dy: Float) {
        val screenHeightHalf = ScreenUtils.getScreenHeight()/4
        val yvelAbs = Math.abs(yvel)
        val dyAbs =  Math.abs(dy)
        if(yvelAbs > 5000 || dyAbs > screenHeightHalf && alpha != 0f){
            val anim = ValueAnimator.ofFloat(alpha,0f)
            anim.duration = 700
            anim.addUpdateListener {
                val alpha = it.animatedValue as Float
                _news_atlas_activity_bottom_view.alpha = alpha
                _text_article_view.alpha = alpha
                _news_atlas_activity_title.alpha  = alpha
                _news_atlas_activity_vp.setBackgroundColor(Color.argb( (alpha * 255).toInt(),0,0,0))
            }
            anim.start()
        }
    }
    override fun onImageOut() {
        finish()
        overridePendingTransition(0,0)
    }

    private lateinit var vpAdapter : VPCommonViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_atlas)
        BarUtils.setStatusBarColor(this, Color.parseColor("#000000"))

        val params = _news_atlas_activity_title.layoutParams as RelativeLayout.LayoutParams
        params.setMargins(0,BaseConfig.STATUS_BAR_SAFE_HEIGHT,0,0)
        _news_atlas_activity_title.layoutParams = params


        val imageList = ArrayList<ImageBean>()

        for(i in 0..30){
            val data = ImageBean("http://imp.qumitech.com/0006f46d-3fcb-4374-bab3-ddd4e5b2d8ee_avatar.jpg")
            imageList.add(data)
        }
        vpAdapter = VPCommonViewAdapter(this,imageList,this)
        _news_atlas_activity_vp.adapter = vpAdapter

        _news_atlas_activity_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

        })

        _news_atlas_activity_bottom_view.ifDarkModel(true)
    }
}
