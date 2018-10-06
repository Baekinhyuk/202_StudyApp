package cau.study_202.network;

import android.annotation.SuppressLint;
import android.os.StrictMode;
public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetworkPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 21) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
