package com.karashok.demohermes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class Request implements Parcelable {

    public static final int TYPE_GET = 1;
    public static final int TYPE_NEW = 2;

    //请求的对象  RequestBean 对应的json字符串
    public String data;
    //    请求对象的类型
    public int type;

    public Request(String data, int type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
        dest.writeInt(this.type);
    }

    public void readFromParcel(Parcel source) {
        this.data = source.readString();
        this.type = source.readInt();
    }

    protected Request(Parcel in) {
        this.data = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel source) {
            return new Request(source);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public String toString() {
        return "Request{" +
                "data='" + data + '\'' +
                ", type=" + type +
                '}';
    }
}
