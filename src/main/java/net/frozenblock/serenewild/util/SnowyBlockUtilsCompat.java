/*
 * Copyright 2025 FrozenBlock
 * This file is part of Serene Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.serenewild.util;

import net.frozenblock.wilderwild.block.impl.SnowloggingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Compatibility layer for SnowyBlockUtils on 1.20.1 where snowlogging uses SNOW_LAYERS on the same block.
 */
public final class SnowyBlockUtilsCompat {

	private SnowyBlockUtilsCompat() {
	}

	public static void replaceWithWorldgenSnowyEquivalent(@NotNull ServerLevel level, @NotNull BlockState state, @NotNull BlockPos pos) {
	}

	@NotNull
	public static BlockState getWorldgenSnowyEquivalent(@NotNull BlockState state) {
		return state;
	}

	public static void replaceWithNonSnowyEquivalent(@NotNull ServerLevel level, @NotNull BlockState state, @NotNull BlockPos pos) {
		BlockState withoutSnow = SnowloggingUtils.getStateWithoutSnow(state);
		if (withoutSnow != null) {
			level.setBlockAndUpdate(pos, withoutSnow);
		}
	}

	@Nullable
	public static BlockState getNonSnowyEquivalent(@NotNull BlockState state) {
		return SnowloggingUtils.getStateWithoutSnow(state);
	}

	@NotNull
	public static BlockState getSnowloggedState(@NotNull BlockState blockState, @NotNull BlockState snowState) {
		if (SnowloggingUtils.supportsSnowlogging(blockState) && snowState.hasProperty(BlockStateProperties.LAYERS)) {
			return blockState.setValue(SnowloggingUtils.SNOW_LAYERS, snowState.getValue(BlockStateProperties.LAYERS));
		}
		return blockState;
	}
}
