package co.hatrus.andrew.paint.model;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 10.02.15.
 */
public class Box implements Parcelable{
    private PointF mOrigin;
    private PointF mCurrent;

    public Box(PointF origin) {
        mOrigin = mCurrent = origin;
    }
    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setOrigin(PointF mOrigin) {
        this.mOrigin = mOrigin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mOrigin.x);
        dest.writeFloat(mOrigin.y);
        dest.writeFloat(mCurrent.x);
        dest.writeFloat(mCurrent.y);
    }
    public static final Creator<Box> CREATOR =
            new Creator<Box>() {
                @Override
                public Box createFromParcel(Parcel source) {
                    return new Box(source);
                }

                @Override
                public Box[] newArray(int size) {
                    return new Box[size];
                }
            };
    private Box(Parcel in){
        mOrigin = new PointF(in.readFloat(),in.readFloat());
        mCurrent = new PointF(in.readFloat(),in.readFloat());
    }
}