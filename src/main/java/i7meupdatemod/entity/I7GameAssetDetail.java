package i7meupdatemod.entity;

import java.util.List;

public class I7GameAssetDetail {
    public List<AssetDownloadDetail> downloads;
    public Integer covertPackFormat;
    public String covertFileName;

    public static class AssetDownloadDetail {
        public String fileName;
        public String fileUrl;
        public String targetVersion;
    }
}