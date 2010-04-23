/*
 *  This file is part of Language Picker Widget.
 *
 *  Language Picker Widget is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Language Picker Widget is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Language Picker Widget.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gnvo.langpicker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * The configuration screen for the ExampleAppWidgetProvider widget sample.
 */
public class WidgetConfigure extends ListActivity{
	private static final String LOG_TAG = "WidgetConfigure";
	private static final int WARNING_DIALOG = 0;
	private ListView mListView = null;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private ArrayList<String>  mLocales;

	
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d(LOG_TAG, "dialog id='" + id + "'");
		switch (id) {
		case WARNING_DIALOG:
			Log.d(LOG_TAG, "inside warning dialog");			
			return new AlertDialog.Builder(WidgetConfigure.this)
			.setTitle(R.string.alert_dialog_language_not_selected)
			.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.create();
		}
		return null;
	}


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED);
		setContentView(R.layout.widget_configure);

		getAndValidateAppWidgetId();

		populateList();

		mListView = getListView();
		mListView.setItemsCanFocus(false);
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


		Button newLocaleButton = (Button) findViewById(R.id.accept);

		newLocaleButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String languageCountry =  (String) mListView.getItemAtPosition(mListView.getCheckedItemPosition());

				if (languageCountry != null){					
					final Context context = v.getContext();
					new LangPickerDataAdapter().addWidgetLanguage(context, mAppWidgetId, mLocales.get(mListView.getCheckedItemPosition()));

					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					LangPicker.prepareAppWidget(context, appWidgetManager, mAppWidgetId);

					Intent resultValue = new Intent();
					resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
					setResult(RESULT_OK, resultValue);
					finish();

				} else {
					showDialog(WARNING_DIALOG);
				}

				Log.d(LOG_TAG, "Selected text='" + languageCountry + "'");

			}
		});


		registerForContextMenu(mListView);
	}


	/**
	 * 
	 */
	private void getAndValidateAppWidgetId() {
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}


	/**
	 * 
	 */
	private void populateList() {
		// Insert all system locales
		Log.d(LOG_TAG, "Current locale='" + Locale.getDefault() + "'");

		String[] systemLocaleIetfLanguageTags = getAssets().getLocales();

		Arrays.sort(systemLocaleIetfLanguageTags);

        ArrayList<String> data = new ArrayList<String>();
		mLocales = new ArrayList<String>();
		
		for (String ietfLanguageTag : systemLocaleIetfLanguageTags) {
			if (ietfLanguageTag != null && ietfLanguageTag.length() == 5) {
				Loc loc = new Loc(ietfLanguageTag);
				
				mLocales.add(ietfLanguageTag);
				data.add(loc.oneLineLanguageCountry());
			}
		}
		
		systemLocaleIetfLanguageTags = null;

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, data));

	}
	

}