package i7meupdatemod.fabricloader;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

import i7meupdatemod.I7MEUpdateMod;
import i7meupdatemod.util.Log;
import i7meupdatemod.util.Reflection;

//1.14-latest
public class FabricLoaderMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Path gameDir = FabricLoader.getInstance().getGameDir();
        Log.setMinecraftLogFile(gameDir);
        String mcVersion = getMcVersion();
        if (mcVersion == null) {
            Log.warning("Minecraft version not found");
            return;
        }
        I7MEUpdateMod.init(gameDir, mcVersion, "Fabric");
    }

    private String getMcVersion() {
        try {
            // Fabric
            return (String) Reflection.clazz("net.fabricmc.loader.impl.FabricLoaderImpl")
                    .get("INSTANCE")
                    .get("getGameProvider()")
                    .get("getNormalizedGameVersion()").get();
        } catch (Exception ignored) {

        }
        try {
            // Quilt
            return (String) Reflection.clazz("org.quiltmc.loader.impl.QuiltLoaderImpl")
                    .get("INSTANCE")
                    .get("getGameProvider()")
                    .get("getNormalizedGameVersion()").get();
        } catch (Exception ignored) {

        }
        return null;
    }
}
