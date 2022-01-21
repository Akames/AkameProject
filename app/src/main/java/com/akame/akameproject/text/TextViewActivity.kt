package com.akame.akameproject.text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.akame.akameproject.databinding.ActivityTextViewBinding

class TextViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewList = arrayListOf<MyTextView>()
        viewList.add(binding.tv01)
        viewList.add(binding.tv02)
        viewList.add(binding.tv03)
        binding.tv01.setProcess(1f)
        binding.vpContent.adapter = MyViewpagerAdapter(supportFragmentManager)
        binding.vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                viewList[position].setProcess(positionOffset, true)
                val nextPosition = position + 1
                if (nextPosition < viewList.size) {
                    viewList[nextPosition].setProcess(positionOffset)
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}