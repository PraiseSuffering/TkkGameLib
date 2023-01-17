package com.twokktwo.tkklib.mapPiece.template;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;

public class BlockDate {
    public final BlockPos pos;
    public final IBlockState blockState;
    public final NBTTagCompound tileEntity;

    public BlockDate(BlockPos posIn, IBlockState stateIn, @Nullable NBTTagCompound compoundIn)
    {
        this.pos = posIn;
        this.blockState = stateIn;
        this.tileEntity = compoundIn;
    }
}
