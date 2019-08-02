package com.tlioylc.demotoutiao

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.tlioylc.demotoutiao.utils.ImeHelper
import kotlinx.android.synthetic.main.activity_input.*

class InputActivity : AppCompatActivity() {

    companion object {
        val INPUT_RESULT_CODE = 0x000001

        fun open(activity: Activity){
            activity.startActivityForResult(Intent(activity,InputActivity::class.java),0)
        }
    }

    private var inputInitTop = Int.MIN_VALUE
    private var keyboardHeight = Int.MIN_VALUE

    private var ifCurrentShowKeyBoard = false

    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener{
        override fun onGlobalLayout() {
            if(inputInitTop > _input_activity_input_ly.top){//如果当前距顶部高度小于初始top高度，则视为键盘或表情打开
                //键盘弹出
                if(_input_activity_emoticon_ly.layoutParams.height == 0) {
                    ifCurrentShowKeyBoard = true
                    if(keyboardHeight == Int.MIN_VALUE)
                        keyboardHeight = inputInitTop - _input_activity_input_ly.top
                }else{
                    ifCurrentShowKeyBoard = false
                    _input_activity_emoticon_ly.layoutParams.height = 0
                }
            }else{
                //键盘收起
                if(inputInitTop == Int.MIN_VALUE){
                    inputInitTop = _input_activity_input_ly.top
                    return
                }
                if(ifCurrentShowKeyBoard) {
                    finish()
                }
                ifCurrentShowKeyBoard = false
            }
        }
    }

    /**
     * 设置UI系统栏沉浸
     * 界面UI将移至系统状态栏高度
     * 需考虑安全距离
     */
    open fun initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 全透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        initWindow()
        _input_activity_content_ly.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)


        /**
         * 表情弹出
         */
        input_activity_switch_keyboard.setOnClickListener {
            if(ifCurrentShowKeyBoard){
                ImeHelper.hideIme(this,input_activity_switch_keyboard)
                _input_activity_emoticon_ly.layoutParams.height = keyboardHeight
                ifCurrentShowKeyBoard = false
            }else{
                ImeHelper.showIme(this)
                _input_activity_emoticon_ly.layoutParams.height = 0
                ifCurrentShowKeyBoard = true
            }
        }


        _input_activity_input_et.requestFocus()
    }


    override fun onDestroy() {
        super.onDestroy()
        _input_activity_content_ly.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }

}
