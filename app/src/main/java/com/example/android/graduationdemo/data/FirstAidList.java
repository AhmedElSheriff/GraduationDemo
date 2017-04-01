package com.example.android.graduationdemo.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abshafi on 3/25/2017.
 */

public class FirstAidList implements Parcelable {

    private String name;
    private int image;

    public FirstAidList(Parcel in) {
        name = in.readString();
        image = in.readInt();
    }

    public FirstAidList()
    {
        
    }

    public static final Creator<FirstAidList> CREATOR = new Creator<FirstAidList>() {
        @Override
        public FirstAidList createFromParcel(Parcel in) {
            return new FirstAidList(in);
        }

        @Override
        public FirstAidList[] newArray(int size) {
            return new FirstAidList[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(image);
    }
}
