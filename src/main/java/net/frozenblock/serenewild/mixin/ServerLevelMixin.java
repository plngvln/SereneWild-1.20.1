package net.frozenblock.serenewild.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.frozenblock.serenewild.util.SnowyBlockUtilsCompat;
import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerLevel.class, priority = 900)
public class ServerLevelMixin {

	@WrapOperation(
		method = "tickChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
			ordinal = 1
		)
	)
	private boolean sereneWild$tickPrecipitation(
		ServerLevel instance,
		BlockPos pos,
		BlockState state,
		Operation<Boolean> original
	) {
		boolean success = original.call(instance, pos, state);

		BlockState currentState = instance.getBlockState(pos);

		if (SnowloggingUtils.isSnowlogged(currentState)) {
			SnowyBlockUtilsCompat.replaceWithWorldgenSnowyEquivalent(instance, currentState, pos);
		}

		return success;
	}
}
