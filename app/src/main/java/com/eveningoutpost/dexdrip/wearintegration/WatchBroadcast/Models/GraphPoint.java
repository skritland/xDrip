package com.eveningoutpost.dexdrip.wearintegration.WatchBroadcast.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class GraphPoint implements Parcelable {
    public static final Creator<GraphPoint> CREATOR = new Creator<GraphPoint>() {

        @Override
        public GraphPoint createFromParcel(Parcel source) {
            return new GraphPoint(source);
        }

        @Override
        public GraphPoint[] newArray(int size) {
            return new GraphPoint[size];
        }
    };
    private float x;
    private float y;

    public GraphPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public GraphPoint(Parcel parcel) {
        x = parcel.readFloat();
        y = parcel.readFloat();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(x);
        parcel.writeFloat(y);
    }
}
