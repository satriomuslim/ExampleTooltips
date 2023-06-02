package com.example.tooltipscontoh.model

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.exampletooltips.model.TooltipBuilder
import com.example.exampletooltips.model.TooltipContentPosition
import com.example.exampletooltips.model.TooltipDialog
import com.example.exampletooltips.model.TooltipLayout
import com.example.exampletooltips.model.TooltipListener
import com.example.exampletooltips.model.TooltipObject
import com.example.exampletooltips.model.TooltipPreference
import com.example.exampletooltips.model.TooltipViewHelper
import com.example.tooltipscontoh.R

class TooltipDialog : DialogFragment() {

    private val delayScrolling = 350
    private val logTag: String = TooltipDialog::class.java.simpleName

    private var tutorsList: ArrayList<TooltipObject>? = null
    private var currentTutorIndex = -1
    private var builder: TooltipBuilder? = null
    private var dialogTag: String? = null

    private var hasViewGroupHandled = false
    private var mFragmentManager: FragmentManager? = null

    private var retryCounter = 0

    companion object {
        private const val ARG_BUILDER = "BUILDER"

        fun newInstance(builder: TooltipBuilder?): TooltipDialog {
            val args = Bundle()
            val fragment = TooltipDialog()
            args.putParcelable(ARG_BUILDER, builder)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs(arguments)
    }

    private fun getArgs(args: Bundle?) {
        builder = args!![ARG_BUILDER] as TooltipBuilder?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog =
            object : Dialog(requireContext(), R.style.UglyTooltip) {
                @Deprecated("Deprecated in Java")
                override
                fun onBackPressed() {
                    if (builder != null) {
                        if (builder!!.isClickable()) {
                            previous()
                        }
                    }
                }
            }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = TooltipLayout(requireActivity(), builder)
        initViews(view as TooltipLayout)
        return view
    }

    private fun initViews(view: TooltipLayout) {
        view.setTooltipListener(object : TooltipListener {
            override
            fun onPrevious() {
                previous()
            }

            override
            fun onNext() {
                next()
            }

            override
            fun onComplete() {
                if (!TextUtils.isEmpty(dialogTag)) {
                    TooltipPreference.setShown(requireContext(), dialogTag, true)
//                    TooltipPreference.hasShown(requireContext(), dialogTag)
                }
                this@TooltipDialog.close()
            }
        })
        if (builder != null) {
            isCancelable = builder!!.isClickable()
        }
    }

    operator fun next() {
        if (currentTutorIndex + 1 >= tutorsList!!.size) {
            close()
        } else {
            if (tutorsList != null) {
                this@TooltipDialog.show(
                    activity,
                    mFragmentManager!!,
                    dialogTag,
                    tutorsList!!,
                    currentTutorIndex + 1
                )
            }
        }
    }

    fun previous() {
        if (currentTutorIndex - 1 < 0) {
            currentTutorIndex = 0
        } else {
            if (tutorsList != null) {
                this@TooltipDialog.show(
                    activity,
                    mFragmentManager!!,
                    dialogTag,
                    tutorsList!!,
                    currentTutorIndex - 1
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog!!.window
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.transparent)
            window.setDimAmount(0f)
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun show(
        activity: Activity?,
        fm: FragmentManager,
        sharedPrefTag: String? = null,
        tutorList: ArrayList<TooltipObject>
    ) {
        mFragmentManager = fm
        show(activity, fm, sharedPrefTag, tutorList, 0)
    }

    private fun show(
        activity: Activity?,
        fm: FragmentManager,
        sharedPrefTag: String? = null,
        tutorList: ArrayList<TooltipObject>,
        index: Int,
        onStep: ((Int) -> Unit)? = null
    ) {
        if (activity == null || activity.isFinishing) {
            return
        }

        var indexToShow = index

        try {
            tutorsList = tutorList
            this.dialogTag = sharedPrefTag

            if (indexToShow < 0 || indexToShow >= tutorList.size) {
                indexToShow = 0
            }

            val previousIndex = currentTutorIndex
            currentTutorIndex = indexToShow
            hasViewGroupHandled = false

            onStep?.invoke(currentTutorIndex)

            if (currentTutorIndex == tutorList.lastIndex + 1) {
                hasViewGroupHandled = true
            }

            // has been handled by listener
            if (hasViewGroupHandled) {
                return
            }

            val tooltipObject: TooltipObject = tutorList[currentTutorIndex]
            val viewGroup: ViewGroup? = tooltipObject.scrollView
            if (viewGroup != null) {
                val viewToFocus: View? = tooltipObject.view
                hasViewGroupHandled = if (viewToFocus != null) {
                    hideLayout()
                    viewGroup.post {
                        if (viewGroup is ScrollView) {
                            val relativeLocation = IntArray(2)
                            TooltipViewHelper.getRelativePositionRec(
                                viewToFocus,
                                viewGroup,
                                relativeLocation
                            )
                            viewGroup.smoothScrollTo(0, relativeLocation[1])
                            viewGroup.postDelayed(
                                {
                                    showLayout(activity, fm, tooltipObject)
                                },
                                delayScrolling.toLong()
                            )
                        } else if (viewGroup is NestedScrollView) {
                            val relativeLocation = IntArray(2)
                            TooltipViewHelper.getRelativePositionRec(
                                viewToFocus,
                                viewGroup,
                                relativeLocation
                            )
                            viewGroup.smoothScrollTo(0, relativeLocation[1])
                            viewGroup.postDelayed(
                                {
                                    showLayout(activity, fm, tooltipObject)
                                },
                                delayScrolling.toLong()
                            )
                        }
                    }
                    true
                } else {
                    false
                }
            }
            if (!hasViewGroupHandled) {
                showLayout(activity, fm, tutorsList!![currentTutorIndex])
            }
        } catch (e: Exception) {
            // to Handle the unknown exception.
            // Since this only for first guide, if any error appears, just don't show the guide
            Log.e(logTag, e.stackTraceToString())
            try {
                this@TooltipDialog.dismiss()
            } catch (e2: Exception) {
                // no op
                Log.e(logTag, e2.stackTraceToString())
            }
        }
    }

    private fun showLayout(activity: Activity?, fm: FragmentManager?, tooltipObject: TooltipObject) {
        if (activity == null || activity.isFinishing) {
            return
        }

        //val fm: FragmentManager? = childFragmentManager
        if (!isVisible) {
            try {
                if (fm != null) {
                    if (!isAdded) {
                        show(fm, logTag)
                    } else if (isHidden) {
                        val ft: FragmentTransaction = fm.beginTransaction()
                        ft.show(this@TooltipDialog)
                        ft.commit()
                    }
                }
            } catch (e: IllegalStateException) {
                // called in illegal state. just return.
                return
            }
        }
        val view: View? = tooltipObject.view
        val title: String? = tooltipObject.title
        val text: String? = tooltipObject.text
        val tooltipContentPosition: TooltipContentPosition =
            tooltipObject.tooltipContentPosition
        val tintBackgroundColor: Int = tooltipObject.tintBackgroundColor
        val location: IntArray? = tooltipObject.getLocation()
        val radius: Int = tooltipObject.getRadius()
        if (view == null) {
            layoutShowTutorial(
                null, title, text, tooltipContentPosition,
                tintBackgroundColor, location, radius
            )
        } else {
            view.post(Runnable {
                layoutShowTutorial(
                    view, title, text, tooltipContentPosition,
                    tintBackgroundColor, location, radius
                )
            })
        }
    }

    private fun hideLayout() {
        val layout: TooltipLayout = this@TooltipDialog.view as TooltipLayout
        layout.hideTutorial()
    }

    private fun layoutShowTutorial(
        view: View?,
        title: String?,
        text: String?,
        showCaseContentPosition: TooltipContentPosition,
        tintBackgroundColor: Int,
        customTarget: IntArray?,
        radius: Int
    ) {
        try {
            val layout: TooltipLayout = this@TooltipDialog.view as TooltipLayout
            retryCounter = 0
            layout.showTutorial(
                view, title, text, currentTutorIndex, tutorsList!!.size,
                showCaseContentPosition, tintBackgroundColor, customTarget, radius
            )
        } catch (t: Throwable) {
            // do nothing
        }
    }

    fun close() {
        try {
            dismiss()
            val layout: TooltipLayout = this@TooltipDialog.view as TooltipLayout
            layout.closeTutorial()
        } catch (e: Exception) {
            Log.e(logTag, e.stackTraceToString())
        }
    }

}