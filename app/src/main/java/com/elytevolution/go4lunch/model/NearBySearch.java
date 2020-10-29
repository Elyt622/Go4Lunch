package com.elytevolution.go4lunch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearBySearch {

    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("results")
    private List<Results> results;
    @Expose
    @SerializedName("html_attributions")
    private List<String> html_attributions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(List<String> html_attributions) {
        this.html_attributions = html_attributions;
    }

    public static class Results {
        @Expose
        @SerializedName("vicinity")
        private String vicinity;
        @Expose
        @SerializedName("types")
        private List<String> types;
        @Expose
        @SerializedName("scope")
        private String scope;
        @Expose
        @SerializedName("reference")
        private String reference;
        @Expose
        @SerializedName("rating")
        private double rating;
        @Expose
        @SerializedName("plus_code")
        private Plus_code plus_code;
        @Expose
        @SerializedName("place_id")
        private String place_id;
        @Expose
        @SerializedName("photos")
        private List<Photos> photos;
        @Expose
        @SerializedName("opening_hours")
        private Opening_hours opening_hours;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("id")
        private String id;
        @Expose
        @SerializedName("icon")
        private String icon;
        @Expose
        @SerializedName("geometry")
        private Geometry geometry;

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public Plus_code getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(Plus_code plus_code) {
            this.plus_code = plus_code;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<Photos> getPhotos() {
            return photos;
        }

        public void setPhotos(List<Photos> photos) {
            this.photos = photos;
        }

        public Opening_hours getOpening_hours() {
            return opening_hours;
        }

        public void setOpening_hours(Opening_hours opening_hours) {
            this.opening_hours = opening_hours;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
    }

    public static class Plus_code {
        @Expose
        @SerializedName("global_code")
        private String global_code;
        @Expose
        @SerializedName("compound_code")
        private String compound_code;

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }
    }

    public static class Photos {
        @Expose
        @SerializedName("width")
        private int width;
        @Expose
        @SerializedName("photo_reference")
        private String photo_reference;
        @Expose
        @SerializedName("html_attributions")
        private List<String> html_attributions;
        @Expose
        @SerializedName("height")
        private int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public List<String> getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(List<String> html_attributions) {
            this.html_attributions = html_attributions;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class Opening_hours {
        @Expose
        @SerializedName("open_now")
        private boolean open_now;

        public boolean getOpen_now() {
            return open_now;
        }

        public void setOpen_now(boolean open_now) {
            this.open_now = open_now;
        }
    }

    public static class Geometry {
        @Expose
        @SerializedName("viewport")
        private Viewport viewport;
        @Expose
        @SerializedName("location")
        private Location location;

        public Viewport getViewport() {
            return viewport;
        }

        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public static class Viewport {
        @Expose
        @SerializedName("southwest")
        private Southwest southwest;
        @Expose
        @SerializedName("northeast")
        private Northeast northeast;

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }
    }

    public static class Southwest {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Northeast {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class Location {
        @Expose
        @SerializedName("lng")
        private double lng;
        @Expose
        @SerializedName("lat")
        private double lat;

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }
}