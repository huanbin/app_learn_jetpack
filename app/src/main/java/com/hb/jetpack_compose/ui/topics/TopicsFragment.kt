package com.hb.jetpack_compose.ui.topics

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hb.jetpack_compose.databinding.FragmentGalleryBinding

class TopicsFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val topicsViewModel =
            ViewModelProvider(this).get(TopicsViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        topicsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.button3.setOnClickListener {
//            showOrHideSoftKeyboard()
//            showOrHideSystembars()
            lightOrDarkStatusbar()
        }
        return root
    }

    private fun lightOrDarkStatusbar() {
        //获取WindowInsetsController
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window,
            requireActivity().window.decorView
        )
        //没有效果
        insetsController.isAppearanceLightStatusBars=!insetsController.isAppearanceLightStatusBars
        insetsController.isAppearanceLightNavigationBars=!insetsController.isAppearanceLightNavigationBars
    }

    //曾经的沉浸式状态栏，如此简单
    private fun showOrHideSystembars() {
        //获取WindowInsetsController
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window,
            binding.editTextTextPersonName
        )
        //设置在sytembar隐藏之后，如何再次滑出systembar
//        用户在sytembar边缘swipe滑动就会出现systembar
//        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
//        用户只要点击屏幕就会出现sytembar
//        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
//        用户在systembar边缘swipe滑动就会出现systembar，只不过显示一会之后会立即隐藏systembar
        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun showOrHideSoftKeyboard() {
        //获取WindowInsetsController
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window,
            binding.editTextTextPersonName
        )
        //判断键盘是否弹出,任何view都可以
        //val rootWindowInsets =ViewCompat.getRootWindowInsets(binding.button3)
        val rootWindowInsets = ViewCompat.getRootWindowInsets(binding.editTextTextPersonName)
        var showSoftKeyboard = rootWindowInsets?.isVisible(WindowInsetsCompat.Type.ime()) ?: false
        Toast.makeText(context, if (showSoftKeyboard) "键盘打开了" else "键盘关闭了", Toast.LENGTH_SHORT)
            .show()
        if (showSoftKeyboard) {
            insetsController.hide(WindowInsetsCompat.Type.ime())
        } else {
            insetsController.show(WindowInsetsCompat.Type.ime())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}