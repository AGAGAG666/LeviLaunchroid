package org.levimc.launcher.mom.core.curseforge.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ContentSearchResponse {
    @SerializedName("data")
    public List<Content> data;

    @SerializedName("pagination")
    public Pagination pagination;
}
