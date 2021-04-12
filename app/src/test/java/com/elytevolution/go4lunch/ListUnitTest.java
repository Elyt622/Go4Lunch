package com.elytevolution.go4lunch;

import com.elytevolution.go4lunch.api.Go4LunchApi;
import com.elytevolution.go4lunch.fakeapi.Go4LunchFakeApi;
import com.elytevolution.go4lunch.fakeapi.ModelGenerator;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.presenter.ListPresenter;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ListUnitTest {

    ListPresenter listPresenter;

    Go4LunchApi go4LunchApi = new Go4LunchFakeApi();

    List<Restaurant> restaurantsGenerator = ModelGenerator.RESTAURANTS;

    @Before
    public void setup() {
        listPresenter = new ListPresenter(null, null, null, go4LunchApi);
        listPresenter.getAllRestaurant();
    }

    @Test
    public void getRestaurantNameWithSuccess() {
        String name1 = listPresenter.getName(0);
        String name2 = restaurantsGenerator.get(0).getName();
        assertEquals(name1, name2);
    }

    @Test
    public void getRestaurantAddressWithSuccess() {
        String address1 = listPresenter.getAddress(0);
        String address2 = restaurantsGenerator.get(0).getAddress();
        assertEquals(address1, address2);
    }

    @Test
    public void getRestaurantListSizeWithSuccess() {
        int size1 = listPresenter.getSizeRestaurant();
        int size2 = restaurantsGenerator.size();
        assertEquals(size1, size2);
    }

    @Test
    public void getPhotoRefWithSuccess() {
        String photoRef1 = listPresenter.getPhotoReference(0);
        String photoRef2 = restaurantsGenerator.get(0).getPhotoRef();
        assertEquals(photoRef1, photoRef2);
    }

    @Test
    public void getOpenWithSuccess() {
        Boolean open1 = listPresenter.getOpenRestaurant(0);
        Boolean open2 = restaurantsGenerator.get(0).isCurrentOpen();
        assertEquals(open1, open2);
    }

    @Test
    public void getParticipantsWithSuccess() {
        int participants1 = listPresenter.getParticipants(0);
        int participants2 = restaurantsGenerator.get(0).getParticipation();
        assertEquals(participants1, participants2);
    }

    @Test
    public void getRatingWithSuccess() {
        Double rating1 = listPresenter.getRating(0);
        Double rating2 = restaurantsGenerator.get(0).getRating();
        assertEquals(rating1, rating2);
    }

    @Test
    public void getLocationWithSuccess() {
        LatLng location1 = listPresenter.getRestaurantLocation(0);
        LatLng location2 = new LatLng(restaurantsGenerator.get(0).getLat(), restaurantsGenerator.get(0).getLgt());
        assertEquals(location1, location2);
    }

    @Test
    public void getIdWithSuccess() {
        String idPlace1 = listPresenter.getRestaurantId(0);
        String idPlace2 = restaurantsGenerator.get(0).getIdPlace();
        assertEquals(idPlace1, idPlace2);
    }
}
