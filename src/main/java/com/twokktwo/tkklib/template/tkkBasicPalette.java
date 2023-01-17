package com.twokktwo.tkklib.template;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ObjectIntIdentityMap;

import javax.annotation.Nullable;
import java.util.Iterator;

public class tkkBasicPalette implements Iterable<IBlockState>
{
    public static final IBlockState DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
    final ObjectIntIdentityMap<IBlockState> ids;
    private int lastId;

    public tkkBasicPalette()
    {
        this.ids = new ObjectIntIdentityMap<IBlockState>(16);
    }

    public int idFor(IBlockState state)
    {
        int i = this.ids.get(state);

        if (i == -1)
        {
            i = this.lastId++;
            this.ids.put(state, i);
        }

        return i;
    }

    @Nullable
    public IBlockState stateFor(int id)
    {
        IBlockState iblockstate = this.ids.getByValue(id);
        return iblockstate == null ? DEFAULT_BLOCK_STATE : iblockstate;
    }

    public Iterator<IBlockState> iterator()
    {
        return this.ids.iterator();
    }

    public void addMapping(IBlockState p_189956_1_, int p_189956_2_)
    {
        this.ids.put(p_189956_1_, p_189956_2_);
    }
}