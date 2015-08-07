package com.coffeeandcookies.fiscalesdelared;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.coffeeandcookies.fiscalesdelared.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseUser;

@SuppressLint("InflateParams")
public class AdaptadorMapas implements InfoWindowAdapter
{
	LayoutInflater inflater = null;

	public AdaptadorMapas(LayoutInflater inflater)
	{
		this.inflater = inflater;
	}

	@Override
	public View getInfoWindow(Marker marker)
	{
		return (null);
	}

	@Override
	public View getInfoContents(Marker marker)
	{
		View popup = inflater.inflate(R.layout.custom_info_window, null);
		TextView tipo = (TextView) popup.findViewById(R.id.tipo);
		TextView txt_direccion = (TextView) popup.findViewById(R.id.direccion);
		TextView txt_denuncia = (TextView) popup.findViewById(R.id.txt_denuncia);
		TextView textView_hint = (TextView) popup.findViewById(R.id.textView_hint);
		String direccion = marker.getSnippet().split("##")[0];
		String denuncia = marker.getSnippet().split("##")[1];
		String user = marker.getSnippet().split("##")[2];
		String id = marker.getSnippet().split("##")[3];
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (user.equals(currentUser.getObjectId()))
		{
			
			textView_hint.setText("Toca para eliminar la denuncia");			
		}
		else
		{
			HelperPrefs hp = new HelperPrefs(inflater.getContext());
			if (hp.getInt(id)!=-1)
			{
				textView_hint.setVisibility(View.GONE);
			}
		}
		
		tipo.setText(marker.getTitle());

		txt_direccion.setText(direccion);
		txt_denuncia.setText(denuncia);
		
		return (popup);
	}

}

