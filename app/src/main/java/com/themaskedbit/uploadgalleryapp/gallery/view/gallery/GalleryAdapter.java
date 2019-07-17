package com.themaskedbit.uploadgalleryapp.gallery.view.gallery;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.GalleryCellBinding;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;


/**
 * Adapter for the gallery of uploaded images.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ImageList imageList;
    private Picasso picasso;

    public GalleryAdapter(Picasso picasso, ImageList imageList) {
        super();
        this.imageList = imageList;
        this.picasso = picasso;
        picasso.setLoggingEnabled(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.
                        from(parent.getContext());
        GalleryCellBinding cellBinding =
                GalleryCellBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(cellBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Image image = imageList.getImages().get(position);
        picasso.load(image.getImageURL())
                .placeholder(R.drawable.ic_insert_photo_black_24dp)
                .resize(800, 800)
                .centerInside()
                .into(holder.cellBinding.imageCell);
    }

    @Override
    public int getItemCount() {
        return imageList.getImages().size();
    }

    /**
     * Stores and recycles views as they are scrolled off screen.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final GalleryCellBinding cellBinding;

        ViewHolder(GalleryCellBinding cellBinding) {
            super(cellBinding.getRoot());
            this.cellBinding = cellBinding;
        }
    }

}
