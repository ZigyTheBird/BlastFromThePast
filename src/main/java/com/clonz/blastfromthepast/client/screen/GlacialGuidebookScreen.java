package com.clonz.blastfromthepast.client.screen;

import com.clonz.blastfromthepast.BlastFromThePast;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public class GlacialGuidebookScreen extends Screen {
    private PageButton forwardButton;
    private PageButton backButton;
    private int currentPage;
    private final int pageCount = 6;
    public static final ResourceLocation BOOK_LOCATION = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_inside.png");
    public static final ResourceLocation BURREL = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_burrel.png");
    public static final ResourceLocation FROSTOMPER = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_frostomper.png");
    public static final ResourceLocation GLACEROS = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_glaceros.png");
    public static final ResourceLocation PSYCHO_BEAR = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_psycho_bear.png");
    public static final ResourceLocation SNOWDO = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_snowdo.png");
    public static final ResourceLocation SPEARTOOTH = BlastFromThePast.location("textures/gui/glacial_guidebook/glacial_guidebook_speartooth.png");

    public GlacialGuidebookScreen() {
        super(Component.literal("screen.blastfromthepast.glacial_guidebook"));
    }

    public boolean setPage(int pageNum) {
        int i = Mth.clamp(pageNum, 0, pageCount - 1);
        if (i != this.currentPage) {
            this.currentPage = i;
            this.updateButtonVisibility();
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(int pageNum) {
        return this.setPage(pageNum);
    }

    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (p_315823_) -> this.onClose()).bounds(this.width / 2 - 100, 196, 200, 20).build());
    }

    protected void createPageControlButtons() {
        int i = (this.width - 192) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(i + 116, 155, true, (p_98297_) -> this.pageForward(), true));
        this.backButton = this.addRenderableWidget(new PageButton(i + 43, 155, false, (p_98287_) -> this.pageBack(), true));
        this.updateButtonVisibility();
    }

    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }

        this.updateButtonVisibility();
    }

    protected void pageForward() {
        if (this.currentPage < pageCount - 1) {
            ++this.currentPage;
        }

        this.updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        this.forwardButton.visible = this.currentPage < pageCount - 1;
        this.backButton.visible = this.currentPage > 0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            switch (keyCode) {
                case 266:
                    this.backButton.onPress();
                    return true;
                case 267:
                    this.forwardButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        int x = (this.width - 192) / 2 - 29;
        int xOffset = this.currentPage % 2 == 0 ? 120 : 0;
        int xOffset2 = this.currentPage % 2 == 0 ? 0 : 120;

        ResourceLocation mob = FROSTOMPER;
        switch (currentPage) {
            case 0 -> mob = BURREL;
            case 1 -> mob = SNOWDO;
            case 2 -> mob = GLACEROS;
            case 3 -> mob = SPEARTOOTH;
            case 4 -> mob = PSYCHO_BEAR;
        }

        guiGraphics.blit(mob, x, 20, 0, 0, 250, 156, 250, 156);

        Component component;
        switch (currentPage) {
            case 0 -> component = Component.translatable("glacial_guidebook.burrel");
            case 1 -> component = Component.translatable("glacial_guidebook.snowdo");
            case 2 -> component = Component.translatable("glacial_guidebook.glaceros");
            case 3 -> component = Component.translatable("glacial_guidebook.speartooth");
            case 4 -> component = Component.translatable("glacial_guidebook.psycho_bear");
            default -> component = Component.translatable("glacial_guidebook.frostomper");
        }

        Component title;
        switch (currentPage) {
            case 0 -> title = Component.translatable("entity.blastfromthepast.burrel");
            case 1 -> title = Component.translatable("entity.blastfromthepast.snowdo");
            case 2 -> title = Component.translatable("entity.blastfromthepast.glaceros");
            case 3 -> title = Component.translatable("entity.blastfromthepast.speartooth");
            case 4 -> title = Component.translatable("entity.blastfromthepast.psycho_bear");
            default -> title = Component.translatable("entity.blastfromthepast.frostomper");
        }
        
        int hp = 0;
        switch (currentPage) {
            case 0 -> hp = 6;
            case 1 -> hp = 10;
            case 2 -> hp = 20;
            case 3 -> hp = 30;
            case 4 -> hp = 70;
            default -> hp = 100;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(1.75F, 1.75F, 1.75F);
        guiGraphics.drawString(this.font, title.getString(), (x + xOffset2 + 9)/1.75F, 50, 0, false);
        guiGraphics.pose().scale(0.571428F, 0.571428F, 0.571428F);
        String type;
        if (currentPage < 3) {
            type = Component.translatable("glacial_guidebook.passive").getString();
        }
        else if (currentPage != 5) type = Component.translatable("glacial_guidebook.hostile").getString();
        else type = Component.translatable("glacial_guidebook.neutral").getString();
        guiGraphics.drawString(this.font, type, x + xOffset2 + 9, 104, 0x555555, false);
        guiGraphics.drawString(this.font, hp + " HP", x + xOffset2 + 9, 112, 0x555555, false);
        if (currentPage == 3 || currentPage == 5)
            guiGraphics.drawString(this.font, Component.translatable("glacial_guidebook.tamable").getString(), x + xOffset2 + 9, 120, 0x555555, false);
        guiGraphics.pose().scale(0.6666666F, 0.6666666F, 0.6666666F);
        List<FormattedCharSequence> list = this.font.split(FormattedText.of(component.getString()), 165);

        for(int l = 0; l < list.size(); ++l) {
            FormattedCharSequence formattedcharsequence = list.get(l);
            guiGraphics.drawString(this.font, formattedcharsequence, (x + xOffset + 9)*1.5F, 44 + l * 9, 0, false);
        }
        guiGraphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(BOOK_LOCATION, (this.width - 192) / 2 - 29, 22, 0, 0, 250, 156, 250, 156);
    }

    @Override
    public boolean handleComponentClicked(Style style) {
        ClickEvent clickevent = style.getClickEvent();
        if (clickevent == null) {
            return false;
        } else if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String s = clickevent.getValue();

            try {
                int i = Integer.parseInt(s) - 1;
                return this.forcePage(i);
            } catch (Exception var5) {
                return false;
            }
        } else {
            boolean flag = super.handleComponentClicked(style);
            if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.closeScreen();
            }

            return flag;
        }
    }

    protected void closeScreen() {
        this.minecraft.setScreen(null);
    }
}
