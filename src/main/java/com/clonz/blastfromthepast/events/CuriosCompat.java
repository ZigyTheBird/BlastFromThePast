package com.clonz.blastfromthepast.events;

import com.clonz.blastfromthepast.access.PlayerBFTPDataAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CuriosCompat {
    @SubscribeEvent
    public static void removeDroppedCurios(CurioDropsEvent event) {
        if (event.getCurioHandler().getWearer() instanceof ServerPlayer player) {
            if (((PlayerBFTPDataAccess) player).bftp$hasInventoryStored()) {
                event.getDrops().clear();
            }
        }
    }

    @Nullable
    public static List<ItemStack> getCuriosItems(ServerPlayer player) {
        // TODO: there's likely a better way to do this
        CurioInventoryCapability curioCap = (CurioInventoryCapability) player.getCapability(CuriosCapability.INVENTORY);
        if (curioCap == null) {
            return null;
        }
        List<ItemStack> items = new ArrayList<>();
        for(Map.Entry<String, ICurioStacksHandler> entry : curioCap.getCurios().entrySet()) {
            ICurioStacksHandler stacksHandler = entry.getValue();
            IDynamicStackHandler stacks = stacksHandler.getStacks();
            IDynamicStackHandler cosmetics = stacksHandler.getCosmeticStacks();
            for (int i = 0; i < stacks.getSlots(); i++) {
                items.add(stacks.getStackInSlot(i));
            }
            for (int i = 0; i < cosmetics.getSlots(); i++) {
                items.add(cosmetics.getStackInSlot(i));
            }
        }
        return items;
    }
}