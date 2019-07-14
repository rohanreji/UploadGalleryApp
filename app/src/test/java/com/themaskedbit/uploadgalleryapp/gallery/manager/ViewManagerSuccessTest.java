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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ViewManagerSuccessTest {
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

    @Mock
    ImageList imageList;


    ViewManager viewManager;



    private String userId="0x1";
    private Image image = new Image("lorem","https://picsum.photos/id/237/200/300");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        when(this.user.getId()).thenReturn(userId);
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
        InOrder inOrder = Mockito.inOrder(apiHelper);
        inOrder.verify(apiHelper, times(1)).uploadImages(Uri.fromFile(file), file, idlingResource);
    }

}
