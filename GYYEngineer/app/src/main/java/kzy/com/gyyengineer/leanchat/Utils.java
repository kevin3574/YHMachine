package kzy.com.gyyengineer.leanchat;

import android.app.Activity;
import android.app.ProgressDialog;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Closeable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import kzy.com.gyyengineer.R;

/**
 * Created by lzw on 15/4/27.
 */
public class Utils {
  public static ProgressDialog showSpinnerDialog(Activity activity) {
    ProgressDialog dialog = new ProgressDialog(activity);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.setCancelable(true);
    dialog.setMessage(activity.getString(R.string.chat_utils_hardLoading));
    if (!activity.isFinishing()) {
      dialog.show();
    }
    return dialog;
  }

  public static String uuid() {
    StringBuilder sb = new StringBuilder();
    int start = 48, end = 58;
    appendChar(sb, start, end);
    appendChar(sb, 65, 90);
    appendChar(sb, 97, 123);
    String charSet = sb.toString();
    StringBuilder sb1 = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 24; i++) {
      int len = charSet.length();
      int pos = random.nextInt(len);
      sb1.append(charSet.charAt(pos));
    }
    return sb1.toString();
  }

  public static void appendChar(StringBuilder sb, int start, int end) {
    int i;
    for (i = start; i < end; i++) {
      sb.append((char) i);
    }
  }

  public static String millisecsToDateString(long timestamp) {
    long gap = System.currentTimeMillis() - timestamp;
    if (gap < 1000 * 60 * 60 * 24) {
      String s = (new PrettyTime()).format(new Date(timestamp));
      return s;
    } else {
      SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
      return format.format(new Date(timestamp));
    }
  }


  public static void closeQuietly(Closeable closeable) {
    try {
      closeable.close();
    } catch (Exception e) {
    }
  }
}
