package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.content.SharedPreferences;
import android.net.Uri;

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

public class SharedPreferenceHelperTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void idWithoutInitializingTest() {
        Mockito.when(sharedPreferences.getString(SharedPreferencesManagerImpl.ID_KEY, null)).thenReturn(null);
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManagerImpl(sharedPreferences);
        assertEquals(null, sharedPreferencesManager.getUserId());
    }

    @Test
    public void idAfterInitializingTest() {
        Mockito.when(sharedPreferences.getString(SharedPreferencesManagerImpl.ID_KEY, null)).thenReturn("0xtest");
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManagerImpl(sharedPreferences);
        assertEquals("0xtest", sharedPreferencesManager.getUserId());
    }

    @Test
    public void uriWithoutInitializingTest() {
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManagerImpl(sharedPreferences);
        assertEquals(null, sharedPreferencesManager.getImageUri());
    }

    @Test
    public void uriAfterInitializingTest() {
        Mockito.when(sharedPreferences.getString(SharedPreferencesManagerImpl.IMG_URI, "")).thenReturn("content://test");
        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManagerImpl(sharedPreferences);
        assertEquals(Uri.parse("content://test"), sharedPreferencesManager.getImageUri());
    }
}
