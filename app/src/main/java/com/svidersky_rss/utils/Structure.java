package com.svidersky_rss.utils;

/**
 * Created by Eren on 24.11.2014.
 */
public class Structure {

    private String title;
    private String picture;
    private String video;
    private String uploaded;
    private String description;

    public Structure(String title, String picture, String video, String uploaded, String description) {
        this.title = title;
        this.picture = picture;
        this.video = video;
        this.uploaded = uploaded;
        this.description = description;
    }


    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getVideo() {
        return video;
    }

    public String getUploaded() {
        return uploaded;
    }

    public String getDescription() {
        return description;
    }
}
