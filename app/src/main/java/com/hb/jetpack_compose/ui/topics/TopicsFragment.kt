package com.hb.jetpack_compose.ui.topics

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.hb.jetpack_compose.databinding.FragmentGalleryBinding
import com.hb.jetpack_compose.ui.BaseFragment

class TopicsFragment : BaseFragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val topicsViewModel by activityViewModels<TopicsViewModel>()

        return createComposeView {
            Screen(viewModel = topicsViewModel)
        }
    }

    @OptIn(ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
    @Composable
    fun Screen(viewModel: TopicsViewModel) {
        val pagerState = rememberPagerState()
        val pages by viewModel.pages.collectAsStateWithLifecycle()
        var selectedIndex by remember {
            mutableStateOf(pagerState.currentPage)
        }

        LaunchedEffect(selectedIndex) {
            //pagerState.animateScrollToPage(selectedIndex)
            pagerState.scrollToPage(selectedIndex)
        }

        //监听pagerState状态，当用户滑动页面，更新selectedIndex，避免用户滑动之后无法再次选中上次的tab
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect {
                selectedIndex = it
            }
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )
        ) {
            ScrollableTabRow(
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }) {
                // Add tabs for all of our pages
                pages.forEachIndexed { index, tab ->
                    Tab(
                        text = {
                            Text(text = tab.name, fontSize = 26.sp)
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            selectedIndex = index
                        },
                    )
                }
            }

            HorizontalPager(
                count = pages.size,
                state = pagerState,
            ) { page ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                ) {
                    Text(text = "card  ${pages[page]}")
                }
            }
        }
    }

    private fun lightOrDarkStatusbar() {
        //获取WindowInsetsController
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window, requireActivity().window.decorView
        )
        //没有效果
        insetsController.isAppearanceLightStatusBars = !insetsController.isAppearanceLightStatusBars
        insetsController.isAppearanceLightNavigationBars =
            !insetsController.isAppearanceLightNavigationBars
    }

    //曾经的沉浸式状态栏，如此简单
    private fun showOrHideSystembars() {
        //获取WindowInsetsController
        val insetsController = WindowCompat.getInsetsController(
            requireActivity().window, binding.editTextTextPersonName
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
            requireActivity().window, binding.editTextTextPersonName
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
}