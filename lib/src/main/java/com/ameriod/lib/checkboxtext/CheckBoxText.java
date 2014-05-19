package com.ameriod.lib.checkboxtext;

/*
 * Copyright (C) 2014 Parker Williams
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
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
 * CheckBoxText is a simple view that will allow the text of the CheckBox to be set to the
 * right, left, above or below the CheckBox (there really is not any use for the left or right
 * orientations due to the actual CheckBox and CheckTextView but they are include just in case).
 * <p/>
 * CheckBoxText view implements Checkable and uses the CompoundButton.OnCheckedChangeListener just
 * like the real CheckBox view. However, within the OnCheckChangeListener the compoundButton.getText()
 * will return an empty string. I have forwarded the actual text showing by the CheckBoxText into the
 * tag of the compoundButton (this is automatically done).
 * <p/>
 * Some of the CheckBox (setting of the background and isChecked)and TextView (setting the text and
 * the TextAppearance) xml attributes are forwarded when setting the view from xml, there are
 * corresponding methods to everything but the setting of the internal margins of the TextView and
 * CheckBox. To set the TextAppearance create a style and any TextView xml attributes can be set.
 * <p/>
 * TextView xml attributes:
 * text (sets the text)
 * padding
 * paddingLeft
 * paddingRight
 * paddingTop
 * paddingBottom
 * layout_margin (text_margin)
 * layout_marginLeft (text_marginLeft)
 * layout_marginRight( text_marginRight)
 * layout_marginTop (text_marginTop)
 * layout_marginBottom (text_marginBottom)
 * <p/>
 * CheckBox xml attributes:
 * isChecked
 * background (checkbox_background)
 * padding
 * paddingLeft
 * paddingRight
 * paddingTop
 * paddingBottom
 * layout_margin (checkbox_margin)
 * layout_marginLeft (checkbox_marginLeft)
 * layout_marginRight(checkbox_marginRight)
 * layout_marginTop (checkbox_marginTop)
 * layout_marginBottom (checkbox_marginBottom)
 * <p/>
 * Setting the margins will mess up the views but since CheckBox view seems to behave differently with
 * each API level (the default CheckBox drawables have different amounts of padding on them) the
 * margins may need to be adjusted. Also using custom checkbox drawables could mess up the TextView
 * and CheckBox placement.
 * <p/>
 */
public class CheckBoxText extends RelativeLayout implements Checkable, View.OnClickListener {

    private CheckBox mCheckBox;
    private TextView mTextView;

    private int mTextAppearanceResId;
    private int mTextOrientation;
    private String mText;
    private Drawable mCheckBoxBackground;

    private int mCheckBoxPadding;
    private int mCheckBoxPaddingLeft;
    private int mCheckBoxPaddingRight;
    private int mCheckBoxPaddingTop;
    private int mCheckBoxPaddingBottom;

    private int mTextPadding;
    private int mTextPaddingLeft;
    private int mTextPaddingRight;
    private int mTextPaddingTop;
    private int mTextPaddingBottom;

    private int mCheckBoxMargin;
    private int mCheckBoxMarginLeft;
    private int mCheckBoxMarginRight;
    private int mCheckBoxMarginTop;
    private int mCheckBoxMarginBottom;

    private int mTextMargin;
    private int mTextMarginLeft;
    private int mTextMarginRight;
    private int mTextMarginTop;
    private int mTextMarginBottom;

    private boolean mIsChecked;

    /**
     * Listener to forward CompoundButton onCheckChangeListener stuff to the actual CheckBox
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
     * Need to use this constructor when setting the CheckBoxText View in a layout programmatically
     * to set the orientation
     * <p/>
     * Orientations to pick from:
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

    /**
     * Defaults to orientation = TEXT_RIGHT, use the @link{CheckBoxText(Context context, int orientation}
     * for the other orientations
     *
     * @param context
     */
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
            // The orientation of the text around the CheckBox
            mTextOrientation = a.getInt(R.styleable.CheckBoxText_orientation, TEXT_RIGHT);

            // TextView styling
            mTextAppearanceResId = a.getResourceId(R.styleable.CheckBoxText_textAppearance, android.R.style.TextAppearance_Small);
            // The actual text set in the TextView
            mText = a.getString(R.styleable.CheckBoxText_text);

            // CheckBox background
            mCheckBoxBackground = a.getDrawable(R.styleable.CheckBoxText_checkboxBackground);
            // boolean for the checking of the CheckBox
            mIsChecked = a.getBoolean(R.styleable.CheckBoxText_isChecked, false);

