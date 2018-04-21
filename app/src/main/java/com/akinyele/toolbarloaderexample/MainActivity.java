package com.akinyele.toolbarloaderexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.akinyele.toolbar_loader.ToolbarLoader;
import com.akinyele.toolbar_loader.networking.ToolBarInterceptor;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbarLoader)
    ToolbarLoader mToolbarLoader;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(ToolBarInterceptor.getInterceptor())
                .build();

        ToolBarInterceptor.registerToolbar(mToolbarLoader);

    }


    @OnClick(R.id.button_make_network_call)
    public void makeSimpleNetworkcall() {

        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();

        String url = "https://jsonip.com/";

        Request simpleRequest = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(simpleRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
            }
        });


    }
}
