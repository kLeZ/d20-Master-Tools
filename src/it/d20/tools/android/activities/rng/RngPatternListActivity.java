/**
 * 
 */
package it.d20.tools.android.activities.rng;

import it.d20.tools.android.R;
import it.d20.tools.android.db.PatternDatabase;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author kLeZ-hAcK
 */
public class RngPatternListActivity extends Activity
{
	public static final int mult_ch_layout = android.R.layout.simple_list_item_multiple_choice;
	public static final int LONG_PRESS_EDIT = 0;
	public static final int LONG_PRESS_DELETE = 1;
	@SuppressWarnings("unused")
	private static final String TAG = "it.d20.tools.android.activities.rng.RngPatternListActivity[%s]";

	ArrayList<String> patternList;
	ArrayAdapter<String> adapter;
	ListView lv;
	EditText et;

	private ArrayList<String> getSelection()
	{
		return getIntent().getExtras().getStringArrayList(RngTabHostActivity.SHARED_P_L_NAME);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rngpatternlist_layout);

		adapter = new ArrayAdapter<String>(this, mult_ch_layout);
		sync(getSelection());
		adapter.setNotifyOnChange(true);
		et = (EditText) findViewById(R.id.patternText);
		lv = ((ListView) findViewById(R.id.listView_PatternList));
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(adapter);
		lv.setItemsCanFocus(false);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (((ListView) parent).getCheckedItemPositions().get(position))
				{
					if (!getSelection().contains(patternList.get(position)))
					{
						getSelection().add(patternList.get(position));
					}
				}
				else
				{
					if (getSelection().contains(patternList.get(position)))
					{
						getSelection().remove(position);
					}
				}
			}
		});
		changeSelectionState(lv, true);
		registerForContextMenu(lv);
	}

	@SuppressWarnings("unchecked")
	public void sync(ArrayList<String> updatedList)
	{
		patternList = (ArrayList<String>) updatedList.clone();
		adapter.clear();
		for (String item : updatedList)
		{
			adapter.add(item);
		}
	}

	public void rereadSelection(PatternDatabase db)
	{
		getSelection().clear();
		getSelection().addAll(db.getPatternList());
		sync(getSelection());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.rng_patlst_menu, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		if (v.getId() == R.id.listView_PatternList)
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(patternList.get(info.position));
			String[] menuItems = getResources().getStringArray(R.array.rng_patlst_menu);
			for (int i = 0; i < menuItems.length; i++)
			{
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		getResources().getStringArray(R.array.rng_patlst_menu);
		String pattern = patternList.get(info.position);
		PatternDatabase db = new PatternDatabase(this);
		db.open();
		switch (menuItemIndex)
		{
			case LONG_PRESS_EDIT:
				db.removePattern(pattern);
				et.setText(pattern);
				et.requestFocus();
				break;
			case LONG_PRESS_DELETE:
				db.removePattern(pattern);
				break;
		}
		rereadSelection(db);
		db.close();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
			case R.id.rng_pl_select_all:
				changeSelectionState(lv, true);
				return true;
			case R.id.rng_pl_deselect_all:
				changeSelectionState(lv, false);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void changeSelectionState(ListView view, boolean checked)
	{
		for (int i = 0; i < view.getCount(); i++)
		{
			view.setItemChecked(i, checked);
			if (checked && !getSelection().contains(patternList.get(i)))
			{
				getSelection().add(patternList.get(i));
			}
		}
		if (!checked)
		{
			getSelection().clear();
		}
	}

	public void addPattern(View view)
	{
		String pattern;
		pattern = et.getText().toString();
		et.setText("");
		PatternDatabase db = new PatternDatabase(this);
		db.open();
		db.setPattern(pattern);
		rereadSelection(db);
		db.close();
	}
}
