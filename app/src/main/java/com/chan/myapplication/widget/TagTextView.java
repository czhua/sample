package com.chan.myapplication.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 带便签的TextView
 */
public class TagTextView extends RelativeLayout {

    private static final String TAG = "TagTextView";

    private String mText;
    private String mTagText;

    /**
     * 标签的leftMargin
     */
    private static final int DEFAULT_TAG_MARGIN_LEFT = 4;
    /**
     * 文字默认大小
     */
    private static final int DEFAULT_TEXT_SIZE = 10;
    /**
     * 文字默认颜色
     */
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    /**
     * 标签默认大小
     */
    private static final int DEFAULT_TAG_TEXT_SIZE = DEFAULT_TEXT_SIZE;
    /**
     * 标签默认颜色
     */
    private static final int DEFAULT_TAG_TEXT_COLOR = Color.RED;

    private static final int ID_FIRST = 1001;
    private static final int ID_END = 1002;

    private TextView mFirstTv;
    private TextView mEndTv;
    private TextView mTagTv;

    public TagTextView(Context context) {
        this(context, null);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setText(final String text, final String tag) {
        mText = text;
        mTagText = tag;
        mTagTv.setText(tag);

//        mTagTv.post(new Runnable() {
//            @Override
//            public void run() {
//                updateView(text);
//            }
//        });

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateView(text, tag);
            }
        });

    }

    private void updateView(String text, String tag) {
        int width = getMeasuredWidth();
        float tagWidth = mTagTv.getPaint().measureText(tag);
        Log.d(TAG, "width:" + width + ", tag width:" + tagWidth);
        String[] arr = text.split("");
        StringBuilder buffer = new StringBuilder();
        label:
        for (int i = 0; i < arr.length; i++) {
            buffer.append(arr[i]);
            float textWidth = mFirstTv.getPaint().measureText(buffer.toString());
            if (textWidth > width) {
                Log.d(TAG, "buffer: " + buffer);
                buffer.deleteCharAt(buffer.length() - 1);
                char end;
                StringBuilder endBuffer = new StringBuilder();
                for (int index = buffer.length() - 1; index >= 0; index--) {
                    end = buffer.charAt(index);
                    if (Character.isLowerCase(end) || Character.isUpperCase(end)) {
                        endBuffer.append(end);
                    } else {
                        Log.d(TAG, "length: " + buffer.length() + " end len:" + endBuffer.length());
                        Log.d(TAG, "buffer:" + buffer + ", end:" + endBuffer);
                        buffer.delete(buffer.length() - endBuffer.length(), buffer.length());
                        break label;
                    }
                }
            }
        }

        Log.d(TAG, "end buffer:" + buffer);
        float firstLen = mFirstTv.getPaint().measureText(buffer.toString());
        float maxWidth = width - tagWidth - DensityUtil.dip2px(getContext(), DEFAULT_TAG_MARGIN_LEFT);
        if (firstLen <= maxWidth) {
            Log.d(TAG, "single line first len:" + firstLen + ", max width:" + maxWidth);
            mFirstTv.setText(buffer);
            mEndTv.setVisibility(View.GONE);
        } else {
            mFirstTv.setText(buffer);
            mEndTv.setVisibility(View.VISIBLE);
            mEndTv.setText(text.substring(buffer.length()).trim());
            mEndTv.setMaxWidth((int) maxWidth);
            LayoutParams lp = (LayoutParams) mTagTv.getLayoutParams();
            lp.addRule(ALIGN_BASELINE, ID_END);
            lp.addRule(END_OF, ID_END);
            mTagTv.setLayoutParams(lp);
        }
    }

    private void init(Context ctx) {
        mFirstTv = initFirstView(ctx);
        mEndTv = initEndView(ctx);
        mTagTv = initTagView(ctx);
        addView(mFirstTv);
        addView(mEndTv);
        addView(mTagTv);


    }

    private TextView initFirstView(Context ctx) {
        TextView view = new TextView(ctx);
        view.setId(ID_FIRST);
        view.setTextSize(DensityUtil.dip2px(ctx, DEFAULT_TEXT_SIZE));
        view.setTextColor(DEFAULT_TEXT_COLOR);
        view.setMaxLines(1);
        view.setEllipsize(TextUtils.TruncateAt.END);
        return view;
    }

    private TextView initEndView(Context ctx) {
        TextView view = new TextView(ctx);
        view.setId(ID_END);
        view.setTextSize(DensityUtil.dip2px(ctx, DEFAULT_TEXT_SIZE));
        view.setTextColor(DEFAULT_TEXT_COLOR);
        view.setMaxLines(1);
        view.setEllipsize(TextUtils.TruncateAt.END);
        view.setVisibility(View.GONE);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(BELOW, ID_FIRST);
        view.setLayoutParams(lp);
        return view;
    }

    private TextView initTagView(Context ctx) {
        TextView view = new TextView(ctx);
        view.setTextSize(DensityUtil.dip2px(ctx, DEFAULT_TAG_TEXT_SIZE));
        view.setTextColor(DEFAULT_TAG_TEXT_COLOR);
        LayoutParams tagLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        tagLp.addRule(BELOW, ID_FIRST);
        tagLp.addRule(ALIGN_BASELINE, ID_FIRST);
        tagLp.addRule(END_OF, ID_FIRST);
        view.setLayoutParams(tagLp);
        return view;
    }
}
