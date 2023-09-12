package net.luconia.boykisser.ui.component.impl.button;

import net.luconia.boykisser.ui.component.Component;
import net.luconia.boykisser.ui.component.impl.ActionHandler;

public class InvisibleButton extends Component {

    protected final ActionHandler handler;

    public InvisibleButton(float x, float y, float width, float height, ActionHandler handler) {
        super(x, y, width, height);
        this.handler = handler;
    }

    public InvisibleButton(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.handler = null;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY, float partialTicks) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 0 && handler != null) {
            handler.handle(this);
        }
    }
}
