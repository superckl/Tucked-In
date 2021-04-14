package me.superckl.tuckedin.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import me.superckl.tuckedin.TuckedIn;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements IContainerListener{

	public ServerPlayerEntityMixin(final World level, final BlockPos pos, final float rotation,
			final GameProfile profile) {
		super(level, pos, rotation, profile);
	}

	//Inject our hook into the method that checks bed range
	@Inject(method = "bedInRange(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;)Z", at = @At("HEAD"), cancellable = true)
	private void checkBedDistance(final BlockPos pos, final Direction dir, final CallbackInfoReturnable<Boolean> ci) {
		if(TuckedIn.isBedInRange(this, pos, dir))
			ci.setReturnValue(true);
	}

}
