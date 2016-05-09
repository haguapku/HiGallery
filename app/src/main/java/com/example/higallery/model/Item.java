package com.example.higallery.model;

/**
 * Created by MarkYoung on 16/5/9.
 */
public class Item {

    private String imageHref;

    public Item(String imageHref) {
        this.imageHref = imageHref;
    }

    public String getImageHref() {
        return imageHref;
    }

    @Override
    public String toString() {
        return "Item{" +
                "imageHref='" + imageHref + '\'' +
                '}';
    }
}
