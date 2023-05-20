package com.example.wdigetdemo.geolo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wdigetdemo.databinding.ActivityCalendarBinding;

import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        List<CalendarItemBean> itemBeanList = new ArrayList<>();
        itemBeanList.add(new CalendarItemBean("aaa",100, 150));
        itemBeanList.add(new CalendarItemBean("bbb",250, 500));
        itemBeanList.add(new CalendarItemBean("cccccc",300, 550));
        itemBeanList.add(new CalendarItemBean("ddd",400, 600));
        itemBeanList.add(new CalendarItemBean("eee",550, 750));
        itemBeanList.add(new CalendarItemBean("fff",600, 800));
        itemBeanList.add(new CalendarItemBean("ggg",650, 900));
        itemBeanList.add(new CalendarItemBean("hhh",250, 800));
        itemBeanList.add(new CalendarItemBean("iii",250, 800));
        itemBeanList.add(new CalendarItemBean("jjj",250, 800));
        itemBeanList.add(new CalendarItemBean("kkkkkk",250, 800));
        itemBeanList.add(new CalendarItemBean("llllll",250, 1800));
        itemBeanList.add(new CalendarItemBean("mmmmmm",550, 1800));
        itemBeanList.add(new CalendarItemBean("nnnnnn",850, 2300));

        viewBinding.calendarView.setCalendarItemBeanList(itemBeanList);
    }
}
