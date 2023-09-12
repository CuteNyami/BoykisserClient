package net.luconia.boykisser.ui.component.impl;

import net.luconia.boykisser.Client;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.luconia.boykisser.ui.component.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class InputComponent extends Component {

    private String text;
    private String placeholder;

    private final int maxLength;

    private boolean focused;

    private int cursorPosition;
    private int firstRenderedCharacterPosition;
    private int lastRenderedCharacterPosition;
    private boolean textSelected;
    private final float blinkingUnderscoreWidth;
    private final InputFlavor inputFlavor;

    private final boolean background;

    private final ActionHandler handler;

    public InputComponent(float x, float y, float width, float height, String placeholder, int maxLength, InputFlavor inputFlavor, boolean background, ActionHandler handler) {
        super(x, y, width, height);
        this.background = background;
        this.focused = false;
        this.cursorPosition = 0;
        this.textSelected = false;
        this.text = "";
        this.placeholder = placeholder;
        this.maxLength = maxLength;
        this.inputFlavor = inputFlavor;
        this.firstRenderedCharacterPosition = 0;
        this.lastRenderedCharacterPosition = placeholder.length();
        this.blinkingUnderscoreWidth = getFontRenderer().getWidth("_");
        this.handler = handler;
    }

    public InputComponent(float x, float y, float width, float height, String placeholder, int maxLength, InputFlavor inputFlavor) {
        this(x, y, width, height, placeholder, maxLength, inputFlavor, true, null);
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void close() {
        Keyboard.enableRepeatEvents(false);
    }

    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        Color color = focused ? getFocusedColor() : getColor();
        Color outlineColor = getOutlineColor() == null ? color : focused ? getOutlineFocusedColor() : getOutlineColor();

        Renderer.drawRoundedRect(this.x, this.y, this.x + this.width, this.y + this.height, getRadius(), color.getRGB());
        Renderer.drawRoundedOutline(this.x, this.y, this.x + this.width, this.y + this.height, getRadius(), getOutlineWidth(), outlineColor.getRGB());
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {
        String s = this.text;

        if (background) {
            drawBackground(mouseX, mouseY, partialTicks);
        }

        if (this.text.equals("") && !this.placeholder.equals("") && !focused) {
            getFontRenderer().drawString(placeholder, (int) (this.x + 5), (int) (this.y + (this.height - (12)) / 2), getPlaceholderColor());
            return;
        }

        int l = this.lastRenderedCharacterPosition;
        Color textColor = focused ? getTextFocusedColor() : getTextColor();

        if (focused && System.currentTimeMillis() % 1060L >= 530L) {
            if (this.cursorPosition == this.text.length()) {
                s = s + "_";
                ++l;
            } else {
                int i1 = this.cursorPosition - this.firstRenderedCharacterPosition < 0 ? 0 : (int) getFontRenderer().getWidth(s.substring(this.firstRenderedCharacterPosition, this.cursorPosition));
                Gui.drawRect((int) (this.x + 5 + i1), (int) (this.y + this.height / 2 - 5), (int) (this.x + 5 + i1 + 1), (int) (this.y + this.height / 2 - 5 + 10), getCursorColor().getRGB());
            }
        }

        if (!this.text.isEmpty()) {
            this.fitTextInField();
            float k = getFontRenderer().getWidth(s.substring(this.firstRenderedCharacterPosition, this.lastRenderedCharacterPosition));

            if (this.textSelected) {
                Gui.drawRect((int) (this.x + 5), (int) (this.y + this.height / 2 - 5), (int) (this.x + 5 + k), (int) (this.y + this.height / 2 - 5 + 10), new Color(51, 144, 255).getRGB());
            }
        }

        getFontRenderer().drawString(this.text.isEmpty() || l - this.firstRenderedCharacterPosition < 0 ? s : s.substring(this.firstRenderedCharacterPosition, l), (int) (this.x + 5), (int) (this.y + (this.height - (12)) / 2), textSelected ? Color.WHITE : textColor);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.focused = false;

        if (mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height) {
            this.focused = true;
            this.textSelected = false;
            this.resetCursorPosition();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.focused) {
            if (keyCode != 1 && keyCode != 28) {
                if (this.text.length() > 0) {
                    if (keyCode == 14) {
                        if (this.textSelected) {
                            this.text = "";
                            this.textSelected = false;
                            this.resetCursorPosition();
                        } else {
                            if (this.cursorPosition == 0) {
                                return;
                            }

                            this.text = this.text.substring(0, this.cursorPosition - 1) + this.text.substring(this.cursorPosition);

                            --this.cursorPosition;

                            --this.lastRenderedCharacterPosition;
                            this.fitTextInFieldDeletion();
                        }

                        if (handler != null)
                            handler.handle(this);
                        return;
                    }

                    if (keyCode == 211) {
                        if (this.textSelected) {
                            this.text = "";
                            this.textSelected = false;
                            this.resetCursorPosition();
                        } else {
                            if (this.cursorPosition == this.text.length()) {
                                return;
                            }

                            this.text = this.text.substring(0, this.cursorPosition) + this.text.substring(this.cursorPosition + 1);
                            --this.lastRenderedCharacterPosition;
                            this.fitTextInFieldDeletion();
                        }

                        return;
                    }
                }

                Character character1 = this.checkInputCharacter(typedChar);

                if (character1 != null) {
                    if (this.textSelected) {
                        this.text = "";
                        this.textSelected = false;
                        this.resetCursorPosition();
                    }

                    if (this.text.length() != this.maxLength) {
                        this.text = this.text.substring(0, this.cursorPosition) + character1 + this.text.substring(this.cursorPosition);
                        ++this.cursorPosition;
                        this.textSelected = false;
                        this.fitTextInFieldKeyTyped();
                    }
                } else if (keyCode == 203) {
                    if (this.cursorPosition > 0) {
                        --this.cursorPosition;
                    }

                    this.textSelected = false;
                } else if (keyCode == 205) {
                    if (this.cursorPosition < this.text.length()) {
                        ++this.cursorPosition;
                    }

                    this.textSelected = false;
                } else {
                    if (GuiScreen.isCtrlKeyDown()) {
                        switch (keyCode) {
                            case 30:
                                this.textSelected = true;
                                return;

                            case 46:
                                if (this.textSelected) {
                                    GuiScreen.setClipboardString(this.text);
                                }
                                return;
                            case 47:
                                if (this.textSelected) {
                                    this.text = "";
                                    this.textSelected = false;
                                    this.resetCursorPosition();
                                }

                                String clipboardString = GuiScreen.getClipboardString();

                                for (char c0 : clipboardString.toCharArray()) {
                                    character1 = this.checkInputCharacter(c0);

                                    if (character1 != null) {
                                        if (this.text.length() == this.maxLength) {
                                            return;
                                        }

                                        this.text = this.text + character1;
                                        ++this.cursorPosition;
                                    }
                                }
                        }
                    }
                }
            } else {
                this.focused = false;
                this.textSelected = false;
                this.resetCursorPosition();
            }

            if (handler != null)
                handler.handle(this);
        }
    }

    private Character checkInputCharacter(char character) {
        return this.inputFlavor.contains(character) ? this.inputFlavor == InputFlavor.HEX_COLOR ? String.valueOf(character).toUpperCase().toCharArray()[0] : character : null;
    }
    
    private void fitTextInFieldKeyTyped() {
        String s = this.text;
        float i = this.width - 10;

        if (this.cursorPosition == s.length()) {
            i -= this.blinkingUnderscoreWidth;
        }

        for (int j = s.length(); j >= this.firstRenderedCharacterPosition; --j) {
            int k = (int) getFontRenderer().getWidth(s.substring(this.firstRenderedCharacterPosition, j));

            if (k <= i) {
                this.lastRenderedCharacterPosition = j;
                break;
            }
        }
    }

    private void fitTextInFieldDeletion() {
        String s = this.text;
        float i = this.width - 10;

        if (this.cursorPosition == s.length()) {
            i -= this.blinkingUnderscoreWidth;
        }

        if (this.lastRenderedCharacterPosition == s.length()) {
            for (int j = 0; j <= this.lastRenderedCharacterPosition; ++j) {
                float k = getFontRenderer().getWidth(s.substring(j, this.lastRenderedCharacterPosition));

                if (k <= i) {
                    this.firstRenderedCharacterPosition = j;
                    break;
                }
            }
        } else {
            for (int l = s.length(); l >= this.firstRenderedCharacterPosition; --l) {
                float i1 = getFontRenderer().getWidth(s.substring(this.firstRenderedCharacterPosition, l));

                if (i1 <= i) {
                    this.lastRenderedCharacterPosition = l;
                    break;
                }
            }
        }
    }

    private void fitTextInField() {
        float width = this.width - 10;

        if (this.cursorPosition == this.text.length()) {
            width -= this.blinkingUnderscoreWidth;
        }

        float textWidth = getFontRenderer().getWidth(this.text.substring(this.firstRenderedCharacterPosition, this.lastRenderedCharacterPosition));

        if (textWidth > width && this.lastRenderedCharacterPosition == this.text.length()) {
            for (int k = 0; k < this.text.length(); ++k) {
                int textWidth1 = (int) getFontRenderer().getWidth(this.text.substring(k));

                if (textWidth1 <= width) {
                    this.firstRenderedCharacterPosition = k;
                    break;
                }
            }
        }

        if (this.cursorPosition < this.firstRenderedCharacterPosition) {
            int i1 = this.firstRenderedCharacterPosition - this.cursorPosition;
            this.firstRenderedCharacterPosition -= i1;

            for (int k1 = this.text.length(); k1 >= this.firstRenderedCharacterPosition; --k1) {
                textWidth = getFontRenderer().getWidth(this.text.substring(this.firstRenderedCharacterPosition, k1));

                if (textWidth <= width) {
                    this.lastRenderedCharacterPosition = k1;
                    break;
                }
            }
        } else if (this.cursorPosition > this.lastRenderedCharacterPosition) {
            int j1 = this.cursorPosition - this.lastRenderedCharacterPosition;
            this.lastRenderedCharacterPosition += j1;

            for (int l1 = 0; l1 <= this.lastRenderedCharacterPosition; ++l1) {
                textWidth = getFontRenderer().getWidth(this.text.substring(l1, this.lastRenderedCharacterPosition));

                if (textWidth <= width) {
                    this.firstRenderedCharacterPosition = l1;
                    break;
                }
            }
        }
    }


    public void reset() {
        this.focused = false;
        this.text = "";
        this.textSelected = false;
        this.resetCursorPosition();
    }

    private void resetCursorPosition() {
        this.cursorPosition = this.text.length();
        this.firstRenderedCharacterPosition = 0;
        this.lastRenderedCharacterPosition = this.text.isEmpty() && !this.placeholder.isEmpty() ? this.placeholder.length() : this.text.length();
    }

    protected FontRenderer getFontRenderer() {
        return Client.getInstance().fontRenderer;
    }

    protected float getRadius() {
        return 5.f;
    }

    protected float getOutlineWidth() {
        return 1.f;
    }

    protected Color getTextColor() {
        return getPlaceholderColor();
    }

    protected Color getPlaceholderColor() {
        return new Color(120, 120, 120);
    }

    protected Color getTextFocusedColor() {
        return Color.WHITE;
    }

    protected Color getColor() {
        return new Color(36, 36, 36);
    }

    protected Color getCursorColor() {
        return new Color(209, 209, 209);
    }

    protected Color getFocusedColor() {
        return getColor();
    }

    protected Color getOutlineColor() {
        return null;
    }

    protected Color getOutlineFocusedColor() {
        return getOutlineColor();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.firstRenderedCharacterPosition = 0;
        this.lastRenderedCharacterPosition = this.text.length();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public static class InputFlavor {
        public static final InputFlavor EMAIL = new InputFlavor("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'*+-/=?^_`{|}~;.@\"(),:;<>@[\\]");
        public static final InputFlavor HEX_COLOR = new InputFlavor("abcdefABCDEF0123456789");
        public static final InputFlavor MOD_PROFILE_NAME = new InputFlavor("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-(),. ");
        public static final InputFlavor NORMAL = new InputFlavor("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%§&'*+-/=?^_`{|}~;.@\"(),:;<>@[\\] ");

        private final String flavor;

        public InputFlavor(String flavor) {
            this.flavor = flavor;
        }

        public boolean contains(char ch) {
            return this.flavor.contains(String.valueOf(ch));
        }
    }

}
