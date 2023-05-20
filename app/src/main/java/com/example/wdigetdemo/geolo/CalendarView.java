package com.example.wdigetdemo.geolo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.wdigetdemo.R;

import java.util.ArrayList;
import java.util.List;

public class CalendarView extends View {
    private Paint finishPaint, textPaint, toDoPaint;
    private int height, viewWidth;
    private RectF parentRectF = new RectF();
    private float defaultFontSize = 0;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        defaultFontSize = context.getResources().getDimension(R.dimen.sp_12);
        finishPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        toDoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        finishPaint.setColor(Color.GREEN);
        textPaint.setColor(Color.BLACK);
        toDoPaint.setColor(Color.BLUE);
        finishPaint.setStyle(Paint.Style.FILL);
        finishPaint.setStrokeWidth(2f);
        toDoPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(defaultFontSize);
        parentRectF.left = 0;
        parentRectF.right = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (itemWidth <= 0) {
            int listSize = verticalBeanList.size();
            itemWidth = (viewWidth - listSize * mInterval) / listSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = 0;
        for (CalendarVerticalBean verticalBean : verticalBeanList) {
            canvas.save();//保存
            verticalBean.setRadius(mRadius);
            verticalBean.setTextPaint(textPaint);
            verticalBean.setToDoPaint(i % 2 == 0 ? toDoPaint : finishPaint);
            verticalBean.setDefaultFontSize(defaultFontSize);
            parentRectF.left = parentRectF.right + mInterval;
            parentRectF.right = parentRectF.left + itemWidth;
            verticalBean.onDraw(canvas, parentRectF);
            canvas.restore();//恢复
            i++;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<CalendarVerticalBean> verticalBeanList = new ArrayList<>();
    private final float mRadius = this.getResources().getDimension(R.dimen.dp_3);
    private float itemWidth = 0;
    private final float mInterval = 20;

    public void setCalendarItemBeanList(List<CalendarItemBean> itemBeanList) {
        mergeItemBean(itemBeanList);
        for (CalendarVerticalBean verticalBean : verticalBeanList) {
            Log.e("geolo", "--> " + verticalBean);
        }
        invalidate();
    }

    private boolean hasApplyToJoinVertical(CalendarItemBean otherItem) {
        for (CalendarVerticalBean verticalBean : verticalBeanList) {
            if (verticalBean.hasApplyToJoin(otherItem)) return true;
        }
        return false;
    }

    private void mergeItemBean(List<CalendarItemBean> itemBeanList) {
        for (CalendarItemBean item : itemBeanList) {
            if (verticalBeanList.isEmpty()) {
                verticalBeanList.add(new CalendarVerticalBean(item));
            } else {
                if (!hasApplyToJoinVertical(item)) verticalBeanList.add(new CalendarVerticalBean(item));
            }
        }
    }
}
