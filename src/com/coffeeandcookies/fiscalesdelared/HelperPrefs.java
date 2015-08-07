package com.coffeeandcookies.fiscalesdelared;

import android.content.Context;
import android.content.SharedPreferences;

public class HelperPrefs
{
	public static String ciudad="ciudad";
	SharedPreferences settings;
	

	public HelperPrefs(Context context)
	{
		settings = context.getSharedPreferences("PREFSV1", Context.MODE_PRIVATE);
	}
	
	public void clearAll()
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

	public boolean exist(String val)
	{
		String value = settings.getString(val, null);

		if (value == null)
		{

			return false;
		}
		else
		{
			return true;
		}
	}

	public int getInt(String value)
	{
		return settings.getInt(value, -1);
	}

	public void setInt(String key, int value)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public String getString(String value)
	{
		return settings.getString(value, "");
	}

	public void setFloat(String key, Float value)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public Float getFloat(String value)
	{
		return settings.getFloat(value, 0f);
	}

	public void setString(String key, String value)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void removePref(String key)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		editor.commit();
	}
}
