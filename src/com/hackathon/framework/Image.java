package com.hackathon.framework;

import com.hackathon.framework.Graphics.ImageFormat;

public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
    public Image resize(int width, int height);
}
