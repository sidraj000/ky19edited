package org.kashiyatra.ky19.inner;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class InnerData {

    public final String title;
    public final String name;
    public final String address;
    public final Drawable avatarUrl;
    public final int background;

    public InnerData(String title, String name, String address, Drawable avatarUrl, int background) {
        this.title = title;
        this.name = name;
        this.address = address;
        this.avatarUrl = avatarUrl;
        this.background = background;
    }
}
