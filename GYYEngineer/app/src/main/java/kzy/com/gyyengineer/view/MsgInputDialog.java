package kzy.com.gyyengineer.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kzy.com.gyyengineer.R;

/**
 * 创建日期：2017/6/16 0016 on 10:39
 * 描述：特别提示dialog
 * 作者：赵金祥  Administrator
 */

public class MsgInputDialog extends Dialog implements View.OnClickListener {
    private DialogListener dialogListener;

    public MsgInputDialog(Context context) {
        super(context);
    }

    public MsgInputDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MsgInputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_msg_input);
        Button but = (Button) findViewById(R.id.but);
        but.setOnClickListener(this);
    }

    public void setOnDialogClickListen(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but:
                dialogListener.click();
                break;
        }
    }
}
