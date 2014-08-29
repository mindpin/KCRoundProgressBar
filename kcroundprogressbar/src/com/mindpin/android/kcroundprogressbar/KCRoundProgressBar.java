package com.mindpin.android.kcroundprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.mindpin.android.kcroundprogressbar.common.DisplayModule;

public class KCRoundProgressBar extends View {
//    private static final String TAG = "KCRoundProgressBar";
    private static final int TRANSPARENT = 0x00000000;
    private static final int DEFAULT_MIN = 0;
    private static final String TAG = "KCRoundProgressBar";
    private int min = DEFAULT_MIN, max = DEFAULT_MAX, current = DEFAULT_MIN;
    private static final int DEFAULT_MAX = 100;
    // 显示border时，border厚度恒为3
    private static final int BORDER_WIDTH = 3;
    // 显示border时，border间距恒为2
    private static final int BORDER_SPACING = 2;
    private int progress = 0;
    boolean isSpinning = false;
    private float scale = 0.3f;
    //    int width, height;
    private boolean bTextDisplay = false, bBorderDisplay = false;
    private int startAngle = 0;
    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 0; // 默认为宽度1/3,自己会设置
    private float contourSize = 0;
    //Padding (with defaults)
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;


    // parent
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;
    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint textBoldPaint = new Paint();
    private Paint contourPaint = new Paint();
    private Paint borderPaint = new Paint();
    //Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF rimBounds = new RectF();
    //内外边
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();
    private RectF borderBounds = new RectF();
    static final boolean GREATER_THEN_4 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    //Animation
    //The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    //The number of milliseconds to wait inbetween each draw
    private int delayMillis = GREATER_THEN_4 ? 100 : 0;
//    private int delayMillis = 0;
    private int smoothStep = GREATER_THEN_4 ? 10 : 20;
    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                progress += spinSpeed;
                if (progress > 360) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
            //super.handleMessage(msg);
        }
    };

    private static final int DEFAULT_DURATION = 1000;
    protected int DURATION = DEFAULT_DURATION;

    private int target = 0;
    private Handler smoothHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            if (current < target) {
                int tmp = current + spinSpeed;
                if (tmp > target)
                    tmp = target;
                set_current(tmp);
                smoothHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
        }
    };

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public KCRoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));

        setBackgroundColor(TRANSPARENT); //圈外背景
        setCircleColor(TRANSPARENT); //中心
    }

    //样式相关的API(参照图片中的详细说明来看)
    // 设置前景色，默认黑色
// 参数中的 int 值用下面的这个方法获得
// int color = Color.parseColor("#636161")
    public void set_fg_color(int color) {
        setBarColor(color); //前景bar
    }

    // 设置背景色，默认透明
    public void set_bg_color(int color) {
        setRimColor(color); //圈的背景色
    }

    // 设置是否显示进度条组件中间的数字，默认不显示
    public void set_text_display(boolean flag) {
        bTextDisplay = flag;
        init_text();
        //一个handler 操作
    }

    private void init_text() {
        if (bTextDisplay) {
            if(text_show_type == TextShowType.CURRENT)
                setText(String.valueOf(current));
            else
                setText(String.valueOf(get_current_percent()) + "%");
        }
        else
            setText("");
    }

    private int get_current_percent() {
        if(max <= 0)
            return 0;
        return (int)(100f * current / max);
    }

    // 设置组件宽度，默认值为空，不设置宽度，组件无法初始化
    public void set_width_px(int px) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = px;
        setLayoutParams(layoutParams);
    }

    public void set_width_dp(int dp) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = DisplayModule.dp_to_px(getContext(), dp);
        setLayoutParams(layoutParams);
    }

    // 设置环形厚度（环形厚度和组件宽度一半的比例值），默认0.3
    public void set_thickness(float scale) {
        this.scale = scale;
        invalidate();
    }

    // 设置是否显示外圈，默认不显示
    public void set_border_display(boolean flag) {
        bBorderDisplay = flag;
    }

    // 设置中间的数字字体大小，默认是组件宽度的三分之一
    public void set_text_size(int px) {
        textSize = px;
        setTextSize(textSize);
    }

    public void set_text_size_dp(int dp) {
        textSize = DisplayModule.dp_to_px(getContext(), dp);
        setTextSize(textSize);
    }

    // 设置起始角度，默认0
    public void set_start_angle(int angle) {
        startAngle = angle;
    }

    //进度相关的API
    // 设置进度最小值，默认是0
    public void set_min(int num) {
        min = num;
    }

    // 设置进度最大值，默认是100
    public void set_max(int num) {
        max = num;
    }

    // 设置进度当前值（该值会显示在进度条组件中间）
// 运行这个方法后组件的数字直接变成这个值
// 环形显示的角度直接变成 (current-min) / (max-min) * 360
    public void set_current(int num) {
        current = num;
        int tmp = 360 * (current - min) / (max - min);
        setBarLength(tmp);
        setProgress(tmp);
        if (bTextDisplay)
            setText(String.valueOf(current));
    }

    // 设置进度当前值（该值会显示在进度条组件中间）
