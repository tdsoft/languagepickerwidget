package org.gnvo.langpicker;

import java.util.Locale;

public class Loc {
	protected Locale locale = null;

	public Loc(String ietfLanguageTag) {
		String[] l = ietfLanguageTag.split("_");
		if (l.length == 2)
			this.locale = new Locale(l[0], l[1]);
	}

	public String oneLineLanguageCountry() {
		return String.format("%s (%s)", toTitleCase(locale.getDisplayLanguage(locale)), toTitleCase(locale.getDisplayCountry(locale)));
	}
	
	public String twoLinesLanguageCountry() {
		
		return String.format("%s\n%s", toTitleCase(locale.getDisplayLanguage(locale)), toTitleCase(locale.getDisplayCountry(locale)));
	}

	public String getIetfLanguageTag(){
		return String.format("%s_%s", 
				locale.getLanguage(),
				locale.getCountry());
	}
	
	public Locale getLocale(){
		return locale;
	}
	
	private static String toTitleCase(String s) {
		if (s.length() == 0) {
			return s;
		}
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	public boolean isLocaleCurrentLocale() {
		return Locale.getDefault().equals(this.getLocale());
	}

}
