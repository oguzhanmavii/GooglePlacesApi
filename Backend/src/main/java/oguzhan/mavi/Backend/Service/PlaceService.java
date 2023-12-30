package oguzhan.mavi.Backend.Service;


import oguzhan.mavi.Backend.Model.PlaceResponse;
import oguzhan.mavi.Backend.Repository.PlaceResponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PlaceService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    @Autowired
    private PlaceResponseRepository placeResponseRepository;

    @Value("${google.places.api.key}")
    private String googlePlacesApiKey;
    @Async
    public CompletableFuture<String> getNearbyPlaces(Double latitude, Double longitude, Double radius) {
        try {
            Optional<PlaceResponse> existingResponse = placeResponseRepository.findByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);
            if (existingResponse.isPresent()) {
                return CompletableFuture.completedFuture(existingResponse.get().getPlacesApiResponse());
            }
            String placesApiResponse=callGooglePlacesAPI(latitude,longitude,radius);
            PlaceResponse newResponse=new PlaceResponse();
            newResponse.setLatitude(latitude);
            newResponse.setLongitude(longitude);
            newResponse.setRadius(radius);
            newResponse.setPlacesApiResponse(placesApiResponse);
            placeResponseRepository.save(newResponse);
            return CompletableFuture.completedFuture(placesApiResponse);
        }
        catch (HttpClientErrorException e){
            logger.error("HTTP error fetching nearby places", e);
            return CompletableFuture.completedFuture("HTTP error fetching nearby places. Please try again.");
        }
        catch (Exception e){
            logger.error("Error fetching nearby places", e);
            return CompletableFuture.completedFuture("Error fetching nearby places. Please try again.");
        }
    }
    private String callGooglePlacesAPI(Double latitude, Double longitude,Double radius){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String apiUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + latitude + "," + longitude + "&radius=" + radius + "&key=" + googlePlacesApiKey;
            return restTemplate.getForObject(apiUrl, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error fetching nearby places from Google Places API", e);
            return "HTTP error fetching nearby places. Please try again.";
        } catch (Exception e) {
            logger.error("Error fetching nearby places from Google Places API", e);
            return "Error fetching nearby places. Please try again.";
        }
    }
}
