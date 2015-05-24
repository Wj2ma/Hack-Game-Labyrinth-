package com.hackathon.framework.implementation;

import android.graphics.Bitmap;

import com.hackathon.framework.Image;
import com.hackathon.framework.Graphics.ImageFormat;

public class AndroidImage implements Image {
    Bitmap bitmap;
    ImageFormat format;
    
    public AndroidImage(Bitmap bitmap, ImageFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public ImageFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }   
    
    @Override
    public Image resize(int width, int height) {
    	return new AndroidImage(Bitmap.createScaledBitmap(bitmap,width,height,true), format);
    }
}
