package com.ameriod.checkboxtext.lib;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

    /**
     * Listener to forward CompoundButton onCheckChangeListener stuff to the checkbox
     */
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    /**
     * Orients the text to the right of the checkbox
     */
    public static final String TEXT_RIGHT = "right";
    /**
     * Orients the text to the left of the checkbox
     */
    public static final String TEXT_LEFT = "left";
    /**
     * Orients the text above of the checkbox
     */
    public static final String TEXT_ABOVE = "above";
    /**
     * Orients the text below the checkbox
     */
    public static final String TEXT_BELOW = "below";

    /**
     * For debugging
     */
    private static final String TAG = "CheckBoxText";

    public CheckBoxText(Context context, String orientation) {
        super(context);
        build(orientation);
    }

    public CheckBoxText(Context context) {
        super(context);
        build(TEXT_RIGHT);
    }

    public CheckBoxText(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(TEXT_RIGHT);
    }

    public CheckBoxText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        build(TEXT_RIGHT);
    }

    private void build(String orientation) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (TextUtils.equals(orientation, TEXT_BELOW)) {
            inflater.inflate(R.layout.checkbox_text_bottom, this, true);
        } else if (TextUtils.equals(orientation, TEXT_LEFT)) {
            inflater.inflate(R.layout.checkbox_text_left, this, true);
        } else if (TextUtils.equals(orientation, TEXT_ABOVE)) {
            inflater.inflate(R.layout.checkbox_text_top, this, true);
        } else if (TextUtils.equals(orientation, TEXT_RIGHT)) {
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
        mCheckBox.setTag(getContext().getString(resId));
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

    /**
     * @param size
     * @link TextView.setTextSize(int unit, float size)
     */
    public void setTextSize(int unit, float size) {
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
        mCheckBox.setBackground(background);
    }

    /**
     * @link CheckBox.setBackgroundDrawablCheckBox(Drawable background)
     * <p/>
     * Use setBackGroundCheckBox instead
     * @parama background
     */
    @Deprecated
    public void setBackgroundDrawablCheckBox(Drawable background) {
        setBackgroundDrawable(background);
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
