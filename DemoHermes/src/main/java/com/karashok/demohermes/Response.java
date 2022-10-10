package com.karashok.demohermes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class Response implements Parcelable {

    // 响应的对象
    public String data;

    public Response(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.data);
    }

    public void readFromParcel(Parcel source) {
        this.data = source.readString();
    }

    protected Response(Parcel in) {
        this.data = in.readString();
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    @Override
    public String toString() {
        return "Responce{" +
                "data='" + data + '\'' +
                '}';
    }
}
