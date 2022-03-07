package com.akame.akameproject.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akame.akameproject.R


class TowFragment : Fragment() {
    private val taget = "---TowFragment"
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.e(taget, "---onAttach----")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(taget, "---onCreate----")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(taget, "---onCreateView----")
        return inflater.inflate(R.layout.fragment_tow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(taget, "---onViewCreated----")
    }

    override fun onStart() {
        super.onStart()
        Log.e(taget, "---onStart----")
    }

    override fun onResume() {
        super.onResume()
        Log.e(taget, "---onResume----")
    }

    override fun onPause() {
        super.onPause()
        Log.e(taget, "---onPause----")
    }

    override fun onStop() {
        super.onStop()
        Log.e(taget, "---onStop----")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(taget, "---onDestroy----")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(taget, "---onDestroyView----")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e(taget, "---onDetach----")
    }


}