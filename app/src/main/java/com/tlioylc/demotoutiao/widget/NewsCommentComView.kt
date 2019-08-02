package com.tlioylc.demotoutiao.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tlioylc.demotoutiao.InputActivity
import com.tlioylc.demotoutiao.R
import com.tlioylc.demotoutiao.utils.BaseUtils
import kotlinx.android.synthetic.main.view_news_comments_bottom_view.view.*

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/2311:56 AM
 *    desc   : 新闻底部评价UI
 */
class NewsCommentComView : FrameLayout{

    private  var onBtnClickListener :OnBtnClickListener ?= null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_news_comments_bottom_view,this)

        _news_bottom_view_content_input.setOnClickListener {
            InputActivity.open(BaseUtils.getActivity(_news_bottom_view_content_input))
        }

        _news_bottom_view_content_comment.setOnClickListener {
            //评论点击
            onBtnClickListener?.onCommentsClick()
        }

        _news_bottom_view_content_share.setOnClickListener {
           //TODO 分享
        }
    }

    fun setListener(onBtnClickListener :OnBtnClickListener){
        this.onBtnClickListener = onBtnClickListener
    }


    fun ifDarkModel(b : Boolean){
        if (b){
            setDarkModel()
        }else{
            setHighLight()
        }
    }

    private fun setHighLight(){
        _news_bottom_view_content_input.setBackgroundResource(R.drawable.bg_f4f4f4_17_radius)
        _news_bottom_view_content_comment.setImageResource(R.mipmap.btn_article_comment)
        _news_bottom_view_content_like.setImageResource(R.mipmap.btn_article_like)
        _news_bottom_view_content_collection.setImageResource(R.mipmap.btn_article_collection)
        _news_bottom_view_content_share.setImageResource(R.mipmap.btn_article_share)
    }

    private fun setDarkModel(){
        _news_bottom_view_content_input.setBackgroundResource(R.drawable.bg_26ffffff_17_radius)
        _news_bottom_view_content_comment.setImageResource(R.mipmap.btn_article_comment_darkmode)
        _news_bottom_view_content_like.setImageResource(R.mipmap.btn_article_like_darkmode)
        _news_bottom_view_content_collection.setImageResource(R.mipmap.btn_article_collection_darkmode)
        _news_bottom_view_content_share.setImageResource(R.mipmap.btn_article_share_darkmode)
    }

    interface OnBtnClickListener{
        fun onCommentsClick()
        fun onShareClick()
    }
}