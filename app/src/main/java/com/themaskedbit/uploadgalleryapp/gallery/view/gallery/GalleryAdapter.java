package com.themaskedbit.uploadgalleryapp.gallery.view.gallery;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.GalleryCellBinding;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;

import java.util.ArrayList;
import java.util.List;


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
        //TODO: can we reuse ImageList
//        images = new SortedList<>(Image.class,
//                new SortedList.Callback<Image>() {
//                    @Override
//                    public int compare(final Image o1, final Image o2) {
//                        return o1.compareTo(o2);
//                    }
//
//                    @Override
//                    public void onChanged(final int position, final int count) {
//                        notifyItemRangeChanged(position, count);
//                    }
//                    @Override
//                    public boolean areContentsTheSame(final Image oldItem,
//                                                      final Image newItem) {
//                        return oldItem.getUrl().equals(newItem.getUrl());
//                    }
//                    @Override
//                    public boolean areItemsTheSame(final Image item1,
//                                                   final Image item2) {
//                        return item1.getCreatedAt().equals(item2.getCreatedAt());
//                    }
//                    @Override
//                    public void onInserted(final int position, final int count) {
//                        notifyItemRangeInserted(position, count);
//                    }
//                    @Override
//                    public void onRemoved(final int position, final int count) {
//                        notifyItemRangeRemoved(position, count);
//                    }
//                    @Override
//                    public void onMoved(final int fromPosition, final int toPosition) {
//                        notifyItemMoved(fromPosition, toPosition);
//                    }
//                });
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
