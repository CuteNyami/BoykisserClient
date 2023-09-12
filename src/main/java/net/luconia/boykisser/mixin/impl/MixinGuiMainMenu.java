package net.luconia.boykisser.mixin.impl;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.ui.FontRenderer;
import net.luconia.boykisser.ui.MainMenuButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @Shadow
    @Final
    public static ResourceLocation minecraftTitleTextures;
    @Shadow
    public float updateCounter;
    @Shadow
    public String splashText;
    @Shadow
    public String openGLWarning1;
    @Shadow
    public int field_92022_t;
    @Shadow
    public int field_92021_u;
    @Shadow
    public int field_92020_v;
    @Shadow
    public int field_92019_w;
    @Shadow
    public String openGLWarning2;
    @Shadow
    public int field_92024_r;
    @Shadow
    public GuiScreen field_183503_M;

    @Shadow
    public abstract boolean func_183501_a();

    @Shadow public abstract void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_);

    @Shadow public DynamicTexture viewportTexture;

    @Shadow public ResourceLocation backgroundTexture;

    /**
     * @reason Custom Client Main Menu
     * @author Nyami
     */
    @Overwrite
    public void initGui() {
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        int j = this.height / 4 + 48;

        this.buttonList.add(new MainMenuButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new MainMenuButton(2, this.width / 2 - 100, j + 24, I18n.format("menu.multiplayer")));
        this.buttonList.add(new MainMenuButton(0, this.width / 2 - 100, j + 55, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new MainMenuButton(4, this.width / 2 + 2, j + 55, 98, 20, I18n.format("menu.quit")));
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void constructor(CallbackInfo ci) {
        if(System.currentTimeMillis() % 5 < 2) {
            List<String> splash = new ArrayList<>(Arrays.asList(
                    "oooooo you like boys you are a boykisser",
                    "The best client",
                    "Crazy?",
                    "Why are you reading this?",
                    "Better then the moon",
                    "Why are lions bad?",
                    "The opposite of a good client!"
            ));

            Collections.shuffle(splash);

            this.splashText = splash.get(0);
        }
    }

    /**
     * @reason Custom Client Main Menu
     * @author Nyami
     */
    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);

        drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 50).getRGB());

        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if ((double) this.updateCounter < 1.0E-4D) {
            this.drawTexturedModalRect(j, k, 0, 0, 99, 44);
            this.drawTexturedModalRect(j + 99, k, 129, 0, 27, 44);
            this.drawTexturedModalRect(j + 99 + 26, k, 126, 0, 3, 44);
            this.drawTexturedModalRect(j + 99 + 26 + 3, k, 99, 0, 26, 44);
            this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        } else {
            this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
            this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        }

        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (this.width / 2 + 90), 70.0F, 0.0F);
        GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L) / 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
        f = f * 100.0F / (fontRenderer.getWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        fontRenderer.drawCenteredString(this.splashText, 0, -8, Color.YELLOW);
        GlStateManager.popMatrix();
        String s = "Minecraft 1.8.9 (Boykisser Edition)";

        if (this.mc.isDemo()) {
            s = s + " Demo";
        }

        fontRenderer.drawString(s, 2, this.height - 13, Color.WHITE);
        String s1 = "Copyright Mojang AB. Do not distribute!";
        fontRenderer.drawString(s1, this.width - fontRenderer.getWidth(s1) - 2, this.height - 13, Color.WHITE);

        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            fontRenderer.drawString(this.openGLWarning1, this.field_92022_t, this.field_92021_u, Color.WHITE);
            fontRenderer.drawString(this.openGLWarning2, (this.width - this.field_92024_r) / 2.f, this.buttonList.get(0).yPosition - 12, Color.WHITE);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.func_183501_a()) {
            this.field_183503_M.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}