package com.touchboarder.weekdaysbuttons;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.TypedValue;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.amulyakhare.textdrawable.util.TypefaceHelper;


/**
 * @author amulya
 * @datetime 17 Oct 2014, 4:02 PM
 * <p/>
 * Modified by TouchBoarder on 12/11/2015.
 */
public class WeekdaysDrawableProvider {

    public static final int MW_RECT = 1;
    public static final int MW_ROUND_RECT = 2;
    public static final int MW_ROUND = 3;
    public static final int MW_RECT_BORDER = 4;
    public static final int MW_ROUND_RECT_BORDER = 5;
    public static final int MW_ROUND_BORDER = 6;
    public static final int MW_MULTIPLE_LETTERS = 7;
    public static final int MW_ROUND_CUSTOM_FONT = 8;
    public static final int MW_RECT_CUSTOM_SIZE = 9;

    private final ColorGenerator mGenerator;

    public void setFontBaseSize(int fontBaseSize) {
        this.fontBaseSize = fontBaseSize;
    }
    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
    }

    public int getTextColorSelected() {
        return textColorSelected;
    }

    public void setTextColorSelected(int textColor) {
        this.textColorSelected = textColor;
    }


    public int getTextColorUnselected() {
        return textColorUnselected;
    }

    public void setFontTypeFace(Typeface fontTypeFace) {
        this.fontTypeFace = fontTypeFace;
    }

    public void setTextColorUnselected(int textUnselectedColor) {
        this.textColorUnselected = textUnselectedColor;
    }

    private int textColorSelected = Color.WHITE;
    private int textColorUnselected = Color.LTGRAY;
    private int selectedColor = Color.RED;
    private int unselectedColor = Color.DKGRAY;
    private int numberOfLetters = 1;
    private int fontBaseSize = 20;
    private Typeface fontTypeFace = TypefaceHelper.get("sans-serif-light", 0);

    public WeekdaysDrawableProvider() {
        mGenerator = ColorGenerator.MATERIAL;
    }

    public Drawable getDrawableFromType(Context context, int type, String letter, boolean selected) {
        Drawable drawable = null;
        if (context == null) return drawable;
        numberOfLetters = letter.length();
        switch (type) {
            case WeekdaysDrawableProvider.MW_RECT:
                drawable = getRect(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_ROUND_RECT:
//                label = "Round Corner with Text";
                drawable = getRoundRect(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_ROUND:
//                label = "Round with Text";
                drawable = getRound(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_RECT_BORDER:
//                label = "Rectangle with Border";
                drawable = getRectWithBorder(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_ROUND_RECT_BORDER:
//                label = "Round Corner with Border";
                drawable = getRoundRectWithBorder(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_ROUND_BORDER:
//                label = "Round with Border";
                drawable = getRoundWithBorder(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_MULTIPLE_LETTERS:
//                label = "Support multiple letters";
                drawable = getRectWithMultiLetter(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_ROUND_CUSTOM_FONT:
//                label = "Support variable font styles";
                drawable = getRoundWithCustomFont(context, letter, selected);
                break;
            case WeekdaysDrawableProvider.MW_RECT_CUSTOM_SIZE:
//                label = "Support for custom size";
                drawable = getRectWithCustomSize(context, letter, letter, selected);
                break;
        }
        return drawable;
    }

    public TextDrawable getRect(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .endConfig()
                .buildRect(text, selected ? getSelectedColor() : getUnselectedColor());
    }


    public TextDrawable getRound(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .endConfig()
                .buildRound(text, selected ? getSelectedColor() : getUnselectedColor());
    }


    public TextDrawable getRoundRect(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .endConfig()
                .buildRoundRect(text, selected ? getSelectedColor() : getUnselectedColor(), toPx(context, 10));
    }


    public TextDrawable getRectWithBorder(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .withBorder(toPx(context, 2))
                .endConfig()
                .buildRect(text, selected ? getSelectedColor() : getUnselectedColor());
    }


    public TextDrawable getRoundWithBorder(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .withBorder(toPx(context, 2))
                .endConfig()
                .buildRound(text, selected ? getSelectedColor() : getUnselectedColor());
    }


    public TextDrawable getRoundRectWithBorder(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize - numberOfLetters))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .withBorder(toPx(context, 2))
                .endConfig()
                .buildRoundRect(text, selected ? getSelectedColor() : getUnselectedColor(), toPx(context, 10));
    }


    public TextDrawable getRectWithMultiLetter(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .toUpperCase()
                .endConfig()
                .buildRect(text, selected ? getSelectedColor() : getUnselectedColor());
    }


    public TextDrawable getRoundWithCustomFont(Context context, String text, boolean selected) {
        return TextDrawable.builder(context)
                .beginConfig()
                .useFont(fontTypeFace)
                .fontSize(toPx(context, fontBaseSize))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .bold()
                .endConfig()
                .buildRound(text, selected ? getSelectedColor() : getUnselectedColor() /*toPx(5)*/);
    }


    public Drawable getRectWithCustomSize(Context context, String leftText, String rightText, boolean selected) {

        TextDrawable.IBuilder builder = TextDrawable.builder(context)
                .beginConfig()
                .width(toPx(context, 29))
                .withBorder(toPx(context, 2))
                .textColor(selected ? getTextColorSelected() : getTextColorUnselected())
                .endConfig()
                .rect();


        TextDrawable left = builder
                .build(leftText, mGenerator.getColor(leftText));


        TextDrawable right = builder
                .build(rightText, mGenerator.getColor(rightText));


        Drawable[] layerList = {
                new InsetDrawable(left, 0, 0, toPx(context, 31), 0),
                new InsetDrawable(right, toPx(context, 31), 0, 0, 0)
        };
        return new LayerDrawable(layerList);
    }


    public static Drawable getRectWithAnimation(Context context, int count, String label, int delay) {
        TextDrawable.IBuilder builder = TextDrawable.builder(context)
                .rect();


        AnimationDrawable animationDrawable = new AnimationDrawable();
        for (int i = count; i > 0; i--) {
            TextDrawable frame = builder.build(label, ColorGenerator.MATERIAL.getRandomColor());
            animationDrawable.addFrame(frame, delay);
        }
        animationDrawable.setOneShot(false);
        animationDrawable.start();


        return animationDrawable;
    }


    public static int toPx(Context context, int dp) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }


}
