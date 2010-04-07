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


import android.content.Context;
import android.content.SharedPreferences;

public class LangPickerDataAdapter {
    
	public static final String PREFS_NAME = "singlePreferences";
	
	private SharedPreferences getPreferencesObject (Context context) {
		return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);		
	}
	
    public void addWidgetLanguage(Context context, int appWidgetId, String language) {
    	getPreferencesObject(context).edit().putString(Integer.toString(appWidgetId) , language).commit();

    }

    public void removeWidgetLanguage(Context context, int appWidgetId) {
    	getPreferencesObject(context).edit().remove(Integer.toString(appWidgetId)).commit();
    }

    public String fetchWidgetLanguage(Context context, int appWidgetId) {
    	String language =  getPreferencesObject(context).getString(Integer.toString(appWidgetId), "");
    	return language != null ? language : ""; 
    }

}
