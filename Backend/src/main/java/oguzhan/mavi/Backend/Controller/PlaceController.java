package oguzhan.mavi.Backend.Controller;

import oguzhan.mavi.Backend.Service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/places")
@CrossOrigin(origins = "http://localhost:3000")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @GetMapping("/nearby")
    public ResponseEntity<String> getNearbyPlaces(
            @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam Double radius
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<String> placesApiResponseFuture = placeService.getNearbyPlaces(latitude, longitude, radius);

        String placesApiResponse = placesApiResponseFuture.get();

        return ResponseEntity.ok(placesApiResponse);
    }
}

