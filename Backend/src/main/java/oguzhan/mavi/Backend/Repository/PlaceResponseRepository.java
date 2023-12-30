package oguzhan.mavi.Backend.Repository;

import oguzhan.mavi.Backend.Model.PlaceResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceResponseRepository extends JpaRepository<PlaceResponse,Long> {
    Optional<PlaceResponse>findByLatitudeAndLongitudeAndRadius(
            Double latitude,Double longitude,Double radius
    );
}
