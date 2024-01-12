package io.github.xypercode.mods.flight.mixin;

import com.mojang.authlib.GameProfile;
import dev.architectury.networking.NetworkManager;
import io.github.xypercode.mods.flight.FlightMod;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class PlayerMixin extends Player {
    @Shadow @Final public static Logger LOGGER;

    protected PlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(level, blockPos, f, gameProfile, profilePublicKey);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onPlayerTick(CallbackInfo ci) {
        FlightMod.onPlayerTick(this);

        if (isOnGround() && level.isClientSide && getAbilities().mayfly) {
            FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(new UnpooledDirectByteBuf(ByteBufAllocator.DEFAULT.buffer().alloc(), 1, 65536));
            friendlyByteBuf.writeBoolean(false);

            NetworkManager.sendToServer(FlightMod.id("flight"), friendlyByteBuf);
        }
    }
}
