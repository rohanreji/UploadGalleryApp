package com.themaskedbit.uploadgalleryapp.gallery.presenter;

import android.content.SharedPreferences;

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
        Mockito.when(sharedPreferences.getString(SharedPreferencesHelperImpl.ID_KEY, null)).thenReturn(null);
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelperImpl(sharedPreferences);
        assertEquals(null,sharedPreferencesHelper.getUserId());
    }

    @Test
    public void idAfterInitializingTest() {
        Mockito.when(sharedPreferences.getString(SharedPreferencesHelperImpl.ID_KEY, null)).thenReturn("0xtest");
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelperImpl(sharedPreferences);
        assertEquals("0xtest",sharedPreferencesHelper.getUserId());
    }
}
