package com.livelihood.client.module.widget.imagearticle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Point
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.tlioylc.demotoutiao.R

/**
 *    author : tlioylc
 *    e-mail : tlioylc@gmail.com
 *    date   : 2019/7/222:54 PM
 *    desc   : 图片新闻单页View
 */
class ImageArticleView : RelativeLayout {
    private var viewDragHelper: ViewDragHelper
    private var viewDragHelperCallBack: ViewDragHelper.Callback
    private var draggedListener: ImageDraggedListener? = null

    private var iv: PhotoView
    private var view: View = LayoutInflater.from(context).inflate(R.layout.view_image_article, this)
    private var initPointPosition: Point = Point()

    private var imageWith = 0f

    private var imageHeight = 0

    private var currentPosition = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        iv = view.findViewById(R.id._photo_view)
        viewDragHelperCallBack = DraggerCallBack(this)
        viewDragHelper = ViewDragHelper.create(this, 1.0f, viewDragHelperCallBack)

        iv.post {
            iv.setOnClickListener {
                draggedListener?.imageClickListener(iv, currentPosition)
            }
            iv.setOnLongClickListener {
                draggedListener?.imageLongClickListener(iv, currentPosition)
                false
            }
            //监听图片放大变换，如果出现变换则不允许拖动，避免操作混乱
            iv.setOnMatrixChangeListener { rect: RectF? ->
                rect?.let {
                    imageWith = it.width()
                }
            }
        }



    }

    fun setPosition(pos: Int) {
        this.currentPosition = pos
    }

    fun setDaggerListener(draggerListener: ImageDraggedListener?) {
        this.draggedListener = draggerListener
    }

    /**
     * 实现手指快速滑动 逐渐递减UI效果
     */
    override fun computeScroll() {
        if (viewDragHelper.continueSettling(true))
            invalidate()
    }

    /**
     * 记录最开始view的位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        initPointPosition.x = iv.left
        initPointPosition.y = iv.top
    }

    /**
     * 传递拖动区域外的触摸事件
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        /**
         * 图片默认填充整个屏幕宽度，若当前宽度不为屏幕宽度，
         * 则说明出现形变，此时禁止拖动，屏蔽触摸
         */
        return viewDragHelper.shouldInterceptTouchEvent(ev) && (imageWith == ScreenUtils.getScreenWidth().toFloat())
    }

    /**
     * 传递拖动区域外的触摸事件
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    /**
     * 设置image
     */
    fun setImageResource(rsd: Int) {
        iv.setImageResource(rsd)
    }

    /**
     * 设置image
     */
    fun setBitmap(bmp: Bitmap?) {
        iv.setImageBitmap(bmp)
    }
    /**
     * 设置image
     */
    fun setImageDrawable(drawable: Drawable?) {
        iv.setImageDrawable(drawable)
    }

    /**
     * 设置大图加载，超出边界需要缩小
     */
    fun initImageBitmap(resource: Bitmap) {
        val bw = resource.width
        val bh = resource.height
        imageHeight = bh
        if (bw > 8192 || bh > 8192) {
            val bitmap = zoomBitmap(resource, 8192, 8192)
            setBitmap(iv, bitmap)
        } else {
            setBitmap(iv, resource)
        }
    }

    /**
     * 设置image放大及UI展示
     */
    private fun setBitmap(imageView: PhotoView, bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
        if (bitmap != null) {
            val bw = bitmap.width
            val bh = bitmap.height
            val vw = imageView.width
            val vh = imageView.height
            if (bw != 0 && bh != 0 && vw != 0 && vh != 0) {
                if (1.0f * bh / bw > 1.0f * vh / vw) {
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    val offset = (1.0f * bh.toFloat() * vw.toFloat() / bw - vh) / 2
                    adjustOffset(imageView, offset)
                } else {
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            }
        }
    }

    /**
     * 反射设置photoView  Matrix
     */
    private fun adjustOffset(view: PhotoView?, offset: Float) {
        val attach = view?.attacher
        try {
            val field = PhotoViewAttacher::class.java.getDeclaredField("mBaseMatrix")
            field.isAccessible = true
            val matrix = field.get(attach) as Matrix
            matrix.postTranslate(0f, offset)
            val method = PhotoViewAttacher::class.java.getDeclaredMethod("resetMatrix")
            method.isAccessible = true
            method.invoke(attach)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 图片宽高等比压缩
     */
    private fun zoomBitmap(bm: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = reqWidth.toFloat() / width
        val scaleHeight = reqHeight.toFloat() / height
        val scale = Math.min(scaleWidth, scaleHeight)
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scale, scale)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true)
    }

    /**
     * ViewDragHelper回调
     */
    inner class DraggerCallBack(var view: ViewGroup) : ViewDragHelper.Callback() {

        /**
         * 设置可拖动的view
         */
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (child == iv )
                return true
            return false
        }

        /**
         * 监听纵向拖动
         */
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//            有边界的拖动
//            val topBound = view.paddingTop
//            val bottomBound = view.height - child.height - topBound
//            return Math.min(Math.max(top,topBound),bottomBound)
//            if(top != 0)
            /**
             * 图片默认填充整个屏幕宽度，若当前宽度不为屏幕宽度，
             * 则说明出现形变，此时禁止拖动，屏蔽触摸
             */
            if (child == iv && (imageWith == ScreenUtils.getScreenWidth().toFloat())) {
                draggedListener?.onDragger(child.width.toFloat(), top.toFloat())
                return Math.min(top, view.height)
            }

            return super.clampViewPositionVertical(child, top, dy)
        }

