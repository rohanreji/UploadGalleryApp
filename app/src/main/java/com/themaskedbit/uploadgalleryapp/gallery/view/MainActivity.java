package com.themaskedbit.uploadgalleryapp.gallery.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.ActivityMainBinding;
import com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.editor.ImageEditFragment;
import com.themaskedbit.uploadgalleryapp.gallery.view.gallery.Gallery;
import com.themaskedbit.uploadgalleryapp.gallery.view.gallery.GalleryAdapter;
import com.themaskedbit.uploadgalleryapp.gallery.view.util.DialogBuilder;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper.deleteCacheFile;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, MainActivityInterface {
    public static final int PERMISSION_REQUEST_CAMERA = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int PERMISSION_REQUEST_GALLERY = 102;
    public static final int REQUEST_IMAGE_GALLERY = 103;
    public static final String GALLERY = "ADD PIC";
    public static final String IMAGE_EDITOR = "EDITOR";
    public static final String UPLOAD_ERROR = "upload";
    public static final String FETCH_ERROR = "fetch";

    private ActivityMainBinding dataBinding;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private Context context;

    @Inject
    DialogBuilder dialogBuilder;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    ViewManager viewManager;

    @Inject
    GalleryAdapter galleryAdapter;


    @Nullable
    private IdlingResourceApp idlingResource;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(dataBinding.toolbar);
        fragmentManager = getSupportFragmentManager();
        fab = dataBinding.fab;
        progressBar = dataBinding.layoutGallery.progress;
        context = this;
        viewManager.setView(this);

        showRefreshButton(false);
        showStub(false);
        showFabButton(true);
        showProgress(true);

        if (isNetworkAvailable(this)) {
            viewManager.start(idlingResource);
        } else {
            showRefreshButton(true);
            showStub(true);
            showProgress(false);
        }


        //click handler for refresh button
        dataBinding.layoutGallery.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
        //click handler for fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //for camera
                int okStringID = 0;
                DialogInterface.OnClickListener okButtonListener = null;
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    okStringID = R.string.dialog_camera;
                    okButtonListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openCamera();
                        }
                    };
                }

                //for gallery
                int cancelStringId = R.string.dialog_gallery;
                DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openGallery();
                    }
                };
                dialogBuilder.showDialog(GALLERY, context, R.string.dialog_upload_title, R.string.dialog_upload_message,
                        okStringID, okButtonListener, cancelStringId, cancelButtonListener);

            }
        });

    }


    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                showProgress(false);
                showStub(false);
                showRefreshButton(false);
                startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
            } else {
                Snackbar.make(fab, R.string.error_open_gallery, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            openCameraIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(this, getApplication().getPackageName(),
                    FileHelper.getCacheFile(this));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
                showProgress(false);
                showStub(false);
                showRefreshButton(false);
                startActivityForResult(openCameraIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Snackbar.make(fab, R.string.error_open_camera, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void inflateEditor() {
        showProgress(false);
        showStub(false);
        showRefreshButton(false);
        showFabButton(false);

        ImageEditFragment imageEditFragment = new ImageEditFragment();
        FragmentTransaction transaction =
                fragmentManager.beginTransaction();
        transaction.add(R.id.editor_fragment, imageEditFragment, IMAGE_EDITOR);
        transaction.addToBackStack(IMAGE_EDITOR);
        transaction.commit();
        imageEditFragment.setEditorListener(idlingResource, viewManager);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Snackbar.make(fab, R.string.camera_perm_error, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case PERMISSION_REQUEST_GALLERY: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    Snackbar.make(fab, R.string.camera_perm_error, Snackbar.LENGTH_LONG).show();
                }
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File file = FileHelper.getCacheFile(getApplicationContext());
                sharedPreferencesManager.setImageUri(FileProvider.getUriForFile(this, getApplication().getPackageName(), file));
                inflateEditor();
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                final Uri imageUri = data.getData();
                sharedPreferencesManager.setImageUri(imageUri);
                inflateEditor();
            }
        } else {
            showFabButton(true);
            if (findGallery() == null) {
                showProgress(false);
                showStub(true);
                showRefreshButton(false);
            }
        }
    }


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new IdlingResourceApp();
        }
        return idlingResource;
    }


    private Gallery showGallery() {
        final Fragment fragmentByTag = findGallery();
        Gallery fragment;
        if (fragmentByTag == null) {
            fragment = new Gallery();
            FragmentTransaction transaction =
                    fragmentManager.beginTransaction();
            transaction.add(R.id.images_fragment, fragment, GALLERY);
            transaction.commit();
            fragment.setAdapter(galleryAdapter);

        } else {
            fragment = (Gallery) fragmentByTag;
        }
        return fragment;
    }

    private Fragment findGallery() {
        return fragmentManager.findFragmentByTag(GALLERY);
    }

    // Fetch image callbacks
    @Override
    public void onFetchImagesStarted() {
        showProgress(true);
    }

    @Override
    public void onFetchImageDone(List<Image> imageList) {
        showProgress(false);
        showFabButton(true);
        if (imageList.isEmpty()) {
            showStub(true);
        } else {
            showGallery();
            galleryAdapter.notifyDataSetChanged();
        }
    }

    //not used now. But may be needed.
    @Override
    public void onFetchImageOver() {
        showProgress(false);
        showFabButton(true);
    }

    @Override
    public void onFetchImagesError(Exception e) {
        showProgress(false);
        showFabButton(false);
        showStub(true);
        showRefreshButton(true);
        showError(FETCH_ERROR);
    }


    // Upload image callbacks
    @Override
    public void onUploadImageStarted() {
        showProgress(true);
        showFabButton(false);
    }


    @Override
    public void onUploadImageCompleted(Image image) {
        onEditorClosed();
        deleteCacheFile(this);
        showProgress(false);
        showFabButton(true);
        showStub(false);
        showRefreshButton(false);
        galleryAdapter.notifyDataSetChanged();
        showGallery();
    }


    @Override
    public void onUploadImageError(Exception e) {
        onEditorClosed();
        deleteCacheFile(this);
        showProgress(false);
        showFabButton(true);
        showStub(false);
        showRefreshButton(false);
        showError(UPLOAD_ERROR);
        showGallery();
    }


    @Override
    public void onEditorClosed() {
        fragmentManager.popBackStack();
        showProgress(false);
        showFabButton(true);
        showRefreshButton(false);
        if (findGallery() != null) {
            showStub(false);
        } else {
            showStub(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showFabButton(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewManager.setView(null);
        viewManager.stop();
    }

    private void showError(String message) {
        switch (message) {
            case FETCH_ERROR:
                dialogBuilder.showDialog(IMAGE_EDITOR, this, R.string.dialog_connect_error,
                        R.string.dialog_fetch_error_message, android.R.string.ok, null, 0, null);
                break;
            case UPLOAD_ERROR:
                dialogBuilder.showDialog(IMAGE_EDITOR, this, R.string.dialog_connect_error,
                        R.string.dialog_upload_error_message, android.R.string.ok, null, 0, null);
                break;

            default:
                dialogBuilder.showDialog(IMAGE_EDITOR, this, R.string.dialog_connect_error,
                        R.string.dialog_connect_error_message, android.R.string.ok, null, 0, null);
        }

    }

    /*
     * Helper method to get internet status
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void refresh() {
        if (isNetworkAvailable(this)) {
            showProgress(true);
            showStub(false);
            showRefreshButton(false);
            showFabButton(false);
            viewManager.start(idlingResource);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showRefreshButton(boolean show) {
        dataBinding.layoutGallery.refreshButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showFabButton(boolean show) {
        if (show)
            fab.show();
        else
            fab.hide();
    }

    private void showStub(boolean show) {
        dataBinding.layoutGallery.tvStub.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}