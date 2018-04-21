package com.akinyele.toolbar_loader;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.akinyele.toolbar_loader.networking.ToolBarInterceptor;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * @author akiny.
 * Created 1/14/2018.
 */

public class ToolbarLoader extends LinearLayout implements ToolBarInterceptor.ToolbarInterceptorObserver {

    private static final String TAG = "ToolbarLoader";


    private View rootView;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private Context mContext;
    private Handler mHandler = new Handler();

    private static int MODE_GRADIENT = 0;
    private static int MODE_LOADER = 1;

    private ConnectionPool mConnectionPool;


    private OkHttpClient client;

    // the type of loader that the toolbar will have
    private int toolbarMode;

    private int toolbarColor;
    private int loaderColor;

    private ToolBarInterceptor mCustomInterceptor = ToolBarInterceptor.getInterceptor();


    private Paint toolbarPaint;

    public ToolbarLoader(Context context) {
        super(context);
        init(context);
    }

    public ToolbarLoader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setAttributes(attrs);
    }

    public ToolbarLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setAttributes(attrs);
    }


    private void init(Context context) {
        this.mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        rootView = inflate(context, R.layout.toolbar_loader_view, this);


        mProgressBar = findViewById(R.id.progress_bar);
        mToolbar = findViewById(R.id.toolbar);

        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(INVISIBLE);

        setOrientation(VERTICAL);
    }


    //==============================================================================================
    //      Helpers
    //==============================================================================================
    private void setAttributes(AttributeSet attrs) {

        toolbarPaint = new Paint();

        // get the attributes defined in our custom attributes set
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ToolbarLoader, 0, 0);
        toolbarColor = a.getColor(R.styleable.ToolbarLoader_tb_color, 0);

        toolbarPaint.setStyle(Paint.Style.FILL);
        toolbarPaint.setAntiAlias(true);
        toolbarPaint.setColor(toolbarColor);

        a.recycle();
    }

    private void setProgressBarVisibility(int visibility) {
        mProgressBar.setVisibility(visibility);
    }


    //==============================================================================================
    //      Loader Methods
    //==============================================================================================
    public void startLoader() {
        setProgressBarVisibility(VISIBLE);
    }

    public void stopLoader() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setProgressBarVisibility(INVISIBLE);
            }
        }, 1200);
    }

    //==============================================================================================
    //      Toolbar Methods
    //==============================================================================================
    public int getToolbarMode() {
        return toolbarMode;
    }

    public void setToolbarMode(int toolbarMode) {
        this.toolbarMode = toolbarMode;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getLoaderColor() {
        return loaderColor;
    }

    public void setLoaderColor(int loaderColor) {
        this.loaderColor = loaderColor;
    }


    //==============================================================================================
    //      Observer Interface Methods
    //==============================================================================================
    @Override
    public void update(final int state) {

        /*
         * Has to be ran on the UI thread because the network interceptor is ran
         * on a back ground thread. View should always be updated on the main thread.
         */
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //stuff that updates ui

                switch (state) {

                    case ToolBarInterceptor.STATE_FINISHED:

                        stopLoader();

                        break;
                    case ToolBarInterceptor.STATE_STARTED:

                        startLoader();

                        break;


                }


            }
        });

    }


}
