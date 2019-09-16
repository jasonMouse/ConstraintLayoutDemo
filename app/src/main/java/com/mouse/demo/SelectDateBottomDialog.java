package com.mouse.demo;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.offcn.calendarview.CalendarLayout;
import com.offcn.calendarview.CalendarView;

/**
 * 日期选择对话框
 */
public class SelectDateBottomDialog {
    private Context context;                                                        // 上下文对象
    private Dialog dateDialog;                                                      // 对话框对象
    private ImageView ivLastMonth;                                                  // 回退到上一个月
    private TextView tvCurMonth;                                                    // 当前选择的年月展示
    private ImageView ivNextMonth;                                                  // 跳转到下一个月
    private ImageView ivClose;                                                      // 关闭按钮
    private CalendarLayout calendarLayout;                                          // 日历外层布局
    private CalendarView calendarView;                                              // 日历展示器
    private View.OnClickListener clickListener;                                     // 点击事件监听器
    private CalendarView.OnCalendarSelectListener calendarSelectListener;           // 日历选择监听器
    private CalendarView.OnMonthChangeListener onMonthChangeListener;               // 月份变化监听器
    private String strSelectDateBottomDate;                                         // XX年XX月

    private int minYear;                                                            // 日期范围：最小年份
    private int minYearMonth;                                                       // 日期范围：最小月份
    private int minYearDay;                                                         // 日期范围：最小日份
    private int maxYear;                                                            // 日期范围：最大年份
    private int maxYearMonth;                                                       // 日期范围：最大月份
    private int maxYearDay;                                                         // 日期范围：最大日份
    private boolean rangeEnable;                                                    // 日期范围是否启用了

    public SelectDateBottomDialog(Context context) {
        this.context = context;
        this.strSelectDateBottomDate = context.getResources().getString(R.string.str_select_bottom_date);
        initDialog();
    }

