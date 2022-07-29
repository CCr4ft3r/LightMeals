package hashmod.lightmeals.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ChorusSoupItem extends BasicFoodItem {
    public ChorusSoupItem() {
        super(Foods.CHORUS_SOUP);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        ItemStack item_stack = super.finishUsingItem(stack, worldIn, entityLiving);
        if (!worldIn.isClientSide) {
            double d0 = entityLiving.getX();
            double d1 = entityLiving.getY();
            double d2 = entityLiving.getZ();

            for (int i = 0; i < 16; ++i) {
                double d3 = entityLiving.getX() + (entityLiving.getRandom().nextDouble() - 0.5D) * 16.0D;
                double d4 = Mth.clamp(entityLiving.getY() + (double) (entityLiving.getRandom().nextInt(16) - 8), (double) worldIn.getMinBuildHeight(), (double) (worldIn.getMinBuildHeight() + ((ServerLevel) worldIn).getLogicalHeight() - 1));
                double d5 = entityLiving.getZ() + (entityLiving.getRandom().nextDouble() - 0.5D) * 16.0D;
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }

                net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(entityLiving, d3, d4, d5);
                if (event.isCanceled()) return item_stack;
                if (entityLiving.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
                    SoundEvent soundevent = entityLiving instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    worldIn.playSound((Player) null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    entityLiving.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }

            if (entityLiving instanceof Player) {
                ((Player) entityLiving).getCooldowns().addCooldown(this, 20);
            }
        }
        if (stack.isEmpty()) {
            return new ItemStack(Items.BOWL);
        } else {
            if (entityLiving instanceof Player player && !((Player) entityLiving).getAbilities().instabuild) {
                ItemStack itemstack = new ItemStack(Items.BOWL);
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }
            return item_stack;
        }
    }
}
