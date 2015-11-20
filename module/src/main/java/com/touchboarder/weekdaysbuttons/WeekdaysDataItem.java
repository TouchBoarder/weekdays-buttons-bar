package com.touchboarder.weekdaysbuttons;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TouchBoarder on 12/11/2015.
 */
public class WeekdaysDataItem implements Parcelable {


    private int position;
    private int calendarId;
    private String label;
    private int numberOfLetters = 1;
    private Drawable drawable;
    private boolean selected;
    private int textDrawableType;


    public int getPosition() {
        return position;
    }

    public int getCalendarDayId() {
        return calendarId;
    }

    public String getLabel() {
        return label;
    }

    public int getNumberOfLetters() {
        return numberOfLetters;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getLetters() {
        if (numberOfLetters >= label.length())
            numberOfLetters = label.length();
        return label.substring(0, numberOfLetters);
    }

    public int getTextDrawableType() {
        return textDrawableType;
    }

    public WeekdaysDataItem setNumberOfLetters(int numberOfLetters) {
        this.numberOfLetters = numberOfLetters;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public WeekdaysDataItem setLabel(String label) {
        this.label = label;
        return this;
    }


    public WeekdaysDataItem setDrawable(Drawable drawable) {
        this.drawable = drawable;
        return this;
    }

    public WeekdaysDataItem setType(int textDrawableType) {
        this.textDrawableType = textDrawableType;
        return this;
    }

    /**
     * The class used to construct a WeekdaysDataItem.
     */
    public static class Builder {

        private int layoutParentId;
        private int position;
        private int calendarId;
        private String label;
        private Drawable drawable;
        private int type = WeekdaysDrawableProvider.MW_ROUND;
        private int numberOfLetters = 1;
        private boolean selected;

        public Builder setLayoutParentId(int layoutParentId) {
            this.layoutParentId = layoutParentId;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder setCalendarId(int calendarId) {
            this.calendarId = calendarId;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setNumberOfLetters(int numberOfLetters) {
            this.numberOfLetters = numberOfLetters;
            return this;
        }

        public Builder setSelected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public WeekdaysDataItem createWeekdaysDataItem() {
            return new WeekdaysDataItem( position, calendarId, label, drawable, type, numberOfLetters, selected);
        }
    }

    public WeekdaysDataItem(int position,int calendarId, String label, Drawable drawable, int textDrawableType, int numberOfLetters, boolean selected) {
        this.position=position;
        this.label = label;
        this.drawable = drawable;
        this.textDrawableType = textDrawableType;
        this.numberOfLetters = numberOfLetters;
        this.selected = selected;
        this.calendarId = calendarId;
    }


    public WeekdaysDataItem toggleSelected() {
        setSelected(!isSelected());
        return this;
    }

    public WeekdaysDataItem setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(this.position);
        dest.writeInt(this.calendarId);
        dest.writeString(this.label);
        dest.writeInt(this.numberOfLetters);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.textDrawableType);
    }

    protected WeekdaysDataItem(Parcel in) {

        this.position = in.readInt();
        this.calendarId = in.readInt();
        this.label = in.readString();
        this.numberOfLetters = in.readInt();
        this.selected = in.readByte() != 0;
        this.textDrawableType = in.readInt();
    }

    public static final Parcelable.Creator<WeekdaysDataItem> CREATOR = new Parcelable.Creator<WeekdaysDataItem>() {
        public WeekdaysDataItem createFromParcel(Parcel source) {
            return new WeekdaysDataItem(source);
        }

        public WeekdaysDataItem[] newArray(int size) {
            return new WeekdaysDataItem[size];
        }
    };
}
