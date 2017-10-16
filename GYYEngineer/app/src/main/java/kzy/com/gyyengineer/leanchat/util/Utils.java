package kzy.com.gyyengineer.leanchat.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import kzy.com.gyyengineer.App;
import kzy.com.gyyengineer.R;

public class Utils {

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showInfoDialog(Activity cxt, String msg, String title) {
        AlertDialog.Builder builder = getBaseDialogBuilder(cxt);
        builder.setMessage(msg)
                .setPositiveButton(cxt.getString(R.string.chat_utils_right), null)
                .setTitle(title)
                .show();
    }

    public static AlertDialog.Builder getBaseDialogBuilder(Activity ctx) {
        return new AlertDialog.Builder(ctx).setTitle(R.string.chat_utils_tips).setIcon(R.drawable.utils_icon_info_2);
    }

    public static void fixAsyncTaskBug() {
        // android bug
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        }.execute();
    }

    public static void toast(int id) {
        toast(App.ctx, id);
    }

    public static void toast(String s) {
        toast(App.ctx, s);
    }

    public static void toast(Context cxt, int id) {
        Toast.makeText(cxt, id, Toast.LENGTH_SHORT).show();
    }

    public static boolean doubleEqual(double a, double b) {
        return Math.abs(a - b) < 1E-8;
    }

    public static String getPrettyDistance(double distance) {
        if (distance < 1000) {
            int metres = (int) distance;
            return String.valueOf(metres) + App.ctx.getString(R.string.discover_metres);
        } else {
            String num = String.format("%.1f", distance / 1000);
            return num + App.ctx.getString(R.string.utils_kilometres);
        }
    }


    public static ProgressDialog showSpinnerDialog(Activity activity) {
        //activity = modifyDialogContext(activity);
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setMessage(App.ctx.getString(R.string.chat_utils_hardLoading));
        if (!activity.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    public static boolean filterException(Exception e) {
        if (e != null) {
            toast(e.getMessage());
            return false;
        } else {
            return true;
        }
    }

    public static void saveBitmap(String filePath, Bitmap bitmap) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
        }
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

}
