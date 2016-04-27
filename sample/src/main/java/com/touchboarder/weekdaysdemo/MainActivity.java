package com.touchboarder.weekdaysdemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;
import com.touchboarder.weekdaysbuttons.WeekdaysDrawableProvider;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements WeekdaysDataSource.Callback {


    private WeekdaysDataSource weekdaysDataSource1;
    private WeekdaysDataSource weekdaysDataSource2;
    private WeekdaysDataSource weekdaysDataSource3;
    private WeekdaysDataSource weekdaysDataSource4;
    private WeekdaysDataSource weekdaysDataSource5;
    private WeekdaysDataSource weekdaysDataSource6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupAppBar();
        ImageView imageView = (ImageView) getAppBar().findViewById(R.id.app_bar_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TouchBoarder/weekdays-buttons-bar"));
                startActivity(browserIntent);
            }
        });

        if (savedInstanceState != null) {
            // Restore the weekdaysDataSource state, save a reference to weekdaysDataSource.

            weekdaysDataSource1 = WeekdaysDataSource.restoreState("wds1", savedInstanceState, this, callback1, null);
            weekdaysDataSource2 = WeekdaysDataSource.restoreState("wds2", savedInstanceState, this, callback2, null);
            weekdaysDataSource3 = WeekdaysDataSource.restoreState("wds3", savedInstanceState, this, callback3, null);
            weekdaysDataSource4 = WeekdaysDataSource.restoreState("wds4", savedInstanceState, this, callback4, drawableListener);
            weekdaysDataSource5 = WeekdaysDataSource.restoreState("wds5", savedInstanceState, this, this, null);
            weekdaysDataSource6 = WeekdaysDataSource.restoreState("wds6", savedInstanceState, this, this, drawableListener);

            TextView textView = (TextView) findViewById(R.id.dialog_result_text);
            if (textView != null && weekdaysDataSource5 != null) {
                textView.setText(getSelectedDaysFromWeekdaysData(weekdaysDataSource5.getWeekdaysItems()));
            }

        } else {
            // No previous state, first creation.
            setupWeekdaysButtons1();
            setupWeekdaysButtons2();
            setupWeekdaysButtons3();
            setupWeekdaysButtons4();
            setupWeekdaysButtons6();
        }
    }

    /**
     * Set up the Caoll
     */
    private void setupAppBar() {

        //Remove the title so we only  display the Image
        if (getCollapsingToolbarLayout() != null) {
            getCollapsingToolbarLayout().setTitle("");
        }
        // Retrieve the Toolbar from our content view, and set it as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("");

        //LANDSCAPE
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Hide the appbar in landscape, it is still possible to scroll it down
            if (getAppBar() != null)
                getAppBar().setExpanded(false);
            setAppBarLayoutHeight(70);
        }
        //PORTRAIT
        else {
            setAppBarLayoutHeight(30);
        }
    }

    /**
     * @param divide Set AppBar height to screen height divided by 2->5
     */
    protected void setAppBarLayoutHeightScreenDivide(@IntRange(from = 2, to = 5) int divide) {
        setAppBarLayoutHeight(100 / divide);
    }

    /**
     * @param percent Set AppBar height to 20->50% of screen height
     */
    protected void setAppBarLayoutHeight(@IntRange(from = 20, to = 80) int percent) {
        setAppBarLayoutHeight(percent / 100F);
    }

    /**
     * @return AppBarLayout
     */
    private AppBarLayout appbar;

    @Nullable
    protected AppBarLayout getAppBar() {
        if (appbar == null) appbar = (AppBarLayout) findViewById(R.id.appbar);
        return appbar;
    }

    /**
     * @param weight Set AppBar height to 0.2->0.5 weight of screen height
     */
    protected void setAppBarLayoutHeight(@FloatRange(from = 0.2F, to = 0.5F) float weight) {
        if (getAppBar() != null) {
            ViewGroup.LayoutParams params = getAppBar().getLayoutParams();
            params.height = Math.round(getResources().getDisplayMetrics().heightPixels * weight);
            getAppBar().setLayoutParams(params);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (weekdaysDataSource1 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource1.saveState("wds1", outState);
        }
        if (weekdaysDataSource2 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource2.saveState("wds2", outState);
        }
        if (weekdaysDataSource3 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource3.saveState("wds3", outState);
        }
        if (weekdaysDataSource4 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource4.saveState("wds4", outState);
        }
        if (weekdaysDataSource5 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource5.saveState("wds5", outState);
        }
        if (weekdaysDataSource6 != null) {
            // If the weekdaysDataSource isn't null, save it's state for restoration in onCreate()
            weekdaysDataSource6.saveState("wds6", outState);
        }
    }

    /**
     * @return CollapsingToolbarLayout collapsingAppBarLayout
     */
    private CollapsingToolbarLayout collapsingAppBarLayout;

    @Nullable
    protected CollapsingToolbarLayout getCollapsingToolbarLayout() {
        if (collapsingAppBarLayout == null)
            collapsingAppBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_appbar);
        return collapsingAppBarLayout;
    }

    public void checkAll(View v) {
        switch (v.getId()) {
            case R.id.check_all_1:
                weekdaysDataSource1.selectAll(((CheckBox) v).isChecked());
                break;
            case R.id.check_all_2:
                weekdaysDataSource2.selectAll(((CheckBox) v).isChecked());
                break;
            case R.id.check_all_3:
                weekdaysDataSource3.selectAll(((CheckBox) v).isChecked());
                break;
            case R.id.check_all_4:
                weekdaysDataSource4.selectAll(((CheckBox) v).isChecked());
                break;
        }
    }

    public void checkFill(View v) {
        switch (v.getId()) {
            case R.id.check_fill_1:
                weekdaysDataSource1.setFillWidth(((CheckBox) v).isChecked());
                break;
            case R.id.check_fill_2:
                weekdaysDataSource2.setFillWidth(((CheckBox) v).isChecked());
                break;
            case R.id.check_fill_3:
                weekdaysDataSource3.setFillWidth(((CheckBox) v).isChecked());
                break;
            case R.id.check_fill_4:
                weekdaysDataSource4.setFillWidth(((CheckBox) v).isChecked());
                break;
        }
    }


    public void setupWeekdaysButtons1() {
        weekdaysDataSource1 = new WeekdaysDataSource(this, R.id.weekdays_stub)
                .start(callback1);
    }

    private WeekdaysDataSource.Callback callback1 = new WeekdaysDataSource.Callback() {
        @Override
        public void onWeekdaysItemClicked(int attachId, WeekdaysDataItem item) {
            CheckBox checkBox = (CheckBox) findViewById(R.id.check_all_1);
            if (checkBox != null) checkBox.setChecked(weekdaysDataSource1.isAllDaysSelected());

            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            if (item.getCalendarDayId() == today && item.isSelected())
                Toast.makeText(MainActivity.this, "Carpe diem", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {
            String selectedDays = getSelectedDaysFromWeekdaysData(items);
            if (!TextUtils.isEmpty(selectedDays))
                showSnackbarShort(selectedDays);
        }
    };

    public void setupWeekdaysButtons2() {
        weekdaysDataSource2 = new WeekdaysDataSource(this, R.id.weekdays_sample_2)
                .setDrawableType(WeekdaysDrawableProvider.MW_ROUND_RECT)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setSelectedDays(Calendar.MONDAY)
                .setFontBaseSize(16)
                .setFontTypeFace(Typeface.MONOSPACE)
                .setUnselectedColorRes(R.color.colorPrimaryLight)
                .setTextColorUnselectedRes(R.color.colorSecondaryText)
                .setNumberOfLetters(3)
                .start(callback2);
    }

    private WeekdaysDataSource.Callback callback2 = new WeekdaysDataSource.Callback() {
        @Override
        public void onWeekdaysItemClicked(int attachId, WeekdaysDataItem item) {
            CheckBox checkBox = (CheckBox) findViewById(R.id.check_all_2);
            if (checkBox != null) checkBox.setChecked(weekdaysDataSource2.isAllDaysSelected());

            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            if (item.getCalendarDayId() == today && item.isSelected())
                Toast.makeText(MainActivity.this, "Carpe diem", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {
            String selectedDays = getSelectedDaysFromWeekdaysData(items);
            if (!TextUtils.isEmpty(selectedDays))
                showSnackbarShort(selectedDays);
        }
    };

    public void setupWeekdaysButtons3() {
        weekdaysDataSource3 = new WeekdaysDataSource(this, R.id.weekdays_sample_3)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setSelectedDays(Calendar.MONDAY, Calendar.WEDNESDAY)
                .setTextColorSelected(Color.WHITE)
                .setFillWidth(false)
                .setTextColorUnselectedRes(R.color.colorPrimaryText)
                .setUnselectedColor(Color.TRANSPARENT)
                .setWeekdayItemLayoutRes(R.layout.weekdays_image_view)
                .setNumberOfLetters(1)
                .start(callback3);
    }

    private WeekdaysDataSource.Callback callback3 = new WeekdaysDataSource.Callback() {
        @Override
        public void onWeekdaysItemClicked(int attachId, WeekdaysDataItem item) {
            CheckBox checkBox = (CheckBox) findViewById(R.id.check_all_3);
            if (checkBox != null) checkBox.setChecked(weekdaysDataSource3.isAllDaysSelected());

            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            if (item.getCalendarDayId() == today && item.isSelected())
                Toast.makeText(MainActivity.this, "Carpe diem", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {
            String selectedDays = getSelectedDaysFromWeekdaysData(items);
            if (!TextUtils.isEmpty(selectedDays))
                showSnackbarShort(selectedDays);
        }
    };

    public void setupWeekdaysButtons4() {
        weekdaysDataSource4 = new WeekdaysDataSource(this, R.id.weekdays_sample_4)
//                .setWeekdayItemLayoutRes(R.layout.weekday_image_button)
                .setOnTextDrawableListener(drawableListener)
                .setNumberOfLetters(3)
                .start(callback4);
    }

    private WeekdaysDataSource.Callback callback4 = new WeekdaysDataSource.Callback() {
        @Override
        public void onWeekdaysItemClicked(int attachId, WeekdaysDataItem item) {
            CheckBox checkBox = (CheckBox) findViewById(R.id.check_all_4);
            if (checkBox != null) checkBox.setChecked(weekdaysDataSource4.isAllDaysSelected());
            if (item.getCalendarDayId() == Calendar.SATURDAY)
                showToastShort("Party on: " + item.getLabel());
//                        else
//                            showToastShort(item.getLabel() + " : " + item.isSelected());
        }

        @Override
        public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {
            String selectedDays = getSelectedDaysFromWeekdaysData(items);
            if (!TextUtils.isEmpty(selectedDays))
                showSnackbarShort(selectedDays);

        }
    };

    public void setupWeekdaysButtons6() {
        weekdaysDataSource6 = new WeekdaysDataSource(this, R.id.weekdays_sample_6)
                .setDrawableType(WeekdaysDrawableProvider.MW_RECT_BORDER)
                .setFillWidth(false)
                .setFirstDayOfWeek(Calendar.MONDAY)
                //We override the onDrawTextDrawable to get a custom look
                .setOnTextDrawableListener(drawableListener)
                .setViewWidth(150)
                .setViewMargin(2)
                .setNumberOfLetters(10)
                .start(this);
    }

    WeekdaysDataSource.TextDrawableListener drawableListener = new WeekdaysDataSource.TextDrawableListener() {
        @Override
        public Drawable onDrawTextDrawable(int weekdayLayoutId, int calendarId, String label, boolean selected) {

            if (weekdayLayoutId == R.id.weekdays_sample_6) {
                if (calendarId == Calendar.SATURDAY || calendarId == Calendar.SUNDAY) {
                    return TextDrawable.builder(MainActivity.this)
                            .beginConfig()
                            .useFont(Typeface.MONOSPACE)
                            .fontSize(WeekdaysDrawableProvider.toPx(MainActivity.this, 12))//px
                            .withBorder(2)
                            .endConfig()
                            .buildRectRes(label, selected ? R.color.colorAccent : R.color.colorPrimary);
                } else {
                    return TextDrawable.builder(MainActivity.this)
                            .beginConfig()
                            .useFont(Typeface.MONOSPACE)
                            .fontSize(WeekdaysDrawableProvider.toPx(MainActivity.this, 12))//px
                            .withBorder(2)
                            .endConfig()
                            .buildRectRes(label, selected ? R.color.colorPrimaryDark : R.color.colorPrimary);
                }
            }

            if (weekdayLayoutId == R.id.weekdays_sample_4) {
                if (calendarId == Calendar.SATURDAY)
                    return WeekdaysDrawableProvider.getRectWithAnimation(MainActivity.this, 20, label, 200);

                else
                    return TextDrawable.builder(MainActivity.this)
                            .beginConfig()
                            .useFont(Typeface.DEFAULT)
                            .fontSize(WeekdaysDrawableProvider.toPx(MainActivity.this, 14))//px
                            .textColor(selected ? Color.DKGRAY : Color.GRAY)
                            .bold()
                            .endConfig()
                            .buildRoundRect(label, selected ? ColorGenerator.MATERIAL.getRandomColor() : Color.LTGRAY, WeekdaysDrawableProvider.toPx(MainActivity.this, 2));
            }
            return null;
        }
    };


    public void showDialog(View v) {
        if (v.getId() == R.id.dialog_button) {
            int[] selectedDays = new int[0];
            if (weekdaysDataSource5 != null) {
                int size = weekdaysDataSource5.getWeekdaysCount();
                selectedDays = new int[size];
                for (int i = 0; i < size; i++) {
                    WeekdaysDataItem dataItem = weekdaysDataSource5.getWeekdaysItems().get(i);
                    if (dataItem.isSelected()) {
                        selectedDays[i] = dataItem.getCalendarDayId();
                    } else selectedDays[i] = 0;
                }
            }
            MaterialDialog dialog = new MaterialDialog.Builder(this)
                    .title("Weekdays")
                    .customView(R.layout.weekdays_dialog, false)
                    .positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            TextView textView = (TextView) findViewById(R.id.dialog_result_text);
                            if (textView != null) {
                                textView.setText(getSelectedDaysFromWeekdaysData(weekdaysDataSource5.getWeekdaysItems()));
                            }
                        }
                    })
                    .neutralText("All")
                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            weekdaysDataSource5.selectAll(true);
                            TextView textView = (TextView) findViewById(R.id.dialog_result_text);
                            if (textView != null) {
                                textView.setText(getSelectedDaysFromWeekdaysData(weekdaysDataSource5.getWeekdaysItems()));
                            }
                        }
                    })
                    .negativeText("Clear")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            weekdaysDataSource5.selectAll(false);
                            TextView textView = (TextView) findViewById(R.id.dialog_result_text);
                            if (textView != null) {
                                textView.setText(getSelectedDaysFromWeekdaysData(weekdaysDataSource5.getWeekdaysItems()));
                            }
                        }
                    })
                    .show();

            weekdaysDataSource5 = new WeekdaysDataSource(this, R.id.weekdays_recycler_view,
                    dialog.getCustomView())
                    .setSelectedDays(selectedDays)
                    .start(this);
        }

    }

    private String getSelectedDaysFromWeekdaysData(ArrayList<WeekdaysDataItem> items) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean selected = false;
        for (WeekdaysDataItem dataItem : items
                ) {
            if (dataItem.isSelected()) {
                selected = true;
                stringBuilder.append(dataItem.getLabel());
                stringBuilder.append(", ");
            }
        }
        if (selected) {
            String result = stringBuilder.toString();
            return result.substring(0, result.lastIndexOf(","));
        } else return "No days selected";
    }

    public void showToastShort(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showSnackbarShort(String message) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.col);
        if (coordinatorLayout != null)
            showSnackbar(coordinatorLayout, message, null, Snackbar.LENGTH_SHORT, null);
    }

    public Snackbar showSnackbar(@NonNull ViewGroup viewGroup, String message, String action, int length, View.OnClickListener onActionListener) {
        Snackbar snackbar = Snackbar.make(viewGroup, message, length);
        if (!TextUtils.isEmpty(action) && onActionListener != null)
            snackbar.setAction(action, onActionListener);
        snackbar.show();
        return snackbar;
    }


    @Override
    public void onWeekdaysItemClicked(int attachId, WeekdaysDataItem item) {


        //Filter on the attached id if there is multiple weekdays data sources
        if (attachId == R.id.weekdays_recycler_view) {
            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            if (item.getCalendarDayId() == today && item.isSelected())
                Toast.makeText(MainActivity.this, "Carpe diem", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onWeekdaysSelected(int attachId, ArrayList<WeekdaysDataItem> items) {
        String selectedDays = getSelectedDaysFromWeekdaysData(items);
        if (!TextUtils.isEmpty(selectedDays))
            showSnackbarShort(selectedDays);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            new MaterialDialog.Builder(this)
                    .title(R.string.about)
                    .positiveText(R.string.dismiss)
                    .content(Html.fromHtml(getString(R.string.about_body)))
                    .contentLineSpacing(1.2f)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}