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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import global.oskar.gottagofast.GottaGoFast;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;


@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    public int movePacketsCount;

    @Shadow
    public int lastTickMovePacketsCount;

    @Unique
    private double gottagofast$getNewLimit(Vec3d velocity, Object dx, Object dy, Object dz) {
        assert double.class.isInstance(dx);
        assert double.class.isInstance(dy);
        assert double.class.isInstance(dz);

        return ((double) dx * (double) dx
            + (double) dy * (double) dy
            + (double) dz * (double) dz) - velocity.lengthSquared();
    }

    @ModifyConstant(method = "onPlayerMove", constant = @Constant(floatValue = 100.0F))
    private float gottagofast$playerLimit(float speed) {
        return GottaGoFast.CONFIG.playerLimit();
    }

    @ModifyConstant(method = "onPlayerMove", constant = @Constant(floatValue = 300.0F))
    private float gottagofast$playerFallFlyingLimit(float speed) {
        return GottaGoFast.CONFIG.playerFallFlyingLimit();
    }

    @WrapOperation(
        method = "onPlayerMove",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"
        )
    )
    private void gottagofast$customPlayerMoveLog(Logger _instance, String _s, Object[] args, Operation<Void> _original) {
        assert args.length == 4;
        assert args[0] instanceof String;

        if (!GottaGoFast.CONFIG.hideConsoleWarnings()) {
            final var limit = gottagofast$getNewLimit(this.player.getVelocity(), args[1], args[2], args[3])
                / (this.movePacketsCount - this.lastTickMovePacketsCount);

            GottaGoFast.logger.warn(
                "[GottaGoFast] {} moved too quickly! Moved by {},{},{} blocks in x,y,z direction. If you wish to increase the limit to exclude this, increase the limit to {}",
                args[0],
                args[1],
                args[2],
                args[3],
                limit
            );
        }
    }

    @ModifyConstant(method = "onVehicleMove", constant = @Constant(doubleValue = 100.0F))
    private double gottagofast$vehicleLimit(double speed) {
        return GottaGoFast.CONFIG.vehicleLimit();
    }

    @WrapOperation(
        method = "onVehicleMove",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V"
        )
    )
    private void gottagofast$custoVehicleMovemLog(Logger _instance, String _s, Object[] args, Operation<Void> _original) {
        assert args.length == 5;
        assert args[0] instanceof String;
        assert args[1] instanceof String;

        if (!GottaGoFast.CONFIG.hideConsoleWarnings()) {
            final var limit = gottagofast$getNewLimit(this.player.getRootVehicle().getVelocity(), args[2], args[3], args[4]);

            GottaGoFast.logger.warn(
                "[GottaGoFast] {} (vehicle of {}) moved too quickly! Moved by {},{},{} blocks in x,y,z direction. If you wish to increase the limit to exclude this, increase the limit to {}",
                args[0],
                args[1],
                args[2],
                args[3],
                args[4],
                limit
            );
        }
    }
}
