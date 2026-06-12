package org.levimc.launcher.mom.ui.dialogs.gameversionselect;

import org.levimc.launcher.mom.core.versions.GameVersion;

import java.util.ArrayList;
import java.util.List;

public class VersionGroup {
    public String versionCode;
    public List<GameVersion> versions = new ArrayList<>();

    public VersionGroup(String code) {
        this.versionCode = code;
    }
}