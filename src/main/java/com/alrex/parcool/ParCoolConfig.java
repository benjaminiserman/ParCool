package com.alrex.parcool;

import com.alrex.parcool.client.hud.Position;
import net.minecraftforge.common.ForgeConfigSpec;

public class ParCoolConfig {
	private static final ForgeConfigSpec.Builder C_BUILDER = new ForgeConfigSpec.Builder();
	private static final ForgeConfigSpec.Builder S_BUILDER = new ForgeConfigSpec.Builder();

	public static final Client CONFIG_CLIENT = new Client(C_BUILDER);
	public static final Server CONFIG_SERVER = new Server(S_BUILDER);

	public static class Client {
		public final ForgeConfigSpec.BooleanValue canCatLeap;
		public final ForgeConfigSpec.BooleanValue canCrawl;
		public final ForgeConfigSpec.BooleanValue canDodge;
		public final ForgeConfigSpec.BooleanValue canFastRunning;
		public final ForgeConfigSpec.BooleanValue canFrontFlip;
		public final ForgeConfigSpec.BooleanValue canGrabCliff;
		public final ForgeConfigSpec.BooleanValue canRoll;
		public final ForgeConfigSpec.BooleanValue canVault;
		public final ForgeConfigSpec.BooleanValue canWallJump;
		public final ForgeConfigSpec.BooleanValue infiniteStamina;
		public final ForgeConfigSpec.IntValue maxStamina;
		public final ForgeConfigSpec.BooleanValue ParCoolActivation;
		public final ForgeConfigSpec.BooleanValue hideStaminaHUD;
		public final ForgeConfigSpec.EnumValue<Position.Horizontal> alignHorizontalStaminaHUD;
		public final ForgeConfigSpec.EnumValue<Position.Vertical> alignVerticalStaminaHUD;
		public final ForgeConfigSpec.IntValue marginHorizontalStaminaHUD;
		public final ForgeConfigSpec.IntValue marginVerticalStaminaHUD;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("Enable ParCool Actions").push("Possibility of Actions");
			{
				canCatLeap = builder.comment("Possibility to CatLeap").define("canCatLeap", true);
				canCrawl = builder.comment("Possibility to Crawl").define("canCrawl", true);
				canFrontFlip = builder.comment("Possibility to FrontFlip").define("canFrontFlip", true);
				canDodge = builder.comment("Possibility to Dodge").define("canDodge", true);
				canFastRunning = builder.comment("Possibility to FastRunning").define("canFastRunning", true);
				canGrabCliff = builder.comment("Possibility to GrabCliff").define("canGrabCliff", true);
				canRoll = builder.comment("Possibility to Roll").define("canRoll", true);
				canVault = builder.comment("Possibility to Vault").define("canVault", true);
				canWallJump = builder.comment("Possibility to WallJump").define("canWallJump", true);
			}
			builder.pop();
			builder.comment("Values").push("Modify Values");
			{
				maxStamina = builder.comment("Max Value of Stamina").defineInRange("maxStamina", 1000, 10, 5000);
			}
			builder.pop();
			builder.comment("HUD").push("Stamina HUD configuration");
			{
				hideStaminaHUD = builder.comment("hide stamina HUD").define("hideS_HUD", false);
				alignHorizontalStaminaHUD = builder.comment("horizontal alignment").defineEnum("align_H_S_HUD", Position.Horizontal.Right);
				alignVerticalStaminaHUD = builder.comment("vertical alignment").defineEnum("align_V_S_HUD", Position.Vertical.Bottom);
				marginHorizontalStaminaHUD = builder.comment("horizontal margin").defineInRange("margin_H_S_HUD", 3, 0, 100);
				marginVerticalStaminaHUD = builder.comment("vertical margin").defineInRange("margin_V_S_HUD", 3, 0, 100);
			}
			builder.pop();
			builder.comment("Others").push("Other configuration");
			{
				infiniteStamina = builder.comment("Infinite Stamina").define("infiniteStamina", false);
			}
			builder.pop();
			builder.comment("About ParCool").push("ParCool");
			{
				ParCoolActivation = builder.comment("ParCool is Active").define("ParCool_Activation", true);
			}
			builder.pop();
		}
	}

	public static class Server {
		public final ForgeConfigSpec.BooleanValue allowInfiniteStamina;
		public final ForgeConfigSpec.BooleanValue allowCatLeap;
		public final ForgeConfigSpec.BooleanValue allowCrawl;
		public final ForgeConfigSpec.BooleanValue allowDodge;
		public final ForgeConfigSpec.BooleanValue allowFastRunning;
		public final ForgeConfigSpec.BooleanValue allowGrabCliff;
		public final ForgeConfigSpec.BooleanValue allowRoll;
		public final ForgeConfigSpec.BooleanValue allowVault;
		public final ForgeConfigSpec.BooleanValue allowWallJump;

		Server(ForgeConfigSpec.Builder builder) {
			builder.comment("Action Permissions").push("Permissions");
			{
				allowCatLeap = builder.comment("allow CatLeap").define("allowCatLeap", true);
				allowCrawl = builder.comment("allow Crawl").define("allowCrawl", true);
				allowDodge = builder.comment("allow Dodge").define("allowDodge", true);
				allowFastRunning = builder.comment("allow FastRunning").define("allowFastRunning", true);
				allowGrabCliff = builder.comment("allow GrabCliff").define("allowGrabCliff", true);
				allowRoll = builder.comment("allow Roll").define("allowRoll", true);
				allowVault = builder.comment("allow Vault").define("allowVault", true);
				allowWallJump = builder.comment("allowWallJump").define("allow WallJump", true);
			}
			builder.pop();
			builder.comment("Others").push("Other Configuration");
			{
				allowInfiniteStamina = builder.comment("allow Infinite Stamina").define("infiniteStamina", false);
			}
			builder.pop();
		}
	}

	public static final ForgeConfigSpec CLIENT_SPEC = C_BUILDER.build();
	public static final ForgeConfigSpec SERVER_SPEC = S_BUILDER.build();
}
