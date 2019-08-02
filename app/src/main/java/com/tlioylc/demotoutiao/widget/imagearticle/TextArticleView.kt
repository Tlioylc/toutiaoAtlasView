package com.livelihood.client.module.widget.imagearticle

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.tlioylc.demotoutiao.R

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/233:11 PM
 *    desc   :
 */
class TextArticleView : FrameLayout{
    private var viewDragHelper: ViewDragHelper
    private var viewDragHelperCallBack: ViewDragHelper.Callback

    private var tv: TextView

    private var tvBottomMargin = 0

    private var initTop = 0

    private var initPointPosition: Point = Point()


    private var view: View = LayoutInflater.from(context).inflate(R.layout.view_image_text_article, this)
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        tv = view.findViewById(R.id._text_view)
        viewDragHelperCallBack = DraggerCallBack(this)
        viewDragHelper = ViewDragHelper.create(this, 1.0f, viewDragHelperCallBack)


        val tx = "测试text分解了看电视剧发动机撒费老劲第三方就快到了书法家的卡萨回复看" +
                "脸决定撒女材料科学你是看见你发了抠脚大汉实际开发年历史的你附近卡里" +
                "的三分类跨境电商材料款奶萨风口浪尖那段时间那弗兰克绝对是你副科级等你" +
                "上床可能是看来你奋达科技两三年副科级大龄剩女附近开深V是你发动机阿基诺测试text分解了看电视剧发动机撒费老劲第三方就快到了书法家的卡萨回复看" +
                "脸决定撒女材料科学你是看见你发了抠脚大汉实际开发年历史的你附近卡里" +
                "的三分类跨境电商材料款奶萨风口浪尖那段时间那弗兰克绝对是你副科级等你" +
                "上床可能是看来你奋达科技两三年副科级大龄剩女附近开深V是你发动机阿基诺测试text分解了看电视剧发动机撒费老劲第三方就快到了书法家的卡萨回复看" +
                "脸决定撒女材料科学你是看见你发了抠脚大汉实际开发年历史的你附近卡里" +
                "的三分类跨境电商材料款奶萨风口浪尖那段时间那弗兰克绝对是你副科级等你" +
                "上床可能是看来你奋达科技两三年副科级大龄剩女附近开深V是你发动机阿基诺"
        tv.post {

            tv.text = tx

            val fm = tv.paint.fontMetrics
            val tvHeight = (fm.bottom - fm.ascent) * tv.lineCount + ConvertUtils.dp2px(10f)
            val maxHeight = ConvertUtils.dp2px(150f)

            layoutParams.height = tvHeight.toInt()
            if (tvHeight > maxHeight) {
                val params = tv.layoutParams as FrameLayout.LayoutParams
                tvBottomMargin = maxHeight - tvHeight.toInt()
                params.setMargins(0, 0, 0, tvBottomMargin)
                tv.layoutParams = params
            }
        }
    }

    fun getTextTop() : Int{
        return tv.top + view.top
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initTop = tv.top
        initPointPosition.x = tv.left
        initPointPosition.y = tv.top
    }

    /**
     * 实现手指快速滑动 逐渐递减UI效果
     */
    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true))
            invalidate()
    }
    /**
     * 传递拖动区域外的触摸事件
     */
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if(event.rawY < (tv.top + view.top))
            return true
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    /**
     * 传递拖动区域外的触摸事件
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.rawY < (tv.top + view.top))
            return false
        viewDragHelper.processTouchEvent(event)
        return true
    }

    /**
     * ViewDragHelper回调
     */
    inner class DraggerCallBack(var view: ViewGroup) : ViewDragHelper.Callback() {

        /**
         * 设置可拖动的view
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (child == tv)
                return true
            return false
        }

        /**
         * 监听纵向拖动
         */
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val topBound = view.height - tvBottomMargin - tv.height
            return Math.max(Math.min(top, topBound), view.height - tv.height)
        }

//        /**
//         * 监听横向滚动
//         */
//        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
//            val leftBound = view.paddingLeft
//            val rightBound = view.width - child.width - leftBound
//            return Math.min(Math.max(left, leftBound), rightBound)
//        }

        /**
         * 松手时监听
         * 可设置View回归原位
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if(yvel  >  0){//向下为正
                viewDragHelper.settleCapturedViewAt(initPointPosition.x, initPointPosition.y)
            }else if(yvel < 0){
                viewDragHelper.settleCapturedViewAt(initPointPosition.x, 0)
            }else{
                if(tv.top < initTop*2/3){
                    viewDragHelper.settleCapturedViewAt(initPointPosition.x, initPointPosition.y)
                }else{
                    viewDragHelper.settleCapturedViewAt(initPointPosition.x, 0)
                }
            }

            invalidate()
        }

//        /**
//         * 设置横向滚动触摸范围
//         */
//        override fun getViewHorizontalDragRange(child: View): Int {
//            return measuredWidth - child.measuredWidth
//        }
//
//        /**
//         * 设置纵向滚动触摸范围
//         */
//        override fun getViewVerticalDragRange(child: View): Int {
//            return measuredHeight - child.measuredHeight
//        }
    }
}