package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

public class FirebaseApi implements ApiHelper {
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ViewManager viewManager;
    private UploadTask uploadTask;
    private Task pushToDbTask;
    private ValueEventListener mListener;
    private FirebaseDatabase firebaseDatabase;
    private ImageList imageList;

    public FirebaseApi(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList){
        this.storageReference = storageReference;
        this.databaseReference = databaseReference;
        this.imageList = imageList;
        this.firebaseDatabase = firebaseDatabase;
    }

    @Override
    public void setPresenter(ViewManager manager) {
        this.viewManager = manager;
    }

    @Override
    public void uploadImages(Uri uri, final File file, final IdlingResourceApp idlingResource) {
        final StorageReference imageStorageRef =  storageReference.child(System.currentTimeMillis()+".jpg" );
        //Firebase will do the upload off the main thread.
        IdlingResourceApp.set(idlingResource, true);
        uploadTask = imageStorageRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Image image = new Image(file.getName(),uri.toString());
                        pushToFirebaseDb(image, idlingResource);
                    }
                });

            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                IdlingResourceApp.set(idlingResource, false);
                viewManager.uploadError(e);
            }
        });
    }

    @Override
    public void downloadImages(final IdlingResourceApp idlingResource) {
        IdlingResourceApp.set(idlingResource, true);
        mListener = databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                imageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Image image = postSnapshot.getValue(Image.class);
                    imageList.setImages(image);
                    viewManager.fetchDone(imageList.getImages());
                }
                viewManager.fetchComplete();
                IdlingResourceApp.set(idlingResource, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                viewManager.fetchError(databaseError.toException());
                IdlingResourceApp.set(idlingResource, false);
            }
        });

    }

    @Override
    public void cancelUpload() {
        if(uploadTask!=null && uploadTask.isInProgress())
            uploadTask.cancel();
        if(pushToDbTask!=null){
            firebaseDatabase.purgeOutstandingWrites();
        }
    }

    @Override
    public void cancelDownload() {
        if(mListener!=null) {
            databaseReference.removeEventListener(mListener);
        }
    }

    private void pushToFirebaseDb(final Image image, final IdlingResourceApp idlingResource){

        pushToDbTask =databaseReference.push().setValue(image);
        pushToDbTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                IdlingResourceApp.set(idlingResource, false);
                viewManager.uploadSuccess(image);
            }
        });
        pushToDbTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                IdlingResourceApp.set(idlingResource, false);
                viewManager.uploadError(e);
            }
        });
    }
}
