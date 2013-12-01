/* 
* Created On: Nov 12, 2013
*
* Created By: dheryta
*/
package com.t1dm.t1dmanagementapp;

import java.util.List;
import java.util.Locale;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GetUserLocation implements LocationListener {
	
	private boolean isGPSServiceEnabled;
	private boolean isNetworkServiceEnabled;
	private Context context;
	private LocationManager locationManager;

	public GetUserLocation(Context context){
		this.context = context;
	}
	
	private static final long MINIMUM_DISTANCE = 10; // 10 meters 
    private static final long MINIMUM_TIME = 1000 * 60 * 1; // 1 minute
    private Location currentLocation;
    private double checkLatitude, checkLongitude;    
    private double range = 1; //kilometers
    
	public String getCompleteAddress() {
		Location location = getCurrentLocationFromGPSorNetwork();
		String address = "";
		if (location != null) {
			try {
				Geocoder geocoder = new Geocoder(this.context, Locale.ENGLISH);
				List<Address> returnedAddresses = geocoder.getFromLocation(
						location.getLatitude(), location.getLongitude(),
						1);
				StringBuilder completeAddress = new StringBuilder(
						"I am around \n");
				for (int i = 0; i < returnedAddresses.get(0)
						.getMaxAddressLineIndex(); i++)
					completeAddress.append(
							returnedAddresses.get(0).getAddressLine(i)).append(
							"\n");
				address = completeAddress.toString();
			} catch (Exception e) {
				address = "Oops..Address could not be geo-coded,\n Please try again!!!!";
			}
		}
		return address;
	}
	
	private boolean checkIfGPSIsEnabled()
	{
		boolean status = false;
		if (locationManager!=null)
			status = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		else
			status = false;
		return status;
	}
	
	private boolean checkIfNetworkIsEnabled()
	{
		boolean status = false;
		if (locationManager!=null)
			status = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		else
			status = false;
		return status;
	}
	
	private Location getLocationFromNetwork()
	{
		Location location = null;
		try{
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
		
        
        if (locationManager != null) 
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}catch(Exception e){
			location = null;
		}
		return location;
	}
	
	private Location getLocationFromGPS()
	{
		Location location = null;
		try{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
        
        if (locationManager != null) 
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}catch(Exception e){
			location = null;
		}
		return location;
	}
		

	/*
	 * Gets location from network and/or GPS
	 * Compares both locations, and returns the best
	 * Returns one location if either of them is on
	 * Returns null if none of them is on.
	 * 
	 */
	private Location getCurrentLocationFromGPSorNetwork()
	{
		Location currentLocation = null;
		Location gpsLocation=null, networkLocation=null;
		try {
			
				locationManager = (LocationManager) this.context.getSystemService("location");
					
		
		isGPSServiceEnabled = checkIfGPSIsEnabled();
		isNetworkServiceEnabled = checkIfNetworkIsEnabled();
		
		if (isNetworkServiceEnabled){
			networkLocation = getLocationFromNetwork();
			currentLocation = networkLocation;
		}
		if (isGPSServiceEnabled){
			gpsLocation = getLocationFromGPS();
			currentLocation = gpsLocation;
		}
		if (isNetworkServiceEnabled && isGPSServiceEnabled && networkLocation!=null && gpsLocation!=null)
		{
			if (gpsLocation.getAccuracy() < networkLocation.getAccuracy())
				currentLocation = gpsLocation;
			else
				currentLocation = networkLocation;
		}
		}
		catch(Exception e){
			currentLocation = null;
		}
		return currentLocation;
	}
	

	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}

