package com.themaskedbit.uploadgalleryapp.gallery.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.themaskedbit.uploadgalleryapp.BuildConfig;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.ActivityMainBinding;
import com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.editor.ImageEditFragment;
import com.themaskedbit.uploadgalleryapp.gallery.view.util.DialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingResource;

import java.io.File;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, MainActivityInterface {
    public static final int PERMISSION_REQUEST_CAMERA = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final String GALLERY = "ADD PIC";
    public static final String IMAGE_EDITOR = "EDITOR";

    private ActivityMainBinding dataBinding;
    private FloatingActionButton fab;
    private ProgressBar progressBar;
    private Context context;

    @Inject
    DialogBuilder dialogBuilder;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Nullable
    private IdlingResourceApp idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar(dataBinding.toolbar);
        fab = dataBinding.fab;
        progressBar = dataBinding.layoutGallery.progress;
        context = this;
        //click handler for fab button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(false);

                //for camera
                int okStringID = 0;
                DialogInterface.OnClickListener okButtonListener = null;
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    okStringID = R.string.dialog_camera;
                    okButtonListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openCamera();
                        }
                    };
                }

                //TODO: for gallery
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
            Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID,
                    FileHelper.getCacheFile(this));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (openCameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(openCameraIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                //TODO: snackbar not showing
                Snackbar.make(fab, R.string.error_open_camera, Snackbar.LENGTH_INDEFINITE).show();
            }
        }
    }

    private void inflateEditor() {
        showProgress(false);
        showFabButton(false);

        ImageEditFragment imageEditFragment = ImageEditFragment.newInstance();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.editor_fragment, imageEditFragment, IMAGE_EDITOR);
        transaction.addToBackStack(IMAGE_EDITOR);
        transaction.commit();
        //TODO: set editor listener of fragment here. Need to have an implementation of Listener.
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showFabButton(boolean show) {
        if(show)
            fab.show();
        else
            fab.hide();
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
                    Snackbar.make(fab, R.string.camera_perm_error, Snackbar.LENGTH_INDEFINITE).show();
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
                sharedPreferencesHelper.setImageUri(
                        Uri.fromFile(FileHelper.getCacheFile(getApplicationContext())));
                //add the image editor here
                inflateEditor();
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

    @Override
    public void onEditorClosed() {
        getSupportFragmentManager().popBackStack();
        showProgress(false);
        showFabButton(true);
    }
}
