package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class UserTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SharedPreferencesManager sharedPreferencesManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void firstTimeUserSetupTest() {
        Mockito.when(sharedPreferencesManager.getUserId()).thenReturn(null);
        User user1 = new User(sharedPreferencesManager);
        User user2 = new User(sharedPreferencesManager);
        assertEquals(user1.getId(), user2.getId());
    }

    @Test
    public void alreadyInitiatedUserSetupTest() {
        Mockito.when(sharedPreferencesManager.getUserId()).thenReturn("0xtest");
        User user = new User(sharedPreferencesManager);
        assertEquals(user.getId(), "0xtest");
    }

}
