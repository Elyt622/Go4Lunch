package com.elytevolution.go4lunch;

import com.elytevolution.go4lunch.api.UserApi;
import com.elytevolution.go4lunch.fakeapi.ModelGenerator;
import com.elytevolution.go4lunch.fakeapi.UserFakeApi;
import com.elytevolution.go4lunch.model.User;
import com.elytevolution.go4lunch.presenter.WorkmatePresenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnitTest {

    WorkmatePresenter workmatePresenter;

    UserApi userApi = new UserFakeApi();

    List<User> users = new ArrayList<>();

    List<User> usersGenerator = ModelGenerator.USERS;

    User currentUser;

    @Before
    public void setup() {
        workmatePresenter = new WorkmatePresenter(null, userApi);
        userApi.getUserList(userList -> users = userList);
        currentUser = users.get(0);
    }

    @Test
    public void getUserListWithSuccess() {
        assertTrue(users.containsAll(usersGenerator));
    }

    @Test
    public void getUIdWithSuccess() {
        String userId1 = userApi.getUserId(0, users);
        String userId2 = usersGenerator.get(0).getUid();
        assertEquals(userId1, userId2);
    }

    @Test
    public void getUserNameWithSuccess() {
        String username1 = userApi.getUserName(0, users);
        String username2 = usersGenerator.get(0).getDisplayName();

        assertEquals(username1, username2);
    }

    @Test
    public void getIdPlaceWithSuccess() {
        String idPlace1 = userApi.getPlaceId(0, users);
        String idPlace2 = usersGenerator.get(0).getIdPlace();

        assertEquals(idPlace1, idPlace2);
    }

    @Test
    public void getUserListSizeWithSuccess() {
        int size1 = userApi.getUserListSize(users);
        int size2 = usersGenerator.size();

        assertEquals(size1, size2);
    }

    @Test
    public void getURLPictureWithSuccess() {
        String urlPicture1 = userApi.getUrlPicture(0, users);
        String urlPicture2 = usersGenerator.get(0).getUrlPicture();

        assertEquals(urlPicture1, urlPicture2);
    }

    @Test
    public void listIsEmptyWithSuccess() {
        assertFalse(users.isEmpty());
        userApi.clearListUser(users);
        assertTrue(users.isEmpty());
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
}