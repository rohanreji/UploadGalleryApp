package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class FirebaseApiSuccessTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    ViewManager viewManager;
    @Mock
    IdlingResourceApp idlingResource;
    @Mock
    ImageList imageList;
    @Mock
    StorageReference storageReference;
    @Mock
    DatabaseReference databaseReference;
    @Mock
    DatabaseReference pushReference;
    @Mock
    FirebaseDatabase firebaseDatabase;
    @Mock
    UploadTask uploadTask;
    @Mock
    UploadTask.TaskSnapshot taskSnapshot;
    @Mock
    StorageReference mockImageStorageReference;
    @Mock
    Task<Uri> mockTask;
    @Mock
    Task mockPush;
    @Mock
    Uri uri;
    @Mock
    Object object;

    Image image;
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    FirebaseApi firebaseApi;
    ArgumentCaptor<Image> argument;

    @Mock
    DataSnapshot dataSnapshot;
    @Mock
    User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        argument = ArgumentCaptor.forClass(Image.class);
        firebaseApi = new FirebaseApi(storageReference, databaseReference, firebaseDatabase, imageList, user);
        firebaseApi.setPresenter(viewManager);
    }

    @Test
    public void uploadImagesTest() {
        File file;
        try {
            file = folder.newFile();
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        }

        when(user.getId()).thenReturn("user1");
        when(storageReference.child(anyString())).thenReturn(mockImageStorageReference);
        when(mockImageStorageReference.child(anyString())).thenReturn(mockImageStorageReference);
        when(mockImageStorageReference.putFile(uri)).thenReturn(uploadTask);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArguments()[0]).onSuccess(taskSnapshot);
                return null;
            }
        }).when(uploadTask).addOnSuccessListener(any(OnSuccessListener.class));

        when(mockImageStorageReference.getDownloadUrl()).thenReturn(mockTask);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArguments()[0]).onSuccess(uri);
                return null;
            }
        }).when(mockTask).addOnSuccessListener(any(OnSuccessListener.class));
        image = new Image(file.getName(), uri.toString());

        when(databaseReference.child(anyString())).thenReturn(databaseReference);
        when(databaseReference.push()).thenReturn(pushReference);
        when(pushReference.setValue(any(Image.class))).thenReturn(mockPush);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((OnSuccessListener) invocation.getArguments()[0]).onSuccess(object);
                return null;
            }
        }).when(mockPush).addOnSuccessListener(any(OnSuccessListener.class));

        firebaseApi.uploadImages(uri, file, idlingResource, "test_image");

        verify(viewManager).uploadSuccess(argument.capture());
        assertEquals(image.getImageName(), argument.getValue().getImageName());
    }

    @Test
    public void downloadImagesTest() {
        when(user.getId()).thenReturn("user1");
        when(databaseReference.child(anyString())).thenReturn(databaseReference);
        when(dataSnapshot.getChildren()).thenReturn(Arrays.asList(dataSnapshot));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ValueEventListener) invocation.getArguments()[0]).onDataChange(dataSnapshot);
                return null;
            }
        }).when(databaseReference).addValueEventListener(any(ValueEventListener.class));
        firebaseApi.downloadImages(idlingResource);
        verify(viewManager).fetchDone(imageList.getImages());
    }



}
