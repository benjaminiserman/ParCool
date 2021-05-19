package com.alrex.parcool.common.network;

import com.alrex.parcool.ParCool;
import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.constants.ActionsEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class ShowActionPossibilityMessage {
	ActionsEnum action = null;

	private void encode(PacketBuffer packet) {
		packet.writeBoolean(action != null);
		if (action != null) packet.writeString(action.name());
	}

	private static ShowActionPossibilityMessage decode(PacketBuffer packet) {
		ShowActionPossibilityMessage message = new ShowActionPossibilityMessage();
		try {
			if (packet.readBoolean()) message.action = ActionsEnum.valueOf(packet.readString());
		} catch (IllegalArgumentException e) {
			message.action = null;
		}

		return message;
	}

	private void handle(Supplier<NetworkEvent.Context> contextSupplier) {
		contextSupplier.get().enqueueWork(() -> {
			ClientPlayerEntity player = Minecraft.getInstance().player;
			if (player == null) return;
			player.sendStatusMessage(ITextComponent.func_241827_a_(getText(action)), false);
		});
		contextSupplier.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	private static String getText(ActionsEnum action) {
		ParCoolConfig.Client c = ParCoolConfig.CONFIG_CLIENT;
		if (action != null) switch (action) {
			case CatLeap:
				return action.name() + " : " + c.canCatLeap.get().toString();
			case Crawl:
				return action.name() + " : " + c.canCrawl.get().toString();
			case Dodge:
				return action.name() + " : " + c.canDodge.get().toString();
			case FastRunning:
				return action.name() + " : " + c.canFastRunning.get().toString();
			case GrabCliff:
				return action.name() + " : " + c.canGrabCliff.get().toString();
			case Roll:
				return action.name() + " : " + c.canRoll.get().toString();
			case Vault:
				return action.name() + " : " + c.canVault.get().toString();
			case WallJump:
				return action.name() + " : " + c.canWallJump.get().toString();
		}
		StringBuilder builder = new StringBuilder();
		builder
				.append(ActionsEnum.CatLeap.name()).append(" : ").append(c.canCatLeap.get().toString()).append('\n')
				.append(ActionsEnum.Crawl.name()).append(" : ").append(c.canCrawl.get().toString()).append('\n')
				.append(ActionsEnum.Dodge.name()).append(" : ").append(c.canDodge.get().toString()).append('\n')
				.append(ActionsEnum.FastRunning.name()).append(" : ").append(c.canFastRunning.get().toString()).append('\n')
				.append(ActionsEnum.GrabCliff.name()).append(" : ").append(c.canGrabCliff.get().toString()).append('\n')
				.append(ActionsEnum.Roll.name()).append(" : ").append(c.canRoll.get().toString()).append('\n')
				.append(ActionsEnum.Vault.name()).append(" : ").append(c.canVault.get().toString()).append('\n')
				.append(ActionsEnum.WallJump.name()).append(" : ").append(c.canWallJump.get().toString());
		return builder.toString();
	}

	public static void send(ServerPlayerEntity player, ActionsEnum action) {
		ShowActionPossibilityMessage message = new ShowActionPossibilityMessage();
		message.action = action;
		ParCool.CHANNEL_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class MessageRegistry {
		private static final int ID = 10;

		@SubscribeEvent
		public static void register(FMLCommonSetupEvent event) {
			ParCool.CHANNEL_INSTANCE.registerMessage(
					ID,
					ShowActionPossibilityMessage.class,
					ShowActionPossibilityMessage::encode,
					ShowActionPossibilityMessage::decode,
					ShowActionPossibilityMessage::handle
			);
		}
	}
}