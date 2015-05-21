package co.hatrus.andrew.paint.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import co.hatrus.andrew.paint.R;
import co.hatrus.andrew.paint.model.Box;

/**
 * Created by user on 10.02.15.
 */
public class BoxDrawingView extends View {
    public static final int DEFAULT_LINE_WIDTH = 8;
    public static final int ERASER_WIDTH = 16;
    private static String TAG = "BoxDrawingView";
    Bitmap mLoadedBitmap;
    private Paint mCirclePaint;
    private Box mCurrentBox;
    private Box mMovingBox;
    private ArrayList<Box> mBoxList = new ArrayList<>(15);
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private String mFileName = "painta.jpg";
    private boolean isEditable = true;

    public BoxDrawingView(Context context) {
        super(context);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int color = getResources().getColor(R.color.fab_main);
        mBoxPaint = new Paint();
        mCirclePaint = new Paint();
        mBoxPaint.setColor(color);
        mBoxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBoxPaint.setStrokeWidth(DEFAULT_LINE_WIDTH);

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(color);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getResources().getColor(R.color.paint_bg));
        loadPaintData();
    }

    public static float distanceBetweenPointFs(PointF p1, PointF p2) {
        return (float) Math.sqrt(
                Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));

    }

    public void setFileNameForSaving(String fname) {
        mFileName = fname;
        loadPaintData();
    }

    public void savePaintData() {
        Bitmap mutableBitmap = null;
        Canvas canvas;
        try {

            if (mLoadedBitmap != null) {
                mutableBitmap = mLoadedBitmap.copy(Bitmap.Config.ARGB_8888, true);
                canvas = new Canvas(mutableBitmap);
            } else {
                mutableBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(mutableBitmap);
                canvas.drawPaint(mBackgroundPaint);

            }
            for (Box box : mBoxList) {
                drawLikeBrush(canvas, box);

            }
            File file = new File(getContext().getExternalFilesDir("painta") + "/" + mFileName);
            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));

        } catch (Exception e) {
            Log.e("SavePaintData", "message: " + e.getMessage());
        }
        mLoadedBitmap = mutableBitmap;
        mBoxList.clear();

    }

    public void removePaintData() {
        File file = new File(getContext().getExternalFilesDir("painta") + "/" + mFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    public void loadPaintData() {
        File file = new File(getContext().getExternalFilesDir("painta") + "/" + mFileName);
        if (file.exists())
            mLoadedBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putParcelable("currentBox", mCurrentBox);
        bundle.putParcelableArrayList("boxes", mBoxList);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentBox = bundle.getParcelable("currentBox");
            mBoxList = bundle.getParcelableArrayList("boxes");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            Log.d(TAG, "curr: " + mCurrentBox + " size: " + mBoxList.size());
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        if (mLoadedBitmap != null)
            canvas.drawBitmap(mLoadedBitmap, 0, 0, mBackgroundPaint);
        for (Box box : mBoxList)
            drawLikeBrush(canvas, box);
    }

    private void drawLikeBrush(Canvas canvas, Box box) {
        canvas.drawLine(box.getCurrent().x, box.getCurrent().y, box.getOrigin().x, box.getOrigin().y, mBoxPaint);
        canvas.drawCircle(box.getCurrent().x, box.getCurrent().y, 4, mCirclePaint);
    }

    private void drawMyRect(Canvas canvas, Box box) {
        float left = Math.min(box.getOrigin().x, box.getCurrent().x);
        float right = Math.max(box.getOrigin().x, box.getCurrent().x);
        float top = Math.min(box.getOrigin().y, box.getCurrent().y);
        float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEditable)
            return false;
        PointF curr = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(curr);
                mBoxList.add(mCurrentBox);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mMovingBox != null) {
                    mMovingBox.setCurrent(curr);
                    mBoxList.add(mMovingBox);
                    invalidate();
                    mMovingBox = new Box(mMovingBox.getCurrent());
                } else
                    mMovingBox = new Box(curr);
                break;

            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                mMovingBox = null;
                break;

            case MotionEvent.ACTION_UP:
                mCurrentBox = null;
                mMovingBox = null;
                break;
        }
        return true;
    }

    public int getLineColor() {
        return mBoxPaint.getColor();
    }

    public void setLineColor(int color) {
        savePaintData();
        mBoxPaint.setColor(color);
        mCirclePaint.setColor(color);
    }

    public void setLineWidth(int width) {
        mBoxPaint.setStrokeWidth(width);
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
}
