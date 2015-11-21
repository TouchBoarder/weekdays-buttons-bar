package com.touchboarder.weekdaysbuttons;

/**
 * Created by Harald on 12/11/2015.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeekdaysDataSource implements Parcelable {


    public interface Callback {
        void onWeekdaysItemClicked(int attachId,WeekdaysDataItem weekdaysItem);

        void onWeekdaysSelected(int attachId,ArrayList<WeekdaysDataItem> weekdaysArray);

    }

    public interface TextDrawableListener {
        Drawable onDrawTextDrawable(int attachId, int calendarDayId, String label, boolean selected);
    }

    public WeekdaysDataSource setOnTextDrawableListener(TextDrawableListener textDrawableCallback) {
        this.textDrawableCallback = textDrawableCallback;
        return this;
    }

    private TextDrawableListener textDrawableCallback;

    private transient AppCompatActivity mContext;
    protected transient RecyclerView mRecyclerView;
    private transient Callback mCallback;
    private ArrayList<WeekdaysDataItem> mDataSource;
    private WeekdaysDrawableProvider mDrawableProvider;

    private Locale locale = Locale.getDefault();

    protected LinearLayoutManager mLayoutManager;


    @IdRes
    private int mAttachId = R.id.weekdays_recycler_view;
    @LayoutRes
    private int mWeekdayLayoutId = -1;//R.layout.weekdays_image_view;
    @IdRes
    private int mWeekdayViewId = -1;//R.id.weekday_view;
    @ColorInt
    private int mBackgroundColor = Color.TRANSPARENT;
    @ColorInt
    private int mTextColorSelected;
    @ColorInt
    private int mTextColorUnselected;
    @ColorInt
    private int mSelectedColor;
    @ColorInt
    private int mUnselectedColor;





    private Typeface fontTypeFace;

    private boolean mIsVisible;
    private boolean mFillWidth = true;
    private boolean mIsAllDaysSelected = false;
    private int mFirstDayOfWeek = Calendar.SUNDAY;
    private int mTextDrawableType = WeekdaysDrawableProvider.MW_ROUND;
    private int mNumberOfLetters = 1;


    private int mViewWidth;
    private int mViewHeight;
    private int mViewMargin;
    private int mViewGravity = Gravity.CENTER;
    private int mLayoutPadding;

    private boolean mNestedScrollEnable = false;

    private LinkedHashMap<Integer, String> mWeekdaysMap;
    private HashMap<Integer, Boolean> mSelectedDaysMap;
    private View mParentView = null;


    public WeekdaysDataSource(@NonNull AppCompatActivity context, @IdRes int attacherId) {
        mContext = context;
        mAttachId = attacherId;
        reset(context);
    }

    public WeekdaysDataSource(@NonNull AppCompatActivity context, @IdRes int attacherId, View view) {
        mContext = context;
        mAttachId = attacherId;
        reset(context);
        mParentView = view;
    }

    public WeekdaysDataSource setWeekdayItemLayoutId(int mWeekdayLayoutId) {
        this.mWeekdayLayoutId = mWeekdayLayoutId;
        return this;
    }

    public WeekdaysDataSource setWeekdayItemViewId(@IdRes int weekdayViewId) {
        this.mWeekdayViewId = weekdayViewId;
        return this;
    }

    private WeekdaysDrawableProvider getDrawableProvider() {
        if (mDrawableProvider == null) {
            mDrawableProvider = new WeekdaysDrawableProvider();
            mDrawableProvider.setSelectedColor(mSelectedColor);
            mDrawableProvider.setUnselectedColor(mUnselectedColor);
            mDrawableProvider.setTextColorSelected(mTextColorSelected);
            mDrawableProvider.setTextColorUnselected(mTextColorUnselected);
            mDrawableProvider.setFontTypeFace(fontTypeFace);
        }
        return mDrawableProvider;
    }

    private LinkedHashMap<Integer, String> getDays() {
        if (mWeekdaysMap == null) {
            String[] days = new DateFormatSymbols(getLocale()).getWeekdays();
            mWeekdaysMap = new LinkedHashMap<>();
            for (int i = mFirstDayOfWeek; i < days.length; i++) {
                if (!TextUtils.isEmpty(days[i])) {
                    mWeekdaysMap.put(i, days[i]);
                    getSelectedDays().put(i, mIsAllDaysSelected);
                }
            }
            if (mFirstDayOfWeek == Calendar.MONDAY) {
                mWeekdaysMap.put(Calendar.SUNDAY, days[Calendar.SUNDAY]);
                getSelectedDays().put(Calendar.SUNDAY, mIsAllDaysSelected);
            }
        }

        return mWeekdaysMap;
    }

    private HashMap<Integer, Boolean> getSelectedDays() {
        if (mSelectedDaysMap == null)
            mSelectedDaysMap = new HashMap<>();

        for (WeekdaysDataItem item : getWeekdaysItems()) {
            mSelectedDaysMap.put(item.getCalendarDayId(), item.isSelected());
        }
        return mSelectedDaysMap;
    }


    public void setContext(@NonNull AppCompatActivity context) {
        mContext = context;
    }


    public boolean isActive() {
        return mIsVisible;
    }

    @UiThread
    public WeekdaysDataSource reset(Context context) {

        mBackgroundColor = WeekdaysUtil.resolveColor(context, R.attr.weekdays_background_color,
                WeekdaysUtil.resolveColor(context, R.attr.weekdays_background_color, Color.TRANSPARENT));
        mSelectedColor = WeekdaysUtil.resolveColor(context, R.attr.weekdays_selected_color,
                WeekdaysUtil.resolveColor(context, R.attr.colorAccent, Color.RED));
        mUnselectedColor = WeekdaysUtil.resolveColor(context, R.attr.weekdays_unselected_color,
                WeekdaysUtil.resolveColor(context, R.attr.colorPrimary, Color.GRAY));

        mTextColorSelected = WeekdaysUtil.resolveColor(context, R.attr.weekdays_text_selected_color,
                WeekdaysUtil.resolveColor(context, R.attr.titleTextColor, Color.WHITE));

        mTextColorUnselected = WeekdaysUtil.resolveColor(context, R.attr.weekdays_text_unselected_color,
                WeekdaysUtil.resolveColor(context, R.attr.titleTextColor, Color.WHITE));

        mViewMargin = WeekdaysUtil.resolveDimension(context, R.attr.weekdays_item_margin, R.dimen.weekdays_button_default_margin);
        mViewWidth = WeekdaysUtil.resolveDimension(context, R.attr.weekdays_item_width, R.dimen.weekdays_button_default_width);
        mViewHeight = WeekdaysUtil.resolveDimension(context, R.attr.weekdays_item_height, R.dimen.weekdays_button_default_height);

        mLayoutPadding = WeekdaysUtil.resolveDimension(context, R.attr.weekdays_layout_padding, R.dimen.weekdays_layout_default_padding);

        return this;
    }


    @UiThread
    public WeekdaysDataSource setFirstDayOfWeek(@IntRange(from = Calendar.SUNDAY, to = Calendar.MONDAY) int firstDayOfWeek) {
        this.mFirstDayOfWeek = firstDayOfWeek;
        return this;
    }

    @UiThread
    public WeekdaysDataSource setFillWidth(boolean mFillView) {
        this.mFillWidth = mFillView;

        if (mRecyclerView != null) mRecyclerView.setAdapter(createAdapter());
        return this;
    }

    public boolean getFillWidth() {
        return mFillWidth;
    }

    public boolean isNestedScrollEnable() {
        return mNestedScrollEnable;
    }
    @UiThread
    public WeekdaysDataSource setNestedScrollEnable(boolean enable) {
        this.mNestedScrollEnable = enable;
        if (mRecyclerView != null) mRecyclerView.setNestedScrollingEnabled(enable);
        return this;
    }

    @UiThread
    public WeekdaysDataSource setDrawableType(int mType) {
        this.mTextDrawableType = mType;
        return this;
    }

    public Locale getLocale() {
        return locale;
    }

    public WeekdaysDataSource setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public WeekdaysDataSource setFontTypeFace(Typeface fontTypeFace) {
        this.fontTypeFace = fontTypeFace;
        if (getDrawableProvider() != null)
            getDrawableProvider().setFontTypeFace(fontTypeFace);
        return this;
    }

    @UiThread
    public WeekdaysDataSource setNumberOfLetters(int numberOfLetters) {
        this.mNumberOfLetters = numberOfLetters;
        for (WeekdaysDataItem item : getWeekdaysItems()) {
            item.setNumberOfLetters(numberOfLetters);
        }
        return this;
    }

    @UiThread
    public WeekdaysDataSource setFontBaseSize(int fontBaseSize) {
        if (getDrawableProvider() != null)
            getDrawableProvider().setFontBaseSize(fontBaseSize);
        return this;
    }

    @UiThread
    public WeekdaysDataSource setSelectedDays(boolean... days) {
        if (days.length > getDays().size())
            days = Arrays.copyOf(days, getDays().size());
        for (int i = 0; i < days.length; i++) {
            mIsAllDaysSelected = false;
            getSelectedDays().put(i, days[i]);
            if (i < getWeekdaysCount()) {
                setSelected(i, days[i], false);
            }
        }

        if (mRecyclerView != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        return this;
    }

    @UiThread
    public WeekdaysDataSource setSelectedDays(int... indexes) {
        if (indexes.length > getDays().size())
            indexes = Arrays.copyOf(indexes, getDays().size());
        for (Integer index : indexes) {
            mIsAllDaysSelected = false;
            if (index <= getDays().size())
                getSelectedDays().put(index, true);
            if (index < getWeekdaysCount()) {
                setSelected(index, true, false);
            }
        }
        if (mRecyclerView != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        return this;
    }


    @UiThread
    public WeekdaysDataSource start(@Nullable Callback callback) {
        mCallback = callback;
        invalidateVisibility(attach());
        return this;
    }

    @UiThread
    public WeekdaysDataSource setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        if (mRecyclerView != null)
            mRecyclerView.setBackgroundColor(color);
        return this;
    }


    @UiThread
    public WeekdaysDataSource setBackgroundColorRes(@ColorRes int colorRes) {
        return setBackgroundColor(mContext.getResources().getColor(colorRes));
    }


    @UiThread
    public WeekdaysDataSource setBackgroundColorAttr(@AttrRes int colorAttr) {
        return setBackgroundColor(WeekdaysUtil.resolveColor(mContext, colorAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setTextColorSelected(@ColorInt int color) {
        mTextColorSelected = color;
        if (getDrawableProvider() != null)
            getDrawableProvider().setTextColorSelected(color);
        return this;
    }

    @UiThread
    public WeekdaysDataSource setTextColorSelectedRes(@ColorRes int colorRes) {
        return setTextColorSelected(mContext.getResources().getColor(colorRes));
    }


    @UiThread
    public WeekdaysDataSource setTextColorSelectedAttr(@AttrRes int colorAttr) {
        return setTextColorSelected(WeekdaysUtil.resolveColor(mContext, colorAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setTextColorUnselected(@ColorInt int color) {
        mTextColorUnselected = color;
        if (getDrawableProvider() != null)
            getDrawableProvider().setTextColorUnselected(color);
        return this;
    }

    @UiThread
    public WeekdaysDataSource setTextColorUnselectedRes(@ColorRes int colorRes) {
        return setTextColorUnselected(mContext.getResources().getColor(colorRes));
    }


    @UiThread
    public WeekdaysDataSource setTextColorUnselectedAttr(@AttrRes int colorAttr) {
        return setTextColorUnselected(WeekdaysUtil.resolveColor(mContext, colorAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setSelectedColor(@ColorInt int color) {
        mSelectedColor = color;
        if (getDrawableProvider() != null)
            getDrawableProvider().setSelectedColor(color);
        return this;
    }


    @UiThread
    public WeekdaysDataSource setSelectedColorRes(@ColorRes int colorRes) {
        return setSelectedColor(mContext.getResources().getColor(colorRes));
    }


    @UiThread
    public WeekdaysDataSource setSelectedColorAttr(@AttrRes int colorAttr) {
        return setSelectedColor(WeekdaysUtil.resolveColor(mContext, colorAttr, 0));
    }


    @UiThread
    public WeekdaysDataSource setUnselectedColor(@ColorInt int color) {
        mUnselectedColor = color;
        if (getDrawableProvider() != null)
            getDrawableProvider().setUnselectedColor(color);
        return this;
    }


    @UiThread
    public WeekdaysDataSource setUnselectedColorRes(@ColorRes int colorRes) {
        return setUnselectedColor(mContext.getResources().getColor(colorRes));
    }


    @UiThread
    public WeekdaysDataSource setUnselectedColorAttr(@AttrRes int colorAttr) {
        return setUnselectedColor(WeekdaysUtil.resolveColor(mContext, colorAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setViewMargin(int mViewMargin) {
        this.mViewMargin = mViewMargin;
        return this;
    }

    @UiThread
    public WeekdaysDataSource setViewMarginRes(@DimenRes int marginRes) {
        return setViewMargin((int) mContext.getResources().getDimension(marginRes));
    }

    @UiThread
    public WeekdaysDataSource setViewMarginAttr(@AttrRes int marginAttr) {
        return setViewMargin(WeekdaysUtil.resolveInt(mContext, marginAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setViewWidth(int mViewWidth) {
        this.mViewWidth = mViewWidth;
        return this;
    }

    @UiThread
    public WeekdaysDataSource setViewWidthRes(@DimenRes int widthRes) {
        return setViewWidth((int) mContext.getResources().getDimension(widthRes));
    }

    @UiThread
    public WeekdaysDataSource setViewWidthAttr(@AttrRes int widthAttr) {
        return setViewWidth(WeekdaysUtil.resolveInt(mContext, widthAttr, 0));
    }

    @UiThread
    public WeekdaysDataSource setViewHeight(int mViewHeight) {
        this.mViewHeight = mViewHeight;
        return this;
    }

    @UiThread
    public WeekdaysDataSource setViewHeightRes(@DimenRes int heigthRes) {
        return setViewHeight((int) mContext.getResources().getDimension(heigthRes));
    }

    @UiThread
    public WeekdaysDataSource setViewHeightAttr(@AttrRes int heightAttr) {
        return setViewHeight(WeekdaysUtil.resolveInt(mContext, heightAttr, 0));
    }
    @UiThread
    public WeekdaysDataSource setLayoutPadding(int layoutPadding) {
        this.mLayoutPadding = layoutPadding;
        return this;
    }

    @UiThread
    public WeekdaysDataSource setLayoutPaddingRes(@DimenRes int layoutPaddingRes) {
        return setLayoutPadding((int) mContext.getResources().getDimension(layoutPaddingRes));
    }

    @UiThread
    public WeekdaysDataSource setLayoutPaddingAttr(@AttrRes int layoutPaddingAttr) {
        return setLayoutPadding(WeekdaysUtil.resolveInt(mContext, layoutPaddingAttr, 0));
    }
    @UiThread
    public WeekdaysDataSource setViewParams(int width, int height, int margin, int gravity,int layoutPadding) {
        setViewWidth(width);
        setViewHeight(height);
        setViewMargin(margin);
        setViewGravity(gravity);
        setLayoutPadding(layoutPadding);

        if (mRecyclerView != null) mRecyclerView.setAdapter(createAdapter());
        return this;
    }

    @UiThread
    public WeekdaysDataSource setViewParamsRes(@DimenRes int widthRes, @DimenRes int heightRes, @DimenRes int marginRes, int gravity,@DimenRes int layoutPaddingRes) {
        setViewWidthRes(widthRes);
        setViewHeightRes(heightRes);
        setViewMarginRes(marginRes);
        setViewGravity(gravity);
        setLayoutPaddingRes(layoutPaddingRes);
        if (mRecyclerView != null) mRecyclerView.setAdapter(createAdapter());
        return this;
    }

    @UiThread
    public WeekdaysDataSource setViewParamsAttr(@AttrRes int widthAttr, @AttrRes int heightAttr, @AttrRes int marginAttr, int gravity, @AttrRes int layoutPaddingAttr) {
        setViewWidthAttr(widthAttr);
        setViewHeightAttr(heightAttr);
        setViewMarginAttr(marginAttr);
        setViewGravity(gravity);
        setLayoutPaddingAttr(layoutPaddingAttr);
        if (mRecyclerView != null) mRecyclerView.setAdapter(createAdapter());
        return this;
    }

    /**
     * Use Gravity.CENTER etc.
     *
     * @param gravity Gravity
     * @return WeekdaysDataSource
     */
    @UiThread
    public WeekdaysDataSource setViewGravity(int gravity) {
        this.mViewGravity = gravity;
        return this;
    }

    public int getViewMargin() {
        return mViewMargin;
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewHeight() {
        return mViewHeight;
    }

    public int getViewGravity() {
        return mViewGravity;
    }
    public int getLayoutPadding() {
        return mLayoutPadding;
    }

    @Nullable
    public View getWeekdaysRecycleView() {
        return mRecyclerView;
    }


    @UiThread
    public void setVisible(boolean visible) {
        invalidateVisibility(visible);
    }

    private void invalidateVisibility(boolean visible) {
        if (mRecyclerView == null) return;
        mRecyclerView.setVisibility(visible ?
                View.VISIBLE : View.GONE);
        mIsVisible = visible;
    }

    public int getWeekdaysCount() {
        return getWeekdaysItems().size();
    }

    public WeekdaysDataSource selectAll(boolean selected) {
        mIsAllDaysSelected = selected;
        for (int i = 0; i < getWeekdaysCount(); i++) {
            setSelected(i, selected, false);
        }
        if (mRecyclerView != null) mRecyclerView.getAdapter().notifyDataSetChanged();
        mCallback.onWeekdaysSelected(mAttachId,getWeekdaysItems());
        return this;
    }

    private void setSelected(int index, boolean selected, boolean notify) {
        WeekdaysDataItem item = getItem(index);
        if (item != null) {

            if (mIsAllDaysSelected) mIsAllDaysSelected = selected;

            getItem(index).setSelected(selected);
            getItem(index).setDrawable(getDrawableFromWeekdayItemProperties(item));
            if (notify && mRecyclerView != null)
                mRecyclerView.getAdapter().notifyItemChanged(index);
        }
    }

    public boolean isAllDaysSelected() {
        if (mIsAllDaysSelected) return true;
        int countSelected = 0;
        for (WeekdaysDataItem item :
                getWeekdaysItems()) {
            if (item.isSelected()) countSelected++;
        }
        return countSelected == getWeekdaysCount();
    }

    private WeekdaysDataItem toggleSelected(WeekdaysDataItem item) {
        item.toggleSelected();
        item.setDrawable(getDrawableFromWeekdayItemProperties(item));
        if (mIsAllDaysSelected) mIsAllDaysSelected = item.isSelected();
        return item;
    }

    public ArrayList<WeekdaysDataItem> getWeekdaysItems() {
        if (mDataSource == null) mDataSource = new ArrayList<>();
        return mDataSource;
    }

    public int getWeekdayLayoutId() {
        return mWeekdayLayoutId;
    }

    @IdRes
    public int getWeekdayViewId() {
        return mWeekdayViewId;
    }

    public WeekdaysDataItem getItem(int position) {
        return mDataSource.get(position);
    }

    private WeekdaysDataItem itemFromType(int position, int calendarDayId, String label, boolean selected) {
        return new WeekdaysDataItem.Builder()
                .setLayoutParentId(mAttachId)
                .setPosition(position)
                .setCalendarId(calendarDayId)
                .setLabel(label)
                .setDrawable(getDrawableFromType( calendarDayId, mTextDrawableType, label, selected))
                .setType(mTextDrawableType)
                .setNumberOfLetters(mNumberOfLetters)
                .setSelected(selected)
                .createWeekdaysDataItem();
    }

    private Drawable getDrawableFromType( int calendarDayId, int textDrawableType, String label, boolean selected) {

        if (mNumberOfLetters >= label.length())
            mNumberOfLetters = label.length();

        Drawable drawable = null;
        if (textDrawableCallback != null)
            drawable = textDrawableCallback.onDrawTextDrawable(mAttachId, calendarDayId, label.substring(0, mNumberOfLetters), selected);

        if (drawable == null && getDrawableProvider() != null)
            drawable = getDrawableProvider().getDrawableFromType(mContext, textDrawableType, label.substring(0, mNumberOfLetters), selected);
        return drawable;
    }

    public Drawable getDrawableFromWeekdayItemProperties(WeekdaysDataItem dayItem) {
        return getDrawableFromType( dayItem.getCalendarDayId(), dayItem.getTextDrawableType(), dayItem.getLabel(), dayItem.isSelected());
    }


    private void initRecyclerView(Context context) {
        if (mRecyclerView == null) return;
        mRecyclerView.setBackgroundColor(mBackgroundColor);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(createAdapter());
        mRecyclerView.setNestedScrollingEnabled(isNestedScrollEnable());


//        mRecyclerView.getItemAnimator().setSupportsChangeAnimations(true);
        RecyclerView.ItemAnimator animator = mRecyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        onRecyclerViewInit(mRecyclerView);
    }

    protected void onRecyclerViewInit(RecyclerView recyclerView) {

    }

    protected RecyclerView.Adapter<WeekdaysAdapter.ViewHolder> createAdapter() {
        return new WeekdaysAdapter(
                getWeekdayLayoutId(),
                getWeekdayViewId(),
                getWeekdaysItems(),
                getFillWidth(),
                getViewWidth(),
                getViewHeight(),
                getViewMargin(),
                getViewGravity(),
                getLayoutPadding()
                ) {
            @Override
            public WeekdaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final WeekdaysAdapter.ViewHolder vh = super.onCreateViewHolder(parent, viewType);
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int pos = vh.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION && pos < getItemCount()) {
                            mCallback.onWeekdaysItemClicked(mAttachId,toggleSelected(getValueAt(pos)));
                            notifyItemChanged(pos);
                            mCallback.onWeekdaysSelected(mAttachId,getWeekdaysItems());
                        }
                    }
                });
                return vh;
            }
        };
    }


    //INIT CODE

    private boolean attach() {

        final View attachView = mParentView == null ? mContext.findViewById(mAttachId) : mParentView.findViewById(mAttachId);
        if (attachView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) attachView;
        } else if (attachView instanceof ViewStub) {
            ViewStub stub = (ViewStub) attachView;
            stub.setLayoutResource(R.layout.weekdays_recycler_view);
            stub.setInflatedId(mAttachId);
            mRecyclerView = (RecyclerView) stub.inflate();
        } else {
            throw new IllegalStateException("Weeekdays Buttons was unable to attach to your Layout, required [ViewStub],[recycleView] or ['parent' View] doesn't exist.");
        }


        if (mRecyclerView != null) {

            getDrawableProvider();

            if (mTextDrawableType == WeekdaysDrawableProvider.MW_MULTIPLE_LETTERS && mNumberOfLetters < 2)
                mNumberOfLetters = 2;


            int position = 0;
            for (Map.Entry<Integer, String> map : getDays().entrySet()) {
                String day = map.getValue();
                int calendarDayId = map.getKey();
                if (!TextUtils.isEmpty(day)) {
                    WeekdaysDataItem item = itemFromType(position, calendarDayId, day, getSelectedDays().get(calendarDayId));
                    if (getWeekdaysCount() == position)
                        getWeekdaysItems().add(item);
                    else getWeekdaysItems().set(position, item);

                    position++;
                }
            }

            initRecyclerView(mContext);

            return true;
        }
        return false;
    }


    @UiThread
    public void saveState(@NonNull String tag, Bundle dest) {
        dest.putParcelable("weekdays_state_" + tag, this);
    }

    @UiThread
    public static WeekdaysDataSource restoreState(@NonNull String tag, Bundle source, AppCompatActivity
            context, Callback callback, TextDrawableListener drawableListener) {
        if (source == null || !source.containsKey("weekdays_state_" + tag))
            return null;
        WeekdaysDataSource weekdaysDataSource = source.getParcelable("weekdays_state_" + tag);
        if (weekdaysDataSource != null) {
            weekdaysDataSource.mContext = context;
            if (weekdaysDataSource.mIsVisible) {
                weekdaysDataSource.setOnTextDrawableListener(drawableListener);
                weekdaysDataSource.reset(context);
                weekdaysDataSource.start(callback);

            }
        }
        return weekdaysDataSource;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(this.mDataSource);
        dest.writeSerializable(this.locale);
        dest.writeInt(this.mAttachId);
        dest.writeInt(this.mWeekdayLayoutId);
        dest.writeInt(this.mWeekdayViewId);
        dest.writeInt(this.mBackgroundColor);
        dest.writeInt(this.mTextColorSelected);
        dest.writeInt(this.mTextColorUnselected);
        dest.writeInt(this.mSelectedColor);
        dest.writeInt(this.mUnselectedColor);
        dest.writeByte(mIsVisible ? (byte) 1 : (byte) 0);
        dest.writeByte(mFillWidth ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsAllDaysSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mFirstDayOfWeek);
        dest.writeInt(this.mTextDrawableType);
        dest.writeInt(this.mNumberOfLetters);

        dest.writeInt(this.mViewWidth);
        dest.writeInt(this.mViewHeight);
        dest.writeInt(this.mViewMargin);
        dest.writeInt(this.mViewGravity);
        dest.writeInt(this.mLayoutPadding);

        dest.writeByte(mNestedScrollEnable ? (byte) 1 : (byte) 0);
    }

    protected WeekdaysDataSource(Parcel in) {

        this.mDataSource = new ArrayList<>();
        in.readList(this.mDataSource, List.class.getClassLoader());

        this.locale = (Locale) in.readSerializable();

        this.mAttachId = in.readInt();
        this.mWeekdayLayoutId = in.readInt();
        this.mWeekdayViewId = in.readInt();
        this.mBackgroundColor = in.readInt();
        this.mTextColorSelected = in.readInt();
        this.mTextColorUnselected = in.readInt();
        this.mSelectedColor = in.readInt();
        this.mUnselectedColor = in.readInt();

        this.mIsVisible = in.readByte() != 0;
        this.mFillWidth = in.readByte() != 0;
        this.mIsAllDaysSelected = in.readByte() != 0;
        this.mFirstDayOfWeek = in.readInt();
        this.mTextDrawableType = in.readInt();
        this.mNumberOfLetters = in.readInt();

        this.mViewWidth= in.readInt();
        this.mViewHeight= in.readInt();
        this.mViewMargin= in.readInt();
        this.mViewGravity= in.readInt();
        this.mLayoutPadding= in.readInt();

        this.mNestedScrollEnable = in.readByte() != 0;
    }

    public static final Parcelable.Creator<WeekdaysDataSource> CREATOR = new Parcelable.Creator<WeekdaysDataSource>() {
        public WeekdaysDataSource createFromParcel(Parcel source) {
            return new WeekdaysDataSource(source);
        }

        public WeekdaysDataSource[] newArray(int size) {
            return new WeekdaysDataSource[size];
        }
    };
}
