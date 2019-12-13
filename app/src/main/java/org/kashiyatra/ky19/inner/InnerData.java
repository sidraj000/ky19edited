package org.kashiyatra.ky19.inner;

import android.graphics.Bitmap;

public class InnerData {

    public final String title;
    public final String name;
    public final String address;
    public final Bitmap avatarUrl;
    public final int age;

    public InnerData(String title, String name, String address, Bitmap avatarUrl, int age) {
        this.title = title;
        this.name = name;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.age = age;
    }
}
