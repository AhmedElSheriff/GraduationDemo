package com.example.android.graduationdemo.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Abshafi on 3/21/2017.
 */

public class FirstAid implements Parcelable {
    int image;
    String sign, cure;

    public FirstAid() {
    }

    public FirstAid(int image, String sign, String cure) {
        this.image = image;
        this.sign = sign;
        this.cure = cure;
    }

    protected FirstAid(Parcel in) {
        image = in.readInt();
        sign = in.readString();
        cure = in.readString();
    }

    public static final Creator<FirstAid> CREATOR = new Creator<FirstAid>() {
        @Override
        public FirstAid createFromParcel(Parcel in) {
            return new FirstAid(in);
        }

        @Override
        public FirstAid[] newArray(int size) {
            return new FirstAid[size];
        }
    };

    public void setImage(int image) {
        this.image = image;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setCure(String cure) {
        this.cure = cure;
    }

    public int getImage() {
        return image;
    }

    public String getSign() {
        return sign;
    }

    public String getCure() {
        return cure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(sign);
        dest.writeString(cure);
    }
}
