package i7meupdatemod.entity;

import java.util.List;

public class GameAssetDetail {
    public List<AssetDownloadDetail> downloads;

    public static class AssetDownloadDetail {
        public String fileName;
        public String fileUrl;
        public String targetVersion;
    }
}
