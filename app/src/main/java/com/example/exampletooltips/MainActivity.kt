package com.example.exampletooltips

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.exampletooltips.R.*
import com.example.exampletooltips.model.TooltipContentPosition
import com.example.exampletooltips.model.TooltipDialog
import com.example.exampletooltips.model.TooltipObject
import com.example.exampletooltips.model.TooltipBuilder

class MainActivity : AppCompatActivity() {

    private var tooltipDialog: TooltipDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        initUglyTooltip()

        startUglyTooltips()

        val buttonClick = findViewById<Button>(id.btn_pindah)
        buttonClick.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initUglyTooltip() {
        tooltipDialog = TooltipBuilder()
            .setPackageName(packageName)
            .titleTextColorRes(color.white)
            .textColorRes(color.white)
            .shadowColorRes(color.shadow)
            .titleTextSizeRes(dimen.title_size)
            .textSizeRes(dimen.text_normal)
            .spacingRes(dimen.spacing_normal)
            .backgroundContentColorRes(color.darker_gray)
            .circleIndicatorBackgroundDrawableRes(drawable.selector_circle)
            .prevString(string.previous)
            .nextString(nextStringText = "Lanjut")
            .finishString(finishStringText = "Selesai")
            .nextTextColorRes(color.green)
            .finishTextColorRes(color.blue)
            .useCircleIndicator(true)
            .showBottomContainer(true)
            .clickable(true)
            .useArrow(true)
            .useSkipWord(false)
            .setFragmentManager(this.supportFragmentManager)
            .lineColorRes(color.line_color)
            .lineWidthRes(dimen.line_width)
            .shouldShowIcons(false)
            .setTooltipRadius(dimen.tooltip_radius)
            .showSpotlight(true)
            .build()
    }

    private fun startUglyTooltips() {
        val tooltips: ArrayList<TooltipObject> = ArrayList()

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(id.iv3),
                null,
                "No title, just description, simple text."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(id.tvTest2),
                null,
                "No title, just description, simple text.",
                tooltipContentPosition = TooltipContentPosition.RIGHT
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(id.tvTest3),
                "<font color=\"#FFC300\"> an ImageView </font>",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                tooltipContentPosition = TooltipContentPosition.LEFT
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(id.iv4),
                "This is a title",
                "This is a description, but a little longer than number 3 but shorter than number 5 that you will see soon."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(id.tvTest),
                "This is a title",
                "This is a description, but a little longer than number 3 but shorter than number 5 that you will see soon."
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(id.iv5),
                "This is another title",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suo enim quisque studio maxime ducitur. Scio enim esse quosdam, qui quavis lingua philosophari possint; Animum autem reliquis rebus ita perfecit, ut corpus; Quo modo autem optimum, si bonum praeterea nullum est?"
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<ImageView>(id.iv6),
                "This is another one",
                "This description point to number 6. <font color=\"#FFC300\"> This is yellow text </font> and this is white.",
                tooltipContentPosition = TooltipContentPosition.UNDEFINED,
                tintBackgroundColor = ResourcesCompat.getColor(resources,   color.blue, null),
                null
            )
        )

        tooltips.add(
            TooltipObject(
                findViewById<TextView>(id.tvTest4),
                "<font color=\"#FFC300\"> an ImageView </font>",
                "This HTML description point to <font color=\"#FFC300\"> an ImageView </font> as you can see.<br/><br/> Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                tooltipContentPosition = TooltipContentPosition.LEFT
            )
        )

        tooltipDialog?.show(this, supportFragmentManager, "SHOWCASE_TAG", tooltips)

    }
}