package com.alrex.parcool.common.event;

import com.alrex.parcool.common.action.impl.BreakfallReady;
import com.alrex.parcool.common.action.impl.Roll;
import com.alrex.parcool.common.action.impl.Tap;
import com.alrex.parcool.common.capability.Parkourability;
import com.alrex.parcool.common.network.StartBreakfallMessage;
import com.alrex.parcool.config.ParCoolConfig;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventPlayerFall {
	@SubscribeEvent
	public static void onDamage(LivingFallEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer))
			return;
		ServerPlayerEntity player = (ServerPlayer) event.getEntity();

		Parkourability parkourability = Parkourability.get(player);
		if (parkourability == null)
			return;

		if (parkourability.get(BreakfallReady.class).isDoing()
				&& (parkourability.getClientInfo().getPossibilityOf(Tap.class)
						|| parkourability.getClientInfo().getPossibilityOf(Roll.class))
				&& (ParCoolConfig.Client.Booleans.EnableJustTimeEffectOfBreakfall.get()
						&& parkourability.get(BreakfallReady.class).getDoingTick() < 5)) {
			boolean justTime = parkourability.get(BreakfallReady.class).getDoingTick() < 5;
			float distance = event.getDistance();
			if (distance > 2) StartBreakfallMessage.send(player, justTime);
			if (distance < 6) {
				event.setCanceled(true);
			} else {
				event.setDamageMultiplier(event.getDamageMultiplier() * 0.6f);
			}
		}
	}
}
