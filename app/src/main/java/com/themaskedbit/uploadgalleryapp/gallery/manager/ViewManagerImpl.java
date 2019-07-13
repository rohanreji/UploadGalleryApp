package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ViewManagerImpl implements ViewManager {
    private final User user;
    private MainActivityInterface view;
    private Scheduler backgroundScheduler;
    private Scheduler mainScheduler;
    private CompositeDisposable subscriptions;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference databaseReference;
    private UploadTask uploadTask;
    private ImageList imageList;
    private UploadTask downloadTask;

    //TODO: remove unwanted

    public ViewManagerImpl(final User user, Scheduler background, Scheduler main, ImageList imageList) {
        this.user = user;
        storage = FirebaseStorage.getInstance();
        this.backgroundScheduler = background;
        this.mainScheduler = main;
        subscriptions = new CompositeDisposable();
        storageRef =  storage.getReference().child("images");
        databaseReference = FirebaseDatabase.getInstance().getReference("images/0x1");
        this.imageList = imageList;
    }
    @Override
    public void onEditorSaved(@Nullable IdlingResourceApp idlingResource, final File file, Bitmap cropping) {
        subscriptions.clear();
        view.onUploadImageStarted();
        IdlingResourceApp.set(idlingResource, true);

        saveToFirebase(saveBitmap(file, cropping), idlingResource);
    }

    private File saveBitmap(final File file, final Bitmap bitmap) {
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return file;
    }

    private void saveToFirebase(final File file, final IdlingResourceApp idlingResource) {
        String name = file.getName();
        Uri uri = Uri.fromFile(file);
        storageRef =  storage.getReference().child("images/0x1/"+System.currentTimeMillis()+".jpg" );
        //Firebase will do the upload off the main thread.
        uploadTask = storageRef.putFile(uri);





        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                IdlingResourceApp.set(idlingResource, false);
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Image image = new Image(file.getName(),uri.toString());
                        view.onUploadImageCompleted(image);

                        // Getting image upload ID.
                        String ImageUploadId = databaseReference.push().getKey();

                        // Adding image upload id s child element into databaseReference.
                        databaseReference.child(ImageUploadId).setValue(image);
                    }
                });

            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.onUploadImageError(e);
                IdlingResourceApp.set(idlingResource, false);
            }
        });
    }

    @Override
    public void onEditorClosed() {
        if (view != null) {
            view.onEditorClosed();
        }
    }

    @Override
    public void setView(MainActivityInterface view) {
        this.view = view;
    }

    @Override
    public void start(@Nullable IdlingResourceApp idlingResource) {
        //add code to load images here
        loadImages(idlingResource);
    }

    @Override
    public void loadImages(final IdlingResourceApp idlingResource) {
        view.onFetchImagesStarted();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Image image = postSnapshot.getValue(Image.class);
                    imageList.setImages(image);
                }
                view.onFetchImageDone(imageList.getImages());
                IdlingResourceApp.set(idlingResource, false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
                view.onFetchImagesError(databaseError.toException());

            }
        });
    }

    @Override
    public void stop() {
        uploadTask.cancel();
    }
}
