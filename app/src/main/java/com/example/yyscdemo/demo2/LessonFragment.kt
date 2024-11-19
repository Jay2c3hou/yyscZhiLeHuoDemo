package com.example.yyscdemo.demo2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.yyscdemo.R
import com.example.yyscdemo.databinding.FragmentContentBinding

private const val ARG_LEXILE = "arg_lexile"
private const val ARG_TYPE_ID = "arg_type_id"

class LessonFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var _binding: FragmentContentBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_LEXILE)
            param2 = it.getString(ARG_TYPE_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initUI()
    }

    private fun initUI() {
    }

    private fun initData() {
    }

    companion object {
        @JvmStatic
        fun newInstance(lexile: Int, typeId: Int) = LessonFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_LEXILE, lexile)
                putInt(ARG_TYPE_ID, typeId)
            }
        }
    }
}