            // Internal padding
            mCheckBoxPadding = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_padding, 0);
            mCheckBoxPaddingLeft = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_paddingLeft, 0);
            mCheckBoxPaddingRight = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_paddingRight, 0);
            mCheckBoxPaddingBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_paddingTop, 0);
            mCheckBoxPaddingBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_paddingBottom, 0);
            mTextPadding = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_padding, 0);
            mTextPaddingLeft = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_paddingLeft, 0);
            mTextPaddingRight = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_paddingRight, 0);
            mTextPaddingBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_paddingTop, 0);
            mTextPaddingBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_paddingBottom, 0);

            // Internal margins
            mCheckBoxMargin = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_margin, 0);
            mCheckBoxMarginLeft = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_marginLeft, 0);
            mCheckBoxMarginRight = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_marginRight, 0);
            mCheckBoxMarginBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_marginTop, 0);
            mCheckBoxMarginBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_checkbox_marginBottom, 0);
            mTextMargin = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_margin, 0);
            mTextMarginLeft = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_marginLeft, 0);
            mTextMarginRight = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_marginRight, 0);
            mTextMarginBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_marginTop, 0);
            mTextMarginBottom = a.getDimensionPixelSize(R.styleable.CheckBoxText_text_marginBottom, 0);
        } finally {
            a.recycle();
        }

    }

    /**
     * Well.. Builds everything. Sets the orientations of the TextView and CheckBox and sets xml
     * view attributes on them. Sets the onClickListeners so any click on the view will be forwarded on to the CheckBox.
     */
    private void build() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.checkbox_text_layout, this, true);

        // set the layout params programmatically due to setting the margins in the xml attributes
        RelativeLayout.LayoutParams paramsCheckBox = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsText = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        // need to check if the margins were set in xml or  if not then set to default
        if (mTextOrientation == TEXT_BELOW) {
            if (mCheckBoxMarginBottom == 0) {
                mCheckBoxMarginBottom = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_vertical_below);
            }
            paramsCheckBox.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsText.addRule(RelativeLayout.BELOW, R.id.checkbox_text_box);
        } else if (mTextOrientation == TEXT_LEFT) {
            if (mTextMarginLeft == 0) {
                mTextMarginRight = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_horizontal_left);
            }
            paramsCheckBox.addRule(RelativeLayout.RIGHT_OF, R.id.checkbox_text_text);
            paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mTextOrientation == TEXT_ABOVE) {
            if (mTextMarginBottom == 0) {
                mTextMarginBottom = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_vertical_above);
            }
            paramsCheckBox.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsCheckBox.addRule(RelativeLayout.BELOW, R.id.checkbox_text_text);
        } else {
            // default to right orientation
            if (mTextMarginRight == 0) {
                mTextMarginRight = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_horizontal_right);
            }
            paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.RIGHT_OF, R.id.checkbox_text_box);
        }

        // set the margins
        paramsText.setMargins(mTextMarginLeft, mTextMarginTop, mTextMarginRight, mTextMarginBottom);
        paramsCheckBox.setMargins(mCheckBoxMarginLeft, mCheckBoxMarginTop, mCheckBoxMarginRight, mCheckBoxMarginBottom);

        mCheckBox = (CheckBox) findViewById(R.id.checkbox_text_box);
        mTextView = (TextView) findViewById(R.id.checkbox_text_text);

        // make everything clickable
        setOnClickListener(this);
        mCheckBox.setOnClickListener(this);
        mTextView.setOnClickListener(this);

        // set the TextView's TextAppearance
        setTextAppearance(getContext(), mTextAppearanceResId);

        if (!TextUtils.isEmpty(mText)) {
            setText(mText);
        }

        if (mCheckBoxBackground != null) {
            setCheckBoxDrawable(mCheckBoxBackground);
        }

        setChecked(mIsChecked);

        if (mCheckBoxMarginBottom == 0) {
            mCheckBoxPaddingBottom = mCheckBoxPadding;
        }

        if (mCheckBoxPaddingTop == 0) {
            mCheckBoxPaddingTop = mCheckBoxPadding;
        }

        if (mCheckBoxPaddingRight == 0) {
            mCheckBoxPaddingRight = mCheckBoxPadding;
        }

        if (mCheckBoxPaddingLeft == 0) {
            mCheckBoxPaddingLeft = mCheckBoxPadding;
        }

        setPaddingCheckBox(mCheckBoxPaddingBottom, mCheckBoxPaddingTop, mCheckBoxPaddingRight, mCheckBoxPaddingBottom);

        if (mTextPaddingBottom == 0) {
            mTextPaddingBottom = mTextPadding;
        }

        if (mTextPaddingTop == 0) {
            mTextPaddingTop = mTextPadding;
        }

        if (mTextPaddingRight == 0) {
            mTextPaddingRight = mTextPadding;
        }

        if (mTextPaddingLeft == 0) {
            mTextPaddingLeft = mTextPadding;
        }

        setPaddingText(mTextPaddingBottom, mTextPaddingTop, mTextPaddingRight, mTextMarginBottom);

        if (mCheckBoxMarginBottom == 0) {
            mCheckBoxMarginBottom = mCheckBoxMargin;
        }

        if (mCheckBoxMarginTop == 0) {
            mCheckBoxMarginTop = mCheckBoxMargin;
        }

        if (mCheckBoxMarginRight == 0) {
            mCheckBoxMarginRight = mCheckBoxMargin;
        }

        if (mCheckBoxMarginLeft == 0) {
            mCheckBoxMarginLeft = mCheckBoxMargin;
        }

        mCheckBox.setLayoutParams(paramsCheckBox);

        if (mTextMarginBottom == 0) {
            mTextMarginBottom = mTextMargin;
        }
        if (mTextMarginTop == 0) {
            mTextMarginTop = mTextMargin;
        }
        if (mTextMarginRight == 0) {
            mTextMarginRight = mTextMargin;
        }
        if (mTextMarginLeft == 0) {
            mTextMarginLeft = mTextPadding;
        }

        mTextView.setLayoutParams(paramsText);

        // set the mCheckBox tag to the mTextView text
        mCheckBox.setTag(getText().toString());
        // set the checkbox text to blank
        mCheckBox.setText("");
        // stop error in viewing the layouts in xml
        isInEditMode();
    }

    @Override
    /**
     * Sets the CheckBox check
     * {@link android.widget.CheckBox}
     */
    public void setChecked(boolean checked) {
        // forward to the checkbox
        mCheckBox.setChecked(checked);
    }

    @Override
    /**
     * Returns if the CheckBox is checked
     * {@link android.widget.CheckBox}
     */
    public boolean isChecked() {
        // forward to the checkbox
        return mCheckBox.isChecked();
    }

    @Override
    /**
     * Toggles the CheckBox
     * {@link android.widget.CheckBox}
     */
    public void toggle() {
        // forward to the checkbox
        mCheckBox.toggle();
    }

    @Override
    public void onClick(View v) {
        // check for the CheckBox, it will handle itself
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
     * {@link android.widget.TextView}
     *
     * @param text
     */
    public void setText(String text) {
        mTextView.setText(text);
        mCheckBox.setTag(text);
    }

    /**
     * Sets the button text with a string resId and the mCheckBox tag to that text
     * {@link android.widget.TextView}
     *
     * @param resId
     */
    public void setText(int resId) {
        mTextView.setText(resId);
        String text = getContext().getString(resId);
        mCheckBox.setTag(text);
    }

    /**
     * Gets the TextView.getText() method
     * {@link android.widget.TextView}
     *
     * @return
     */
    public CharSequence getText() {
        return mTextView.getText();
    }

    /**
     * Forwards to TextView.setTypeface
     *
     * @param tf {@link android.widget.TextView}
     */
    public void setTypeface(Typeface tf) {
        mTextView.setTypeface(tf);
    }

    /**
     * Forwards to TextView.setTypeface
     * {@link android.widget.TextView}
     *
     * @param tf
     * @param style
     */
    public void setTypeface(Typeface tf, int style) {
        mTextView.setTypeface(tf, style);
    }

    /**
     * Forwards to TextView.setTextSize
     *
     * @param size {@link android.widget.TextView}
     */
    public void setTextSize(float size) {
        mTextView.setTextSize(size);
    }

    /**
     * Forwards the TextView.getTextSize
     * {@link android.widget.TextView}
     *
     * @return
     */
    public float getTextSize() {
        return mTextView.getTextSize();
    }

    /**
     * Forwards on the the TextView.setTextColor(int color)
     * {@link android.widget.TextView}
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    /**
     * Sets the TextView color by a ColorStateList{@link android.widget.TextView}
     *
     * @param textColorStateList
     */
    public void setTextColor(ColorStateList textColorStateList) {
        mTextView.setTextColor(textColorStateList);
    }

    /**
     * Forwards to TextView.setTextSize
     * {@link android.widget.TextView}}
     *
     * @param size
     */
    public void setTextSize(int unit, float size) {
        //mTextSize = size;
        mTextView.setTextSize(unit, size);
    }

    /**
     * Forwards to the TextView.setTextAppearance
     * {@link android.widget.TextView}}
     *
     * @param context
     * @param resId
     */
    public void setTextAppearance(Context context, int resId) {
        mTextView.setTextAppearance(context, resId);
    }

    /**
     * Sets the CheckBox buttonBackground with a resourceId
     * {@link android.widget.CheckBox}
     *
     * @param resId
     */
    public void setCheckBoxDrawable(int resId) {
        mCheckBox.setButtonDrawable(resId);
    }

    /**
     * Sets the buttonDrawable of the CheckBox
     * {@link android.widget.CheckBox}
     *
     * @parama background
     */
    public void setCheckBoxDrawable(Drawable checkBoxDrawable) {
        mCheckBox.setButtonDrawable(checkBoxDrawable);
    }

    /**
     * Set the internal Padding of the CheckBox
     * {@link android.widget.CheckBox}
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPaddingCheckBox(int left, int top, int right, int bottom) {
        mCheckBox.setPadding(left, top, right, bottom);
    }

    /**
     * Sets the Internal Padding of the TextView
     * {@link android.widget.TextView}
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPaddingText(int left, int top, int right, int bottom) {
        mTextView.setPadding(left, top, right, bottom);
    }
}