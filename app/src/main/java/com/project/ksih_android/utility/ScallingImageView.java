package com.project.ksih_android.utility;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import timber.log.Timber;

public class ScallingImageView extends AppCompatImageView implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    // Image States
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "ScalingImageView";
    //shared constructing
    Context mContext;
    ScaleGestureDetector mScaleDetector;
    GestureDetector mGestureDetector;
    Matrix mMatrix;
    float[] mMatrixValues;
    int mode = NONE;

    // Scales
    float mSaveScale = 1f;
    float mMinScale = 1f;
    float mMaxScale = 4f;

    // view dimensions
    float origWidth, origHeight;
    int viewWidth, viewHeight;

    PointF mLast = new PointF();
    PointF mStart = new PointF();



    public ScallingImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public ScallingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }


    public ScallingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        mContext = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mMatrix = new Matrix();
        mMatrixValues = new float[9];
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);

        mGestureDetector = new GestureDetector(context, this);
        setOnTouchListener(this);
    }

    public void fitToScreen() {
        mSaveScale = 1;

        float scale;
        Drawable drawable = getDrawable();
        if (drawable == null || drawable.getIntrinsicWidth() == 0
                || drawable.getIntrinsicHeight() == 0)
            return;
        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        Timber.d( "imageWidth: " + imageWidth + " imageHeight : " + imageHeight);

        float scaleX = (float) viewWidth / (float) imageWidth;
        float scaleY = (float) viewHeight / (float) imageHeight;
        scale = Math.min(scaleX, scaleY);
        mMatrix.setScale(scale, scale);

        // Center the image
        float redundantYSpace = (float) viewHeight
                - (scale * (float) imageHeight);
        float redundantXSpace = (float) viewWidth
                - (scale * (float) imageWidth);
        redundantYSpace /= (float) 2;
        redundantXSpace /= (float) 2;

        Timber.d("fitToScreen: redundantXSpace: %s", redundantXSpace);
        Timber.d("fitToScreen: redundantYSpace: %s", redundantYSpace);
        Timber.d( "fitToScreen: redundantXSpace: %s", redundantXSpace);
        Timber.d( "fitToScreen: redundantYSpace: %s", redundantYSpace);

        mMatrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = viewWidth - 2 * redundantXSpace;
        origHeight = viewHeight - 2 * redundantYSpace;
        setImageMatrix(mMatrix);
    }

    void fixTranslation() {
        mMatrix.getValues(mMatrixValues); //put matrix values into a float array so we can analyze
        float transX = mMatrixValues[Matrix.MTRANS_X]; //get the most recent translation in x direction
        float transY = mMatrixValues[Matrix.MTRANS_Y]; //get the most recent translation in y direction

        float fixTransX = getFixTranslation(transX, viewWidth, origWidth * mSaveScale);
        float fixTransY = getFixTranslation(transY, viewHeight, origHeight * mSaveScale);

        if (fixTransX != 0 || fixTransY != 0)
            mMatrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTranslation(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) { // case: NOT ZOOMED
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else { //CASE: ZOOMED
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans) { // negative x or y translation (down or to the right)
            Timber.d("getFixTranslation: minTrans: " + minTrans + ", trans: " + trans);
            Timber.d("getFixTranslation: return: %s", (-trans + minTrans));
            return -trans + minTrans;
        }

        if (trans > maxTrans) { // positive x or y translation (up or to the left)
            Timber.d("getFixTranslation: maxTrans: " + maxTrans + ", trans: " + trans);
            Timber.d("getFixTranslation: return: %s", (-trans + maxTrans));
            return -trans + maxTrans;
        }

        return 0;
    }

    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (mSaveScale == 1) {

            // Fit to screen.
            fitToScreen();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        mScaleDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        PointF currentPoint = new PointF(event.getX(), event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLast.set(currentPoint);
                mStart.set(mLast);
                mode = DRAG;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    float dx = currentPoint.x - mLast.x;
                    float dy = currentPoint.y - mLast.y;

                    float fixTransX = getFixDragTrans(dx, viewWidth, origWidth * mSaveScale);
                    Timber.d( "onTouch: fixTransX: %s", fixTransX);
                    float fixTransY = getFixDragTrans(dy, viewHeight, origHeight * mSaveScale);
                    Timber.d( "onTouch: fixTransY: %s", fixTransY);
                    mMatrix.postTranslate(fixTransX, fixTransY);

                    fixTranslation();

                    mLast.set(currentPoint.x, currentPoint.y);
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
        }

        setImageMatrix(mMatrix);

        return false;
    }

    /*
        GestureListener
     */


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    /*
        onDoubleTap
     */

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        fitToScreen();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Timber.d("Scale%s", detector.getScaleFactor());
            float mScaleFactor = detector.getScaleFactor();
            float prevScale = mSaveScale;
            mSaveScale *= mScaleFactor;
            if (mSaveScale > mMaxScale) {
                mSaveScale = mMaxScale;
                mScaleFactor = mMaxScale / prevScale;
            } else if (mSaveScale < mMinScale) {
                mSaveScale = mMinScale;
                mScaleFactor = mMinScale / prevScale;
            }

            if (origWidth * mSaveScale <= viewWidth
                    || origHeight * mSaveScale <= viewHeight) {
                Timber.d("onScale: VIEW CENTERED FOCUS");
                mMatrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                        viewHeight / 2);
            } else {
                Timber.d("onScale: DETECTOR FOCUS");
                mMatrix.postScale(mScaleFactor, mScaleFactor,
                        detector.getFocusX(), detector.getFocusY());
            }

            fixTranslation();
            return true;
        }
    }
}
