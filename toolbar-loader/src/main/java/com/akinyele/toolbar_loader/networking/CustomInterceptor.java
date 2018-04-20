package com.akinyele.toolbar_loader.networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author akiny.
 * Created 4/7/2018.
 */
public abstract class CustomInterceptor implements Interceptor {

    private static final String TAG = "CustomInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        boolean hasResponseBody = responseBody != null;
        start();


        Log.d(TAG, "intercept Request: " + request.url());

        if (hasResponseBody) {
            {
                Log.d(TAG, "intercept Response: " + responseBody.string());
                stop();
            }
        }

        return response;
    }


    public abstract void start();

    public abstract void stop();

}
