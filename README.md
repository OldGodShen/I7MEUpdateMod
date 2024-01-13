# 自动摸鱼人生服务器材质包加载
[![Version](https://img.shields.io/github/v/release/OldGodShen/I7MEUpdateMod?label=&logo=V&labelColor=E1F5FE&color=5D87BF&style=for-the-badge)](https://github.com/OldGodShen/I7MEUpdateMod/tags)
[![License](https://img.shields.io/github/license/OldGodShen/I7MEUpdateMod?label=&logo=c&style=for-the-badge&color=A8B9CC&labelColor=455A64)](https://github.com/OldGodShen/I7MEUpdateMod/blob/main/LICENSE)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/OldGodShen/I7MEUpdateMod/release.yml?style=for-the-badge&label=&logo=Gradle&labelColor=388E3C)](https://github.com/OldGodShen/I7MEUpdateMod/actions)
[![Star](https://img.shields.io/github/stars/OldGodShen/I7MEUpdateMod?label=&logo=GitHub&labelColor=black&color=FAFAFA&style=for-the-badge)](https://github.com/OldGodShen/I7MEUpdateMod/stargazers)


本Mod基于「[自动汉化更新模组Ⅲ](https://github.com/CFPAOrg/I18nUpdateMod3)」用于自动下载、更新、应用「[摸鱼人生服务器材质包](http://43.248.184.175:26009/generated.zip)」，来避免「[ItemsAdder](https://www.spigotmc.org/resources/%E2%9C%A8itemsadder%E2%AD%90emotes-mobs-items-armors-hud-gui-emojis-blocks-wings-hats-liquids.73355)」尚未完全适配1.20.4导致切换子服时不能避免重复加载相同材质包问题，请在安装本模组后禁用摸鱼人生的服务器资源包加载

- 官方资源：「[摸鱼人生服务器材质包](http://43.248.184.175:26009/generated.zip)」是由「[摸鱼人生](https://www.mcbbs.net/thread-1438743-1-1.html)」的官方服务器下载



- 「[自动汉化更新模组Ⅲ](https://github.com/CFPAOrg/I18nUpdateMod3)」是由「[CFPAOrg](http://cfpa.team/)」团队维护的「[自动汉化资源包](https://github.com/CFPAOrg/Minecraft-Mod-Language-Package)」的自动汉化更新模组

## 已知问题
- ~~与「[自动汉化更新模组Ⅲ](https://github.com/CFPAOrg/I18nUpdateMod3)」一起使用时会导致资源包合并，但是不会影响使用，目前没找到解决方案。~~ （已修复）
- 目前未发现其他问题

## 支持的版本
- Minecraft：1.20.4
- Mod加载器：MinecraftForge、NeoForge、Fabric、Quilt 都支持
- Java：17~21 都支持

仅仅需要在mods文件夹中放置本Mod的jar文件即可，Mod本身与Mod Loader、Java版本均兼容，Mod本身不需要进行任何版本隔离