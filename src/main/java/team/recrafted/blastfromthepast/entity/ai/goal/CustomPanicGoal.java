package team.recrafted.blastfromthepast.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

import java.util.function.Predicate;

public class CustomPanicGoal<T extends PathfinderMob> extends PanicGoal {
    private final T typedMob;
    private final Predicate<T> shouldPanic;
    private final Predicate<T> shouldAttackSourcePanic;

    public CustomPanicGoal(T mob, double speedModifier, Predicate<T> shouldAttackSourcePanic) {
        this(mob, p->true, speedModifier, shouldAttackSourcePanic);
    }

    public CustomPanicGoal(T mob, Predicate<T> shouldPanic, double speedModifier) {
        this(mob, shouldPanic, speedModifier, p -> true);
    }

    public CustomPanicGoal(T mob, Predicate<T> shouldPanic, double speedModifier, Predicate<T> shouldAttackSourcePanic) {
        super(mob, speedModifier);
        this.typedMob = mob;
        this.shouldPanic = shouldPanic;
        this.shouldAttackSourcePanic=shouldAttackSourcePanic;
    }

    @Override
    protected boolean shouldPanic() {
        return this.shouldPanic.test(this.typedMob) && this.shouldAttackSourcePanic.test(this.typedMob);
    }

    public static boolean isMobDamage(Mob mob) {
        return mob.getLastHurtByMob() != null;
    }

    public static boolean isEnvironmentDamage(Mob mob) {
        return mob.isFreezing() || mob.isOnFire();
    }

    public static boolean isAllDamage(Mob mob){
        return isMobDamage(mob) || isEnvironmentDamage(mob);
    }
}
