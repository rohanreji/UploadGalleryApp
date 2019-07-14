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
import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
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
    private MainActivityInterface view;
    private ApiHelper apiHelper;

    public ViewManagerImpl(ApiHelper apiHelper) {
         this.apiHelper = apiHelper;
        apiHelper.setPresenter(this);
    }

    @Override
    public void onEditorSaved(@Nullable IdlingResourceApp idlingResource, final File file, Bitmap cropping) {
        view.onUploadImageStarted();
        File imageFile = FileHelper.saveBitmap(file, cropping);
        final Uri uri = Uri.fromFile(imageFile);
        apiHelper.uploadImages(uri,imageFile,idlingResource, System.currentTimeMillis()+".jpg");
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
        apiHelper.downloadImages(idlingResource);
    }


    @Override
    public void uploadSuccess(Image image) {
        view.onUploadImageCompleted(image);
    }

    @Override
    public void uploadError(Exception e) {
        view.onUploadImageError(e);
    }

    @Override
    public void fetchDone(List<Image> imageList) {
        view.onFetchImageDone(imageList);
    }

    @Override
    public void fetchComplete() {
        view.onFetchImageOver();
    }

    @Override
    public void fetchError(Exception e) {
        view.onFetchImagesError(e);
    }

    @Override
    public void stop() {
        apiHelper.cancelDownload();
        apiHelper.cancelUpload();
    }
}
