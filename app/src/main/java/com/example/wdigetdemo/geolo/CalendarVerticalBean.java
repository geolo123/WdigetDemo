package com.example.wdigetdemo.geolo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CalendarVerticalBean {

    public List<CalendarItemBean> itemBeanList = new ArrayList<>();
    public float mRadius = 0;
    private Paint finishPaint, textPaint, toDoPaint;


    public CalendarVerticalBean(CalendarItemBean newItem) {
        itemBeanList.add(newItem);
    }

    /**
     * 是否可以加入？
     * false: 不能加入
     * true: 允许加入
     */
    public boolean hasApplyToJoin(CalendarItemBean otherItem) {
        for (CalendarItemBean item : itemBeanList) {
            if (item.isCollision(otherItem)) return false;
        }
        itemBeanList.add(otherItem);
        return true;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }

    public void setToDoPaint(Paint toDoPaint) {
        this.toDoPaint = toDoPaint;
    }


    /**
     * X轴和宽度，外部自己能够计算，这边就算需要的高度空间。
     */
    public void onDraw(Canvas canvas, RectF parentRectF) {
        for (CalendarItemBean item : itemBeanList) {
            RectF tempRectF = item.calculateRectF(parentRectF);
            canvas.drawRoundRect(tempRectF, mRadius, mRadius, toDoPaint);
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CalendarItemBean item : itemBeanList) {
            sb.append(item.toString()).append("; ");
        }
        return sb.toString();
    }
}
