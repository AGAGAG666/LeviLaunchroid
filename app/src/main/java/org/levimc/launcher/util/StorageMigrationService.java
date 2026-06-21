package org.levimc.launcher.util;
import android.content.Context;
import android.content.Intent;
public class StorageMigrationService extends android.app.Service {
    public static boolean isMigrationRunning(Context c) { return false; }
    public static void startMigration(Context c) {}
    public static class MigrationState {
        public enum Status { IDLE, SCANNING, RUNNING, COMPLETED, PARTIAL, FAILED }
        public final Status status = Status.IDLE;
        public final int percent = 0;
        public final int processedFiles = 0;
        public final int totalFiles = 0;
        public final long processedBytes = 0;
        public final long totalBytes = 0;
        public final String currentFile = "";
        public final int skippedFiles = 0;
        public final int failedFiles = 0;
        public final String errorMessage = "";
        public final long estimatedRemainingMillis = -1;
        public final long estimatedCompletionAtMillis = -1;
        public boolean isActive() { return false; }
        public boolean isFinished() { return false; }
        static MigrationState idle() { return new MigrationState(); }
    }
    public interface MigrationListener { void onMigrationStateChanged(MigrationState state); }
    public class LocalBinder extends android.os.Binder {
        public StorageMigrationService getService() { return StorageMigrationService.this; }
    }
    public void addListener(MigrationListener l) {}
    public void removeListener(MigrationListener l) {}
    public MigrationState getCurrentState() { return MigrationState.idle(); }
    @Override public android.os.IBinder onBind(Intent intent) { return new LocalBinder(); }
}
