package com.example.ui.base;

import android.content.res.Resources;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.R;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.webservice.APIs.BASE_IMAGE_PATH;

/**
 * Created by viraj.patel on 05-Sep-18
 */
public class BaseBinder {

    @BindingAdapter({"app:setLanguageIcon"})
    public static void setFlagIcon(AppCompatImageView iv, String image) {

        if (image != null && image.length() > 0 && !image.equals("null")) {
            Resources resources = iv.getContext().getResources();
            final int resourceId = resources.getIdentifier(image, "drawable",
                    iv.getContext().getPackageName());
            iv.setImageResource(resourceId);
        }/* else {
            iv.setImageResource(R.drawable.ic_vector_flag_soomaaliya);
        }*/
    }

    @BindingAdapter({"app:setSimpleImage"})
    public static void setSimpleImage(AppCompatImageView iv, String image) {

        if (image != null && image.length() > 0 && !image.equals("null"))
            Glide.with(iv.getContext())
                    .load(BASE_IMAGE_PATH + image)
                    .apply(new RequestOptions()
                            .placeholder(R.color.transparent)
                            .error(R.color.grey)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .centerCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
       /* else
            iv.setImageResource(R.drawable.ic_vector_onkout);*/
    }

    @BindingAdapter({"app:setImageUri", "app:setImageUrl"})
    public static void setImage(AppCompatImageView iv, Uri imageUri, String Url) {

        if (Url == null || Url.length() == 0) {
            setImageUri(iv, imageUri);
        } else {
            setImageUrl(iv, Url);
        }
    }

    @BindingAdapter({"app:setImageUri"})
    public static void setImageUri(AppCompatImageView iv, Uri imageUri) {

        if (imageUri != null)
            Glide.with(iv.getContext())
                    .load(imageUri.getPath())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
        else
            iv.setImageResource(R.drawable.shape_circle_grey);
    }

    @BindingAdapter({"app:setImageUrl"})
    public static void setImageUrl(AppCompatImageView iv, String imageUrl) {

        if (imageUrl != null && imageUrl.length() > 0 && !imageUrl.equals("null"))
            Glide.with(iv.getContext())
                    .load(BASE_IMAGE_PATH + imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
        else
            iv.setImageResource(R.drawable.shape_circle_grey);
    }

    @BindingAdapter({"app:setImageUrl", "app:isSetOnkoutLogo"})
    public static void setImageUrl(AppCompatImageView iv, String imageUrl, boolean isSetOnkoutLogo) {

        if (imageUrl != null && imageUrl.length() > 0 && !imageUrl.equals("null"))
            Glide.with(iv.getContext())
                    .load(BASE_IMAGE_PATH + imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.shape_circle_grey)
                            .error(R.drawable.shape_circle_grey)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .circleCrop())
                    .transition(withCrossFade(300))
                    .into(iv);
      /*  else
          iv.setImageResource(isSetOnkoutLogo ? R.drawable.ic_vector_onkout : R.drawable.shape_circle_grey);*/
    }

    @BindingAdapter({"app:setFlaggedImage"})
    public static void setFlaggedImage(AppCompatImageView iv, boolean isFlagged) {
     //   iv.setImageResource(isFlagged ? R.drawable.ic_vector_flag_red : R.drawable.ic_vector_flag_gray);
    }
}
