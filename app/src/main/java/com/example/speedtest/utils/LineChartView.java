package com.example.speedtest.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

public final class LineChartView extends View {
    private float check;
    private int count;
    private int lineColor;
    private float lineWidth;
    private final List<PointF> listPoint;
    private long maxTime;
    private float maxWave;
    private final Paint paint;
    private final Path path;
    private float scaleFactor;
    private Matrix scaleMatrix;
    private boolean stopped;
    private float strokeWidth;
    public ValueAnimator timeLineAnimator;
    private float wave;
    private float waveFactor;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public LineChartView(Context context) {
        this(context, (AttributeSet) null, 0, 6, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public LineChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ LineChartView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LineChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkNotNullParameter(context, "context");
        this.lineColor = Color.parseColor("#0DFFF0");
        Paint paint2 = new Paint(1);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeJoin(Paint.Join.ROUND);
        this.paint = paint2;
        this.path = new Path();
        this.strokeWidth = 1.0f;
        this.waveFactor = 8.0f;
        this.maxWave = 40.0f;
        this.scaleMatrix = new Matrix();
        this.scaleFactor = 2.0f;
        this.count = 100;
        this.listPoint = new ArrayList();
    }

    public final ValueAnimator getTimeLineAnimator() {
        ValueAnimator valueAnimator = this.timeLineAnimator;
        if (valueAnimator != null) {
            return valueAnimator;
        }
        Intrinsics.throwUninitializedPropertyAccessException("timeLineAnimator");
        return null;
    }

    public final void setTimeLineAnimator(ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(valueAnimator, "<set-?>");
        this.timeLineAnimator = valueAnimator;
    }

    public final void setStrokeWidth(float f) {
        this.strokeWidth = f;
    }

    public final void setLineColor(int i) {
        this.lineColor = i;
    }

    public final void setMaxTime(int i) {
        this.maxTime = (long) i;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        initDrawing(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        super.onDraw(canvas);
        canvas.drawPath(this.path, this.paint);
    }

    private final void getPoints() {
        PointF[] pointFArr = new PointF[this.count];
        PathMeasure pathMeasure = new PathMeasure(this.path, false);
        float length = pathMeasure.getLength();
        float f = length / ((float) this.count);
        float[] fArr = new float[2];
        int i = 0;
        for (float f2 = 0.0f; f2 < length && i < this.count; f2 += f) {
            pathMeasure.getPosTan(f2, fArr, (float[]) null);
            pointFArr[i] = new PointF(fArr[0], fArr[1]);
            i++;
        }
        createPathFromPoints(pointFArr);
    }

    private final void createPathFromPoints(PointF[] pointFArr) {
        this.path.reset();
        int length = pointFArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            PointF pointF = pointFArr[i];
            if (i == 0) {
                Path path2 = this.path;
                Intrinsics.checkNotNull(pointF);
                path2.moveTo(pointF.x, pointF.y);
            } else if (i < pointFArr.length - 1) {
                PointF pointF2 = pointFArr[i - 1];
                PointF pointF3 = pointFArr[i2];
                Path path3 = this.path;
                Intrinsics.checkNotNull(pointF2);
                float f = pointF2.x;
                float f2 = pointF2.y;
                Intrinsics.checkNotNull(pointF);
                float f3 = pointF.x;
                float f4 = pointF.y;
                Intrinsics.checkNotNull(pointF3);
                path3.cubicTo(f, f2, f3, f4, pointF3.x, pointF3.y);
            }
            i = i2;
        }
        invalidate();
    }

    private final void initDrawing(int i, int i2) {
        float f = (float) i2;
        this.scaleFactor = 0.1f * f;
        this.paint.setStrokeWidth(this.strokeWidth);
        this.paint.setColor(this.lineColor);
        this.path.moveTo(0.0f, f - (this.strokeWidth / 2.0f));
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(this.maxTime);
        ofFloat.addUpdateListener(new LineChartView$$ExternalSyntheticLambda0(this));
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(0f, 1f).apply {\n…)\n            }\n        }");
        setTimeLineAnimator(ofFloat);
    }

    /* access modifiers changed from: private */
    /* renamed from: initDrawing$lambda-2$lambda-1  reason: not valid java name */
    public static final void m409initDrawing$lambda2$lambda1(LineChartView lineChartView, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lineChartView, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
        float floatValue = ((Float) animatedValue).floatValue();
        float height = ((float) lineChartView.getHeight()) - ((((float) lineChartView.getHeight()) * lineChartView.wave) / lineChartView.maxWave);
        if (lineChartView.stopped) {
            lineChartView.lineWidth += lineChartView.waveFactor;
        } else {
            lineChartView.lineWidth = ((float) lineChartView.getWidth()) * floatValue;
        }
        lineChartView.path.lineTo(lineChartView.lineWidth, height);
        lineChartView.getPoints();
        Log.i("AIKO", Intrinsics.stringPlus("y: ", Float.valueOf(height)));
        lineChartView.invalidate();
        if (lineChartView.lineWidth >= ((float) lineChartView.getWidth())) {
            valueAnimator.cancel();
        }
    }

    public final void setWave(float f) {
        this.wave = f;
    }

    public final void start() {
        this.path.reset();
        this.path.moveTo(0.0f, ((float) getHeight()) - (this.strokeWidth / 2.0f));
        this.stopped = false;
        this.lineWidth = 0.0f;
        this.wave = 0.0f;
        if (!getTimeLineAnimator().isRunning()) {
            getTimeLineAnimator().start();
        }
    }

    public final void stop() {
        this.stopped = true;
    }
}