package i7meupdatemod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import i7meupdatemod.core.GameConfig;
import i7meupdatemod.core.I18nConfig;
import i7meupdatemod.core.ResourcePack;
import i7meupdatemod.core.ResourcePackConverter;
import i7meupdatemod.entity.GameAssetDetail;
import i7meupdatemod.util.FileUtil;
import i7meupdatemod.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class I18nUpdateMod {
    public static final String MOD_ID = "i7meupdatemod";
    public static String MOD_VERSION;

    public static final Gson GSON = new Gson();

    public static void init(Path minecraftPath, String minecraftVersion, String loader) {
        try (InputStream is = I18nConfig.class.getResourceAsStream("/i7meMetaData.json")) {
            MOD_VERSION = GSON.fromJson(new InputStreamReader(is), JsonObject.class).get("version").getAsString();
        } catch (Exception e) {
            Log.warning("Error getting version: " + e);
        }
        Log.info(String.format("I18nUpdate Mod %s is loaded in %s with %s", MOD_VERSION, minecraftVersion, loader));
        Log.debug(String.format("Minecraft path: %s", minecraftPath));
        String userHome = System.getProperty("user.home");
        if (userHome.equals("null")) {
            userHome = minecraftPath.toString();
        }
        Log.debug(String.format("User home: %s", userHome));

        try {
            Class.forName("com.netease.mc.mod.network.common.Library");
            Log.warning("T7MEUpdateMod will get resource pack from Internet, whose content is uncontrolled.");
            Log.warning("This behavior contraries to Netease Minecraft developer content review rule: " +
                    "forbidden the content in game not match the content for reviewing.");
            Log.warning("To follow this rule, T7MEUpdateMod won't download any thing.");
            Log.warning("T7MEUpdateMod会从互联网获取内容不可控的资源包。");
            Log.warning("这一行为违背了网易我的世界「开发者内容审核制度」：禁止上传与提审内容不一致的游戏内容。");
            Log.warning("为了遵循这一制度，T7MEUpdateMod不会下载任何内容。");
            return;
        } catch (ClassNotFoundException ignored) {
        }

        FileUtil.setResourcePackDirPath(minecraftPath.resolve("resourcepacks"));

        int minecraftMajorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);

        try {
            //Get asset
            GameAssetDetail assets = I18nConfig.getAssetDetail(minecraftVersion, loader);

            //Update resource pack
            List<ResourcePack> languagePacks = new ArrayList<>();
            boolean convertNotNeed = assets.downloads.size() == 1 && assets.downloads.get(0).targetVersion.equals(minecraftVersion);
            String applyFileName = assets.downloads.get(0).fileName;
            for (GameAssetDetail.AssetDownloadDetail it : assets.downloads) {
                FileUtil.setTemporaryDirPath(Paths.get(userHome, "." + MOD_ID, it.targetVersion));
                ResourcePack languagePack = new ResourcePack(it.fileName, convertNotNeed);
                languagePack.checkUpdate(it.fileUrl);
                languagePacks.add(languagePack);
            }

            //Convert resourcepack
            if (!convertNotNeed) {
                FileUtil.setTemporaryDirPath(Paths.get(userHome, "." + MOD_ID, minecraftVersion));
                applyFileName = assets.covertFileName;
                ResourcePackConverter converter = new ResourcePackConverter(languagePacks, applyFileName);
                converter.convert(assets.covertPackFormat, getResourcePackDescription(assets.downloads));
            }

            //Apply resource pack
            GameConfig config = new GameConfig(minecraftPath.resolve("options.txt"));
            config.addResourcePack("Minecraft-Mod-Language-Modpack",
                    (minecraftMajorVersion <= 12 ? "" : "file/") + applyFileName);
            config.writeToFile();
        } catch (Exception e) {
            Log.warning(String.format("Failed to update resource pack: %s", e));
//            e.printStackTrace();
        }
    }

    private static String getResourcePackDescription(List<GameAssetDetail.AssetDownloadDetail> downloads) {
        return downloads.size() > 1 ?
                String.format("该包由%s版本合并\n作者：CFPA团队及汉化项目贡献者",
                        downloads.stream().map(it -> it.targetVersion).collect(Collectors.joining("和"))) :
                String.format("该包对应的官方支持版本为%s\n作者：CFPA团队及汉化项目贡献者",
                        downloads.get(0).targetVersion);

    }
}