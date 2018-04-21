package com.akinyele.toolbar_loader.networking;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author akiny.
 * Created 4/7/2018.
 * <p>
 * This class is desinged to listen to all HTTP reqeuest going in and out of the client it is attacthed to.
 * It keeps track of the state of the HTTP requests, i.e. if the have started or ended. A call is made to the
 * attached observer with the change in states.
 * <p>
 * NB - There is only ever be on instance of this class and on observer attached to it at any time. So
 * whenever a new toolbar needs to listen to the change in state they need to register them.
 */
public class ToolBarInterceptor implements Interceptor {

    private static final String TAG = "ToolBarInterceptor";


    private static ToolbarInterceptorObserver mObserver;
    private static ToolBarInterceptor mCustomInterceptor;
    private int mCurrentState = STATE_UNDEFINED;

    public static final int STATE_UNDEFINED = 9;
    public static final int STATE_STARTED = 0;
    public static final int STATE_FINISHED = 1;

    private ToolBarInterceptor() {
    }


    //==============================================================================================
    //      Interceptor
    //==============================================================================================
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        updateState(STATE_STARTED);
        Log.d(TAG, "intercept Request: " + request.url());


        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            Log.e(TAG, "intercept: <-- HTTP FAILED: ", e);
            updateState(STATE_FINISHED);
            throw e;
        }

        Log.d(TAG, "intercept: finished");
        updateState(STATE_FINISHED);

        return response;
    }


    //==============================================================================================
    //      Observer Methods
    //==============================================================================================
    public interface ToolbarInterceptorObserver {
        void update(int State);
    }

    private void updateState(int state) {
        if (mObserver != null)
            mObserver.update(state);
    }


    //==============================================================================================
    //      Getters & Setters
    //==============================================================================================
    public static ToolBarInterceptor getInterceptor() {

        if (mCustomInterceptor != null) {
            return mCustomInterceptor;
        }

        mCustomInterceptor = new ToolBarInterceptor();
        return mCustomInterceptor;
    }


    public int getCurrentState() {
        return mCurrentState;
    }


    public static void registerToolbar(ToolbarInterceptorObserver observer) {
        mObserver = observer;
    }


}