//        /**
//         * 监听横向滚动
//         */
//        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
//            val leftBound = view.paddingLeft
//            val rightBound = view.width - child.width - leftBound
//            if (child == iv)
//                draggedListener?.onDragger(left.toFloat(), child.top.toFloat())
//            return Math.min(Math.max(left, leftBound), rightBound)
//        }

        /**
         * 松手时监听
         * 可设置View回归原位
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            if (releasedChild == iv) {
                draggedListener?.fingerUpSpeed(xvel, yvel, releasedChild.left.toFloat(), releasedChild.top.toFloat())
                val screenHeightHalf = ScreenUtils.getScreenHeight()/4
                val yvelAbs = Math.abs(yvel)
                val dy = releasedChild.top.toFloat()
                val dyAbs =  Math.abs(dy)
                if(yvelAbs > 5000 || dyAbs > screenHeightHalf) {
                    val targetTop = if(dy < 0) (-ScreenUtils.getScreenHeight()).toFloat() else (ScreenUtils.getScreenHeight()).toFloat()
                    val animTop = ValueAnimator.ofFloat(releasedChild.top.toFloat(),targetTop)
                    animTop.duration = 700
                    animTop.addUpdateListener {
                        releasedChild.top = (it.animatedValue as Float).toInt()
                        if(it.animatedValue as Float == targetTop){
                            draggedListener?.onImageOut()
                        }
                    }
                    animTop.start()
                    return
                }
                initPointPosition.run {
                    viewDragHelper.settleCapturedViewAt(this.x, this.y)
                    draggedListener?.onDragger(0f, 0f)
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
        /**
         * 设置纵向滚动触摸范围
         */
        override fun getViewVerticalDragRange(child: View): Int {
            if (child is PhotoView) {
                return measuredHeight - imageHeight
            }
            return measuredHeight - child.measuredHeight
        }
    }

    /**
     * 拖动监听
     */
    interface ImageDraggedListener {
        /**
         * 拖动UI移动距离监听
         * @param dx x轴拖动距离
         * @param dy y轴拖动距离
         */
        fun onDragger(dx: Float, dy: Float)

        /**
         * 手指离开View时回调
         * @param xvel x轴拖动速度
         * @param yvel y轴拖动速度
         */
        fun fingerUpSpeed(xvel: Float, yvel: Float, dx: Float, dy: Float)



        /**
         * 图片彻底踢开屏幕时回调关闭页面
         * @param xvel x轴拖动速度
         * @param yvel y轴拖动速度
         */
        fun onImageOut()

        /**
         * 图片点击事件
         * @param iv 图片View
         * @param pos 当前图片position
         */
        fun imageClickListener(iv: PhotoView, pos: Int)

        fun imageLongClickListener(iv: PhotoView, pos: Int)
    }
}