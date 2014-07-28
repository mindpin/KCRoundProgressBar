package com.mindpin.android.kcroundprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import com.mindpin.android.kcroundprogressbar.common.DisplayModule;
import com.mindpin.android.kcroundprogressbar.module.ProgressWheel;

public class KCRoundProgressBar extends ProgressWheel {
    private static final String TAG = "KCRoundProgressBar";
    private static final int TRANSPARENT = 0x00000000;
    private static final int DEFAULT_MIN = 0;
    private static final int DEFAULT_MAX = 100;
    private float scale = 0.0f;
    private int min = DEFAULT_MIN,max = DEFAULT_MAX, current = DEFAULT_MIN;
    private boolean bTextDisplay = false, bBorderDisplay = false;
    private int textSize = 0;
//    int width, height;

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public KCRoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(TRANSPARENT); //圈外背景
        setCircleColor(TRANSPARENT); //中心
    }

    //样式相关的API(参照图片中的详细说明来看)
    // 设置前景色，默认黑色
// 参数中的 int 值用下面的这个方法获得
// int color = Color.parseColor("#636161")
    public void set_fg_color(int color) {
        setTextColor(color); //前景文字
        setBarColor(color); //前景bar
    }

    // 设置背景色，默认透明
    public void set_bg_color(int color) {
        setRimColor(color); //圈的背景色
    }

    // 设置是否显示进度条组件中间的数字，默认不显示
    public void set_text_display(boolean flag) {
        bTextDisplay = flag;
        if(bTextDisplay)
            setText(String.valueOf(current));
        else
            setText("");
        //一个handler 操作
    }

    // 设置组件宽度，默认值为空，不设置宽度，组件无法初始化
    public void set_width_px(int px) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = px;
        layoutParams.height = px;
        setLayoutParams(layoutParams);
//        setBarWidth(px);
    }

    public void set_width_dp(int dp) {

    }

    // 设置环形厚度（环形厚度和组件宽度一半的比例值），默认0.3
    public void set_thickness(float scale) {
//        Log.d(TAG, "getWidth():" + getWidth());
        this.scale = scale;
//        setBarWidth(30);
//        setRimWidth(30);
//        setBarLength((int) (getWidth() * scale / 2));
//        setRimWidth((int) (getWidth() * scale / 2));
    }

    // 设置是否显示外圈，默认不显示
    public void set_border_display(boolean flag) {

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
        Log.d(TAG, "current:" + current);
        Log.d(TAG, "min:" + min);
        Log.d(TAG, "max:" + max);
        Log.d(TAG, "tmp:" + tmp);
        setBarLength(tmp);
        setProgress(tmp);
        if(bTextDisplay)
            setText(String.valueOf(current));
    }

    // 设置进度当前值（该值会显示在进度条组件中间）
// 运行这个方法后组件的数字从正在显示的值平滑渐变的变化到这个值（不能出现小数）
// 环形显示的角度从正在显示的值平滑渐变的变成 (current-min) / (max-min) * 360
    public void set_current_smooth(int num) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (scale != 0) {
            int barWidth = (int) getScaleBarWidth();
            Log.d(TAG, "barWidth:" + barWidth);
            setBarWidth(barWidth);
            setRimWidth(barWidth);
        }
        if(textSize == 0)
            set_text_size(getDefaultTextSize());
        else
            setTextSize(textSize);

        if(bBorderDisplay){
            super.setupBounds();
            super.setupPaints();
            invalidate();
        }
        else
            super.onSizeChanged(w, h, oldw, oldh);
    }

    private int getDefaultTextSize() {
        return getWidth() / 3;
    }

    private float getScaleBarWidth() {
        // 半径 * scale
        return getWidth() * scale / 2;
    }



    // parent

    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    //Padding (with defaults)
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int paddingLeft = 0;
    private int paddingRight = 0;

    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();


    //Animation
    //The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    //The number of milliseconds to wait inbetween each draw
    private int delayMillis = 0;
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
    int progress = 0;
    boolean isSpinning = false;

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */













}
