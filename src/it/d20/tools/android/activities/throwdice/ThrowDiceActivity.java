package it.d20.tools.android.activities.throwdice;

import it.d20.tools.android.R;
import it.gecko.android.utils.DiceExpressionKeyListener;
import it.gecko.utils.Dice;
import it.gecko.utils.OperatorType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class ThrowDiceActivity extends Activity
{
	private static final String TAG = "it.d20.tools.android.activities.throwdice.ThrowDiceActivity";
	private TextView tvDiceExpression;
	private TextView tvResults;
	private EditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.throwdice_layout);
		et = (EditText) findViewById(R.id.diceExpressionEdit);
		tvDiceExpression = (TextView) findViewById(R.id.throwdice_tvDiceExpression);
		tvResults = (TextView) findViewById(R.id.throwdice_tvResults);
		et.setKeyListener(new DiceExpressionKeyListener());
		tvDiceExpression.setOnLongClickListener(new View.OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				boolean ret = false;
				if (((TextView) v).getText().length() >= 5)
				{
					getOperatorChoiceDialog().show();
					ret = true;
				}
				return ret;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.throwdice_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.throwdice_menu_clearTextView:
				onClear();
				return true;
			case R.id.throwdice_menu_resetDiceExpression:
				onResetDiceExpression();
				return true;
			case R.id.throwdice_menu_help:
				onHelp();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void onHelp()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		ScrollView scroll = new ScrollView(this);
		TextView text = new TextView(this);
		text.setText(R.string.help_throwdice);
		scroll.addView(text);

		builder.setView(scroll);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
	}

	public void onResetDiceExpression()
	{
		tvDiceExpression.setText("");
	}

	public void onClear()
	{
		tvResults.setText("");
	}

	public void onThrowDice(View view)
	{
		try
		{
			String msg = getText(R.string.throwdice_rolled_show_text).toString();
			msg = msg.concat(System.getProperty("line.separator"));
			msg = msg.concat(Dice.rollShowResults(tvDiceExpression.getText().toString()));
			tvResults.append(System.getProperty("line.separator"));
			tvResults.append(msg);
		}
		catch (Exception e)
		{
			Log.w(TAG, getText(R.string.throwdice_nade).toString());
		}
	}

	public void onEvaluateSingle(View view)
	{
		Dice d = Dice.parse(et.getText().toString());
		tvDiceExpression.append(d.toEnclosedString());
		et.setText("");
	}

	private ArrayAdapter<OperatorType> getAdapter()
	{
		int spinner_view = android.R.layout.simple_spinner_item;
		return new ArrayAdapter<OperatorType>(this, spinner_view, OperatorType.values());
	}

	protected Dialog getOperatorChoiceDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		final TextView info = new TextView(this);
		info.setText(getText(R.string.throwdice_operatorchoice_info));
		layout.addView(info);

		final Spinner opCombo = new Spinner(this);
		opCombo.setAdapter(getAdapter());
		layout.addView(opCombo);

		builder.setView(layout);

		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				OperatorType opType = (OperatorType) opCombo.getSelectedItem();
				if (opType != null)
				{
					tvDiceExpression.append(opType.toString());
					dialog.dismiss();
				}
				else
				{
					Context ctx = ((Dialog) dialog).getContext();
					CharSequence msg = ctx.getText(R.string.throwdice_choose_operator_msg);
					Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		return builder.create();
	}
}
