package com.solucioneshr_soft.changeicon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private boolean isBrithday;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("ChangeIcons", MODE_PRIVATE);
        isBrithday = preferences.getBoolean("iconsBirthday", false);

        //-----------------------------------------------------------------------------------------
        //---------WebView
        //-----------------------------------------------------------------------------------------
        WebView webView = findViewById(R.id.webView);
        WebSettings settingWeb = webView.getSettings();
        settingWeb.setAppCacheEnabled(false);
        settingWeb.setAllowContentAccess(true);
        settingWeb.setAllowFileAccess(true);
        settingWeb.setLoadsImagesAutomatically(true);
        settingWeb.setJavaScriptEnabled(true);
        settingWeb.setDomStorageEnabled(true);
        settingWeb.setBlockNetworkImage(false);
        settingWeb.setBuiltInZoomControls(false);
        settingWeb.setJavaScriptCanOpenWindowsAutomatically(true);
        settingWeb.setLoadWithOverviewMode(true);
        settingWeb.setSupportMultipleWindows(true);
        settingWeb.setUseWideViewPort(true);
        settingWeb.setAllowUniversalAccessFromFileURLs(true);
        settingWeb.setMediaPlaybackRequiresUserGesture(true);
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("Apk-WebViewJS", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }


        });

        webView.addJavascriptInterface(new FunJavascriptInterface(this), "Apk_WebView");

        webView.loadUrl("file:///android_asset/index.html");
        //=========================================================================================

    }

    private void newIconAdd() {
        // enable old icon
        PackageManager manager=getPackageManager();
        manager.setComponentEnabledSetting(new ComponentName(MainActivity.this,"com.solucioneshr_soft.changeicon.MainActivity")
                ,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);

        // enable new icon
        manager.setComponentEnabledSetting(new ComponentName(MainActivity.this,"com.solucioneshr_soft.changeicon.MainActivityAlias")
                ,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        Toast.makeText(MainActivity.this,"Enable New Icon" ,Toast.LENGTH_LONG).show();
    }

    private void oldIconAdd() {
        // enable old icon
        PackageManager manager=getPackageManager();
        manager.setComponentEnabledSetting(new ComponentName(MainActivity.this,"com.solucioneshr_soft.changeicon.MainActivity")
                ,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);

        // disable new icon
        manager.setComponentEnabledSetting(new ComponentName(MainActivity.this,"com.solucioneshr_soft.changeicon.MainActivityAlias")
                ,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
        Toast.makeText(MainActivity.this,"Enable Old Icon" ,Toast.LENGTH_LONG).show();
    }

    class FunJavascriptInterface{
        private  Context context;

        public FunJavascriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showToast(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void getIconChange(String data){
            if (data.contains("birthday")){
                newIconAdd();
            }

            if (data.contains("old")){
                oldIconAdd();
            }
        }

        @JavascriptInterface
        public String getInputText(){
            int random = new Random().nextInt(100);
            return "Ramdon  # " + String.valueOf(random);
        }
    }
}