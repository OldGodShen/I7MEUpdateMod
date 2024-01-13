package i7meupdatemod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import i7meupdatemod.core.GameConfig;
import i7meupdatemod.core.I7meConfig;
import i7meupdatemod.core.ResourcePack;
import i7meupdatemod.entity.GameAssetDetail;
import i7meupdatemod.util.FileUtil;
import i7meupdatemod.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class I7MEUpdateMod {
    public static final String MOD_ID = "i7meupdatemod";
    public static String MOD_VERSION;

    public static final Gson GSON = new Gson();

    public static void init(Path minecraftPath, String minecraftVersion, String loader) {
        try (InputStream is = I7meConfig.class.getResourceAsStream("/i7meMetaData.json")) {
            MOD_VERSION = GSON.fromJson(new InputStreamReader(is), JsonObject.class).get("version").getAsString();
        } catch (Exception e) {
            Log.warning("Error getting version: " + e);
        }
        Log.info(String.format("I7MEUpdateMod %s is loaded in %s with %s", MOD_VERSION, minecraftVersion, loader));
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

        try {
            // 获取资源
            GameAssetDetail I7assets = I7meConfig.getAssetDetail(minecraftVersion, loader);

            // 更新资源包
            List<ResourcePack> i7mePacks = new ArrayList<>();
            String applyFileName = "摸鱼人生材质包.zip";
            for (GameAssetDetail.AssetDownloadDetail it : I7assets.downloads) {
                FileUtil.setTemporaryDirPath(Paths.get(userHome, "." + MOD_ID, it.targetVersion));
                ResourcePack i7mePack = new ResourcePack(it.fileName, true);
                i7mePack.checkUpdate(it.fileUrl);
                i7mePacks.add(i7mePack);
            }

            // 应用资源包
            GameConfig I7config = new GameConfig(minecraftPath.resolve("options.txt"));
            I7config.I7addResourcePack("file/" + applyFileName);
            I7config.I7addincompatibleResourcePack("file/" + applyFileName);
            I7config.I7writeToFile();
        } catch (Exception e) {
            Log.warning(String.format("更新资源包失败：%s", e));
        }
    }
}