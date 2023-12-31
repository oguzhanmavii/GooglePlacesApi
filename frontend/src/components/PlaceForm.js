import React, { useState } from 'react';
import axios from 'axios';
import { GoogleMap, LoadScript, Circle } from '@react-google-maps/api';

const Map = ({ latitude, longitude, radius }) => {
  const defaultCenter = { lat: parseFloat(latitude), lng: parseFloat(longitude) };

  return (
    <LoadScript googleMapsApiKey="AIzaSyBMOu8ZBxVLhMwAyQsrESFgswRpOtkcL1w">
      <GoogleMap
        mapContainerStyle={{ height: '400px', width: '100%', borderRadius: '8px' }}
        zoom={10}
        center={defaultCenter}
      >
        <Circle
          center={defaultCenter}
          radius={parseFloat(radius) * 1000} // Convert km to meters
          options={{ fillColor: '#FF0000', fillOpacity: 0.35, strokeWeight: 1, clickable: false }}
        />
      </GoogleMap>
    </LoadScript>
  );
};

const PlaceForm = () => {
  const [latitude, setLatitude] = useState('');
  const [longitude, setLongitude] = useState('');
  const [radius, setRadius] = useState('');
  const [error, setError] = useState(null);
  const [placesApiResponse, setPlacesApiResponse] = useState(null); // or { results: [] } if you have a specific structure
  const [showMap, setShowMap] = useState(false);

  const validateInput = () => {
    if (isNaN(latitude) || isNaN(longitude) || isNaN(radius)) {
      setError('Please enter valid numeric values for latitude, longitude, and radius.');
      return false;
    }

    if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
      setError('Latitude must be between -90 and 90, and longitude must be between -180 and 180.');
      return false;
    }

    return true;
  };

  const getNearbyPlaces = async () => {
    if (!validateInput()) {
      return;
    }

    try {
      const response = await axios.get('http://localhost:8070/api/places/nearby', {
        params: {
          latitude: parseFloat(latitude),
          longitude: parseFloat(longitude),
          radius: parseFloat(radius),
        },
      });

      if (response.data.status === 'ZERO_RESULTS') {
        setError('No results found. Please adjust your search parameters.');
        setShowMap(false);
      } else {
        setPlacesApiResponse(response.data);
        setError(null);
        setShowMap(true);
      }
    } catch (error) {
      // Handle error
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '100vh', backgroundColor: '#3498db' }}>
      <img src="https://ubilabs.com/media/pages/google-maps/api-sdk/56d66b39a8-1695915127/google-maps-icon-api-13.png" alt="Google Maps API" style={{ width: '150px', marginTop: '20px' }} />
      <h1 style={{ color: 'white' }}>Google Places API</h1>
      <div style={{ textAlign: 'center', width: '400px', padding: '20px', boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)', borderRadius: '8px', backgroundColor: 'white', marginTop: '20px' }}>
        <div style={{ marginBottom: '20px' }}>
          <label htmlFor="latitude">Latitude:</label>
          <input type="text" id="latitude" value={latitude} onChange={(e) => setLatitude(e.target.value)} />
        </div>

        <div style={{ marginBottom: '20px' }}>
          <label htmlFor="longitude">Longitude:</label>
          <input type="text" id="longitude" value={longitude} onChange={(e) => setLongitude(e.target.value)} />
        </div>

        <div style={{ marginBottom: '20px' }}>
          <label htmlFor="radius">Radius:</label>
          <input type="text" id="radius" value={radius} onChange={(e) => setRadius(e.target.value)} />
        </div>

        <button onClick={getNearbyPlaces}>Search</button>

        {showMap && placesApiResponse && (
          <div style={{ marginTop: '20px' }}>
            <h2>Places API Response</h2>
            {placesApiResponse.results && placesApiResponse.results.length === 0 && placesApiResponse.status === 'ZERO_RESULTS' ? (
              <p>Places API returned successfully</p>
            ) : (
              <pre>Places API returned successfully</pre>
            )}
          </div>
        )}

        {error && <div style={{ color: 'red', marginTop: '20px' }}>{error}</div>}

        {showMap && latitude && longitude && radius && (
          <Map latitude={latitude} longitude={longitude} radius={radius} />
        )}
      </div>
    </div>
  );
};

export default PlaceForm;
