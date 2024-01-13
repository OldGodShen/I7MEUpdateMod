package i7meupdatemod.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

import i7meupdatemod.util.AssetUtil;
import i7meupdatemod.util.FileUtil;
import i7meupdatemod.util.Log;

public class I7ResourcePack {
    /**
     * Limit update check frequency
     */
    private final String filename;
    private final Path filePath;
    private final Path tmpFilePath;
    private final boolean saveToGame;

    public I7ResourcePack(String filename, boolean saveToGame) {
        //If target version is not current version, not save
        this.saveToGame = saveToGame;
        this.filename = filename;
        this.filePath = FileUtil.getResourcePackPath(filename);
        this.tmpFilePath = FileUtil.getTemporaryPath(filename);
        try {
            FileUtil.syncTmpFile(filePath, tmpFilePath, saveToGame);
        } catch (Exception e) {
            Log.warning(
                    String.format("Error while sync temp file %s <-> %s: %s", filePath, tmpFilePath, e));
        }
    }

    public void I7checkUpdate(String fileUrl) throws IOException, URISyntaxException, NoSuchAlgorithmException {
        I7downloadFull(fileUrl);
    }

    private void I7downloadFull(String fileUrl) throws IOException {
        try {
            Path downloadTmp = FileUtil.getTemporaryPath(filename + ".tmp");
            AssetUtil.download(fileUrl, downloadTmp);
            Files.move(downloadTmp, tmpFilePath, StandardCopyOption.REPLACE_EXISTING);
            Log.debug(String.format("Updates temp file: %s", tmpFilePath));
        } catch (Exception e) {
            Log.warning("Error while downloading: %s", e);
        }
        if (!Files.exists(tmpFilePath)) {
            throw new FileNotFoundException("Tmp file not found.");
        }
        FileUtil.syncTmpFile(filePath, tmpFilePath, saveToGame);
    }

    public Path I7getTmpFilePath() {
        return tmpFilePath;
    }

    public String I7getFilename() {
        return filename;
    }
}
