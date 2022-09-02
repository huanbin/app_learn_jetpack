package com.hb.jetpack_compose.ui.setting


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import com.hb.jetpack_compose.ui.BaseFragment

class SettingFragment : BaseFragment() {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun createView() {
        LazyColumn(content = {
            items(10,itemContent = {
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