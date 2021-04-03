package com.elytevolution.go4lunch;

import com.elytevolution.go4lunch.api.RestaurantApi;
import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.fakeapi.ModelGenerator;
import com.elytevolution.go4lunch.fakeapi.RestaurantFakeApi;
import com.elytevolution.go4lunch.fakeapi.UserFakeApi;
import com.elytevolution.go4lunch.model.Restaurant;
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.presenter.ListPresenter;
import com.elytevolution.go4lunch.presenter.WorkmatePresenter;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitTest {

    WorkmatePresenter workmatePresenter;

    ListPresenter listPresenter;

    UserApi userApi = new UserFakeApi();

    RestaurantApi restaurantApi = new RestaurantFakeApi();

    List<User> users = new ArrayList<>();

    List<Restaurant> restaurants = new ArrayList<>();

    List<User> usersGenerator = ModelGenerator.USERS;

    List<Restaurant> restaurantsGenerator = ModelGenerator.RESTAURANTS;

    User currentUser;

    @Before
    public void setup() {
        workmatePresenter = new WorkmatePresenter(null, userApi);
        listPresenter = new ListPresenter(null, null, null, restaurantApi);

        userApi.getUserList(userList -> users = userList);
        restaurantApi.getRestaurantList(restaurantList -> restaurants = restaurantList);

        workmatePresenter.setUserList(users);
        listPresenter.setListRestaurant(restaurants);

        currentUser = users.get(0);
    }

    @Test
    public void getUserListWithSuccess() {
        assertTrue(users.containsAll(usersGenerator));
    }

    @Test
    public void getUIdWithSuccess() {
        String userId1 = workmatePresenter.getUserId(0);
        String userId2 = usersGenerator.get(0).getUid();

        assertEquals(userId1, userId2);
    }

    @Test
    public void getUserNameWithSuccess() {
        String username1 = workmatePresenter.getUserName(0);
        String username2 = usersGenerator.get(0).getDisplayName();
        assertEquals(username1, username2);
    }

    @Test
    public void getIdPlaceWithSuccess() {
        String idPlace1 = workmatePresenter.getPlaceId(0);
        String idPlace2 = usersGenerator.get(0).getIdPlace();
        assertEquals(idPlace1, idPlace2);
    }

    @Test
    public void getUserListSizeWithSuccess() {
        int size1 = workmatePresenter.getUserListSize();
        int size2 = usersGenerator.size();

        assertEquals(size1, size2);
    }

    @Test
    public void getURLPictureWithSuccess() {
        String urlPicture1 = workmatePresenter.getUrlPicture(0);
        String urlPicture2 = usersGenerator.get(0).getUrlPicture();
        assertEquals(urlPicture1, urlPicture2);
    }

    @Test
    public void removeCurrentUserByIdWithSuccess() {
        List<String> uIds = new ArrayList<>();
        for(User user : users){
            uIds.add(user.getUid());
        }
        assertTrue(uIds.contains(currentUser.getUid()));
        uIds = userApi.removeCurrentUserWithId(uIds, currentUser);
        assertFalse(uIds.contains(currentUser.getUid()));
    }

    @Test
    public void getRestaurantListWithSuccess() {
        assertTrue(restaurants.containsAll(restaurantsGenerator));
    }

    @Test
    public void getRestaurantByIdWithSuccess() {
        Restaurant restaurant = restaurantApi.getRestaurantWithId(restaurantsGenerator.get(0).getIdPlace(), restaurantsGenerator);
        assertEquals(restaurant, restaurantsGenerator.get(0));
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