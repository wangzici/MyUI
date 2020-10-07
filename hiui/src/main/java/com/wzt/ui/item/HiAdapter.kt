package com.wzt.ui.item

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.ParameterizedType

class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataSets = mutableListOf<HiDataItem<*, out RecyclerView.ViewHolder>>()

    //每一个viewType保存第一个item实例，用于createViewHolder时调用
    private var typeArrays = SparseArray<HiDataItem<*, out RecyclerView.ViewHolder>>()

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    fun addItem(index: Int, item: HiDataItem<*,out RecyclerView.ViewHolder>, notify: Boolean) {
        if (index >= 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }
        item.setAdapter(this@HiAdapter)
        val notifyPos = if(index >= 0) index else dataSets.size - 1
        if (notify) {
            notifyItemInserted(notifyPos)
        }
    }

    fun addItems(items: List<HiDataItem<*,out RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        items.forEach {
            dataSets.add(it)
            it.setAdapter(this@HiAdapter)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }
    }

    fun removeItem(index: Int): HiDataItem<*,out RecyclerView.ViewHolder>? {
        if (index >= 0 && index < dataSets.size) {
            val item = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return item
        }
        return null
    }

    fun removeItem(item: HiDataItem<*, *>) {
        val index = dataSets.indexOf(item)
        removeItem(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val item = typeArrays[viewType]
        var view = item.getItemView()
        if (view == null) {
            val layoutRes = item.getLayoutFromRes()
            if (layoutRes < 0) {
                throw RuntimeException("dataItem:${item.javaClass.name} must override getItemView or getLayoutFromRes")
            }
            view = inflater.inflate(item.getLayoutFromRes(), parent, false)
        }
        return createViewHolderInternal(item.javaClass, view)
    }

    /**
     * 根据ViewHolder子类泛型，获取其构造方法并生成实例
     */
    private fun createViewHolderInternal(
        clazz: Class<HiDataItem<*, out RecyclerView.ViewHolder>>,
        view: View?
    ): RecyclerView.ViewHolder {
        val superClass = clazz.genericInterfaces
        if (superClass is ParameterizedType) {
            val arguments = superClass.actualTypeArguments
            for (argument in arguments) {
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(
                        argument
                    )
                ) {
                    return argument.getConstructor(View::class.java)
                        .newInstance(view) as RecyclerView.ViewHolder
                }
            }
        }
        return object : RecyclerView.ViewHolder(view!!) {}
    }

    override fun getItemCount(): Int {
        return dataSets.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSets[position] as HiDataItem<*, RecyclerView.ViewHolder>
        item.onBindData(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataSets[position]
        val type = item.javaClass.hashCode()
        if (typeArrays[type] == null) {
            typeArrays.append(type, item)
        }
        return type
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    if (position < dataSets.size) {
                        return dataSets[position].getSpanSize()
                    }
                    return spanCount
                }

            }
        }
    }

    fun refreshItem(dataItem: HiDataItem<*, *>) {
        val pos = dataSets.indexOf(dataItem)
        notifyItemChanged(pos)
    }

}