// 运行这个方法后组件的数字从正在显示的值平滑渐变的变化到这个值（不能出现小数）
// 环形显示的角度从正在显示的值平滑渐变的变成 (current-min) / (max-min) * 360
    public void set_current_smooth(int num) {
//        int tmp = 360 * (num - min) / (max - min);
        target = num;
        if (target > current) {
            spinSpeed = (target - current) / smoothStep;
            smoothHandler.sendEmptyMessage(0);
        }
    }

    private int getDefaultTextSize() {
        return getWidth() / 3;
    }

    private float getScaleBarWidth() {
        // 半径 * scale
        return getWidth() * scale / 2;
    }


    //----------------------------------
    //Setting up stuff
    //----------------------------------

    /*
     * When this is called, make the view square.
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     *
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The first thing that happen is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We can’t use getWidth() or getHight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heigthWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // Finally we have some simple logic that calculates the size of the view
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding,
        // and when we set the dimension we add it back again. Now the actual content
        // of the view will be square, but, depending on the padding, the total dimensions
        // of the view might not be.
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }

        // If you override onMeasure() you have to call setMeasuredDimension().
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your
        // application will crash.
        // We are calling the onMeasure() method of the superclass so we don’t
        // actually need to call setMeasuredDimension() since that takes care
        // of that. However, the purpose with overriding onMeasure() was to
        // change the default behaviour and to do that we need to call
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        if (scale > 0) {
            int barWidth = (int) getScaleBarWidth();
            setBarWidth(barWidth);
            setRimWidth(barWidth);
        }
        if (textSize == 0)
            set_text_size(getDefaultTextSize());
        else
            setTextSize(textSize);

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(barWidth);
//        rimPaint.setStrokeWidth(rimWidth);


        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        textBoldPaint.setColor(textColor);
        textBoldPaint.setStyle(Paint.Style.FILL);
        textBoldPaint.setAntiAlias(true);
        textBoldPaint.setTextSize(textSize);
        textBoldPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textBoldPaint.setFakeBoldText(true);

        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Paint.Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);

        borderPaint.setColor(barColor);
        borderPaint.setAntiAlias(true); // 边缘锯齿
        borderPaint.setStyle(Paint.Style.STROKE); //外层的线
        borderPaint.setStrokeWidth(BORDER_WIDTH);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to steup the circle
        int minValue = Math.min(layout_width, layout_height);

        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth(); //this.getLayoutParams().width;
        int height = getHeight(); //this.getLayoutParams().height;

        if (bBorderDisplay) { // 有border
            // border bound
            borderBounds = new RectF(paddingLeft + getBorderPadding(),
                    paddingTop + getBorderPadding() ,
                    width - paddingRight - getBorderPadding() ,
                    height - paddingBottom - getBorderPadding()
            );
            fullRadius = (width - paddingRight ) / 2;
            circleRadius = (fullRadius - barWidth - BORDER_WIDTH - BORDER_SPACING);

            circleBounds = new RectF(paddingLeft + getBarPadding() + getBorderPadding() + getScaleBarWidth()/2,
                    paddingTop + getBarPadding() +  + getBorderPadding() + getScaleBarWidth()/2,
                    width - paddingRight - getBarPadding() - getBorderPadding() - getScaleBarWidth()/2,
                    height - paddingBottom - getBarPadding() - getBorderPadding() - getScaleBarWidth()/2
            );
            rimBounds = new RectF(paddingLeft + getBarPadding() + getBorderPadding(),
                    paddingTop + getBarPadding() + getBorderPadding(),
                    width - paddingRight - getBarPadding() - getBorderPadding() - getBorderPadding(),
                    height - paddingBottom - getBarPadding() - getBorderPadding() - getBorderPadding()
            );
        } else {
//            circleBounds = new RectF(paddingLeft + getBarPadding(),
//                    paddingTop + getBarPadding(),
//                    width - paddingRight - getBarPadding(),
//                    height - paddingBottom - getBarPadding());
            rimBounds = new RectF(paddingLeft + getBarPadding(),
                    paddingTop + getBarPadding(),
                    width - paddingRight - getBarPadding(),
                    height - paddingBottom - getBarPadding());
            circleBounds = new RectF(paddingLeft + getBarPadding() + getScaleBarWidth()/2,
                    paddingTop + getBarPadding() + getScaleBarWidth() /2,
                    width - paddingRight - getBarPadding() - getBorderPadding() - getScaleBarWidth() /2 ,
                    height - paddingBottom - getBarPadding() - getBorderPadding() - getScaleBarWidth() /2
            );

            fullRadius = (width - paddingRight) / 2;
            circleRadius = (fullRadius - barWidth);
        }
    }

    private int getBorderPadding() {
        return BORDER_WIDTH / 2;
    }

    private int getBarPadding() {
        //空心基本往线条两边扩，所以距离为 / 2 + 1
        if (bBorderDisplay)
            return BORDER_SPACING + barWidth / 2;
        else
            return barWidth / 2;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_barWidth,
                barWidth);

        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_rimWidth,
                rimWidth);

        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_spinSpeed,
                spinSpeed);

        delayMillis = a.getInteger(R.styleable.ProgressWheel_delayMillis,
                delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(R.styleable.ProgressWheel_barColor, barColor);

        barLength = (int) a.getDimension(R.styleable.ProgressWheel_barLength,
                barLength);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_textSize,
                textSize);

        textColor = (int) a.getColor(R.styleable.ProgressWheel_textColor,
                textColor);

        //if the text is empty , so ignore it
        if (a.hasValue(R.styleable.ProgressWheel_text)) {
            setText(a.getString(R.styleable.ProgressWheel_text));
        }

        rimColor = (int) a.getColor(R.styleable.ProgressWheel_rimColor,
                rimColor);

        circleColor = (int) a.getColor(R.styleable.ProgressWheel_circleColor,
                circleColor);

        contourColor = a.getColor(R.styleable.ProgressWheel_contourColor, contourColor);
        contourSize = a.getDimension(R.styleable.ProgressWheel_contourSize, contourSize);


        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the inner circle
        canvas.drawArc(circleBounds, 360, 360, false, circlePaint);
//        //Draw the rim
//        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        canvas.drawArc(rimBounds, 360, 360, false, rimPaint);

        //draw board
//        canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
//        canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
        if (isSpinning) {
            canvas.drawArc(rimBounds, startAngle + progress - 90, barLength, false,
                    barPaint);
        } else {
            canvas.drawArc(rimBounds, startAngle - 90, progress, false, barPaint);
        }
        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        if(text_show_type == TextShowType.CURRENT) {
            for (String s : splitText) {
                    float horizontalTextOffset = textPaint.measureText(s) / 2;
                    canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                            this.getHeight() / 2 + verticalTextOffset, textPaint);
            }
        }
        else{
            String str_current_percent = String.valueOf(get_current_percent());
            float percent_width = textBoldPaint.measureText(str_current_percent);
            float percent_icon_width = textPaint.measureText("%");
            float percent_offset, percent_icon_offset;
            percent_offset = (percent_width + percent_icon_width) / 2.0f;
            percent_icon_offset = (percent_width + percent_icon_width) / 2.0f - percent_icon_width;

            canvas.drawText(str_current_percent, this.getWidth() / 2.0f - percent_offset,
                this.getHeight() / 2 + verticalTextOffset, textBoldPaint);
            canvas.drawText("%", this.getWidth() / 2.0f + percent_icon_offset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);

        }

        //Draw the border
        if (bBorderDisplay) {
            canvas.drawArc(borderBounds, 360, 360, false, borderPaint);
        }
    }

    /**
     * Check if the wheel is currently spinning
     */

    protected boolean isSpinning() {
        if (isSpinning) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Reset the count (in increment mode)
     */
    protected void resetCount() {
        progress = 0;
        setText("0%");
        invalidate();
    }

    /**
     * Turn off spin mode
     */
    protected void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }


    /**
     * Puts the view on spin mode
     */
    protected void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Increment the progress by 1 (of 360)
     */
    protected void incrementProgress() {
        isSpinning = false;
        progress++;
        if (progress > 360)
            progress = 0;
//        setText(Math.round(((float) progress / 360) * 100) + "%");
        spinHandler.sendEmptyMessage(0);
    }


    /**
     * Set the progress to a specific value
     */
    protected void setProgress(int i) {
        isSpinning = false;
        progress = i;
        spinHandler.sendEmptyMessage(0);
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    protected void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }
//
//    public int getCircleRadius() {
//        return circleRadius;
//    }
//
//    public void setCircleRadius(int circleRadius) {
//        this.circleRadius = circleRadius;
//    }

    protected int getBarLength() {
        return barLength;
    }

    protected void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    protected int getBarWidth() {
        return barWidth;
    }

    protected void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    protected int getTextSize() {
        return textSize;
    }

    protected void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    protected void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    protected void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    protected void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    protected void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    protected int getBarColor() {
        return barColor;
    }

    protected void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    protected int getCircleColor() {
        return circleColor;
    }

    protected void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    protected int getRimColor() {
        return rimColor;
    }

    protected void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }


    protected Shader getRimShader() {
        return rimPaint.getShader();
    }

    protected void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    protected int getTextColor() {
        return textColor;
    }

    protected void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    protected int getSpinSpeed() {
        return spinSpeed;
    }

    protected void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    protected int getRimWidth() {
        return rimWidth;
    }

    protected void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    protected int getDelayMillis() {
        return delayMillis;
    }

    protected void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public void set_internal_bg_color(int color){
        setCircleColor(color); //中心
    }

    public void set_text_color(int color){
        setTextColor(color);
    }

    public enum TextShowType {
        CURRENT,
        PERCENTAGE
    }

    TextShowType text_show_type = TextShowType.CURRENT;
    public void set_text_show_type(TextShowType type){
        text_show_type = type;
        init_text();
    }
}
