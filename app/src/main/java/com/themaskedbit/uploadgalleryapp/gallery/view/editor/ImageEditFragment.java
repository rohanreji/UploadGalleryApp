package com.themaskedbit.uploadgalleryapp.gallery.view.editor;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.FragmentImageEditBinding;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper.getCacheFile;

/**
 * A simple {@link Fragment} subclass..
 */
public class ImageEditFragment extends Fragment implements ImageEditFragmentInterface  {

    FragmentImageEditBinding binding;

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @Inject
    Picasso picasso;

    private EditorListener listener;
    private IdlingResourceApp idlingResource;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_image_edit, container, false);
        binding.setEditor(this);
        binding.editorCropview.setCropMode(CropImageView.CropMode.FREE);
        IdlingResourceApp.set(idlingResource, true);
        binding.editorProgressbar.setVisibility(View.VISIBLE);
        Log.d("TAG",sharedPreferencesManager.getImageUri().toString());
        picasso.load(sharedPreferencesManager.getImageUri())
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .resize(800, 800)
                .centerInside()
                .into(binding.editorCropview, new Callback() {
                    @Override
                    public void onSuccess() {
                        binding.editorProgressbar.setVisibility(View.GONE);
                        IdlingResourceApp.set(idlingResource, false);
                    }
                    @Override
                    public void onError() {
                        binding.editorProgressbar.setVisibility(View.GONE);
                        IdlingResourceApp.set(idlingResource, false);
                    }
                });
        View view = binding.getRoot();
        return view;
    }



    @Override
    public void onAttach(final Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setEditorListener(@Nullable IdlingResourceApp idlingResource, EditorListener listener) {
        this.listener = listener;
        this.idlingResource = idlingResource;
    }

    public void save() {
        IdlingResourceApp.set(idlingResource, true);
        binding.editorProgressbar.setVisibility(View.VISIBLE);
        manipulateControls(false);
        close();
        listener.onEditorSaved(idlingResource, getCacheFile(getContext()),binding.editorCropview.getCroppedBitmap());
    }

    public void close() {
        listener.onEditorClosed();
    }

    public void rotateLeft() {
        binding.editorCropview.rotateImage(CropImageView.RotateDegrees.ROTATE_90D, 1000);
    }

    public void rotateRight() {
        binding.editorCropview.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D, 1000);
    }

    private void manipulateControls(boolean bool) {
        binding.editorRotateRight.setEnabled(bool);
        binding.editorRotateRight.setClickable(bool);
        binding.editorRotateLeft.setEnabled(bool);
        binding.editorRotateLeft.setClickable(bool);
        binding.editorSave.setEnabled(bool);
        binding.editorSave.setClickable(bool);
        binding.editorClose.setEnabled(bool);
        binding.editorClose.setClickable(bool);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return idlingResource;
    }
}
