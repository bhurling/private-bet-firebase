package io.bhurling.privatebet.ui

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> RecyclerView.Adapter<*>.diffableList(
        compareId: (T, T) -> Boolean,
        compareContent: (T, T) -> Boolean
): ReadWriteProperty<Any?, List<T>> = object : ObservableProperty<List<T>>(emptyList()) {
    override fun afterChange(property: KProperty<*>, oldValue: List<T>, newValue: List<T>) {
        autoNotify(oldValue, newValue, compareId, compareContent)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
        super.setValue(thisRef, property, value.map { it })
    }

    private fun autoNotify(
            old: List<T>,
            new: List<T>,
            compareId: (T, T) -> Boolean,
            compareContent: (T, T) -> Boolean
    ) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compareId(old[oldItemPosition], new[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compareContent(old[oldItemPosition], new[newItemPosition])
            }

            override fun getOldListSize() = old.size

            override fun getNewListSize() = new.size
        })

        diff.dispatchUpdatesTo(this@diffableList)
    }
}