package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.api.FirebaseApi;
import com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ViewManagerTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Mock
    ApiHelper apiHelper;

    @Mock
    User user;

    @Mock
    MainActivityInterface view;

    @Mock
    Context context;

    @Mock
    IdlingResourceApp idlingResource;



    ViewManager viewManager;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        viewManager = new ViewManagerImpl(apiHelper);
        viewManager.setView(view);
    }

    @Test
    public void testEditorSave() {
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        File file;
        try {
            file = folder.newFile();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }
        viewManager.onEditorSaved(idlingResource,file, bitmap);
        verify(apiHelper).uploadImages(eq(Uri.fromFile(file)), eq(file), eq(idlingResource),anyString());
    }



}
