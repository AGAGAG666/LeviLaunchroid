package org.levimc.launcher.mom.core.curseforge.models;

import com.google.gson.annotations.SerializedName;

public class Pagination {
    @SerializedName("index")
    public int index;
    
    @SerializedName("totalCount")
    public long totalCount;
}
