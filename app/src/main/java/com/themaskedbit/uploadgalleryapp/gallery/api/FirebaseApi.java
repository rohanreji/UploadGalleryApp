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
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
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
    private User user;

    public FirebaseApi(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList, User user) {
        this.storageReference = storageReference;
        this.databaseReference = databaseReference;
        this.imageList = imageList;
        this.firebaseDatabase = firebaseDatabase;
        this.user = user;
    }

    @Override
    public void setPresenter(ViewManager manager) {
        this.viewManager = manager;
    }

    @Override
    public void uploadImages(Uri uri, final File file, final IdlingResourceApp idlingResource, String name) {
        final StorageReference imageStorageRef = storageReference.child(user.getId()).child(name);
        //Firebase will do the upload off the main thread.
        IdlingResourceApp.set(idlingResource, false);
        uploadTask = imageStorageRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> imageUri = imageStorageRef.getDownloadUrl();
                imageUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Image image = new Image(file.getName(), uri.toString());
                        pushToFirebaseDb(image, idlingResource, user);
                    }
                });

            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                IdlingResourceApp.set(idlingResource, true);
                viewManager.uploadError(e);
            }
        });
    }

    @Override
    public void downloadImages(final IdlingResourceApp idlingResource) {
        IdlingResourceApp.set(idlingResource, false);

        mListener = databaseReference.child(user.getId()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                imageList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Image image = postSnapshot.getValue(Image.class);
                    imageList.setImages(image);

                }
                viewManager.fetchDone(imageList.getImages());
                //viewManager.fetchComplete();
                IdlingResourceApp.set(idlingResource, true);
                databaseReference.child(user.getId()).removeEventListener(mListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                viewManager.fetchError(databaseError.toException());
                IdlingResourceApp.set(idlingResource, true);
                databaseReference.child(user.getId()).removeEventListener(mListener);
            }
        });

    }

    @Override
    public void cancelUpload() {
        if (uploadTask != null && uploadTask.isInProgress())
            uploadTask.cancel();
        if (pushToDbTask != null) {
            firebaseDatabase.purgeOutstandingWrites();
        }
    }

    @Override
    public void cancelDownload() {
        if (mListener != null) {
            databaseReference.child(user.getId()).removeEventListener(mListener);
        }
    }

    /**
     * Push the image uri from storage bucket to firebase realtime db
     *
     * @param image          Image object prefilled with name and uri
     * @param idlingResource idling resource for espresso
     * @param user           user id, object under which data has to be pushed.
     */
    private void pushToFirebaseDb(final Image image, final IdlingResourceApp idlingResource, User user) {
        DatabaseReference pushReference = databaseReference.child(user.getId()).push();
        pushToDbTask = pushReference.setValue(image);
        pushToDbTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                viewManager.uploadSuccess(image);
                imageList.setImages(image);
                IdlingResourceApp.set(idlingResource, true);
            }
        });
        pushToDbTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewManager.uploadError(e);
                imageList.setImages(image);
                IdlingResourceApp.set(idlingResource, true);
            }
        });
    }

}