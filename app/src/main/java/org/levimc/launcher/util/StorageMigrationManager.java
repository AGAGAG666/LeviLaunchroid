package org.levimc.launcher.util;
import android.content.Context;
public class StorageMigrationManager {
    public StorageMigrationManager(Context context) {}
    public boolean shouldOfferMigration() { return false; }
    public boolean canReadLegacyRoot() { return false; }
}
