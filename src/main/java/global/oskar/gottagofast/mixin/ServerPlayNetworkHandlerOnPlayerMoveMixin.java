/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020-2021 TheRandomLabs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package global.oskar.gottagofast.mixin;

import global.oskar.gottagofast.GottaGoFast;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerOnPlayerMoveMixin {
    @Shadow
    private static double clampHorizontal(double d) { return 0; }

    @Shadow
    private static double clampVertical(double d) { return 0; }


    @Inject(
            method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler instance = ((ServerPlayNetworkHandler)(Object)this);

        double x = clampHorizontal(packet.getX(instance.player.getX()));
        double y = clampVertical(packet.getY(instance.player.getY()));
        double z = clampHorizontal(packet.getZ(instance.player.getZ()));

        double distanceXFromLastTick = x - instance.lastTickX;
        double distanceYFromLastTick = y - instance.lastTickY;
        double distanceZFromLastTick = z - instance.lastTickZ;

        double velocity = instance.player.getVelocity().lengthSquared();

        double distance = distanceXFromLastTick * distanceXFromLastTick
                + distanceYFromLastTick * distanceYFromLastTick
                + distanceZFromLastTick * distanceZFromLastTick;

        int packetCount = instance.movePacketsCount - instance.lastTickMovePacketsCount;
        float threshold = instance.player.isFallFlying() ? GottaGoFast.CONFIG.playerFallFlyingLimit() : GottaGoFast.CONFIG.playerLimit();

        if (distance - velocity > (double)(threshold * (float) packetCount) && !instance.isHost()) {
            if (!GottaGoFast.CONFIG.hideConsoleWarnings()) {
                GottaGoFast.logger.warn(
                        "[GottaGoFast] {} moved too quickly! Moved by {},{},{} blocks in x,y,z direction. If you wish to increase the limit to exclude this, increase the limit to {}",
                        instance.player.getName().getString(),
                        distanceXFromLastTick,
                        distanceYFromLastTick,
                        distanceZFromLastTick,
                        (distance - velocity) / packetCount
                );
            }

            instance.requestTeleport(instance.player.getX(), instance.player.getY(), instance.player.getZ(), instance.player.getYaw(), instance.player.getPitch());
            ci.cancel();
        }
    }
}
