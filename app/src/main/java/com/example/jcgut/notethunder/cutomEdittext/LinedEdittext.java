package com.example.jcgut.notethunder.cutomEdittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


public class LinedEdittext extends androidx.appcompat.widget.AppCompatEditText {

    private Paint mPaint = new Paint();

    public LinedEdittext(Context context) {
        super(context);
        initPaint();
    }

    public LinedEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinedEdittext(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = (height-paddingTop-paddingBottom) / lineHeight;

        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i+1) + paddingTop;
            canvas.drawLine(left+paddingLeft, baseline+10, right-paddingRight, baseline+10, mPaint);
        }

        super.onDraw(canvas);
    }
}
