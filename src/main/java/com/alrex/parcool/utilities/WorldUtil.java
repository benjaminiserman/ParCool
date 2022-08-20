package com.alrex.parcool.utilities;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class WorldUtil {

	@Nullable
	public static Vec3 getWall(LivingEntity entity) {
		final double d = 0.3;
		double distance = entity.getBbWidth() / 2;
		double wallX = 0;
		double wallZ = 0;
		byte wallNumX = 0;
		byte wallNumZ = 0;
		Vec3 pos = entity.position();

		AABB baseBox = new AABB(
				pos.x() - d,
				pos.y(),
				pos.z() - d,
				pos.x() + d,
				pos.y() + entity.getBbHeight(),
				pos.z() + d
		);

		if (!entity.level.noCollision(baseBox.expandTowards(distance, 0, 0))) {
			wallX++;
			wallNumX++;
		}
		if (!entity.level.noCollision(baseBox.expandTowards(-distance, 0, 0))) {
			wallX--;
			wallNumX++;
		}
		if (!entity.level.noCollision(baseBox.expandTowards(0, 0, distance))) {
			wallZ++;
			wallNumZ++;
		}
		if (!entity.level.noCollision(baseBox.expandTowards(0, 0, -distance))) {
			wallZ--;
			wallNumZ++;
		}
		if (wallNumX == 2 || wallNumZ == 2 || (wallNumX == 0 && wallNumZ == 0)) return null;

		return new Vec3(wallX, 0, wallZ);
	}

	@Nullable
	public static Vec3 getVaultableStep(LivingEntity entity) {
		final double d = 0.3;
		Level world = entity.level;
		double distance = entity.getBbWidth() / 2;
		double baseLine = 1.55;
		double stepX = 0;
		double stepZ = 0;
		Vec3 pos = entity.position();

		AABB baseBoxSide = new AABB(
				pos.x() - d,
				pos.y(),
				pos.z() - d,
				pos.x() + d,
				pos.y() + baseLine,
				pos.z() + d
		);
		AABB baseBoxTop = new AABB(
				pos.x() - d,
				pos.y() + baseLine,
				pos.z() - d,
				pos.x() + d,
				pos.y() + entity.getBbHeight(),
				pos.z() + d
		);
		if (!world.noCollision(baseBoxSide.expandTowards(distance, 0, 0)) && world.noCollision(baseBoxTop.expandTowards((distance + 1.8), 0, 0))) {
			stepX++;
		}
		if (!world.noCollision(baseBoxSide.expandTowards(-distance, 0, 0)) && world.noCollision(baseBoxTop.expandTowards(-(distance + 1.8), 0, 0))) {
			stepX--;
		}
		if (!world.noCollision(baseBoxSide.expandTowards(0, 0, distance)) && world.noCollision(baseBoxTop.expandTowards(0, 0, (distance + 1.8)))) {
			stepZ++;
		}
		if (!world.noCollision(baseBoxSide.expandTowards(0, 0, -distance)) && world.noCollision(baseBoxTop.expandTowards(0, 0, -(distance + 1.8)))) {
			stepZ--;
		}
		if (stepX == 0 && stepZ == 0) return null;

		return new Vec3(stepX, 0, stepZ);
	}

	public static double getWallHeight(LivingEntity entity) {
		Vec3 wall = getWall(entity);
		if (wall == null) return 0;
		Level world = entity.level;
		final double v = 0.1;
		final double d = 0.3;
		int loopNum = (int) Math.round(entity.getBbHeight() / v);
		Vec3 pos = entity.position();
		double x1 = pos.x() + d + (wall.x() > 0 ? 1 : 0);
		double y1 = pos.y();
		double z1 = pos.z() + d + (wall.z() > 0 ? 1 : 0);
		double x2 = pos.x() - d + (wall.x() < 0 ? -1 : 0);
		double z2 = pos.z() - d + (wall.z() < 0 ? -1 : 0);
		boolean canReturn = false;
		for (int i = 0; i < loopNum; i++) {
			AABB box = new AABB(
					x1, y1 + v * i, z1, x2, y1 + v * (i + 1), z2
			);

			if (!world.noCollision(box)) {
				canReturn = true;
			} else {
				if (canReturn) return v * i;
			}
		}
		return entity.getBbHeight();
	}

	public static boolean existsGrabbableWall(LivingEntity entity) {
		final double d = 0.3;
		Level world = entity.level;
		double distance = entity.getBbWidth() / 2;
		double baseLine1 = entity.getEyeHeight() + (entity.getBbHeight() - entity.getEyeHeight()) / 2;
		double baseLine2 = entity.getBbHeight() + (entity.getBbHeight() - entity.getEyeHeight()) / 2;
		return existsGrabbableWall(entity, distance, baseLine1) || existsGrabbableWall(entity, distance, baseLine2);
	}

	private static boolean existsGrabbableWall(LivingEntity entity, double distance, double baseLine) {
		final double d = 0.3;
		Level world = entity.level;
		Vec3 pos = entity.position();
		AABB baseBoxSide = new AABB(
				pos.x() - d,
				pos.y() + baseLine - entity.getBbHeight() / 6,
				pos.z() - d,
				pos.x() + d,
				pos.y() + baseLine,
				pos.z() + d
		);
		AABB baseBoxTop = new AABB(
				pos.x() - d,
				pos.y() + baseLine,
				pos.z() - d,
				pos.x() + d,
				pos.y() + entity.getBbHeight(),
				pos.z() + d
		);

		if (!world.noCollision(baseBoxSide.expandTowards(distance, 0, 0)) && world.noCollision(baseBoxTop.expandTowards(distance, 0, 0)))
			return true;
		if (!world.noCollision(baseBoxSide.expandTowards(-distance, 0, 0)) && world.noCollision(baseBoxTop.expandTowards(-distance, 0, 0)))
			return true;
		if (!world.noCollision(baseBoxSide.expandTowards(0, 0, distance)) && world.noCollision(baseBoxTop.expandTowards(0, 0, distance)))
			return true;
		if (!world.noCollision(baseBoxSide.expandTowards(0, 0, -distance)) && world.noCollision(baseBoxTop.expandTowards(0, 0, -distance)))
			return true;

		return false;
	}
}
