package it.d20.tools.android.dialogs;

import android.view.View;

public abstract class PickerClickListener implements View.OnClickListener
{
	private PickerDialog dialog;

	public PickerClickListener(PickerDialog dialog)
	{
		this.dialog = dialog;
	}

	public PickerDialog getDialog()
	{
		return dialog;
	}

	@Override
	public abstract void onClick(View v);
}
