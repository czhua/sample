package com.chan.myapplication.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;

/**
 * 带便签的TextView
 *
 * @author chan
 */
public class TagTextView extends RelativeLayout {

    private static final String TAG = "TagTextView";

    /**
     * 默认开始遍历的下标，用于减少遍历次数，一定要确保使用当前字符数量没有超过一行，否则会显示异常
     */
    private static final int DEFAULT_START_POSITION = 10;

    /**
     * 文字默认大小
     */
    private static final int DEFAULT_TEXT_SIZE = 12;
    /**
     * tag水平方向的padding
     */
    private static final int DEFAULT_TAG_PADDING_HOR = 6;
    /**
     * tag垂直方向的padding
     */
    private static final int DEFAULT_TAG_PADDING_VER = 1;
    /**
     * 文字默认颜色
     */
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;
    /**
     * 标签默认大小
     */
    private static final int DEFAULT_TAG_TEXT_SIZE = 10;
    /**
     * 标签的leftMargin
     */
    private static final int DEFAULT_TAG_MARGIN_LEFT = 4;
    /**
     * 标签默认颜色
     */
    private static final int DEFAULT_TAG_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_TAG_BG_RADIUS = 3;
    private static final String DEFAULT_TAG_BG_COLOR = "#66939393";

    private static final int ID_FIRST = 1001;
    private static final int ID_END = 1002;

