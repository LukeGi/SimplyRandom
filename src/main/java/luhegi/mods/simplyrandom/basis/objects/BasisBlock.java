package luhegi.mods.simplyrandom.basis.objects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BasisBlock extends Block {
    public BasisBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        Optional.ofNullable(worldIn.getTileEntity(pos)).ifPresent(tile ->
                tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(inventory -> {
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        spawnAsEntity(worldIn, pos, inventory.extractItem(i, inventory.getSlotLimit(i), false));
                    }
                })
        );
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String tooltipKey = getTranslationKey() + ".tooltip";
        ITextComponent tooltipText = new TranslationTextComponent(tooltipKey).applyTextStyle(TextFormatting.GRAY);
        if (!tooltipText.getString().equals(tooltipKey)) {
            tooltip.add(tooltipText);
        }
    }
}
