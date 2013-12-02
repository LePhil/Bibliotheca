package i18n;

import java.beans.Beans;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Bundle access
	//
	////////////////////////////////////////////////////////////////////////////
	private static final String BUNDLE_NAME = "i18n.messages";
	private static final ResourceBundle RESOURCE_BUNDLE = loadBundle();
	private static ResourceBundle loadBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Strings access
	//
	////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * @param key key for the locale
	 * @param param Optional, value to replace the $PARAM placeholder with, if that exists
	 * @return
	 * @author pchr
	 */
	public static String getString(String key, Object param) {
		try {
			ResourceBundle bundle = Beans.isDesignTime() ? loadBundle() : RESOURCE_BUNDLE;
			
			String value = bundle.getString( key );
			if ( param == null ) {
				param = "";
			}
			
			return value.replace( "$PARAM", (CharSequence) param );
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}
	/**
	 * 
	 * @param key key for the locale
	 * @return
	 * @author pchr
	 */
	public static String getString( String key) {
		return getString(key, "");
	}
}
