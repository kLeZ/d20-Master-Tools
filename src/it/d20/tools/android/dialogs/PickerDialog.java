/**
 * 
 */
package it.d20.tools.android.dialogs;

import it.d20.tools.android.R;
import it.gecko.android.widgets.NumberPicker;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

/**
 * @author kLeZ-hAcK
 */
public class PickerDialog extends Dialog
{
	private int current;
	private NumberPicker np;
	private int title;
	private int buttonText;
	private Button btnSave;

	/**
	 * @param context
	 */
	public PickerDialog(Context context, int title, int buttonText, int current)
	{
		super(context);
		this.current = current;
		this.title = title;
		this.buttonText = buttonText;
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		btnSave = ((Button) findViewById(R.id.save_n_res));
		np = ((NumberPicker) findViewById(R.id.np_n_results));

		setContentView(R.layout.picker_layout);
		setTitle(title);
		btnSave.setText(buttonText);
		np.setCurrent(current);
		setCancelable(true);
	}

	public void setOnClickListener(PickerClickListener clickListener)
	{
		btnSave.setOnClickListener(clickListener);
	}

	public void setOnDismissListener(PickerDismissListener dismissListener)
	{
		super.setOnDismissListener(dismissListener);
	}

	/**
	 * @return the current
	 */
	public int getCurrent()
	{
		if (np.getCurrent() != current)
		{
			current = np.getCurrent();
		}
		return current;
	}
}
