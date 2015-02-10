package co.hatrus.andrew.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by user on 10.02.15.
 */
public class BoxDrawingView extends View {
    private static final float DRAW_RADIUS = 8f;
    private static String TAG = "BoxDrawingView";
    private Box mCurrentBox;
    private Box mMovingBox;
    private ArrayList<Box> mBoxList = new ArrayList<>(15);
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    public BoxDrawingView(Context context) {
        super(context);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x88aabbee);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(8);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG,"onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState",super.onSaveInstanceState());
        bundle.putParcelable("currentBox",mCurrentBox);
        bundle.putParcelableArrayList("boxes",mBoxList);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG,"onRestoreInstanceState");
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mCurrentBox = bundle.getParcelable("currentBox");
            mBoxList = bundle.getParcelableArrayList("boxes");
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
            Log.d(TAG,"curr: "+mCurrentBox+" size: "+mBoxList.size());
            return;
        }
        super.onRestoreInstanceState(state);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        for(Box box:mBoxList) {
            //drawMyRect(canvas,box);
            drawLikeBrush(canvas, box);

        }
    }
    private void drawLikeBrush(Canvas canvas, Box box){
        //canvas.drawCircle(box.getCurrent().x,box.getCurrent().y,DRAW_RADIUS,mBoxPaint);
        if(box.getCurrent()==box.getOrigin())
            canvas.drawPoint(box.getCurrent().x,box.getCurrent().y,mBackgroundPaint);
        else
        canvas.drawLine(box.getCurrent().x,box.getCurrent().y,box.getOrigin().x,box.getOrigin().y,mBoxPaint);
    }
    private void drawMyRect(Canvas canvas,Box box){
        float left = Math.min(box.getOrigin().x, box.getCurrent().x);
        float right = Math.max(box.getOrigin().x, box.getCurrent().x);
        float top = Math.min(box.getOrigin().y, box.getCurrent().y);
        float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);
        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }
    public static float distanceBetweenPointFs(PointF p1, PointF p2){
        float dist = (float) Math.sqrt(
                Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
        return dist;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(),event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"onTouch dwn"+MotionEvent.ACTION_DOWN);
                mCurrentBox = new Box(curr);
                mBoxList.add(mCurrentBox);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"onTouch mv"+MotionEvent.ACTION_MOVE);
                if(mMovingBox!=null) {
                    mMovingBox.setCurrent(curr);
//                    float dist =distanceBetweenPointFs(mMovingBox.getCurrent(),mCurrentBox.getCurrent());
//                    if(dist>DRAW_RADIUS/3){
//                        mMovingBox.setOrigin(mCurrentBox.getCurrent());
//                        Log.d(TAG,"onTouch "+MotionEvent.ACTION_MOVE+" dist: "+dist);
//                    }
                    mBoxList.add(mMovingBox);
                    invalidate();
                    mMovingBox = new Box(mMovingBox.getCurrent());
                }else mMovingBox= new Box(curr);

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d(TAG,"onTouch cncl"+MotionEvent.ACTION_CANCEL);
                mCurrentBox=null;
                mMovingBox=null;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG,"onTouch up"+MotionEvent.ACTION_UP);
                mCurrentBox=null;
                mMovingBox=null;
                break;
        }
        return true;
    }
}
