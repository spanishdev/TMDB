package com.jordic.tmdbapp.pojo.configuration;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This represents the Result when the Configuration webservice is called
 */
public class ConfigurationResult {

    @SerializedName("images")
    @Expose
    private Image images;
    @SerializedName("change_keys")
    @Expose
    private List<String> changeKeys = null;

    public Image getImages() {
        return images;
    }

    public void setImages(Image images) {
        this.images = images;
    }

    public List<String> getChangeKeys() {
        return changeKeys;
    }

    public void setChangeKeys(List<String> changeKeys) {
        this.changeKeys = changeKeys;
    }

}