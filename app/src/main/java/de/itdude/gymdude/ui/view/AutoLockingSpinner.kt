package de.itdude.gymdude.ui.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatSpinner

class AutoLockingSpinner : AppCompatSpinner {

	constructor(context: Context) : super(context)
	constructor(context: Context, mode: Int) : super(context, mode)
	constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int) : super(context, attrs, defStyleAttr, mode)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, mode: Int, popupTheme: Resources.Theme?) : super(context,
		attrs,
		defStyleAttr,
		mode,
		popupTheme)

	override fun dispatchTouchEvent(ev: MotionEvent?) = if (adapter.count <= 1) true else super.dispatchTouchEvent(ev)

}