package com.coffeeandcookies.fiscalesdelared;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.coffeeandcookies.fiscalesdelared.R;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ParseStarterProjectActivity extends Activity
{
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpenedInBackground(getIntent());

		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("foo", "bar");
		testObject.saveInBackground();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
		// query.whereEqualTo("playerName", "Dan Stemkoski");
		query.findInBackground(new FindCallback<ParseObject>()
		{
			public void done(List<ParseObject> scoreList, ParseException e)
			{
				if (e == null)
				{
					Log.d(ParseApplication.TAG, "Retrieved " + scoreList.size() + " scores");
				}
				else
				{
					Log.d(ParseApplication.TAG, "Error: " + e.getMessage());
				}
			}
		});	
		
		ParsePush.subscribeInBackground("pepe", new SaveCallback()
		{
			@Override
			public void done(ParseException e)
			{
				if (e == null)
				{
					Log.d(ParseApplication.TAG, "successfully subscribed to the broadcast channel.");
				}
				else
				{
					Log.e(ParseApplication.TAG, "failed to subscribe for push", e);
				}
			}
		});

	}
}
