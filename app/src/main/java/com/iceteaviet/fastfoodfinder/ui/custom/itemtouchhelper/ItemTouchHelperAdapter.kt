package com.iceteaviet.fastfoodfinder.ui.custom.itemtouchhelper

/**
 * Created by MyPC on 11/17/2016.
 */
interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}
