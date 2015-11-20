package com.touchboarder.weekdaysbuttons;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by TouchBoarder on 13/11/2015.
 */
public class WeekdaysAdapter extends RecyclerView.Adapter<WeekdaysAdapter.ViewHolder> {

    @LayoutRes
    private int mLayoutId;
    @IdRes
    private int mViewId;

    private int mWidthPx =36;
    private int mHeightPx =36;
    private int layout_gravity=Gravity.CENTER;
    private int margin=2;
    private int layoutPadding=0;

    private ArrayList<WeekdaysDataItem> mValues;
    private boolean isFillWidth =true;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mViewHolder;
        public ImageView mWeekdayView;

        public ViewHolder(View v,int id) {
            super(v);
            mViewHolder = v;
            mWeekdayView = (ImageView) v.findViewById(id);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mViewHolder.getTag();
        }
    }

    public WeekdaysDataItem getValueAt(int position) {
        return mValues.get(position);
    }


    public WeekdaysAdapter(
            @LayoutRes int layout_id,
            @IdRes int view_id,
            ArrayList<WeekdaysDataItem> items,
            boolean fillWidth,
            int viewWidth,
            int viewHeight,
            int viewMargin,
            int viewGravity,
            int layoutPaddingParent

    ) {
        mLayoutId=layout_id;
        mViewId=view_id;
        mValues = new ArrayList<>(items);
        isFillWidth =fillWidth;
        mWidthPx =viewWidth;
        mHeightPx =viewHeight;
        margin=viewMargin;
        layout_gravity=viewGravity;
        layoutPadding=layoutPaddingParent;
    }

    public void swap(int pos1, int pos2) {
        WeekdaysDataItem tmp = mValues.get(pos1);
        mValues.set(pos1, mValues.get(pos2));
        mValues.set(pos2, tmp);
        notifyItemRemoved(pos1);
        notifyItemInserted(pos2);
    }

    @Override
    public WeekdaysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =null;
        if(mLayoutId>0) view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        if (view == null) {
            view=new FrameLayout(parent.getContext());
            view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
            view.setPadding(layoutPadding,layoutPadding,layoutPadding,layoutPadding);
        }
        final ViewHolder h = new ViewHolder(view,mViewId);

        if(h.mWeekdayView==null&&h.mViewHolder instanceof ViewGroup){
            h.mWeekdayView=new ImageView(parent.getContext());
            FrameLayout.LayoutParams params =new FrameLayout.LayoutParams(
                    mWidthPx,
                    mHeightPx,
                    layout_gravity);
            params.setMargins(margin,0,margin,0);
            ((ViewGroup)h.mViewHolder).addView(
                    h.mWeekdayView, params);
        }


        if(h.mWeekdayView!=null) {
            if(h.mWeekdayView instanceof FloatingActionButton)
            h.mWeekdayView.setPadding(0, 0, 0, 0);
            h.mWeekdayView.setFocusable(true);
            h.mWeekdayView.setClickable(false);
        }
        if(isFillWidth)
        h.mViewHolder.setMinimumWidth(Math.round(parent.getWidth()/ getItemCount()));

        return h;
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        if(h.mWeekdayView!=null) {
            h.mWeekdayView.setImageDrawable(mValues.get(position).getDrawable());
        }else{
            h.mViewHolder.setBackgroundDrawable(mValues.get(position).getDrawable());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