    private TextView mFirstTv;
    private TextView mEndTv;
    private TextView mTagTv;
    private int mHorPadding = DEFAULT_TAG_PADDING_HOR;
    private int mVerPadding = DEFAULT_TAG_PADDING_VER;
    private String mText;
    private String mTagText;
    private GradientDrawable mTagBgDrawable;
    private int mMaxLines = Integer.MAX_VALUE;
    private boolean mSingleLineMode = false;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateView();
    }

    public void setText(final String text, final String tag) {
        mText = text;
        mTagText = tag;
        if (TextUtils.isEmpty(tag)) {
            mTagTv.setVisibility(View.GONE);
            return;
        }
        mTagTv.setText(tag);
    }

    public void setSingleLine() {
        setSingleLine(true);
    }

    public void setSingleLine(boolean singleLine) {
        mSingleLineMode = singleLine;
        requestLayout();
    }

    /**
     * 设置文本颜色
     *
     * @param color color
     */
    public void setTextColor(@ColorInt int color) {
        mFirstTv.setTextColor(color);
        mEndTv.setTextColor(color);
        mTagTv.setTextColor(color);
    }

    public void setTagBackgroundColor(@ColorInt int color) {
        mTagBgDrawable.setColor(color);
        mTagTv.setBackground(mTagBgDrawable);
    }

    /**
     * 设置tag的水平padding
     *
     * @param padding 水平padding， dp
     */
    public void setPaddingHor(int padding) {
        mHorPadding = DensityUtil.dip2px(getContext(), padding);
    }

    private void updateView() {
        if (TextUtils.isEmpty(mText) || TextUtils.isEmpty(mTagText)) {
            Log.d(TAG, "text is empty text:" + mText + ", tag text:" + mTagText);
            return;
        }
        int width = getMeasuredWidth();
        float tagWidth = mTagTv.getPaint().measureText(mTagText) + 2 * mHorPadding;
        Log.d(TAG, "width:" + width + ", tag width:" + tagWidth);
        String[] arr = mText.split("");
        StringBuilder buffer = new StringBuilder();
        float firstWidth = 0;
        if (!mSingleLineMode && arr.length > DEFAULT_START_POSITION) {
            buffer.append(mText.substring(0, DEFAULT_START_POSITION - 1));
            label:
            for (int i = DEFAULT_START_POSITION; i < arr.length; i++) {
                buffer.append(arr[i]);
                firstWidth = mFirstTv.getPaint().measureText(buffer.toString());
                if (firstWidth > width) {
                    firstWidth = width;
                    Log.d(TAG, "buffer: " + buffer);
                    buffer.deleteCharAt(buffer.length() - 1);
                    char end;
                    StringBuilder endBuffer = new StringBuilder();
                    for (int index = buffer.length() - 1; index >= 0; index--) {
                        end = buffer.charAt(index);
                        if (Character.isLowerCase(end) || Character.isUpperCase(end)) {
                            endBuffer.append(end);
                            if (index <= 0) {
                                // 当前单词超过整行
                                endBuffer.setLength(0);
                                break label;
                            }
                            Log.d(TAG, "append end:" + end + ", end buffer:" + endBuffer);
                        } else {
                            Log.d(TAG, "length: " + buffer.length() + " end len:" + endBuffer.length());
                            Log.d(TAG, "buffer:" + buffer + ", end:" + endBuffer);
                            if (endBuffer.length() > 0) {
                                buffer.delete(buffer.length() - endBuffer.length(), buffer.length());
                            }
                            break label;
                        }
                    }
                }
            }
        }

        Log.d(TAG, "end buffer:" + buffer);
        float maxWidth = width - tagWidth - DensityUtil.dip2px(getContext(), DEFAULT_TAG_MARGIN_LEFT);
        if (mSingleLineMode) {
            mFirstTv.setMaxWidth((int) maxWidth);
            mFirstTv.setText(mText);
            mEndTv.setVisibility(View.GONE);
        } else if (firstWidth <= maxWidth) {
            Log.d(TAG, "single line first width:" + firstWidth + ", max width:" + maxWidth);
            mFirstTv.setMaxWidth(width);
            mFirstTv.setText(buffer);
            mEndTv.setVisibility(View.GONE);
        } else {
            mFirstTv.setMaxWidth(width);
            mFirstTv.setText(buffer);
            mEndTv.setVisibility(View.VISIBLE);
            String endText = mText.substring(buffer.length()).trim();
            mEndTv.setText(endText);
            mEndTv.setMaxWidth((int) maxWidth);
            LayoutParams lp = (LayoutParams) mTagTv.getLayoutParams();
            if (TextUtils.isEmpty(endText)) {
                lp.setMarginStart(0);
            } else {
                lp.setMarginStart(DensityUtil.dip2px(getContext(), DEFAULT_TAG_MARGIN_LEFT));
            }
            lp.addRule(ALIGN_BASELINE, ID_END);
            lp.addRule(END_OF, ID_END);
            mTagTv.setLayoutParams(lp);
        }
    }

    private void init(Context ctx) {
        mHorPadding = DensityUtil.dip2px(ctx, DEFAULT_TAG_PADDING_HOR);
        mVerPadding = DensityUtil.dip2px(ctx, DEFAULT_TAG_PADDING_VER);

        mTagBgDrawable = new GradientDrawable();
        mTagBgDrawable.setCornerRadius(DensityUtil.dip2px(getContext(), DEFAULT_TAG_BG_RADIUS));
        mTagBgDrawable.setColor(Color.parseColor(DEFAULT_TAG_BG_COLOR));

        mFirstTv = initFirstView(ctx);
        mEndTv = initEndView(ctx);
        mTagTv = initTagView(ctx);
        addView(mFirstTv);
        addView(mEndTv);
        addView(mTagTv);

      /*  getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateView();
            }
        });*/
    }

    private TextView initFirstView(Context ctx) {
        TextView view = new TextView(ctx);
        view.setId(ID_FIRST);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        view.setTextColor(DEFAULT_TEXT_COLOR);
        view.setMaxLines(1);
        view.setEllipsize(TextUtils.TruncateAt.END);
        return view;
    }

    private TextView initEndView(Context ctx) {
        TextView view = new TextView(ctx);
        view.setId(ID_END);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
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
        view.setMaxLines(1);
        view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TAG_TEXT_SIZE);
        view.setBackground(mTagBgDrawable);
        view.setPadding(mHorPadding, mVerPadding, mHorPadding, mVerPadding);
        view.setTextColor(DEFAULT_TAG_TEXT_COLOR);
        LayoutParams tagLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tagLp.addRule(ALIGN_BASELINE, ID_FIRST);
        tagLp.setMarginStart(DensityUtil.dip2px(ctx, DEFAULT_TAG_MARGIN_LEFT));
        tagLp.addRule(END_OF, ID_FIRST);
        view.setLayoutParams(tagLp);
        return view;
    }
}
