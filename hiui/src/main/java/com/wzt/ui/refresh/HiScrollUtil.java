package com.wzt.ui.refresh;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wzt.log.HiLog;

/**
 * Created by Kyrie
 * Date: 2020/7/15
 */
public class HiScrollUtil {

    /**
     * 判断child是否发生了滚动
     *
     * @param child
     * @return true 发生了滚动
     */
    public static boolean childScrolled(@NonNull View child) {
        if (child instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) child;
            if (adapterView.getFirstVisiblePosition() != 0
                    || adapterView.getFirstVisiblePosition() == 0 && adapterView.getChildAt(0) != null
                    && adapterView.getChildAt(0).getTop() < 0) {
                return true;
            }
        } else if (child.getScrollY() > 0) {
            return true;
        }
        if (child instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) child;
            View view = recyclerView.getChildAt(0);
            int firstPosition = recyclerView.getChildAdapterPosition(view);
//            HiLog.d("----:top", view.getTop() + "");
            return firstPosition != 0 || view.getTop() != 0;
        }
        return false;
    }

    /**
     * 查找可以滚动的child
     * @return 可滚动的child，最多查找两层，若未找到则直接返回包含的第一个view（除去head）
     */
    public static View findScrollableChild(@NonNull ViewGroup viewGroup) {
        View child = viewGroup.getChildAt(1);
        if (child instanceof RecyclerView || child instanceof AdapterView) {
            return child;
        }
        if (child instanceof ViewGroup) {//最多往下多找一层
            View temp = ((ViewGroup) child).getChildAt(0);
            if (temp instanceof RecyclerView || temp instanceof AdapterView) {
                child = temp;
            }
        }
        return child;
    }
}
