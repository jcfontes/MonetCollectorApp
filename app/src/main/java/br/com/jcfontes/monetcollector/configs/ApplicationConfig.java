package br.com.jcfontes.monetcollector.configs;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class ApplicationConfig extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
