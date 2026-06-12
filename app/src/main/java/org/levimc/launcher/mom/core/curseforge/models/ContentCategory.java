package org.levimc.launcher.mom.core.curseforge.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ContentCategory implements Serializable {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("url")
    public String url;
}
