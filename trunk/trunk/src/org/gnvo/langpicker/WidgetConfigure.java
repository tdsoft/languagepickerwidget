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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

/**
 * The configuration screen for the ExampleAppWidgetProvider widget sample.
 */
public class WidgetConfigure extends ListActivity{
	private static final String LOG_TAG = "WidgetConfigure";
	private static final int WARNING_DIALOG = 0;
	private ListView mListView = null;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private String mLocales[];

	private static class Loc{
		protected String localizedLanguage = null;
		protected String localizedCountry = "";	
		protected Locale locale;

		public Loc(String localizedLanguage, Locale locale) {
			this.localizedLanguage = localizedLanguage;
			this.locale = locale;
		}
		
		public void setLocalizedCountry(){
			this.localizedCountry = locale.getDisplayCountry(locale);
		}
		
		@Override
		public String toString() {
			if (localizedCountry.length() > 0)
				return String.format("%s (%s)", toTitleCase(localizedLanguage), toTitleCase(localizedCountry));
			else
				return toTitleCase(localizedLanguage);
		}

		public Object getLanguageCode() {
			return locale.getLanguage();
		}

		public Object getCountryCode() {
			return locale.getCountry();
		}

		public Object getlocalizedCountry() {
			return toTitleCase(localizedCountry);
		}

		public Object getlocalizedLanguage() {
			return toTitleCase(localizedLanguage);
		}
	}
	
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
					new LangPickerDataAdapter().addWidgetLanguage(context, mAppWidgetId, mLocales[mListView.getCheckedItemPosition()]);

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

		String[] systemLocales = getAssets().getLocales();

		Arrays.sort(systemLocales);

		Loc[] preprocess = new Loc[systemLocales.length];

		int finalSize = 0;
		
		for (String l : systemLocales) {
			
			if (l != null && l.length() == 5) {
				String language = l.substring(0, 2);
				String country = l.substring(3, 5);
				Locale locale = new Locale(language, country);

				if (finalSize == 0) {
					preprocess[finalSize++] = new Loc(locale.getDisplayLanguage(locale), locale);
				} else {
					// check previous entry:
					// same lang and a country -> upgrade to full name and
					// insert ours with full name
					// diff lang -> insert ours with lang-only name
					if (preprocess[finalSize - 1].locale.getLanguage().equals(language)) {
						preprocess[finalSize] = new Loc(locale.getDisplayLanguage(locale), locale);
						preprocess[finalSize - 1].setLocalizedCountry();
						preprocess[finalSize++].setLocalizedCountry();
					} else {
						preprocess[finalSize++] = new Loc(locale.getDisplayLanguage(locale), locale);
					}
				}
			}
		}
		
		String data[] = new String[finalSize];
		mLocales = new String[finalSize];
		for (int i = 0; i < finalSize ; i++) {
			data[i] = preprocess[i].toString();
			mLocales[i] =  String.format("%s;%s;%s_%s", 
					preprocess[i].getlocalizedLanguage(), 
					preprocess[i].getlocalizedCountry(), 
					preprocess[i].getLanguageCode(),
					preprocess[i].getCountryCode());
		}

		systemLocales = null;

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, data));

	}
	
	private static String toTitleCase(String s) {
		if (s.length() == 0) {
			return s;
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

}