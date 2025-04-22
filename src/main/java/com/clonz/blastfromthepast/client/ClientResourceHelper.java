package com.clonz.blastfromthepast.client;

import net.minecraft.resources.ResourceLocation;

public class ClientResourceHelper {
    public static ResourceLocation entityTexLoc(ResourceLocation entityLocation) {
        return entityLocation.withPath(ClientResourceHelper::entityTexLocPath);
    }

    public static String entityTexLocPath(String path) {
        return "textures/entity/" + path + ".png";
    }

    public static ResourceLocation entityTexLocWithTypeSubFolder(ResourceLocation entityLocation) {
        return entityLocation.withPath(path -> entityTexLocPath(path + "/" + path));
    }

    public static ResourceLocation entityTecLocWithTypeSubFolderWithSuffix(ResourceLocation entityLocation, String suffix) {
        return entityLocation.withPath(path -> entityTexLocPath(path + "/" + path + suffix));
    }

    public static ResourceLocation entityTexLocWithTypeSubFolderWithPrefix(ResourceLocation entityLocation, String prefix) {
        return entityLocation.withPath(path -> entityTexLocPath(path + "/" + prefix + path));
    }

    public static ResourceLocation entityTexLocWithTypeSubFolderWithPrefixAndSuffix(ResourceLocation entityLocation, String prefix, String suffix) {
        return entityLocation.withPath(path -> entityTexLocPath(path + "/" + prefix + path + suffix));
    }
}
