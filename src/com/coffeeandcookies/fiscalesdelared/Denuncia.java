package com.coffeeandcookies.fiscalesdelared;

import com.parse.ParseGeoPoint;

public class Denuncia
{
	String nombre;
	String direccion;
	ParseGeoPoint gp;
	String id;
	String denuncia;
	int votos;
	String user;
	
	
	public String getUser()
	{
		return user;
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	public String getDireccion()
	{
		return direccion;
	}
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	public ParseGeoPoint getGp()
	{
		return gp;
	}
	public void setGp(ParseGeoPoint gp)
	{
		this.gp = gp;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getDenuncia()
	{
		return denuncia;
	}
	public void setDenuncia(String denuncia)
	{
		this.denuncia = denuncia;
	}
	public int getVotos()
	{
		return votos;
	}
	public void setVotos(int votos)
	{
		this.votos = votos;
	}
	
	
}
