package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SharedPreferencesHelper sharedPreferencesHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void firstTimeUserSetupTest() {
        Mockito.when(sharedPreferencesHelper.getUserId()).thenReturn(null);
        User user1 = new User(sharedPreferencesHelper);
        User user2 = new User(sharedPreferencesHelper);
        assertEquals(user1.getId(),user2.getId());
    }

    @Test
    public void alreadyInitiatedUserSetupTest() {
        Mockito.when(sharedPreferencesHelper.getUserId()).thenReturn("0xtest");
        User user = new User(sharedPreferencesHelper);
        assertEquals(user.getId(),"0xtest");
    }

}
