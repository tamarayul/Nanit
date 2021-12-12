package com.e.realnanitapp.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class BabyModule implements Parcelable {

    public String name;
    public String uri;
    public int year;
    public int month;
    public int day;

    public BabyModule() {
    }

    public BabyModule(Parcel in) {
        this.day = in.readInt();
        this.month = in.readInt();
        this.year = in.readInt();
        this.name = in.readString();
        this.uri = in.readString();
    }

    public static final Creator<BabyModule> CREATOR = new Creator<BabyModule>() {
        @Override
        public BabyModule createFromParcel(Parcel in) {
            return new BabyModule(in);
        }

        @Override
        public BabyModule[] newArray(int size) {
            return new BabyModule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeString(name);
        dest.writeString(uri);
    }
}