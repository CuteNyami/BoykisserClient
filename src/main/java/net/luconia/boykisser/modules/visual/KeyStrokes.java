package net.luconia.boykisser.modules.visual;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.Delta;
import net.luconia.boykisser.MathUtil;
import net.luconia.boykisser.module.Category;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.ModuleRect;
import net.luconia.boykisser.ui.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class KeyStrokes extends Module {
    public KeyStrokes() {
        super("KeyStrokes", Category.VISUAL);
    }

    @Override
    public void render() {
        GL11.glPushMatrix();

        FontRenderer fr = Client.getInstance().fontRenderer;
        KeystrokesMode mode = KeystrokesMode.WASD_JUMP_MOUSE;

        for (Key key : mode.getKeys()) {
            int fadeTime = 20;
            if (key.isDown()) {
                key.fade += (1F / (float) fadeTime) * (Delta.DELTA_TIME * 0.1f);
            } else {
                key.fade -= (1F / (float) fadeTime) * (Delta.DELTA_TIME * 0.1f);
            }
            // Clamp fade
            key.fade = Math.max(0, Math.min(1, key.fade));

            int textWidth = (int) fr.getWidth(key.getName());

            Gui.drawRect((int) (getX() + key.getX()), (int) (getY() + key.getY()), (int) (getX() + key.getX() + key.getWidth()), (int) (getY() + key.getY() + key.getHeight()), MathUtil.lerp(new Color(0, 0, 0, 102), new Color(255, 255, 255, 120), key.fade).getRGB());

            if (!key.getName().contains("-")) {
                fr.drawString(key.getName(), getX() + key.getX() + key.getWidth() / 2 - textWidth / 2 - 0.5f, (int) (getY() + key.getY() + key.getHeight() / 2 - 7), MathUtil.lerp(new Color(255, 255, 255, 255), new Color(0, 0, 0, 255), key.fade));
            } else {
                Minecraft.getMinecraft().fontRendererObj.drawString(key.getName(), (int) (getX() + key.getX() + key.getWidth() / 2 - textWidth / 2 - 3), (int) (getY() + key.getY() + key.getHeight() / 2 - 4), MathUtil.lerp(new Color(255, 255, 255, 255), new Color(0, 0, 0, 255), key.fade).getRGB());
            }
        }

        GL11.glPopMatrix();
    }

    public static class Key {
        public static Minecraft mc = Minecraft.getMinecraft();

        private static final Key W = new Key("W", mc.gameSettings.keyBindForward, 21, 1, 18, 18);
        private static final Key A = new Key("A", mc.gameSettings.keyBindLeft, 1, 21, 18, 18);
        private static final Key S = new Key("S", mc.gameSettings.keyBindBack, 21, 21, 18, 18);
        private static final Key D = new Key("D", mc.gameSettings.keyBindRight, 41, 21, 18, 18);

        private static final Key LMB = new Key("LMB", mc.gameSettings.keyBindAttack, 1, 41, 28, 18);
        private static final Key RMB = new Key("RMB", mc.gameSettings.keyBindUseItem, 31, 41, 28, 18);

        private static final Key Jump1 = new Key("§m---", mc.gameSettings.keyBindJump, 1, 41, 58, 10);
        private static final Key Jump2 = new Key("§m---", mc.gameSettings.keyBindJump, 1, 61, 58, 10);

        private final String name;
        private final KeyBinding keyBind;
        private final int x, y, w, h;

        private float fade;

        public Key(String name, KeyBinding keyBind, int x, int y, int w, int h) {
            this.name = name;
            this.keyBind = keyBind;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public boolean isDown() {
            return keyBind.isKeyDown();
        }

        public int getHeight() {
            return h;
        }

        public int getWidth() {
            return w;
        }

        public String getName() {
            return name;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    @Override
    public ModuleRect getSize() {
        return new ModuleRect(60, 72);
    }

    public static Color outlineColor;

    public enum KeystrokesMode {
        WASD(Key.W, Key.A, Key.S, Key.D),
        WASD_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB),
        WASD_JUMP(Key.W, Key.A, Key.S, Key.D, Key.Jump1),
        WASD_JUMP_MOUSE(Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, Key.Jump2);

        private final Key[] keys;
        private int width, height;

        KeystrokesMode(Key... keysIn) {
            this.keys = keysIn;

            for (Key key : keys) {
                this.width = Math.max(this.width, key.getX() + key.getWidth());
                this.height = Math.max(this.height, key.getY() + key.getHeight());
            }
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public Key[] getKeys() {
            return keys;
        }
    }
}
