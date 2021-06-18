package com.example.awesomeapp.util;

import android.app.Application;
import android.content.Context;

import com.example.awesomeapp.service.InternetConnection;

import java.util.Timer;
import java.util.TimerTask;

public class StartApplication extends Application {
    public static Timer t = null;

    public static boolean checkInternetConnection(Context context) {
        if (InternetConnection.isAvailable(context)) {
            Constant.INTERNET_STATUS = 0;
            if (t == null) {
                t = new Timer();
                t.scheduleAtFixedRate(new TimerTask() {
                                          @Override
                                          public void run() {
                                              checkInternetConnection(context);
                                          }
                                      },
                        0,
                        4000);
            }
            return true;
        } else {
            Constant.INTERNET_STATUS = 1;
            try {
                t.cancel();
                t = null;
            } catch (Exception ex) {
            }
            return false;
        }
    }
}
