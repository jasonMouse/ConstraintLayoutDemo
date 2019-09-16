package com.mouse.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.offcn.calendarview.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDate;
    private Activity context;
    private SelectDateBottomDialog selectDateDialog;

    private int lastSelectedYear;
    private int lastSelectedMonth;
    private int lastSelectedDay;
    private String strSelectDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = MainActivity.this;
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvDate = this.findViewById(R.id.tvDate);
    }

    private void initData() {
        selectDateDialog = new SelectDateBottomDialog(context);
        strSelectDate = context.getResources().getString(R.string.str_select_date);

        Calendar calendar = Calendar.getInstance();
        lastSelectedYear = calendar.get(Calendar.YEAR);
        lastSelectedMonth = calendar.get(Calendar.MONTH) + 1;
        lastSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        tvDate.setText(String.format(strSelectDate, lastSelectedYear, lastSelectedMonth, lastSelectedDay));
        // 动态设置区域范围
        selectDateDialog.setRange(2019, 8, 8, 2019, 10, 10);
    }

    private void initListener() {
        tvDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvDate:
                selectDateDialog.showDialog(lastSelectedYear, lastSelectedMonth, lastSelectedDay);
                selectDateDialog.setCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
                    @Override
                    public void onCalendarOutOfRange(com.offcn.calendarview.Calendar calendar) {

                    }

                    @Override
                    public void onCalendarSelect(com.offcn.calendarview.Calendar calendar, boolean isClick) {
                        if (isClick) {
                            lastSelectedYear = calendar.getYear();
                            lastSelectedMonth = calendar.getMonth();
                            lastSelectedDay = calendar.getDay();
                            // 刷新日期显示
                            tvDate.setText(String.format(strSelectDate,lastSelectedYear,lastSelectedMonth,lastSelectedDay));
                            // 隐藏对话框
                            selectDateDialog.dismissDialog();
                        }
                    }
                });
                break;
        }
    }
}
