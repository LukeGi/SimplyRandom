package lhg.forgemods.simplyrandom.core;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Data storage for Tooltips... considering removal of this until it's implementation is more solid
 */
public class Tooltip
{
    private static final TranslationTextComponent SHIFT_MESSAGE = new TranslationTextComponent("simplyrandom.tooltip.pressShift");
    private static final TranslationTextComponent CTRL_MESSAGE = new TranslationTextComponent("simplyrandom.tooltip.pressCtrl");
    private Set<ITextComponent> basicLines;
    private Set<ITextComponent> shiftLines;
    private Set<ITextComponent> ctrlLines;

    private Tooltip(Set<ITextComponent> basicLines, @Nullable Set<ITextComponent> shiftLines, @Nullable Set<ITextComponent> ctrlLines)
    {
        this.basicLines = basicLines;
        this.shiftLines = shiftLines;
        this.ctrlLines = ctrlLines;
    }

    public void add(List<ITextComponent> tooltip)
    {
        if (basicLines != null)
        {
            tooltip.addAll(basicLines);
        }
        if (shiftLines != null)
        {
            if (showShiftInfo())
            {
                tooltip.addAll(shiftLines);
            } else
            {
                tooltip.add(SHIFT_MESSAGE);
            }
        }
        if (ctrlLines != null)
        {
            if (showCtrlInfo())
            {
                tooltip.addAll(ctrlLines);
            } else
            {
                tooltip.add(CTRL_MESSAGE);
            }
        }
    }

    private boolean showShiftInfo()
    {
        return Screen.hasShiftDown();
    }

    private boolean showCtrlInfo()
    {
        return Screen.hasControlDown();
    }

    public static class Builder
    {
        private Set<ITextComponent> basicLines;
        private Set<ITextComponent> shiftLines;
        private Set<ITextComponent> ctrlLines;

        public Builder setBasicLines(Set<ITextComponent> basicLines)
        {
            this.basicLines = basicLines;
            return this;
        }

        public Builder setShiftLines(Set<ITextComponent> shiftLines)
        {
            this.shiftLines = shiftLines;
            return this;
        }

        public Builder setCtrlLines(Set<ITextComponent> ctrlLines)
        {
            this.ctrlLines = ctrlLines;
            return this;
        }

        public Tooltip createTooltip()
        {
            return new Tooltip(basicLines, shiftLines, ctrlLines);
        }
    }
}
