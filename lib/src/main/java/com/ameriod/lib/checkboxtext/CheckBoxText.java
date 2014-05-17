package com.ameriod.lib.checkboxtext;

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

    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;


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

            mTypefaceIndex = a.getInt(R.styleable.CheckBoxText_typeface, -1);
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
        inflater.inflate(R.layout.checkbox_text_layout, this, true);
        // need to check if the margins were set or not if not then set to default, we can set the
        // margins via xml attributes
        mTextView = new TextView(getContext());
        mCheckBox = new CheckBox(getContext());

        RelativeLayout.LayoutParams paramsCheckBox = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsCheckBox.setMargins(mCheckBoxMarginLeft, mCheckBoxMarginTop, mCheckBoxMarginRight, mCheckBoxMarginBottom);

        RelativeLayout.LayoutParams paramsText = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsText.setMargins(mTextMarginLeft, mTextMarginTop, mTextMarginRight, mTextMarginBottom);

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

        setChecked(mIsChecked);

        // set the mCheckBox tag to the mTextView text
        mCheckBox.setTag(getText().toString());
        // set the checkbox text to blank
        mCheckBox.setText("");
    }

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
        String text = getContext().getString(resId);
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

    public float getTextSize() {
        return mTextView.getTextSize();
    }

    public void setTextColor(int color) {
        mTextView.setTextColor(color);
    }

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
     * @link CheckBox.setBackground(Drawable background)
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