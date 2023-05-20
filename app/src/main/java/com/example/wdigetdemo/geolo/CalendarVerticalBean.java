package com.example.wdigetdemo.geolo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;

import com.example.wdigetdemo.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CalendarVerticalBean {

    public List<CalendarItemBean> itemBeanList = new ArrayList<>();
    public float mRadius = 0;
    private Paint finishPaint, textPaint, toDoPaint;
    private float defaultFontSize = 0;

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

    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
    }

    public void setToDoPaint(Paint toDoPaint) {
        this.toDoPaint = toDoPaint;
    }

    public void setDefaultFontSize(float fontSize) {
        this.defaultFontSize = fontSize;
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

    /**
     * X轴和宽度，外部自己能够计算，这边就算需要的高度空间。
     */
    public void onDraw(Canvas canvas, RectF parentRectF) {

        for (CalendarItemBean item : itemBeanList) {
            RectF tempRectF = item.calculateRectF(parentRectF);
            canvas.drawRoundRect(tempRectF, mRadius, mRadius, toDoPaint);
            drawText(canvas, item.getTitle(), tempRectF);
        }
    }


    private void drawText(Canvas canvas, String text, RectF parentRectF) {
        float itemWidth = parentRectF.right - parentRectF.left;
        int textMaxNum = textPaint.breakText(text, true, itemWidth, null);
        textMaxNum = textMaxNum == 0 ? 1 : textMaxNum;
        int lines = BigDecimal.valueOf(text.length() / (float) textMaxNum).setScale(0, RoundingMode.UP).intValue();
        float textHeight = textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent;
        float maxHeight = parentRectF.bottom - parentRectF.top;
        while (maxHeight < lines * textHeight) {
            textPaint.setTextSize(textPaint.getTextSize() - 3.0f);
            textMaxNum = textPaint.breakText(text, true, itemWidth, null);
            textMaxNum = textMaxNum == 0 ? 1 : textMaxNum;
            lines = text.length() / textMaxNum + (text.length() % textMaxNum > 0 ? 1 : 0);
            textHeight = textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent;
        }
        float sY = parentRectF.top + (maxHeight - lines * textHeight) / 2;
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        for (int n = 0; n < lines; n++) {
            int sIndex = n * textMaxNum;
            int eIndex = Math.min((n + 1) * textMaxNum, text.length());
            int textWidth = getTextWidth(textPaint, text.substring(sIndex, eIndex));
            float textX = parentRectF.left + (itemWidth - textWidth) / 2;
            float textY = sY - textPaint.getFontMetrics().descent + (n + 1) * textHeight;
            canvas.drawText(text.substring(sIndex, eIndex), textX, textY, textPaint);
        }
        textPaint.setTextSize(this.defaultFontSize);
    }

    public static int getTextWidth(Paint paint, String str) {
        int w = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                w += (int) Math.ceil(widths[j]);
            }
        }
        return w;
    }
}
