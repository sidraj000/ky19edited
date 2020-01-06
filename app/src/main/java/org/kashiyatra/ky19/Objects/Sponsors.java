package org.kashiyatra.ky19.Objects;

import com.google.firebase.database.FirebaseDatabase;

public class Sponsors {
    public String type,url;

    public Sponsors() {
    }

    public Sponsors(String type, String url) {
        this.type = type;
        this.url = url;
    }
}
