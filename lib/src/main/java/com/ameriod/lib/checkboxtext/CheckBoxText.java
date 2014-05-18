package com.ameriod.lib.checkboxtext;

/*
 * Copyright (C) 2010 Parker Williams
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
 * CheckBoxText is a simple view that will allow the text of the CheckBox to be set to the
 * right, left, above or below the CheckBox (there really is not any use for the left or right
 * orientations due to the actual CheckBox and CheckTextView but they are include just in case).
 * <p/>
 * CheckBoxText view implements Checkable and uses the CompoundButton.OnCheckedChangeListener just
 * like the real CheckBox view. However, within the OnCheckChangeListener the compoundButton.getText()
 * will return an empty string. I have forwarded the actual text showing by the CheckBoxText into the
 * tag of the compoundButton (this is automatically done).
 * <p/>
 * Some of the CheckBox and TextView xml attributes are forwarded when setting the view from xml,
 * there are corresponding methods to everything but the setting of the internal margins of the
 * TextView and CheckBox.
 * <p/>
 * For the TextView:
 * textSize
 * textStyle
 * typeface
 * textColor
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
 * For the CheckBox
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

    private int mTextOrientation;
    private float mTextSize;
    private int mTextStyle;
    private int mTypefaceIndex;
    private String mText;
    private ColorStateList mTextColor;
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

    //From TextView typeface attr and constants
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;


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
            // TextView attributes which are forwarded on to the TextView, all values match to the
            // equivalent TextView attributes
            mTextOrientation = a.getInt(R.styleable.CheckBoxText_orientation, TEXT_RIGHT);
            mTextSize = a.getDimensionPixelSize(R.styleable.CheckBoxText_textSize, -1);
            // getColor can not handle references (They can be color state lists and single colors)
            mTextColor = a.getColorStateList(R.styleable.CheckBoxText_textColor);
            mTextStyle = a.getInt(R.styleable.CheckBoxText_textStyle, -1);
            mText = a.getString(R.styleable.CheckBoxText_text);
            mTypefaceIndex = a.getInt(R.styleable.CheckBoxText_typeface, -1);
            // CheckBox attributes which are forwarded on to the CheckBox
            mCheckBoxBackground = a.getDrawable(R.styleable.CheckBoxText_checkboxBackground);
            mIsChecked = a.getBoolean(R.styleable.CheckBoxText_isChecked, false);
            // padding
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
            // margins
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
     *
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
            paramsText.addRule(RelativeLayout.BELOW, mCheckBox.getId());
        } else if (mTextOrientation == TEXT_LEFT) {
            if (mTextMarginLeft == 0) {
                mTextMarginRight = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_horizontal_left);
            }
            paramsCheckBox.addRule(RelativeLayout.RIGHT_OF, mTextView.getId());
            paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else if (mTextOrientation == TEXT_ABOVE) {
            if (mTextMarginBottom == 0) {
                mTextMarginBottom = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_vertical_above);
            }
            paramsCheckBox.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsText.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsCheckBox.addRule(RelativeLayout.BELOW, mTextView.getId());
        } else {
            // default to right orientation
            if (mTextMarginRight == 0) {
                mTextMarginRight = getResources().getDimensionPixelSize(R.dimen.checkbox_text_margin_horizontal_right);
            }
            paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsText.addRule(RelativeLayout.RIGHT_OF, mCheckBox.getId());
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

        // Set the attrs that were passed in for the TextView and the CheckBox
        if (mTextSize > 0) {
            setTextSize(mTextSize);
        }

        Typeface typeface = getTypefaceByIndex(mTypefaceIndex);

        if (mTextStyle > 0) {
            setTypeface(typeface, mTextStyle);
        } else {
            // no textStyle so set to normal but only do this when the textStyle has not been set
            if (typeface != null) {
                setTypeface(typeface, Typeface.NORMAL);
            }
        }

        if (mTextColor != null) {
            setTextColor(mTextColor);
        }

        if (!TextUtils.isEmpty(mText)) {
            setText(mText);
        }

        if (mCheckBoxBackground != null) {
            setBackgroundCheckBox(mCheckBoxBackground);
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
    }

    /**
     * Gets the TypeFace for the int value of the typefaceIndex from the xml attributes
     *
     * @param typefaceIndex
     * @return
     */
    private Typeface getTypefaceByIndex(int typefaceIndex) {
        Typeface tf = null;
        switch (typefaceIndex) {
            case SANS:
                tf = Typeface.SANS_SERIF;
                break;

            case SERIF:
                tf = Typeface.SERIF;
                break;

            case MONOSPACE:
                tf = Typeface.MONOSPACE;
                break;
        }
        return tf;
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
        String text = getContext().getString(resId);
        mCheckBox.setTag(text);
    }

    /**
     * Gets the TextView's getText() method
     *
     * @return
     */
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
     * @param tf
     * @param style
     * @link TextView.setTypeface(Typeface tf, int style)
     */
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

    /**
     * Forwards the TextView.getTextSize
     *
     * @return
     */
    public float getTextSize() {
        return mTextView.getTextSize();
    }

    /**
     * Forwards on the the TextView.setTextColor(int color)
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

    /**
     * Forwards on to the TextView.setTextColor(ColorStateList textColorStateList)
     *
     * @param textColorStateList
     */
    public void setTextColor(ColorStateList textColorStateList) {
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

    public Drawable getBackgroundCheckBox() {
        return mCheckBox.getBackground();
    }

    @SuppressWarnings("NewApi")
    /**
     * Depending on the API level, sets the background drawable of the CheckBox by:
     * @link CheckBox.setBackground(Drawable background)
     * @link CheckBox.setBackgroundDrawable(Drawable background)
     *
     * @parama background
     */
    public void setBackgroundCheckBox(Drawable background) {
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

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @link {TextView.setPadding(int left, int top, int right, int bottom)}
     */
    public void setPaddingText(int left, int top, int right, int bottom) {
        mTextView.setPadding(left, top, right, bottom);
    }
}