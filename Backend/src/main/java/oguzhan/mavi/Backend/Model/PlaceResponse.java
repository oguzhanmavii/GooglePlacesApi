package oguzhan.mavi.Backend.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PlaceResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Model Fields
    private  Long id;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private String placesApiResponse;

    //Constructor Method Injection
    public PlaceResponse()
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.placesApiResponse = placesApiResponse;
    }

    //Get Set islemleri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getPlacesApiResponse() {
        return placesApiResponse;
    }

    public void setPlacesApiResponse(String placesApiResponse) {
        this.placesApiResponse = placesApiResponse;
    }
}
