package i7meupdatemod;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import i7meupdatemod.core.I7GameConfig;
import i7meupdatemod.core.I7meConfig;
import i7meupdatemod.core.I7ResourcePack;
import i7meupdatemod.core.I7ResourcePackConverter;
import i7meupdatemod.entity.I7GameAssetDetail;
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

        int minecraftMajorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);

        try {
            // 获取资源
            I7GameAssetDetail I7assets = I7meConfig.getAssetDetail(minecraftVersion, loader);

            // 更新资源包
            List<I7ResourcePack> i7mePacks = new ArrayList<>();
            String applyFileName = "摸鱼人生材质包.zip";
            for (I7GameAssetDetail.AssetDownloadDetail it : I7assets.downloads) {
                FileUtil.setTemporaryDirPath(Paths.get(userHome, "." + MOD_ID, it.targetVersion));
                I7ResourcePack i7mePack = new I7ResourcePack(it.fileName, true);
                i7mePack.I7checkUpdate(it.fileUrl);
                i7mePacks.add(i7mePack);
            }

            // 应用资源包
            I7GameConfig I7config = new I7GameConfig(minecraftPath.resolve("options.txt"));
            I7config.I7addResourcePack("摸鱼人生材质包",
                    (minecraftMajorVersion <= 12 ? "" : "file/") + applyFileName);
            I7config.I7writeToFile();
        } catch (Exception e) {
            Log.warning(String.format("更新资源包失败：%s", e));
        }
    }
}