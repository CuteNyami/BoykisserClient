package net.luconia.boykisser.gui;

import lombok.SneakyThrows;
import net.luconia.boykisser.Client;
import net.luconia.boykisser.mixin.DevTweaker;
import net.luconia.boykisser.module.Module;
import net.luconia.boykisser.module.Option;
import net.luconia.boykisser.render.Renderer;
import net.luconia.boykisser.ui.FontRenderer;
import net.luconia.boykisser.ui.component.Component;
import net.luconia.boykisser.ui.component.ComponentScreen;
import net.luconia.boykisser.ui.component.impl.*;
import net.luconia.boykisser.ui.component.impl.button.CheckBox;
import net.luconia.boykisser.ui.component.impl.button.ToggleButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiModMenu extends ComponentScreen {

    private GuiModMenuList list;

    private InputComponent search;

    private final float modMenuWidth = 497.5f;
    private final float modMenuHeight = 308;

    private float x;
    private float y;

    private final FontRenderer smallFont = new FontRenderer("fonts/product_sans.ttf", 18.f);

    @Override
    @SneakyThrows
    public void initGui() {
        Method method = mc.entityRenderer.getClass().getDeclaredMethod(DevTweaker.devEnv ? "loadShader" : "a", ResourceLocation.class);
        method.setAccessible(true);
        method.invoke(mc.entityRenderer, new ResourceLocation("shaders/post/menu_blur.json"));

        x = width / 2.f - modMenuWidth / 2;
        y = height / 2.f - modMenuHeight / 2;

        this.components.clear();
        this.list = new GuiModMenuList();
        this.components.add(list);
        this.components.add(search = new InputComponent(x + modMenuWidth - 97 + 10, y + 5 + 12 / 2.f - 3, 85, 8.5f, "Search", -1, InputComponent.InputFlavor.NORMAL, false, null) {
            @Override
            protected FontRenderer getFontRenderer() {
                return smallFont;
            }

            @Override
            protected Color getTextFocusedColor() {
                return Color.BLACK;
            }

            @Override
            protected Color getPlaceholderColor() {
                return new Color(221, 221, 221);
            }

            @Override
            protected Color getCursorColor() {
                return new Color(28, 28, 28);
            }
        });
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontRenderer = Client.getInstance().fontRenderer;

        Renderer.drawSelectRoundedRect(x, y, x + 109.5f, y + modMenuHeight, 15.f, 15.f, 0, 0, new Color(232, 218, 231, 242).getRGB());
        Renderer.drawGradientRect(x + 109.5f - 3, y, x + 109.5f, y + modMenuHeight, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 23).getRGB());
        Renderer.drawSelectRoundedRect(x + 109.5f, y, x + 109.5f + modMenuWidth - 109.5f, y + modMenuHeight, 0, 0, 15.f, 15.f, new Color(246, 246, 246).getRGB());

        fontRenderer.drawString("Client Modules", (int) (x + 109.5f + 31), (int) (y + 24.5f), Color.BLACK);
        Renderer.drawRoundedOutline(x + modMenuWidth - 97, y + 5, (x + modMenuWidth - 97) + 92.5f, y + 5 + 12, 5.f, 1.f, new Color(221, 221, 221).getRGB());
        mc.getTextureManager().bindTexture(new ResourceLocation("client/icons/search.png"));

        GL11.glPushMatrix();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        drawModalRectWithCustomSizedTexture((int) (x + modMenuWidth - 97 + 4), (int) y + 5 + 2, 0, 0, 7, 7, 7, 7);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    @SneakyThrows
    public void onGuiClosed() {
        Client.getInstance().getConfig().saveMods();
        Method method = mc.entityRenderer.getClass().getDeclaredMethod(DevTweaker.devEnv ? "stopUseShader" : "b");
        method.setAccessible(true);
        method.invoke(mc.entityRenderer);
    }

    private class GuiModMenuList extends ScrollListComponent {

        public GuiModMenuList() {
            super(GuiModMenu.this.x + 109.5f + 31, GuiModMenu.this.y + 43, 326, 258, new ScrollbarComponent(5.f) {

                private boolean moved;

                @Override
                public void draw(int mouseX, int mouseY, float partialTicks) {
                    if (!moved) {
                        this.x = x + 20;
                        moved = true;
                    }

                    Renderer.drawRoundedRect(x, location, size, thumbSize, 2.5f, new Color(237, 237, 237));
                }
            });

            for (Module module : Client.getInstance().getModules()) {
                this.add(new GuiModEntry(module));
            }
        }

        @Override
        public void drawComponent(int mouseX, int mouseY, float partialTicks) {
            float compY = y;

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            glScissor(x, y, width, height);

            for (Component component : components) {
                if (component instanceof GuiModEntry) {
                    GuiModEntry entry = (GuiModEntry) component;

                    if (!entry.module.getName().toLowerCase().contains(search.getText().toLowerCase()))
                        continue;
                }
                if (component.getX() == 0) {
                    component.setX(this.x);
                }

                component.setY(compY - scrollbar.getScroll());

                component.drawComponent(mouseX, mouseY, partialTicks);
                compY += component.getHeight() + getSpacing();
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

            float content = scrollbar.content;
            scrollbar.content -= getSpacing();

            if (scrollbar.content > height)
                scrollbar.drawComponent(mouseX, mouseY, partialTicks);

            scrollbar.content = content;        }

        @Override
        public float getSpacing() {
            return 7.5f;
        }

        public class GuiModEntry extends Component {

            private ToggleButton toggleButton;

            public final Module module;

            private boolean showSettings;

            private final HashMap<Component, String> settings = new HashMap<>();

            private final List<Component> settingsComponents = new ArrayList<>();

            private final float defaultHeight;

            public GuiModEntry(Module module) {
                super(0, 0, 326, 24);
                this.module = module;
                this.showSettings = false;
                this.defaultHeight = height;
            }

            @SneakyThrows
            public void init() {
                this.toggleButton = new ToggleButton(x, y, component -> {
                    ToggleButton comp = (ToggleButton) component;
                    this.module.setEnabled(comp.isToggled());
                });
                this.toggleButton.setToggled(module.isEnabled());
                this.settingsComponents.clear();
                for (String name : module.getOptions().keySet()) {
                    Field field;

                    try {
                        field = module.getClass().getDeclaredField(name);
                    } catch (NoSuchFieldException e) {
                        continue;
                    }

                    Option option = field.getAnnotation(Option.class);

                    if (field.getType() == Integer.class || field.getType() == Double.class || field.getType() == Float.class ||
                            field.getType() == int.class || field.getType() == double.class || field.getType() == float.class) {
                        SliderComponent slider = new SliderComponent("", 0, 0, ((Double) field.get(module)).floatValue(), (float) option.min(), (float) option.max(), component -> module.setSetting(field.getName(), ((SliderComponent)component).getValue())) {
                            @Override
                            protected Color getTextColor() {
                                return Color.BLACK;
                            }
                        };
                        this.settings.put(slider, option.displayName());
                        this.settingsComponents.add(slider);
                    }

                    if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                        CheckBox checkBox = new CheckBox(0, 0, component -> module.setSetting(field.getName(), ((CheckBox)component).isToggled()));
                        checkBox.setToggled(field.getBoolean(module));
                        this.settings.put(checkBox, option.displayName());
                        this.settingsComponents.add(checkBox);
                    }

                    if (field.getType() == String.class) {
                        InputComponent input = new InputComponent(0, 0, 70, 15, option.placeholder(), -1, InputComponent.InputFlavor.NORMAL, true, component -> module.setSetting(field.getName(), ((InputComponent)component).getText())) {
                            @Override
                            public void drawBackground(int mouseX, int mouseY, float partialTicks) {
                                Renderer.drawRoundedOutline(this.x, this.y, this.x + this.width, this.y + this.height, 5.f, 1.f, new Color(30, 138, 255).getRGB());
                            }

                            @Override
                            protected Color getTextFocusedColor() {
                                return Color.BLACK;
                            }
                        };
                        input.setText((String) field.get(module));
                        this.settings.put(input, option.displayName());
                        this.settingsComponents.add(input);
                    }
                }
            }

            @Override
            public void drawComponent(int mouseX, int mouseY, float partialTicks) {
                FontRenderer fontRenderer = Client.getInstance().fontRenderer;

                Renderer.drawRoundedRect(x, y, width, height, 5, new Color(237, 237, 237));
                fontRenderer.drawString(this.module.getName(), (int) this.x + 15, (int) (this.y + (24 - (8 + 15 - 12)) / 2), Color.BLACK);

                this.toggleButton.setX(x + width - 47.5f);
                this.toggleButton.setY(y + 24 / 2.f - 5.5f);
                this.toggleButton.drawComponent(mouseX, mouseY, partialTicks);

                if (showSettings) {
                    float compY = y + defaultHeight + 10;
                    for (Component component : settingsComponents) {
                        component.setX(x + width - 30 - component.getWidth());
                        component.setY(compY);
                        component.drawComponent(mouseX, mouseY, partialTicks);
                        fontRenderer.drawString(settings.get(component), x + 13, component.getY(), Color.BLACK);
                        compY += component.getHeight() + 5;
                    }
                }
            }

            @Override
            public void keyTyped(char typedChar, int keyCode) {
                if (showSettings)
                    this.settingsComponents.forEach(component -> component.keyTyped(typedChar, keyCode));
            }

            @Override
            public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
                this.toggleButton.mouseClicked(mouseX, mouseY, mouseButton);
                if (showSettings)
                    this.settingsComponents.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
            }

            @Override
            public void mouseReleased(int mouseX, int mouseY, int state) {
                if (showSettings)
                    this.settingsComponents.forEach(component -> component.mouseReleased(mouseX, mouseY, state));
                if ((mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + 24) && !toggleButton.isHovered(mouseX, mouseY) && state == 0) {
                    showSettings = !showSettings;
                    float settingsHeight = 0;

                    for (Component component : settingsComponents) {
                        Component last = settingsComponents.get(settingsComponents.size() - 1);
                        settingsHeight += last != component ? component.getHeight() + 5 : component.getHeight() + 15;
                    }

                    if (showSettings) {
                        height = defaultHeight + settingsHeight;
                        GuiModMenu.this.list.getScrollbar().content += settingsHeight;
                    } else {
                        height = defaultHeight;
                        GuiModMenu.this.list.getScrollbar().content -= settingsHeight;
                    }
                }
            }

            @Override
            public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
                if (showSettings)
                    this.settingsComponents.forEach(component -> component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick));
            }
        }
    }
}
