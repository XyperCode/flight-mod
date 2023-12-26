package io.github.xypercode.mods.flight;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class FlightMod {
	private static boolean wasJumpDown = false;
	private static long lastJumpTime = System.currentTimeMillis();
	private static int jumpCount = 1;

	public static final String MOD_ID = "xyper_flight";

    public static void init() {

    }

    public static void postInit() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, id("flight"), (buf, context) -> {
            boolean flight = buf.readBoolean();
            Player player = context.getPlayer();
            if (player != null) {
                player.getAbilities().flying = !player.getAbilities().mayfly;
                player.getAbilities().mayfly = !player.getAbilities().mayfly;
                boolean mayfly = player.getAbilities().mayfly;
                System.out.println("mayfly = " + mayfly);
                player.onUpdateAbilities();
            }
        });
    }

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void onPlayerTick(Player playerMixin) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.options.keyJump.isDown() && !wasJumpDown) {
            wasJumpDown = true;

            if (lastJumpTime + 500 > System.currentTimeMillis()) {
                System.out.println("Hello");
                jumpCount++;
            } else {
                jumpCount = 1;
            }

            lastJumpTime = System.currentTimeMillis();

            if (jumpCount >= 2) {
                jumpCount = 0;
                FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(new UnpooledDirectByteBuf(ByteBufAllocator.DEFAULT.buffer().alloc(), 1, 65536));
                friendlyByteBuf.writeBoolean(!playerMixin.getAbilities().mayfly);

                NetworkManager.sendToServer(FlightMod.id("flight"), friendlyByteBuf);
            }
        } else if (!minecraft.options.keyJump.isDown()) {
            wasJumpDown = false;
        }
    }
}
