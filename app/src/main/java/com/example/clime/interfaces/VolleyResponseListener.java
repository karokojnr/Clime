package com.example.clime.interfaces;

public interface VolleyResponseListener {
    void onResponse(String cityID);
    void onError(String error);

}
