package it.d20.tools.android.dialogs;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

public abstract class PickerDismissListener implements OnDismissListener
{
	private PickerDialog dialog;

	public PickerDismissListener(PickerDialog dialog)
	{
		this.dialog = dialog;
	}

	public PickerDialog getDialog()
	{
		return dialog;
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnDismissListener#onDismiss(android.content.DialogInterface)
	 */
	@Override
	public abstract void onDismiss(DialogInterface dialog);
}
