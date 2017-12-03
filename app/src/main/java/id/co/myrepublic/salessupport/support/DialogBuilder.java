package id.co.myrepublic.salessupport.support;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import id.co.myrepublic.salessupport.R;
import id.co.myrepublic.salessupport.listener.DialogListener;

/**
 * Common dialog builder
 */

public class DialogBuilder {

    private static DialogBuilder dialogBuilder = new DialogBuilder();
    private DialogListener dialogListener;

    private DialogBuilder() {}

    public static DialogBuilder getInstance() {
        return dialogBuilder;
    }

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    /**
     * Create simple alert dialog
     * @param context
     * @param title
     * @param message
     */
    public void createAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
            .setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();

    }

    /**
     * Create simple input dialog
     * @param context
     * @param title
     * @param message
     */
    public void createInputDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogListener != null && input.getText() != null) {
                    dialogListener.onDialogOkPressed(dialog,which,input.getText().toString().toLowerCase(),which);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialogListener != null) {
                    dialogListener.onDialogCancelPressed(dialog,which);
                }
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }


}
