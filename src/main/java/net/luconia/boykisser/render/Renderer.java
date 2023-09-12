package net.luconia.boykisser.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renderer class
 * <br>
 * Contains multiple drawing functions
 * <p>The code is originally made by <a href="https://github.com/Quexii">shoroa</a></p>
 * <br>
 * The code is modified and fixes some gl rendering issues
 * @author Nyami, Shoroa
 */
public class Renderer {

    /**
     * Draw a rounded rectangle using the {@link RoundedShaderRenderer}
     */
    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        RoundedShaderRenderer.getInstance().drawRound(x, y, width, height, radius, color);
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, float radius, int color) {
        glPushMatrix();
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glBegin(GL_LINE_LOOP);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();

        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        glPopMatrix();
    }

    public static void drawRoundedOutline(float x, float y, float x1, float y1, float radius,float lineWidth, int color) {
        glPushMatrix();
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y1 - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius + Math.sin(i * Math.PI / 180.0D) * radius, y + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glDisable(GL_LINE_SMOOTH);
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        glPopMatrix();
    }

    public static void drawSelectRoundedRect(float x, float y, float x1, float y1, float radius1,float radius2,float radius3,float radius4, int color) {
        glPushMatrix();
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glBegin(GL_POLYGON);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius1 + Math.sin(i * Math.PI / 180.0D) * radius1 * -1.0D, y + radius1 + Math.cos(i * Math.PI / 180.0D) * radius1 * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius2 + Math.sin(i * Math.PI / 180.0D) * radius2 * -1.0D, y1 - radius2 + Math.cos(i * Math.PI / 180.0D) * radius2 * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius3 + Math.sin(i * Math.PI / 180.0D) * radius3, y1 - radius3 + Math.cos(i * Math.PI / 180.0D) * radius3);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius4 + Math.sin(i * Math.PI / 180.0D) * radius4, y + radius4 + Math.cos(i * Math.PI / 180.0D) * radius4);
        glEnd();
        glBegin(GL_LINE_LOOP);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius1 + Math.sin(i * Math.PI / 180.0D) * radius1 * -1.0D, y + radius1 + Math.cos(i * Math.PI / 180.0D) * radius1 * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius2 + Math.sin(i * Math.PI / 180.0D) * radius2 * -1.0D, y1 - radius2 + Math.cos(i * Math.PI / 180.0D) * radius2 * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius3 + Math.sin(i * Math.PI / 180.0D) * radius3, y1 - radius3 + Math.cos(i * Math.PI / 180.0D) * radius3);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius4 + Math.sin(i * Math.PI / 180.0D) * radius4, y + radius4 + Math.cos(i * Math.PI / 180.0D) * radius4);
        glEnd();
        glDisable(GL_LINE_SMOOTH);

        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();

        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        glPopMatrix();
    }

    public static void drawSelectRoundedOutline(float x, float y, float x1, float y1, float radius1,float radius2,float radius3,float radius4,float lineWidth, int color) {
        glPushMatrix();
        glPushAttrib(0);
        glScaled(0.5D, 0.5D, 0.5D);
        x *= 2.0D;
        y *= 2.0D;
        x1 *= 2.0D;
        y1 *= 2.0D;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        setColor(color);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        int i;
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x + radius1 + Math.sin(i * Math.PI / 180.0D) * radius1 * -1.0D, y + radius1 + Math.cos(i * Math.PI / 180.0D) * radius1 * -1.0D);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x + radius2 + Math.sin(i * Math.PI / 180.0D) * radius2 * -1.0D, y1 - radius2 + Math.cos(i * Math.PI / 180.0D) * radius2 * -1.0D);
        for (i = 0; i <= 90; i += 3)
            glVertex2d(x1 - radius3 + Math.sin(i * Math.PI / 180.0D) * radius3, y1 - radius3 + Math.cos(i * Math.PI / 180.0D) * radius3);
        for (i = 90; i <= 180; i += 3)
            glVertex2d(x1 - radius4 + Math.sin(i * Math.PI / 180.0D) * radius4, y + radius4 + Math.cos(i * Math.PI / 180.0D) * radius4);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glPopAttrib();
        glLineWidth(1);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        glPopMatrix();
    }

    public static void drawLine(float x, float x1, float y, float y1, int color) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glBegin(GL_LINES);
        glVertex2f(y, y1);
        glVertex2f(x, x1);
        glEnd();
        glBegin(GL_LINES);
        glVertex2f(y, y1);
        glVertex2f(x, x1);
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static void drawLine(float x, float y, int color) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        setColor(color);
        glBegin(GL_LINES);
        glVertex2f(x, x);
        glVertex2f(y, y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2f(x, x);
        glVertex2f(y, y);
        glEnd();
        glDisable(GL_BLEND);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static void drawGradientRect(float minX, float minY, float maxX, float maxY, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        glBegin(GL_POLYGON);
        setColor(startColor);
        glVertex2f(minX, minY);
        glVertex2f(minX, maxY);
        setColor(endColor);
        glVertex2f(maxX, maxY);
        glVertex2f(maxX, minY);
        glEnd();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientRect(float x, float y, float width, float height, int col, int col1, int col2, int col3) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        glBegin(GL_POLYGON);
        setColor(col);
        glVertex2f(x, y);
        setColor(col1);
        glVertex2f(x, height);
        setColor(col2);
        glVertex2f(width, height);
        setColor(col3);
        glVertex2f(width, y);
        glEnd();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;
        glColor4f(r, g, b, a);
    }
}
