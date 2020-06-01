package luhegi.mods.simplyrandom.cobblegen;

import luhegi.mods.simplyrandom.basis.objects.BasisBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class CobbleGenBlock extends BasisBlock {
    public CobbleGenBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CobbleGenTile();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String tooltipKey = getTranslationKey() + (CobblestoneGenerator.getUseEnergy() ? "tooltip.requirement" : "tooltip.norequirement");
        ITextComponent tooltipText = new TranslationTextComponent(tooltipKey, CobblestoneGenerator.getEnergyPerCobble()).applyTextStyle(TextFormatting.YELLOW);
        if (!tooltipText.getString().equals(tooltipKey)) {
            tooltip.add(tooltipText);
        }
    }
}
