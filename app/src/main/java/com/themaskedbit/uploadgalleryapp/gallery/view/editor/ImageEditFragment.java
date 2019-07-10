package com.themaskedbit.uploadgalleryapp.gallery.view.editor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.IdlingResource;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.isseiaoki.simplecropview.CropImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.FragmentImageEditBinding;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass..
 * Use the {@link ImageEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageEditFragment extends Fragment implements ImageEditFragmentInterface  {

    FragmentImageEditBinding binding;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Inject
    Picasso picasso;

    private EditorListener listener;
    private IdlingResourceApp idlingResource;

    public ImageEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ImageEditFragment.
     */
    public static ImageEditFragment newInstance() {
        ImageEditFragment fragment = new ImageEditFragment();
        return fragment;
    }

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
        binding.editorCropview.setCropMode(CropImageView.CropMode.FREE);
        IdlingResourceApp.set(idlingResource, true);
        binding.editorProgressbar.setVisibility(View.VISIBLE);
        picasso.load(sharedPreferencesHelper.getImageUri())
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

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return idlingResource;
    }
}