    private void initDialog() {
        if (dateDialog == null) {
            this.dateDialog = new Dialog(context, R.style.practice_bottom_dialog);
            dateDialog.setContentView(R.layout.layout_select_date_bottom_dialog);
            dateDialog.setCanceledOnTouchOutside(true);
            dateDialog.setCancelable(true);
            Window window = dateDialog.getWindow();
            window.setWindowAnimations(R.style.bottom_dialog_anim_style);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawableResource(android.R.color.transparent);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;// (948 PX / 1068 PX)
            window.setAttributes(layoutParams);
        }
        ivLastMonth = (ImageView) dateDialog.findViewById(R.id.ivLastMonth);
        tvCurMonth = (TextView) dateDialog.findViewById(R.id.tvCurMonth);
        ivNextMonth = (ImageView) dateDialog.findViewById(R.id.ivNextMonth);
        ivClose = (ImageView) dateDialog.findViewById(R.id.ivClose);
        calendarLayout = (CalendarLayout) dateDialog.findViewById(R.id.calendarLayout);
        calendarView = (CalendarView) dateDialog.findViewById(R.id.calendarView);
        // 设置点击事件监听器
        if (clickListener == null) {
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()){
                        // 上一个月
                        case R.id.ivLastMonth:
                            calendarView.scrollToPre();
                            break;
                        // 下一个月
                        case R.id.ivNextMonth:
                            calendarView.scrollToNext();
                            break;
                        // 关闭对话框
                        case R.id.ivClose:
                            dismissDialog();
                            break;
                    }
                }
            };
        }
        ivLastMonth.setOnClickListener(clickListener);
        ivNextMonth.setOnClickListener(clickListener);
        ivClose.setOnClickListener(clickListener);
        // 设置日期选择监听器
        if (calendarSelectListener != null) {
            calendarView.setOnCalendarSelectListener(calendarSelectListener);
        }
        // 设置月份变化监听器
        if (onMonthChangeListener == null) {
            onMonthChangeListener = new CalendarView.OnMonthChangeListener() {
                @Override
                public void onMonthChange(int year, int month) {
                    tvCurMonth.setText(String.format(strSelectDateBottomDate, year, month));
                    if (rangeEnable) {
                        // 上一个月按钮是否可用的标识
                        boolean minFlag = false;
                        if (year > minYear) {
                            minFlag = true;
                        } else if (year == minYear) {
                            if (month > minYearMonth) {
                                minFlag = true;
                            }
                        }
                        // 下一个月按钮是否可用的标识
                        boolean maxFlag = false;
                        if (year < maxYear) {
                            maxFlag = true;
                        } else if (year == maxYear) {
                            if (month < maxYearMonth) {
                                maxFlag = true;
                            }
                        }
                        ivLastMonth.setEnabled(minFlag);
                        ivNextMonth.setEnabled(maxFlag);
                    }
                    else{
                        ivLastMonth.setEnabled(true);
                        ivNextMonth.setEnabled(true);
                    }
                }
            };
        }
        calendarView.setOnMonthChangeListener(onMonthChangeListener);
        // 设置区域范围
        if (minYear > 0 && maxYear > 0) {
            setRange(minYear, minYearMonth, minYearDay, maxYear, maxYearMonth, maxYearDay);
        }
    }

    // 根据年月日修改选择时间点
    private void setDialogData(int year, int month, int day) {
        calendarView.setSelectStartCalendar(year, month, day);
        calendarView.scrollToCalendar(year, month, day);
        tvCurMonth.setText(String.format(strSelectDateBottomDate, year, month));
    }

    // 执行对话框的展示
    public void showDialog(int year, int month, int day) {
        // 执行判空操作
        if (dateDialog != null) {
            if (!dateDialog.isShowing()) {
                // 设置对话框的数据
                setDialogData(year, month, day);
                // 展示对话框
                dateDialog.show();
            }
        } else {
            // 初始化对话框
            initDialog();
            // 设置对话框的数据
            setDialogData(year, month, day);
            // 展示对话框
            dateDialog.show();
        }
    }

    // 关闭对话框
    public void dismissDialog() {
        if (dateDialog != null && dateDialog.isShowing()) {
            dateDialog.dismiss();
        }
    }

    // 当前是否正在展示中
    public boolean isShowing() {
        return this.dateDialog != null && dateDialog.isShowing();
    }

    // 设置可选择时间的区域范围
    public void setRange(int minYear, int minYearMonth, int minYearDay, int maxYear, int maxYearMonth, int maxYearDay) {
        this.rangeEnable = false;
        // 确定有效范围
        if (minYear <= 1970 || minYearMonth <= 0 || minYearMonth > 12 || minYearDay <= 0 || minYearDay > 31 || maxYear <= 1970 || maxYearMonth <= 0 || maxYearMonth > 12 || maxYearDay <= 0 || maxYearDay > 31) return;
        if (minYear < maxYear) {
            setRangValue(minYear, minYearMonth, minYearDay, maxYear, maxYearMonth, maxYearDay);
        } else if (minYear == maxYear) {
            if (minYearMonth < maxYearMonth) {
                setRangValue(minYear, minYearMonth, minYearDay, maxYear, maxYearMonth, maxYearDay);
            } else if (minYearMonth == maxYearMonth) {
                if (minYearDay <= maxYearDay) {
                    setRangValue(minYear, minYearMonth, minYearDay, maxYear, maxYearMonth, maxYearDay);
                }
            }
        }
    }

    // 将具体值设置到具体的项目上去
    private void setRangValue(int minYear, int minYearMonth, int minYearDay, int maxYear, int maxYearMonth, int maxYearDay){
        this.rangeEnable = true;
        this.minYear = minYear;
        this.minYearMonth = minYearMonth;
        this.minYearDay = minYearDay;
        this.maxYear = maxYear;
        this.maxYearMonth = maxYearMonth;
        this.maxYearDay = maxYearDay;
        this.calendarView.setRange(minYear, minYearMonth, minYearDay, maxYear, maxYearMonth, maxYearDay);
    }

    // 设置选择监听器
    public void setCalendarSelectListener(CalendarView.OnCalendarSelectListener selectListener) {
        this.calendarSelectListener = selectListener;
        calendarView.setOnCalendarSelectListener(selectListener);
    }
}
