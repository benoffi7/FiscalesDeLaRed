package com.coffeeandcookies.fiscalesdelared.vistas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.coffeeandcookies.fiscalesdelared.R;

public class LayAbout extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.lay_about);
		cc();
		super.onCreate(savedInstanceState);
	}

	private void cc()
	{

		findViewById(R.id.ll_twitter).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/pdr_mardelplata")));
			}
		});

		findViewById(R.id.ll_facebook).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/PartidoDeLaRedMDQ")));
			}
		});

		findViewById(R.id.ll_mail).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("message/rfc822");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "hola@partidodelaredmdq.org" });
				intent.putExtra(Intent.EXTRA_SUBJECT, "[FiscalesDeLaRed] Contacto");
				startActivity(Intent.createChooser(intent, "Email"));
			}
		});

		findViewById(R.id.ll_playstore).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Coffee And Cookies Soft")));
			}
		});

		findViewById(R.id.ll_web).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://partidodelared.org")));
			}
		});
	}
}