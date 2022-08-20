package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.animation.impl.RollAnimator;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.common.capability.impl.Stamina;
import com.alrex.parcool.utilities.BufferUtil;
import com.alrex.parcool.utilities.VectorUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;

import java.nio.ByteBuffer;

public class Roll extends Action {
	private boolean start = false;
	private boolean rolling = false;
	private int rollingTick = 0;

	@Override
	public void onTick(Player player, Parkourability parkourability, Stamina stamina) {
		if (rolling) {
			rollingTick++;
		} else {
			rollingTick = 0;
		}
	}

	private int creativeCoolTime = 0;

	@OnlyIn(Dist.CLIENT)
	@Override
	public void onClientTick(Player player, Parkourability parkourability, Stamina stamina) {
		if (player.isLocalPlayer()) {
			if (
					KeyBindings.getKeyBreakfall().isDown()
							&& KeyBindings.getKeyForward().isDown()
							&& ParCoolConfig.CONFIG_CLIENT.enableRollWhenCreative.get()
							&& player.isCreative()
							&& parkourability.getAdditionalProperties().getLandingTick() <= 1
							&& player.isOnGround()
							&& !rolling
							&& creativeCoolTime == 0
			) {
				start = true;
				creativeCoolTime = 20;
			}
			if (creativeCoolTime > 0) creativeCoolTime--;
			if (rollingTick >= getRollMaxTick()) rolling = false;
		}
		if ((rolling && rollingTick <= 1) || start) {
			Animation animation = Animation.get(player);
			if (animation != null) animation.setAnimator(new RollAnimator());
		}
		if (start) {
			start = false;
			rolling = true;
			if (player.isLocalPlayer()) {
				Vec3 vec = VectorUtil.fromYawDegree(player.yBodyRot);
				player.setDeltaMovement(vec.x(), 0, vec.z());
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void onRender(TickEvent.RenderTickEvent event, Player player, Parkourability parkourability) {
	}


	public void startRoll(Player player) {
		start = true;
	}

	@Override
	public void saveState(ByteBuffer buffer) {
		BufferUtil.wrap(buffer).putBoolean(start).putBoolean(rolling);
	}

	@Override
	public void restoreState(ByteBuffer buffer) {
		start = BufferUtil.getBoolean(buffer);
		rolling = BufferUtil.getBoolean(buffer);
	}

	public int getRollingTick() {
		return rollingTick;
	}

	public boolean isRolling() {
		return rolling;
	}

	public int getRollMaxTick() {
		return 9;
	}
}
