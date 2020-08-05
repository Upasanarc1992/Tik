package com.joahquin.app.tik.Utils.CustomViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.joahquin.app.tik.R;
import com.joahquin.app.tik.Utils.BasicUtils;
import com.joahquin.app.tik.Utils.MonitorVariable;


public class LabelView extends LinearLayout {

    private static boolean LABEL_VISIBLE = false;
    private static boolean ICON_VISIBLE = false;

    public static final int INPUT_TEXT = 0;
    public static final int INPUT_NUMBER = 1;
    public static final int INPUT_DECIMAL = 2;
    public static final int INPUT_DATE_TIME = 3;
    public static final int INPUT_SPINNER = 4;


    LinearLayout llContainer;
    int icon;
    boolean showIcon;
    boolean showLabel;
    boolean showSuffix;
    boolean showPrefix;
    String label;
    String hintText;
    int defaultColor;

    Context context;
    BasicUtils basicUtils;

    LinearLayout llLabel;
    LinearLayout llText;
    ImageView ivIcon;
    TextView tvLabel;
    TextView tvTopLabel;
    AutoCompleteTextView atvText;
    DateTimeEditText dtText;
    TextView tvSuffix;
    TextView tvPrefix;
    TextView tvError;
    View vDivider;

    float textSize;
    float padding;
    int textGravity;
    int inputType;
    int labelType;
    int labelColor;
    int iconTint;
    int iconSize;
    int minLines;
    boolean allowAdapter;
    boolean allowEdit;
    boolean hideBorder;
    MonitorVariable isErrorShowing;

    public static final int LABEL_TOP = 0;
    public static final int LABEL_LEFT = 1;

    public static final int ICON_SMALL = 0;
    public static final int ICON_SMALL_LEFT = 2;
    public static final int ICON_NORMAL = 1;

    OnFocusChangeListener focusChangeListener;
    OnTouchListener touchListener;

    public static final String TAG = "LabelView";

    public LabelView(Context context) {
        this(context, null);
        this.context = context;
    }

