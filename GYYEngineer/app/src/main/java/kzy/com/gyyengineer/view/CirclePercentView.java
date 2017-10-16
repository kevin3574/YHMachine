package kzy.com.gyyengineer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import kzy.com.gyyengineer.R;
import kzy.com.gyyengineer.utils.PxUtils;


/**
 * 一个圆形百分比进度 View
 * 用于展示简易的图标
 * Created by Administrator on 2015/12/16.
 */
public class CirclePercentView extends View {

    //圆的半径
    private float mRadius;

    //色带的宽度
    private float mStripeWidth;
    //总体大小
    private int mHeight;
    private int mWidth;

    //动画位置百分比进度
    private int mCurPercent;

    //实际百分比进度
    private int mPercent;
    //圆心坐标
    private float x;
    private float y;

    //要画的弧度
    private int mEndAngle;

    //小圆的颜色
    private int mSmallColor;
    //大圆颜色
    private int mBigColor;

    //中心百分比文字大小
    private float mCenterTextSize;

    public CirclePercentView(Context context) {
        this(context, null);
    }

    public CirclePercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePercentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView, defStyleAttr, 0);
        mStripeWidth = a.getDimension(R.styleable.CirclePercentView_stripeWidth, PxUtils.dpToPx(30, context));
        mCurPercent = a.getInteger(R.styleable.CirclePercentView_percent, 0);
        mSmallColor = a.getColor(R.styleable.CirclePercentView_smallColor, 0xffafb4db);
        mBigColor = a.getColor(R.styleable.CirclePercentView_bigColor, 0xff6950a1);
        mCenterTextSize = a.getDimensionPixelSize(R.styleable.CirclePercentView_centerTextSize, PxUtils.spToPx(20, context));
        mRadius = a.getDimensionPixelSize(R.styleable.CirclePercentView_radius, PxUtils.dpToPx(100, context));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            mRadius = widthSize / 2;
            x = widthSize / 2;
            y = heightSize / 2;
            mWidth = widthSize;
            mHeight = heightSize;
        }

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            mWidth = (int) (mRadius * 2);
            mHeight = (int) (mRadius * 2);
            x = mRadius;
            y = mRadius;

        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        mEndAngle = (int) (mCurPercent * 3.6);

        //饼状图  进度圆
        Paint sectorPaint = new Paint();
        sectorPaint.setColor(getResources().getColor(R.color.yellow));
        sectorPaint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, mWidth, mHeight);
        canvas.drawArc(rect, 270, mEndAngle, true, sectorPaint);

        // 绘制中心圆
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setAntiAlias(true);
        canvas.drawCircle(x, y, mRadius - mStripeWidth, paint);
        // 绘制中心圆
        Paint paint3 = new Paint();
        paint3.setColor(getResources().getColor(R.color.yellow));
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setAntiAlias(true);
        canvas.drawCircle(x, y, mRadius, paint3);
        // 绘制中心圆
        Paint paint4 = new Paint();
        paint4.setColor(getResources().getColor(R.color.yellow));
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setAntiAlias(true);
        canvas.drawCircle(x, y, mRadius - mStripeWidth, paint4);
        // 绘制中心圆
        Paint paint2 = new Paint();
        paint2.setColor(getResources().getColor(R.color.yellow));
        paint2.setAntiAlias(true);
        canvas.drawCircle(x, y, (mRadius - mStripeWidth) / 2, paint2);
        //绘制文本
        Paint textPaint = new Paint();
        String text = mCurPercent + "";
        textPaint.setTextSize(mCenterTextSize);
        float textLength = textPaint.measureText(text);
        textPaint.setColor(Color.WHITE);
        Rect rect1 = new Rect();
        textPaint.getTextBounds(text, 0, 1, rect1);
        int height = rect1.height();
        canvas.drawText(text, x - textLength / 2, y + height/2, textPaint);


    }

    //外部设置百分比数
    public void setPercent(int percent) {


        setCurPercent(percent);


    }

    //内部设置百分比 用于动画效果
    private void setCurPercent(int percent) {

        mPercent = percent;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 1;
                for (int i = 0; i < mPercent; i++) {
                    if (i % 20 == 0) {
                        sleepTime += 2;
                    }
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mCurPercent = i;
                    CirclePercentView.this.postInvalidate();
                }
            }

        }).start();

    }


}
