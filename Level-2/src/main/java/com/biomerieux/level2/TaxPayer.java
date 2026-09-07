/*
 * You know how to play by now, good luck!
 */
package com.biomerieux.level2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TaxPayer {

    private final String username;
    private final String password;
    private final Path baseDir;

    public TaxPayer(String username, String password) {
        this.username = username;
        this.password = password;
        this.baseDir = resolveAssetsBaseDir();
    }

    private static Path resolveAssetsBaseDir() {
        try {
            URL url = TaxPayer.class.getClassLoader().getResource("assets");
            return Paths.get(url.toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new IllegalStateException("assets folder not found on classpath", e);
        }
    }

    // returns the path of an optional profile picture that users can set
    public String getProfPicture(String path) throws IOException {
        if (path == null) {
            return null;
        }

        // FIXED: resolve + verify the result stays inside baseDir (allow-list, not blocklist)
        Path profPicturePath = baseDir.resolve(path).normalize();
        if (!profPicturePath.startsWith(baseDir)) {
            return null;
        }

        byte[] picture = Files.readAllBytes(profPicturePath);
        return profPicturePath.toString();
    }

    // returns the path of an attached tax form that every user should submit
    public String getTaxFormAttachment(String path) throws IOException {
        if (path == null) {
            throw new IllegalStateException("Error: Tax form is required for all users");
        }

        // FIXED: same allow-list check as above (this method previously had none at all)
        Path taxFormPath = baseDir.resolve(path).normalize();
        if (!taxFormPath.startsWith(baseDir)) {
            return null;
        }

        byte[] taxData = Files.readAllBytes(taxFormPath);
        return taxFormPath.toString();
    }

    public Path getBaseDir() {
        return baseDir;
    }

    // Exposed for tests only: the "secret" file living just outside the public assets folder.
    public static Path resolveSecretFileOutsideBaseDir() {
        try {
            URL url = TaxPayer.class.getClassLoader().getResource("secret.txt");
            return Paths.get(url.toURI());
        } catch (URISyntaxException | NullPointerException e) {
            throw new IllegalStateException("secret.txt not found on classpath", e);
        }
    }
}
