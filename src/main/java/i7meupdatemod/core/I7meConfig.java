package i7meupdatemod.core;

import com.google.gson.Gson;

import i7meupdatemod.entity.AssetMetaData;
import i7meupdatemod.entity.GameAssetDetail;
import i7meupdatemod.entity.GameMetaData;
import i7meupdatemod.entity.I7meMetaData;
import i7meupdatemod.util.Log;
import i7meupdatemod.util.Version;
import i7meupdatemod.util.VersionRange;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class I7meConfig {
    private static final String CFPA_ASSET_ROOT = "http://43.248.184.175:26009/generated.zip";
    private static final Gson GSON = new Gson();
    private static I7meMetaData i18nMetaData;

    static {
        init();
    }

    private static void init() {
        try (InputStream is = I7meConfig.class.getResourceAsStream("/i7meMetaData.json")) {
            if (is != null) {
                i18nMetaData = GSON.fromJson(new InputStreamReader(is), I7meMetaData.class);
            } else {
                Log.warning("Error getting index: is is null");
            }
        } catch (Exception e) {
            Log.warning("Error getting index: " + e);
        }
    }

    private static GameMetaData getGameMetaData(String minecraftVersion) {
        Version version = Version.from(minecraftVersion);
        return i18nMetaData.games.stream().filter(it -> {
            VersionRange range = new VersionRange(it.gameVersions);
            return range.contains(version);
        }).findFirst().orElseThrow(IllegalStateException::new);
    }

    private static AssetMetaData getAssetMetaData(String minecraftVersion, String loader) {
        List<AssetMetaData> current = i18nMetaData.assets.stream()
                .filter(it -> it.targetVersion.equals(minecraftVersion))
                .collect(Collectors.toList());
        return current.stream()
                .filter(it -> it.loader.equalsIgnoreCase(loader)).findFirst().orElseGet(() -> current.get(0));
    }

    public static GameAssetDetail getAssetDetail(String minecraftVersion, String loader) {
        GameMetaData convert = getGameMetaData(minecraftVersion);
        GameAssetDetail ret = new GameAssetDetail();

        ret.downloads = convert.convertFrom.stream().map(it->getAssetMetaData(it,loader)).map(it -> {
            GameAssetDetail.AssetDownloadDetail adi = new GameAssetDetail.AssetDownloadDetail();
            adi.fileName = it.filename;
            adi.fileUrl = CFPA_ASSET_ROOT;
            adi.targetVersion = it.targetVersion;
            return adi;
        }).collect(Collectors.toList());
        ret.covertPackFormat = convert.packFormat;
        ret.covertFileName =
                String.format("摸鱼人生材质包.zip", minecraftVersion);
        return ret;
    }
}
