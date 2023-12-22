package net.creeperhost.backupmanager.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.creeperhost.backupmanager.BackupManager;
import net.creeperhost.backupmanager.client.gui.BackupsGui;
import net.creeperhost.polylib.client.modulargui.ModularGuiScreen;
import net.creeperhost.polylib.client.modulargui.lib.GuiRender;
import net.creeperhost.polylib.client.modulargui.lib.geometry.Rectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;

/**
 * Created by brandon3055 on 09/12/2023
 */
public class WorldEntryHooks {
    private static boolean mouseOver = false;
    private static Rectangle bounds = null;

    public static void renderWorldEntry(SelectWorldScreen screen, PoseStack poseStack, int index, int yPos, int xPos, int width, int height, int mouseX, int mouseY, boolean mouseOverEntry, float partialTicks) {
        if (!BackupManager.hasBackups() || index != screen.list.children().size() - 1) {
            if (mouseOverEntry) mouseOver = false;
            return;
        }

        GuiRender render = new GuiRender(Minecraft.getInstance(), poseStack, MultiBufferSource.immediate(Tesselator.getInstance().getBuilder()));
        bounds = Rectangle.create(xPos, yPos + height + 3, width, 18);
        mouseOver = bounds.contains(mouseX, mouseY);

        render.borderRect(bounds, 1, 0xFF000000, mouseOver ? 0xFFFFFFFF : 0xFF606060);
        render.drawCenteredString(new TranslatableComponent("backupmanager:button.backups_entry"), bounds.x() + (width / 2D), bounds.y() + 5, mouseOver ? 0x66FF00 : 0xFFFFFF, false);
        if (!mouseOver) bounds = null;
    }

    public static boolean click(SelectWorldScreen screen, double mouseX, double mouseY, int button) {
        if (mouseOver && bounds != null && bounds.contains(mouseX, mouseY)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
            Minecraft.getInstance().setScreen(new ModularGuiScreen(new BackupsGui(screen), screen));
            return true;
        }
        return false;
    }
}
