package com.twokktwo.tkklib.mapPiece.template;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import javax.annotation.Nullable;
import java.util.List;

public class Template{
    private final List<BlockDate> blocks = Lists.<BlockDate>newArrayList();
    public String name="noName";
    public BlockPos size=BlockPos.ORIGIN;


    public Template(String name){
        this.name=name;
    }
    public Template(){}
    public void takeBlock(World world, BlockPos A,BlockPos B){
        /*
        BlockPos startPos=new BlockPos(Math.min(A.getX(),B.getX()),Math.min(A.getY(),B.getY()),Math.min(A.getZ(),B.getZ()));
        BlockPos BB=new BlockPos(Math.max(A.getX(),B.getX()),Math.max(A.getY(),B.getY()),Math.max(A.getZ(),B.getZ()));
        BlockPos endPos=new BlockPos(startPos.getX()-BB.getX(),startPos.getY()-BB.getY(),startPos.getZ()-BB.getZ());
        BlockPos blockpos = startPos.add(endPos).add(-1, -1, -1);
        BlockPos blockpos1 = new BlockPos(Math.min(startPos.getX(), blockpos.getX()), Math.min(startPos.getY(), blockpos.getY()), Math.min(startPos.getZ(), blockpos.getZ()));
        BlockPos blockpos2 = new BlockPos(Math.max(startPos.getX(), blockpos.getX()), Math.max(startPos.getY(), blockpos.getY()), Math.max(startPos.getZ(), blockpos.getZ()));
        * */
        this.size=B;
        BlockPos blockpos = A.add(B).add(-1, -1, -1);
        BlockPos blockpos1 = new BlockPos(Math.min(A.getX(), blockpos.getX()), Math.min(A.getY(), blockpos.getY()), Math.min(A.getZ(), blockpos.getZ()));
        BlockPos blockpos2 = new BlockPos(Math.max(A.getX(), blockpos.getX()), Math.max(A.getY(), blockpos.getY()), Math.max(A.getZ(), blockpos.getZ()));
        List<BlockDate> list = Lists.<BlockDate>newArrayList();
        List<BlockDate> list1 = Lists.<BlockDate>newArrayList();
        for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(blockpos1,blockpos2)) {
            BlockPos blockpos3 = pos.subtract(blockpos1);
            IBlockState iblockstate = world.getBlockState(pos);
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity != null) {
                NBTTagCompound nbttagcompound = tileentity.writeToNBT(new NBTTagCompound());
                nbttagcompound.removeTag("x");
                nbttagcompound.removeTag("y");
                nbttagcompound.removeTag("z");
                list1.add(new BlockDate(blockpos3, iblockstate, nbttagcompound));
            } else {
                list.add(new BlockDate(blockpos3, iblockstate, (NBTTagCompound) null));
            }
        }
            this.blocks.clear();
            this.blocks.addAll(list);
            this.blocks.addAll(list1);
    }

    public void addBlock(World world,BlockPos A,BlockPos B){
        BlockPos blockpos = A.add(B).add(-1, -1, -1);
        addBlock(world,new BlockPos(Math.min(A.getX(), blockpos.getX()), Math.min(A.getY(), blockpos.getY()), Math.min(A.getZ(), blockpos.getZ())));
    }
    public void addBlock(World world,BlockPos A){
        for(BlockDate block:this.blocks){
            BlockPos pos=block.pos.add(A);
            if(world.getTileEntity(pos)!=null){world.removeTileEntity(pos);}
            setBlock(world,pos,block.blockState,block.tileEntity);
        }
        for(BlockDate block:this.blocks){
            updateBlock(world,block.pos.add(A),block.blockState);
        }
        //world.markBlockRangeForRenderUpdate(A,A.add(this.size).add(-1, -1, -1));
    }
    public void addBlock(World world, BlockPos A, int mirror){
        BlockPos B=A.add(size).add(-1, -1, -1);
        BlockPos AA = new BlockPos(Math.min(A.getX(), B.getX()), Math.min(A.getY(), B.getY()), Math.min(A.getZ(), B.getZ()));
        BlockPos BB = new BlockPos(Math.max(A.getX(), B.getX()), Math.max(A.getY(), B.getY()), Math.max(A.getZ(), B.getZ()));
        BlockPos start;
        int[] order={1,1,1};
        switch (mirror){
            case 0://原图
                start=new BlockPos(A.getX(),A.getY(), A.getZ());
                break;
            case 1://水平
                start=new BlockPos(A.getX(),A.getY(), B.getZ());
                order[2]=order[2]*-1;
                break;
            case 2://垂直
                start=new BlockPos(B.getX(),A.getY(), A.getZ());
                order[0]=order[0]*-1;
                break;
            case 3://水平+垂直
                start=new BlockPos(B.getX(),A.getY(), B.getZ());
                order[2]=order[2]*-1;
                order[0]=order[0]*-1;
                break;
            default:
                start=A;
                break;
        }
        for(BlockDate block:this.blocks){
            BlockPos pos=start.add(block.pos.getX()*order[0],block.pos.getY()*order[1],block.pos.getZ()*order[2]);
            if(world.getTileEntity(pos)!=null){world.removeTileEntity(pos);}
            setBlock(world,pos,block.blockState,block.tileEntity);
        }
        for(BlockDate block:this.blocks){
            updateBlock(world,start.add(block.pos.getX()*order[0],block.pos.getY()*order[1],block.pos.getZ()*order[2]),block.blockState);
        }
        //world.markBlockRangeForRenderUpdate(A,A.add(this.size).add(-1, -1, -1));
    }
    public static IBlockState setBlock(World world,BlockPos pos,IBlockState block,@Nullable NBTTagCompound tile) {
        IBlockState old=SetOnlyBlocks(world, pos, block);
        if(tile!=null){
            TileEntity tileEntity = TileEntity.create(world, (NBTTagCompound) tile);
            world.setTileEntity(pos, tileEntity);
            tileEntity.setPos(pos);
            tileEntity.setWorld(world);
        }
        return old;
    }
    public static IBlockState SetOnlyBlocks(World world,BlockPos pos,IBlockState state){
        if (world.isOutsideBuildHeight(pos)) {
            return null;
        } else if (!world.isRemote && world.getWorldInfo().getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return null;
        } else {
            Chunk chunk = world.getChunkFromBlockCoords(pos);

            pos = pos.toImmutable(); // Forge - prevent mutable BlockPos leaks
            IBlockState iblockstate = chunk.getBlockState(pos);
            Block block = state.getBlock();
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[pos.getY() >> 4];
            if (iblockstate == state)
            {
                return null;
            }
            if (extendedblockstorage == Chunk.NULL_BLOCK_STORAGE)
            {
                if (block == Blocks.AIR)
                {
                    return null;
                }
                extendedblockstorage = new ExtendedBlockStorage(pos.getY() >> 4 << 4, chunk.getWorld().provider.hasSkyLight());
                chunk.getBlockStorageArray()[pos.getY() >> 4] = extendedblockstorage;
            }
            extendedblockstorage.set(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15, state);
            if (extendedblockstorage.get(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15).getBlock() != block)
            {
                return null;
            }
            return iblockstate;

        }
    }

    public static void updateBlock(World world, BlockPos pos,IBlockState old) {
        IBlockState state = world.getBlockState(pos);
        //world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        /*
        if ((3 & 2) != 0 && (!world.isRemote || (3 & 4) == 0) && (chunk == null || chunk.isPopulated())) {
            world.notifyBlockUpdate(pos, old, state, 3);
        }
        */
        if (!world.isRemote && (chunk.isPopulated())) {
            world.notifyBlockUpdate(pos, old, state, 3);
        }
    }

    private NBTTagList writeInts(int... values)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i : values)
        {
            nbttaglist.appendTag(new NBTTagInt(i));
        }

        return nbttaglist;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        tkkBasicPalette template$basicpalette = new tkkBasicPalette();
        NBTTagList nbttaglist = new NBTTagList();

        for (BlockDate template$blockinfo : this.blocks)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("pos", this.writeInts(template$blockinfo.pos.getX(), template$blockinfo.pos.getY(), template$blockinfo.pos.getZ()));
            nbttagcompound.setInteger("state", template$basicpalette.idFor(template$blockinfo.blockState));

            if (template$blockinfo.tileEntity != null)
            {
                nbttagcompound.setTag("nbt", template$blockinfo.tileEntity);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        NBTTagList nbttaglist2 = new NBTTagList();

        for (IBlockState iblockstate : template$basicpalette)
        {
            nbttaglist2.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), iblockstate));
        }

        net.minecraftforge.fml.common.FMLCommonHandler.instance().getDataFixer().writeVersionData(nbt); //Moved up for MC updating reasons.
        nbt.setTag("palette", nbttaglist2);
        nbt.setTag("blocks", nbttaglist);
        nbt.setTag("size", this.writeInts(this.size.getX(), this.size.getY(), this.size.getZ()));
        nbt.setString("name", this.name);
        return nbt;
    }

    public void read(NBTTagCompound compound)
    {
        this.blocks.clear();
        NBTTagList nbttaglist = compound.getTagList("size", 3);
        this.size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));
        this.name = compound.getString("name");
        tkkBasicPalette template$basicpalette = new tkkBasicPalette();
        NBTTagList nbttaglist1 = compound.getTagList("palette", 10);

        for (int i = 0; i < nbttaglist1.tagCount(); ++i)
        {
            template$basicpalette.addMapping(NBTUtil.readBlockState(nbttaglist1.getCompoundTagAt(i)), i);
        }

        NBTTagList nbttaglist3 = compound.getTagList("blocks", 10);

        for (int j = 0; j < nbttaglist3.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound = nbttaglist3.getCompoundTagAt(j);
            NBTTagList nbttaglist2 = nbttagcompound.getTagList("pos", 3);
            BlockPos blockpos = new BlockPos(nbttaglist2.getIntAt(0), nbttaglist2.getIntAt(1), nbttaglist2.getIntAt(2));
            IBlockState iblockstate = template$basicpalette.stateFor(nbttagcompound.getInteger("state"));
            NBTTagCompound nbttagcompound1;

            if (nbttagcompound.hasKey("nbt"))
            {
                nbttagcompound1 = nbttagcompound.getCompoundTag("nbt");
            }
            else
            {
                nbttagcompound1 = null;
            }

            this.blocks.add(new BlockDate(blockpos, iblockstate, nbttagcompound1));
        }
    }

    /*
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.writeToNBT(new NBTTagCompound()).toString());
        out.defaultWriteObject();
        //out.close();
    }
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException, NBTException {
        String sNBT = (String) ois.readObject();
        NBTTagCompound nbt;
        ois.defaultReadObject();
        nbt=JsonToNBT.getTagFromJson(sNBT);
        //this.blocks=Lists.<BlockDate>newArrayList();
        try {
            this.read(nbt);
            //TkkGameLib.logger.log(Level.DEBUG,this.test());
        }catch (Exception e){
            TkkGameLib.logger.log(Level.ERROR,"Error Template.readObject():"+e);
            StackTraceElement ste=e.getStackTrace()[0];
            TkkGameLib.logger.log(Level.ERROR,"Error.Line Template.readObject():"+ste.getLineNumber());
        }

        //TkkGameLib.logger.log(Level.DEBUG,this.name);
        //ois.close();
    }
    */

}
