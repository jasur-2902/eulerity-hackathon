package uz.shukurov.eulerityproject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import uz.shukurov.eulerityproject.other.InternetCheck;

public class SplashScreen extends AppCompatActivity {


    WebView webView;

    private ConstraintLayout mConstraintLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //initializing
        mConstraintLayout = findViewById(R.id.constraintLayout);
        webView = findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/index.html");



        // if the internet is not available calls initSnackbar element
        if (!InternetCheck.isInternetAvailable(this)) {
            initSnackbar();
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO: Animation more smooth
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    SplashScreen.this.finish();

                }
            }, 2500);

        }


    }


    /**
     * initSnackbar creates a new Snackbar element, and checks if Internet Connections is available or not
     */
    private void initSnackbar() {
        final Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE).setAction(R.string.retry, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetCheck.isInternetAvailable(view.getContext())) {
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    SplashScreen.this.finish();
                } else initSnackbar();
            }
        });
        snackbar.show();
    }
}
