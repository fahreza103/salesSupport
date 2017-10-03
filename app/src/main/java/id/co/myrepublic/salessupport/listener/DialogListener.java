package id.co.myrepublic.salessupport.listener;

import android.content.DialogInterface;

/**
 * Listener for dialog action
 */

public interface DialogListener {

    /**
     * Invoked when ok / confirm has been pressed
     * @param dialog
     * @param result
     */
    public void onDialogOkPressed(DialogInterface dialog, Object... result);
}
