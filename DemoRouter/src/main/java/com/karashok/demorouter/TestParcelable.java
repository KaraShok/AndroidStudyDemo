package com.karashok.demorouter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
public class TestParcelable implements Parcelable {

    private int id;
    private String text;


    public TestParcelable(int id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "TestParcelable{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.text);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.text = source.readString();
    }

    public TestParcelable() {
    }

    protected TestParcelable(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
    }

    public static final Creator<TestParcelable> CREATOR = new Creator<TestParcelable>() {
        @Override
        public TestParcelable createFromParcel(Parcel source) {
            return new TestParcelable(source);
        }

        @Override
        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };
}
