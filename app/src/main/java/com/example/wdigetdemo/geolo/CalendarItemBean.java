package com.example.wdigetdemo.geolo;

import android.graphics.RectF;

public class CalendarItemBean {
    public float startVirtualY = 0;
    public float endVirtualY = 0;

    public CalendarItemBean(int startVirtualY, int endVirtualY) {
        this.startVirtualY = startVirtualY;
        this.endVirtualY = endVirtualY;
    }

    /**
     * 是否发生碰撞
     */
    public boolean isCollision(CalendarItemBean otherItem) {
        return (this.startVirtualY > otherItem.startVirtualY && this.startVirtualY < otherItem.endVirtualY)
                || (otherItem.startVirtualY > this.startVirtualY && otherItem.startVirtualY < this.endVirtualY);
    }

    /**
     * X轴和宽度，外部自己能够计算，这边就算需要的高度空间。
     */
    public RectF calculateRectF(RectF parentRectF) {
        parentRectF.top = startVirtualY;
        parentRectF.bottom = endVirtualY;
        return parentRectF;
    }
}
