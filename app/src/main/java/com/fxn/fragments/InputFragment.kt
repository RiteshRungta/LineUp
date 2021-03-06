package com.fxn.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fxn.lineup.R
import com.fxn.models.TextItem
import com.fxn.utilities.CustomTypefaceSpan
import com.fxn.utilities.Utility
import kotlinx.android.synthetic.main.fragment_input.view.*
import java.text.SimpleDateFormat
import java.util.*

class InputFragment : Fragment(), TextWatcher {

    private var mParam1: TextItem? = null
    private var mParam2: String? = null
    var text: String? = ""

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: TextItem, param2: String): InputFragment {
            val fragment = InputFragment()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getSerializable(ARG_PARAM1) as TextItem?
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_input, container, false)
        text = mParam1!!.title
        view.main_input.setText(text)
        view.main_input.addTextChangedListener(this)
        var s: String = SimpleDateFormat("MMM dd HH:mm:aa").format(Date()).toString()
        var str: Spannable = Spannable.Factory.getInstance().newSpannable(s)
        str.setSpan(CustomTypefaceSpan(ResourcesCompat.getFont(activity!!.applicationContext, R.font.quicksand_bold)),
                0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.back_date.setText(str, TextView.BufferType.SPANNABLE)
        view.back.setOnClickListener { activity!!.onBackPressed() }
        //  view.viewPager.adapter = ColorAdapter(fragmentManager!!, 5)

        view.main_input.setOnTouchListener { view, motionEvent -> false }
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (mParam1 == null && text!!.isNotEmpty()) {
            TextItem(text!!, Utility.getDateTimeString(), 1).save()
            return
        } else if (text!!.isEmpty() && mParam1 != null && mParam1!!.title.isNotEmpty()) {
            mParam1!!.delete()
            return
        } else if (text!!.isEmpty()) {
            return
        }
        if (mParam1!!.title != text) {
            mParam1!!.date = Utility.getDateTimeString()
        }
        mParam1!!.color = 0
        mParam1!!.title = "" + text

        mParam1!!.save()
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        text = p0.toString()
    }

}
