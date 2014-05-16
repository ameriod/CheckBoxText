package com.ameriod.checkboxtext.lib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Parker on 5/14/2014.
 * <p/>
 * TODO make resource attrs for the TextView Stuff textSize, textStyle, text
 * TODO make resource attrs for the CheckBox setting the drawable, padding (the custom drawables have different padding)
 */
public class CheckBoxText extends RelativeLayout implements Checkable, View.OnClickListener {

    private CheckBox mCheckBox;
    private TextView mTextView;

    private int mTextOrientation;
    private float mTextSize;
    private int mTextStyle;
    private String mText;
    private ColorStateList mTextColor;
    private Drawable mCheckboxBackground;

    /**
     * Listener to forward CompoundButton onCheckChangeListener stuff to the checkbox
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    /**
     * Orients the text to the right of the checkbox
     */
    public static final int TEXT_RIGHT = 0;
    /**
     * Orients the text to the left of the checkbox
     */
    public static final int TEXT_LEFT = 1;
    /**
     * Orients the text above of the checkbox
     */
    public static final int TEXT_ABOVE = 2;
    /**
     * Orients the text below the checkbox
     */
    public static final int TEXT_BELOW = 3;

    /**
     * For debugging
     */
    private static final String TAG = "CheckBoxText";

    /**
     * Orientations to pick from
     * <p/>
     * TEXT_RIGHT = 0;
     * TEXT_LEFT = 1;
     * TEXT_ABOVE = 2;
     * TEXT_BELOW = 3;
     *
     * @param orientation
     * @param context
     */
    public CheckBoxText(Context context, int orientation) {
        super(context);
        mTextOrientation = orientation;
        build();
    }

    public CheckBoxText(Context context) {
        super(context);
        build();
    }

    public CheckBoxText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
        build();
    }

    public CheckBoxText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttrs(context, attrs);
        build();
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckBoxText, 0, 0);

        try {
            mTextOrientation = a.getInt(R.styleable.CheckBoxText_orientation, TEXT_RIGHT);
            mTextSize = a.getDimensionPixelSize(R.styleable.CheckBoxText_textSize, -1);
            // getColor can not handle references (They can be color state lists)
            mTextColor = a.getColorStateList(R.styleable.CheckBoxText_textColor);
            mTextStyle = a.getInt(R.styleable.CheckBoxText_textStyle, -1);
            mText = a.getString(R.styleable.CheckBoxText_text);
            mCheckboxBackground = a.getDrawable(R.styleable.CheckBoxText_checkboxBackground);
        } finally {
            a.recycle();
        }

    }

    /**
     * Orientations to pick from
     * <p/>
     * TEXT_RIGHT = 0;
     * TEXT_LEFT = 1;
     * TEXT_ABOVE = 2;
     * TEXT_BELOW = 3;
     */
    private void build() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (mTextOrientation == TEXT_BELOW) {
            inflater.inflate(R.layout.checkbox_text_below, this, true);
        } else if (mTextOrientation == TEXT_LEFT) {
            inflater.inflate(R.layout.checkbox_text_left, this, true);
        } else if (mTextOrientation == TEXT_ABOVE) {
            inflater.inflate(R.layout.checkbox_text_above, this, true);
        } else if (mTextOrientation == TEXT_RIGHT) {
            inflater.inflate(R.layout.checkbox_text_right, this, true);
        } else {
            // default to right orientation
            inflater.inflate(R.layout.checkbox_text_right, this, true);
        }

        mCheckBox = (CheckBox) findViewById(R.id.checkbox_text_box);
        mTextView = (TextView) findViewById(R.id.checkbox_text_text);
        // make everything clickable
        setOnClickListener(this);
        mCheckBox.setOnClickListener(this);
        mTextView.setOnClickListener(this);

        // set the attrs that were passed in
        if (mTextSize > 0) {
            setTextSize(mTextSize);
        }

        if (mTextStyle > 0) {
            setTypeface(null, mTextStyle);
        }

        if (mTextColor != null) {
            setTextColor(mTextColor);
        }

        if (!TextUtils.isEmpty(mText)) {
            setText(mText);
        }

        if (mCheckboxBackground != null) {
            setBackgroundCheckBox(mCheckboxBackground);
        }

        // set the mCheckBox tag to the mTextView text
        mCheckBox.setTag(mTextView.getText().toString());
        // set the checkbox text to blank
        mCheckBox.setText("");
    }


    @Override
    public void setChecked(boolean checked) {
        // forward to the checkbox
        mCheckBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        // forward to the checkbox
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {
        // forward to the checkbox
        mCheckBox.toggle();
    }

    @Override
    public void onClick(View v) {
        // check the mCheckBox, it handles its self
        if (v != mCheckBox) {
            // forward the onClicks to the toggle
            this.toggle();
        }
    }


    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     * <p/>
     * Forwards the CompoundButton.OnCheckedChangeListener to the checkbox.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
        mCheckBox.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    /**
     * Sets the button text and the mCheckBox tag to that text
     *
     * @param text
     */
    public void setText(String text) {
        mText = text;
        mTextView.setText(text);
        mCheckBox.setTag(text);
    }

    /**
     * Sets the button text with a string resId and the mCheckBox tag to that text
     *
     * @param resId
     */
    public void setText(int resId) {
        mTextView.setText(resId);
        String text = getContext().getString(resId);
        mText = text;
        mCheckBox.setTag(text);
    }

    public CharSequence getText() {
        return mTextView.getText();
    }

    /**
     * @param tf
     * @link TextView.setTypeface(Typeface tf)
     */
    public void setTypeface(Typeface tf) {
        mTextView.setTypeface(tf);
    }

    public void setTypeface(Typeface tf, int style) {
        mTextView.setTypeface(tf, style);
    }
    /**
     * @param size
     * @link TextView.setTextSize(float size)
     */
    public void setTextSize(float size) {
        mTextView.setTextSize(size);
    }


    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    public void setTextColor(ColorStateList textColorStateList) {
        mTextColor = textColorStateList;
        mTextView.setTextColor(textColorStateList);
    }

    /**
     * @param size
     * @link TextView.setTextSize(int unit, float size)
     */
    public void setTextSize(int unit, float size) {
        mTextSize = size;
        mTextView.setTextSize(unit, size);
    }

    public void setBackgroundCheckBox(int resId) {
        mCheckBox.setBackgroundResource(resId);
    }

    @SuppressWarnings("NewApi")
    /**
     * @link CheckBox.setBackground(Drawable background)
     *
     * @parama background
     */
    public void setBackgroundCheckBox(Drawable background) {
        mCheckboxBackground = background;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mCheckBox.setBackground(background);
        } else {
            mCheckBox.setBackgroundDrawable(background);
        }
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @link CheckBox.setPadding(int left, int top, int right, int bottom)
     */
    public void setPaddingCheckBox(int left, int top, int right, int bottom) {
        mCheckBox.setPadding(left, top, right, bottom);
    }
}
