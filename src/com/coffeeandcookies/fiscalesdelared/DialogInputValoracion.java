package com.coffeeandcookies.fiscalesdelared;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DialogInputValoracion extends DialogFragment
{

	private Button buttonSave;
	private View view;
	private TextView text_title;
	EditText editText_denuncia;
	private EditSearchDialogListener mListener;
	private LatLng point;
	private GoogleMap googleMap;
	private EditText editText_nombre;
	private EditText editText_direccion;
	CheckBox checkBox1;

	private void findViews()
	{
		buttonSave = (Button) view.findViewById(R.id.button_save);
		editText_nombre = (EditText) view.findViewById(R.id.editText_nombre);
		editText_direccion = (EditText) view.findViewById(R.id.editText_direccion);
		editText_denuncia = (EditText) view.findViewById(R.id.editText_denuncia);
		text_title = (TextView) view.findViewById(R.id.text_title);
		checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);

	}

	public interface EditSearchDialogListener
	{
		void onFinishSearchDialog(GoogleMap googleMap);
	}

	@Override
	public void onAttach(Activity activity)
	{
		mListener = (EditSearchDialogListener) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDetach()
	{
		mListener = null;
		super.onDetach();
	}

	@Override
	public void onStart()
	{
		super.onStart();

		if (getDialog() == null)
			return;

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;

		getDialog().getWindow().setLayout(width, getDialog().getWindow().getAttributes().height);

	}

	void setEvents()
	{

		buttonSave.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (!errores())
				{
					ParseObject testObject = new ParseObject("Denuncia");
					testObject.put("Geo", new ParseGeoPoint(point.latitude, point.longitude));
					testObject.put("Nombre", editText_nombre.getText().toString());
					testObject.put("Direccion", editText_direccion.getText().toString());
					testObject.put("Denuncia", editText_denuncia.getText().toString());
					testObject.put("Votos", 1);
					ParseACL p = new ParseACL();
					ParseUser currentUser = ParseUser.getCurrentUser();
					testObject.put("User", currentUser.getObjectId());
					p.setPublicWriteAccess(true);
					p.setPublicReadAccess(true);
					testObject.setACL(p);
					final ProgressDialog pd = new ProgressDialog(getActivity());
					pd.setMessage("Guardando denuncia");
					pd.show();
					testObject.saveInBackground(new SaveCallback()

					{

						@Override
						public void done(ParseException e)
						{
							pd.dismiss();
							if (e == null)
							{
								getDialog().dismiss();
								Toast.makeText(getActivity(), "Se guardo correctamente la denuncia", Toast.LENGTH_SHORT).show();
								if (checkBox1.isChecked())
								{
								
									
									Intent sendIntent = new Intent();
									sendIntent.setAction(Intent.ACTION_SEND);
									sendIntent.putExtra(Intent.EXTRA_TEXT, editText_denuncia.getText().toString()+" en "+editText_nombre.getText().toString()+ " ubicada en "+editText_direccion.getText().toString()
											+" via #FiscalesDeLaRed");
									sendIntent.setType("text/plain");
									startActivity(sendIntent);
								}
								
								mListener.onFinishSearchDialog(googleMap);
							}
							else
							{
								Toast.makeText(getActivity(), "Intentelo nuevamente", Toast.LENGTH_SHORT).show();
							}

						}
					});
				}
			}

			private boolean errores()
			{
				if (editText_denuncia.getText().toString().trim().length() == 0)
				{
					Toast.makeText(getActivity(), "Falta completar la denuncia", Toast.LENGTH_SHORT).show();
					return true;
				}
				else

					if (editText_nombre.getText().toString().trim().length() == 0)
					{
						Toast.makeText(getActivity(), "Falta completar el nombre", Toast.LENGTH_SHORT).show();
						return true;
					}
					else

						if (editText_direccion.getText().toString().trim().length() == 0)
						{
							Toast.makeText(getActivity(), "Falta completar la dirección", Toast.LENGTH_SHORT).show();
							return true;
						}
				return false;
			}
		});

	}

	public DialogInputValoracion(LatLng point, GoogleMap googleMap)
	{
		this.point = point;
		this.googleMap = googleMap;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.dialog_valoracion, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		findViews();
		setEvents();
		text_title.setText("Denunciar");
		return view;
	}

}