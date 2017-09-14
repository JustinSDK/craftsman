package cc.openhome.forgemod.command;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Walker {
    private final EnumFacing facing;
    private final BlockPos pos;

    private static class Stepper {
        final int x;
        final int z;

        Stepper(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    private static Map<EnumFacing, Stepper> frontSteppers = new HashMap<>();
    private static Map<EnumFacing, Stepper> rightSteppers = new HashMap<>();

    static {
        frontSteppers.put(EnumFacing.EAST, new Stepper(1, 0));
        frontSteppers.put(EnumFacing.WEST, new Stepper(-1, 0));
        frontSteppers.put(EnumFacing.SOUTH, new Stepper(0, 1));
        frontSteppers.put(EnumFacing.NORTH, new Stepper(0, -1));

        rightSteppers.put(EnumFacing.EAST, new Stepper(0, 1));
        rightSteppers.put(EnumFacing.WEST, new Stepper(0, -1));
        rightSteppers.put(EnumFacing.SOUTH, new Stepper(-1, 0));
        rightSteppers.put(EnumFacing.NORTH, new Stepper(1, 0));
    }

    public Walker(EnumFacing facing, BlockPos pos) {
        this.facing = facing;
        this.pos = pos;
    }

    public Walker forward(int i) {
        Stepper stepper = frontSteppers.get(facing);
        return new Walker(facing, pos.add(stepper.x * i, 0, stepper.z * i));
    }

    public Walker right(int i) {
        Stepper stepper = rightSteppers.get(facing);
        return new Walker(facing, pos.add(stepper.x * i, 0, stepper.z * i));
    }

    public Walker up(int i) {
        return new Walker(facing, pos.add(0, i, 0));
    }

    public BlockPos getBlockPos() {
        return pos;
    }

    public static BlockPos forward(EnumFacing facing, BlockPos pos, int i) {
        Stepper stepper = frontSteppers.get(facing);
        return pos.add(stepper.x * i, 0, stepper.z * i);
    }

    public static BlockPos right(EnumFacing facing, BlockPos pos, int i) {
        Stepper stepper = rightSteppers.get(facing);
        return pos.add(stepper.x * i, 0, stepper.z * i);
    }
}
