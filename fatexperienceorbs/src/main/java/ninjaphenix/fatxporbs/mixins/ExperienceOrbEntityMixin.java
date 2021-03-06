package ninjaphenix.fatxporbs.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin extends Entity
{
    @Shadow public int orbAge;
    @Shadow private int amount;

    public ExperienceOrbEntityMixin(final EntityType<?> type, final World world) { super(type, world); }

    @Inject(at = @At("TAIL"), method = "tick", cancellable = true)
    private void tick(CallbackInfo info)
    {
        if (world.isClient) { return; }
        if (world.getTime() % 5 == 0)
        {
            final BlockPos pos = getBlockPos();
            final List<ExperienceOrbEntity> entities = world.getEntitiesByClass(ExperienceOrbEntity.class,
                    new Box(pos.west(2).north(2).up(2), pos.east(2).south(2).down(2)), e -> e.isAlive() && !e.getUuid().equals(uuid));
            if (entities.isEmpty()) { return; }
            final ExperienceOrbEntity orb = entities.get(0);
            amount += orb.getExperienceAmount();
            orb.remove();
            orbAge = 0;
        }
    }
}
