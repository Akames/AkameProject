package com.akame.akameproject.fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.akame.akameproject.MainActivity
import com.akame.akameproject.R
import com.akame.akameproject.databinding.ActivityMyFragmentBinding
import com.akame.akameproject.text.TextViewActivity

class MyFragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyFragmentBinding
    private var isShowOne = true
    private val f1 = OneFragment()
    private val f2 = TowFragment()
    private var lastFragment: Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvChange.setOnClickListener {
            changeFragment()
        }
        binding.tvChange.callOnClick()
        binding.tv02.setOnClickListener {
            startActivity(Intent(this, TextViewActivity::class.java))
        }
    }

    private fun changeFragment() {
        if (isShowOne) {
            showFragment("f1", f1)
        } else {
            showFragment("f2", f2)
        }
        isShowOne = !isShowOne
    }

    private fun showFragment(fmTag: String, showFragment: Fragment) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        lastFragment?.let { beginTransaction.hide(it) }
        val fragment = supportFragmentManager.findFragmentByTag(fmTag)
        if (fragment == null) {
            beginTransaction.add(R.id.fl_fragment, showFragment, fmTag)
        } else {
            beginTransaction.show(fragment)
        }
        lastFragment = showFragment
        beginTransaction.commit()
//        beginTransaction.commitNow()
//        beginTransaction.commitAllowingStateLoss()
//        beginTransaction.commitNowAllowingStateLoss()
    }
}