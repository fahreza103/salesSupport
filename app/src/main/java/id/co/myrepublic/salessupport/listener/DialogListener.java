package id.co.myrepublic.salessupport.listener;

import android.content.DialogInterface;

/**
 * Listener for dialog action
 */

public interface DialogListener {

    /**
     * Invoked when ok / confirm has been pressed
     * @param dialog dialogBuilder Object
     * @param result all input from the dialog should be stored here
     */
    public void onDialogOkPressed(DialogInterface dialog, int which,Object... result);

    /**
     * invoked when cancel button has been pressed
     */
    public void onDialogCancelPressed (DialogInterface dialog, int which);
}
