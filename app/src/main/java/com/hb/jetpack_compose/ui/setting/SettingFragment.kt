package com.hb.jetpack_compose.ui.setting


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import com.hb.jetpack_compose.ui.BaseFragment

class SettingFragment : BaseFragment() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createComposeView {
            LazyColumn(content = {
                items(10, itemContent = {
                    ListItem(
                        icon = {
                            Icon(Icons.Filled.Face, contentDescription = "")
                        },
                        secondaryText = {
                            Text(text = "secondaryText")
                        },
                        overlineText = {
                            Text(text = "overlineText")
                        },
                        trailing = {
                            Switch(false, onCheckedChange = {

                            })
                        },
                        text = {
                            Text(text = "main text,main text,main text,main text。")
                        }
                    )
                })
            })
        }
    }
}