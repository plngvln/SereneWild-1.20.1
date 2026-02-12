package net.frozenblock.serenewild.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.serenewild.util.SnowyBlockUtilsCompat;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import sereneseasons.season.RandomUpdateHandler;

@Pseudo
@Mixin(RandomUpdateHandler.class)
public class RandomUpdateHandlerMixin {

	@WrapOperation(
		method = "meltInChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",
			ordinal = 0
		)
	)
	private static Block sereneWild$fixSnowloggedMelting(
		BlockState blockState,
		Operation<Block> original,
		@Local(argsOnly = false) ServerLevel serverLevel,
		@Local(ordinal = 0, argsOnly = false) BlockPos topAirPos
	) {
		if (SnowloggingUtils.isSnowlogged(blockState)) {
			BlockState nonSnowy = SnowyBlockUtilsCompat.getNonSnowyEquivalent(blockState);
			if (nonSnowy != null) {
				serverLevel.setBlockAndUpdate(topAirPos, SnowloggingUtils.getStateWithoutSnow(nonSnowy));
			} else {
				SnowyBlockUtilsCompat.replaceWithNonSnowyEquivalent(serverLevel, blockState, topAirPos);
			}
		}
		return original.call(blockState);
	}
}
