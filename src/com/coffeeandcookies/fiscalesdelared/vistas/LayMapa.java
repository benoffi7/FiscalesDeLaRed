package com.coffeeandcookies.fiscalesdelared.vistas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coffeeandcookies.fiscalesdelared.AdaptadorMapas;
import com.coffeeandcookies.fiscalesdelared.Denuncia;
import com.coffeeandcookies.fiscalesdelared.DialogInputValoracion;
import com.coffeeandcookies.fiscalesdelared.DialogInputValoracion.EditSearchDialogListener;
import com.coffeeandcookies.fiscalesdelared.HelperPrefs;
import com.coffeeandcookies.fiscalesdelared.ParseApplication;
import com.coffeeandcookies.fiscalesdelared.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class LayMapa extends Activity implements OnMapReadyCallback, EditSearchDialogListener
{
	private MapFragment mMap;
	boolean firstTime = true;
	boolean satelite = false;
	HashMap<Marker, Denuncia> markers;
	private ImageView image_satelite;
	public static ArrayList<Denuncia> gustos;
	LinearLayout ll_voto;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_mapa);
		ll_voto = (LinearLayout) findViewById(R.id.ll_voto);
		ll_voto.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				FireMissilesDialogFragment f = new FireMissilesDialogFragment();
				f.show(getFragmentManager(), "aaa");

			}
		});
		setFragment();
		markers = new HashMap<Marker, Denuncia>();
		gustos = new ArrayList<Denuncia>();
		ParseAnalytics.trackAppOpenedInBackground(getIntent());
	}

	public class FireMissilesDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("Ingrese texto").setPositiveButton("Masculino", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Uri smsUri = Uri.parse("tel:" + "30777");
					Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
					intent.putExtra("address", "30777");
					intent.putExtra("sms_body", "VOTO DNI M");
					intent.setType("vnd.android-dir/mms-sms");
					startActivity(intent);
				}
			}).setNegativeButton("Femenino", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					Uri smsUri = Uri.parse("tel:" + "30777");
					Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
					intent.putExtra("address", "30777");
					intent.putExtra("sms_body", "VOTO DNI F");
					intent.setType("vnd.android-dir/mms-sms");
					startActivity(intent);
				}
			});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_busqueda, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_info:
			startActivity(new Intent(LayMapa.this, LayAbout.class));
			return true;
		case R.id.action_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "Soy Fiscal de la Red via #FdR "
					+ "https://play.google.com/store/apps/details?id=com.coffeeandcookies.fiscalesdelared");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setFragment()
	{
		mMap = MapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map_root, mMap);
		fragmentTransaction.commit();
		mMap.getMapAsync(this);
	}

	private void setUpMapa(final GoogleMap googleMap)
	{
		googleMap.setMyLocationEnabled(true);
		AdaptadorMapas ciw = new AdaptadorMapas(getLayoutInflater());
		googleMap.setInfoWindowAdapter(ciw);
		googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
		{

			@Override
			public void onMyLocationChange(android.location.Location location)
			{
				if (firstTime)
				{

					Toast.makeText(getApplicationContext(), "Toca (dos segundos) sobre el mapa para agregar una denuncia", Toast.LENGTH_SHORT).show();
					LatLng lt = new LatLng(location.getLatitude(), location.getLongitude());
					CameraPosition camPos = new CameraPosition.Builder().target(lt).zoom(14).build();
					CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
					googleMap.animateCamera(camUpd3);
					firstTime = false;
				}
			}
		});

		googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
		{
			@Override
			public boolean onMarkerClick(Marker arg0)
			{

				LatLng lt = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
				CameraPosition camPos = new CameraPosition.Builder().target(lt).zoom(14).build();
				CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
				googleMap.animateCamera(camUpd3);
				arg0.showInfoWindow();

				return true;
			}
		});

		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener()
		{

			@Override
			public void onInfoWindowClick(final Marker marker)
			{
				marker.hideInfoWindow();
				final Denuncia aux = markers.get(marker);
				final HelperPrefs hp = new HelperPrefs(getApplicationContext());
				if (hp.getInt(aux.getId()) == -1)
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Denuncia");
					final ProgressDialog pd = new ProgressDialog(LayMapa.this);
					pd.setMessage("Enviando información");
					pd.show();
					query.getInBackground(aux.getId().trim(), new GetCallback<ParseObject>()
					{

						@Override
						public void done(ParseObject object, ParseException e)
						{
							pd.dismiss();
							if (e == null)
							{
								ParseUser currentUser = ParseUser.getCurrentUser();
								if (aux.getUser().equals(currentUser.getObjectId()))
								{
									marker.remove();
									object.deleteInBackground();
								}
								else
								{

									object.increment("Votos");
									object.saveInBackground(new SaveCallback()
									{

										@Override
										public void done(ParseException e)
										{
											if (e == null)
											{
												Toast.makeText(getApplicationContext(), "Voto enviado", Toast.LENGTH_SHORT).show();
												aux.setVotos(aux.getVotos() + 1);
												marker.remove();
												mostrarMarcador(aux, googleMap);
												hp.setInt(aux.getId(), 1);
											}
											else
											{
												Toast.makeText(getApplicationContext(), aux.getId() + " " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

											}

										}
									});
								}
							}
						}
					});
				}

				else
				{
					Toast.makeText(getApplicationContext(), "Una sola votación es válida por usuario por establecimiento", Toast.LENGTH_SHORT).show();
				}

			}
		});

		googleMap.setOnMapLongClickListener(new OnMapLongClickListener()
		{

			@Override
			public void onMapLongClick(LatLng point)
			{
				DialogInputValoracion fragment1 = new DialogInputValoracion(point, googleMap);
				fragment1.show(getFragmentManager(), "");

			}
		});

		image_satelite = (ImageView) findViewById(R.id.image_satelite);
		image_satelite.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (satelite == false)
				{
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

				}
				else
				{
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				}
				satelite = !satelite;

			}
		});

		getDenuncias(googleMap);
	}

	void getDenuncias(final GoogleMap googleMap)
	{
		googleMap.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Denuncia");
		final ProgressDialog pd = new ProgressDialog(LayMapa.this);
		pd.setMessage("Descargando denuncias");
		pd.show();
		query.findInBackground(new FindCallback<ParseObject>()
		{

			public void done(List<ParseObject> scoreList, ParseException e)
			{
				pd.dismiss();
				if (e == null)
				{
					Log.d(ParseApplication.TAG, "Retrieved Denuncia " + scoreList.size() + " scores");
					for (ParseObject parseObject : scoreList)
					{
						Denuncia h = new Denuncia();
						h.setId(parseObject.getObjectId());
						h.setNombre(parseObject.getString("Nombre"));
						h.setGp(parseObject.getParseGeoPoint("Geo"));
						h.setDireccion(parseObject.getString("Direccion"));
						h.setVotos(parseObject.getInt("Votos"));
						h.setDenuncia(parseObject.getString("Denuncia"));
						h.setUser(parseObject.getString("User"));
						gustos.add(h);
						mostrarMarcador(h, googleMap);
					}
				}
				else
				{
					Log.d(ParseApplication.TAG, "Error Denuncia: " + e.getMessage());
				}
			}
		});
	}

	private void mostrarMarcador(Denuncia h, GoogleMap googleMap)
	{
		MarkerOptions mo = new MarkerOptions().position(new LatLng(h.getGp().getLatitude(), h.getGp().getLongitude())).title(h.getNombre())
				.snippet(h.getDireccion() + "##" + h.getDenuncia() + " (" + h.getVotos() + ")" + "##" + h.getUser() + "##" + h.getId());
		markers.put(googleMap.addMarker(mo), h);
	}

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		setUpMapa(googleMap);

	}

	@Override
	public void onFinishSearchDialog(GoogleMap googleMap)
	{
		getDenuncias(googleMap);

	}
}
