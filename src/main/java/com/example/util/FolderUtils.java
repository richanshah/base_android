package com.example.util;

import android.os.Environment;

;

import com.example.R;

import java.io.File;

public class FolderUtils {

    private static File getExternalDir() {
        File dir = new File(Environment.getExternalStorageDirectory(),
                Cisner.getInstance().getResources().getString(R.string.app_name));
        if (!dir.exists())
            dir.mkdirs();
        return dir;
    }

    private static File getImageFolder() {
        File imageDir = new File(getExternalDir(), "Images");
        if (!imageDir.exists())
            imageDir.mkdirs();
        return imageDir;
    }

    private static File getThumbFolder() {
        File thumbnail = new File(getExternalDir(), ".Thumbnail");
        if (!thumbnail.exists())
            thumbnail.mkdirs();
        return thumbnail;
    }

    public static File getImageFile() {
        return new File(getImageFolder(), "img_" + System.currentTimeMillis() +
                ".jpg");
    }

    public static File getThumbnailFile() {
        return new File(getThumbFolder(), "thumnail_" + System.currentTimeMillis() +
                ".jpg");
    }
}
