package PBO7;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

/** Menangani pembuatan laporan, backup, dan pencatatan log operasi. */
public class ReportBackupService {
    private final Path reportFile = Paths.get("PBO7", "report.txt");
    private final Path backupFile = Paths.get("PBO7", "backup", "report.txt");
    private final Path logFile = Paths.get("PBO7", "log.txt");

    public void createReport() throws IOException {
        Files.createDirectories(reportFile.getParent());
        Files.write(reportFile,
                java.util.Arrays.asList("Report Generated", "Status: OK"),
                StandardCharsets.UTF_8);
        System.out.println("Laporan dibuat: " + reportFile.toAbsolutePath());
    }

    public boolean createBackup() throws IOException {
        if (Files.notExists(reportFile)) {
            writeLog("Gagal menyalin: report.txt tidak ditemukan.");
            System.out.println("File sumber tidak ditemukan: " + reportFile.toAbsolutePath());
            return false;
        }

        Files.createDirectories(backupFile.getParent());
        Files.copy(reportFile, backupFile, StandardCopyOption.REPLACE_EXISTING);
        writeLog("Backup berhasil dibuat di " + backupFile);
        System.out.println("Backup berhasil dibuat: " + backupFile.toAbsolutePath());
        return true;
    }

    private void writeLog(String message) throws IOException {
        String entry = LocalDateTime.now() + " - " + message + System.lineSeparator();
        Files.write(logFile, entry.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public void run() {
        try {
            createReport();
            createBackup();
        } catch (IOException e) {
            System.err.println("Operasi file gagal: " + e.getMessage());
            try {
                writeLog("Operasi gagal: " + e.getMessage());
            } catch (IOException logError) {
                System.err.println("Log gagal ditulis: " + logError.getMessage());
            }
        }
    }
}