    public LabelView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        basicUtils = new BasicUtils(context);
        defaultColor = R.color.colorText;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.labelView);
        this.icon = typedArray.getResourceId(R.styleable.labelView_lablIcon, 0);
        this.showIcon = typedArray.getBoolean(R.styleable.labelView_showIcon, ICON_VISIBLE);
        this.showLabel = typedArray.getBoolean(R.styleable.labelView_showLabel, LABEL_VISIBLE);
        this.label = typedArray.getString(R.styleable.labelView_label);
        this.hintText = typedArray.getString(R.styleable.labelView_hintText);
        this.textSize = typedArray.getDimension(R.styleable.labelView_size, basicUtils.getDpsInPixels(15));
        this.padding = typedArray.getDimension(R.styleable.labelView_inernalPadding, 5);
        this.textGravity = typedArray.getInt(R.styleable.labelView_textGravity, Gravity.LEFT);
        this.inputType = typedArray.getInt(R.styleable.labelView_inputType, INPUT_TEXT);
        this.labelType = typedArray.getInt(R.styleable.labelView_labelType, LABEL_LEFT);
        this.iconSize = typedArray.getInt(R.styleable.labelView_iconType, ICON_NORMAL);
        this.iconTint = typedArray.getColor(R.styleable.labelView_iconTint, 0);
        this.labelColor = typedArray.getColor(R.styleable.labelView_labelColor, 0);
        this.allowAdapter = typedArray.getBoolean(R.styleable.labelView_allowAdapter, false);
        this.minLines = typedArray.getInteger(R.styleable.labelView_minLines, 1);
        this.allowEdit = typedArray.getBoolean(R.styleable.labelView_allowEdit, true);
        this.hideBorder = typedArray.getBoolean(R.styleable.labelView_hideBorder, false);

        typedArray.recycle();
        setUpView();
    }

    private void setUpView() {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        View v = View.inflate(context, R.layout.item_label_view, linearLayout);

        llContainer = v.findViewById(R.id.llContainer);
        llLabel = v.findViewById(R.id.llLabel);
        llText = v.findViewById(R.id.llText);
        ivIcon = v.findViewById(R.id.ivIcon);
        tvLabel = v.findViewById(R.id.tvLabel);
        tvTopLabel = v.findViewById(R.id.tvTopLabel);
        dtText = v.findViewById(R.id.dtText);
        atvText = v.findViewById(R.id.atvText);
        vDivider = v.findViewById(R.id.vDivider);
        tvSuffix = v.findViewById(R.id.tvSuffix);
        tvPrefix = v.findViewById(R.id.tvPrefix);
        tvError = v.findViewById(R.id.tvError);

        dtText.setDefault();
        dtText.setDate(null);
        dtText.setTint(ContextCompat.getColor(context, R.color.white));
        dtText.setVisibility(GONE);

        ivIcon.setVisibility(showIcon ? VISIBLE : GONE);
        tvLabel.setVisibility((showLabel && labelType==LABEL_LEFT) ? VISIBLE : GONE);
        tvTopLabel.setVisibility((showLabel && labelType==LABEL_TOP) ? VISIBLE : GONE);
        vDivider.setVisibility(showIcon || (showLabel && labelType == LABEL_LEFT) ? VISIBLE : GONE);

        tvLabel.setText(label);
        tvTopLabel.setText(label);
        atvText.setHint(hintText);

        isErrorShowing = new MonitorVariable();
        isErrorShowing.setBoolValue(false);

        ivIcon.setImageResource(icon);
        if(iconTint!= 0)
            ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(iconTint));

        dtText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        atvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        if(labelColor!= 0)
            tvLabel.setTextColor(ColorStateList.valueOf(labelColor));

        int paddingPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,padding, getResources().getDisplayMetrics());
        llContainer.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        this.addView(linearLayout);

        llText.setGravity(textGravity| Gravity.CENTER_VERTICAL);
        atvText.setGravity(textGravity| Gravity.CENTER_VERTICAL);
        tvTopLabel.setGravity(textGravity| Gravity.CENTER_VERTICAL);

        setInputType(inputType);

        switch (iconSize) {
            case ICON_SMALL:
                ivIcon.setScaleX(0.9f);
                ivIcon.setScaleY(0.9f);
                llLabel.setPadding(
                        basicUtils.getDpsInPixels(2),
                        basicUtils.getDpsInPixels(2),
                        basicUtils.getDpsInPixels(5),
                        basicUtils.getDpsInPixels(2)
                );
                llLabel.setGravity(Gravity.TOP);
                vDivider.setVisibility(GONE);
                break;

            case ICON_SMALL_LEFT:
                ivIcon.setScaleX(0.9f);
                ivIcon.setScaleY(0.9f);
                llLabel.setPadding(
                        basicUtils.getDpsInPixels(2),
                        basicUtils.getDpsInPixels(2),
                        basicUtils.getDpsInPixels(5),
                        basicUtils.getDpsInPixels(2)
                );
                llLabel.setGravity(Gravity.CENTER_VERTICAL);
                break;

            case ICON_NORMAL:
                ivIcon.setScaleX(1f);
                llLabel.setPadding(
                        basicUtils.getDpsInPixels(10),
                        basicUtils.getDpsInPixels(10),
                        basicUtils.getDpsInPixels(10),
                        basicUtils.getDpsInPixels(10)
                );
                ivIcon.setScaleY(1f);
                break;

        }

        atvText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && showSuffix)
                    tvSuffix.setVisibility(VISIBLE);
                else
                    tvSuffix.setVisibility(GONE);

                if (editable.length() > 0 && showPrefix)
                    tvPrefix.setVisibility(VISIBLE);
                else
                    tvPrefix.setVisibility(GONE);
            }
        });

        if (!allowAdapter) {
            atvText.setAdapter(null);
        } else {
            atvText.setThreshold(1);

            atvText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    atvText.showDropDown();
                }
            });

            atvText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b)
                        atvText.showDropDown();
                }
            });
        }

        atvText.setLines(minLines);
        if(minLines > 1){
            atvText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            atvText.setSingleLine(false);
        }
        else
            atvText.setSingleLine(true);

        if(hideBorder)
            llContainer.setBackground(null);

        isErrorShowing.setChangeListener(new MonitorVariable.ChangeListener() {
            @Override
            public void onChange() {
                if(isErrorShowing.isBoolValue())
                {
                    tvError.setVisibility(VISIBLE);

                    focusChangeListener = new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            tvError.setVisibility(GONE);
                        }
                    };

                    touchListener = new OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            tvError.setVisibility(GONE);
                            return false;
                        }
                    };

                    llContainer.setOnTouchListener(touchListener);
                    atvText.setOnFocusChangeListener(focusChangeListener);
                }
                else
                {
                    focusChangeListener = null;
                    touchListener = null;
                    llContainer.setOnTouchListener(null);
                    atvText.setOnFocusChangeListener(null);
                    tvError.setVisibility(GONE);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !allowEdit;
    }

    public ImageView getIcon(){
        return ivIcon;
    }

    public TextView getLabel(){
        return tvLabel;
    }

    public AutoCompleteTextView getData(){
        return atvText;
    }

    public DateTimeEditText getDTData(){
        return dtText;
    }

    public String getText(){
        return atvText.getText().toString();
    }

    public void setText(String text){
        atvText.setText(text);
    }

    public void setSuffix(String suffix){
        tvSuffix.setText(suffix);
        showSuffix = true;
    }

    public void setPrefix(String prefix){
        tvPrefix.setText(prefix);
        showPrefix = true;
    }

    public void setError(String error){
        tvError.setText(String.valueOf(error));
        isErrorShowing.setBoolValue(true);
    }

    public void setLabel(String label){
        tvLabel.setText(label);
        tvTopLabel.setText(label);
    }

    public void setEditAllow(boolean allowEdit){
        this.allowEdit = allowEdit;
    }

    public void setInputType(int inputType){
        this.inputType = inputType;

        dtText.setVisibility(GONE);
        atvText.setVisibility(VISIBLE);
        switch (inputType) {
            case INPUT_TEXT:
                atvText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;

            case INPUT_NUMBER:
                atvText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;

            case INPUT_DECIMAL:
                atvText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); //for decimal numbers
                break;

            case INPUT_DATE_TIME:
                atvText.setVisibility(GONE);
                dtText.setVisibility(VISIBLE);
                break;

            case INPUT_SPINNER:
                setSpinner();
                break;
        }
    }

    private void setSpinner() {
        allowEdit = false;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                atvText.showDropDown();
            }
        });
    }
}