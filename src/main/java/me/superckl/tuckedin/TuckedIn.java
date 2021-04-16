package me.superckl.tuckedin;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLNetworkConstants;

@Mod(TuckedIn.MOD_ID)
public class TuckedIn {

	public static final String MOD_ID = "tuckedin";

	public TuckedIn() {
		LogHelper.setLogger(LogManager.getFormatterLogger(TuckedIn.MOD_ID));
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}

	/**
	 * Hook for checking bed distance based on distance from player to point of interaction.
	 * Vanilla checks that the player's distance from the bottom-center of either bed block
	 * is less than a certain value in each dimension. This hook instead checks the player's
	 * distance from where they interacted with the block via a raytrace across the distance of
	 * the player's reach.
	 * @param player The player interacting with the bed.
	 * @param pos The position of the bed.
	 * @param dir The direction of the attached second bed block.
	 * @return Returns true if the bed is within the player's reach distance. False if the check failed and
	 * logic should default to vanilla.
	 */
	public static boolean isBedInRange(final PlayerEntity player, final BlockPos pos, final Direction dir) {
		//If the direction is null, we can't do a proper check. Default to vanilla.
		if(dir == null)
			return false;
		//Perform a raytrace with distance equal to the player's reach
		final RayTraceResult trace =  player.pick(player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()), 1, false);
		//If it is a block, check that the result position matches the bed position. If so, the bed is within reach.
		if(trace.getType() == Type.BLOCK) {
			final BlockRayTraceResult result = (BlockRayTraceResult) trace;
			return result.getBlockPos().equals(pos) || result.getBlockPos().equals(pos.relative(dir.getOpposite()));
		}
		//It doesn't seem like the bed is within reach distance, or the player is using some form of interaction proxy
		return false;
	}

}
