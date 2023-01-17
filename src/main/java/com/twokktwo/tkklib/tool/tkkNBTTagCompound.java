package com.twokktwo.tkklib.tool;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.nbt.*;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.regex.Pattern;

public class tkkNBTTagCompound extends NBTTagCompound implements Serializable {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, NBTBase> tagMap = Maps.<String, NBTBase>newHashMap();

    public Set<String> getKeySet()
    {
        return this.tagMap.keySet();
    }

    public byte getId()
    {
        return 10;
    }

    public int getSize()
    {
        return this.tagMap.size();
    }

    public void setTag(String key, NBTBase value)
    {
        if (value == null) throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        this.tagMap.put(key, value);
    }

    public void setByte(String key, byte value)
    {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    public void setShort(String key, short value)
    {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    public void setInteger(String key, int value)
    {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    public void setLong(String key, long value)
    {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    public void setUniqueId(String key, UUID value)
    {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }

    @Nullable
    public UUID getUniqueId(String key)
    {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key)
    {
        return this.hasKey(key + "Most", 99) && this.hasKey(key + "Least", 99);
    }

    public void setFloat(String key, float value)
    {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    public void setDouble(String key, double value)
    {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    public void setString(String key, String value)
    {
        this.tagMap.put(key, new NBTTagString(value));
    }

    public void setByteArray(String key, byte[] value)
    {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    public void setIntArray(String key, int[] value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    public void setBoolean(String key, boolean value)
    {
        this.setByte(key, (byte)(value ? 1 : 0));
    }

    public NBTBase getTag(String key)
    {
        return this.tagMap.get(key);
    }

    public byte getTagId(String key)
    {
        NBTBase nbtbase = this.tagMap.get(key);
        return nbtbase == null ? 0 : nbtbase.getId();
    }

    public boolean hasKey(String key)
    {
        return this.tagMap.containsKey(key);
    }

    public boolean hasKey(String key, int type)
    {
        int i = this.getTagId(key);

        if (i == type)
        {
            return true;
        }
        else if (type != 99)
        {
            return false;
        }
        else
        {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
    }

    public byte getByte(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getByte();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public short getShort(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getShort();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public int getInteger(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getInt();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public long getLong(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getLong();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0L;
    }

    public float getFloat(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getFloat();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0F;
    }

    public double getDouble(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getDouble();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0D;
    }

    public String getString(String key)
    {
        return super.getString(key);
    }

    public byte[] getByteArray(String key)
    {
        try
        {
            if (this.hasKey(key, 7))
            {
                return ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
        }

        return new byte[0];
    }

    public int[] getIntArray(String key)
    {
        try
        {
            if (this.hasKey(key, 11))
            {
                return ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
        }

        return new int[0];
    }

    public tkkNBTTagCompound getCompoundTag(String key)
    {
        try
        {
            if (this.hasKey(key, 10))
            {
                return (tkkNBTTagCompound)this.tagMap.get(key);
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
        }

        return new tkkNBTTagCompound();
    }

    public NBTTagList getTagList(String key, int type)
    {
        try
        {
            if (this.getTagId(key) == 9)
            {
                NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);

                if (!nbttaglist.hasNoTags() && nbttaglist.getTagType() != type)
                {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String key)
    {
        return this.getByte(key) != 0;
    }

    public void removeTag(String key)
    {
        this.tagMap.remove(key);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.tagMap.keySet();

        if (LOGGER.isDebugEnabled())
        {
            List<String> list = Lists.newArrayList(this.tagMap.keySet());
            Collections.sort(list);
            collection = list;
        }

        for (String s : collection)
        {
            if (stringbuilder.length() != 1)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s));
        }

        return stringbuilder.append('}').toString();
    }

    public boolean hasNoTags()
    {
        return this.tagMap.isEmpty();
    }

    private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.NBT_TYPES[((NBTBase)tkkNBTTagCompound.this.tagMap.get(key)).getId()];
            }
        });
        crashreportcategory.addDetail("Tag type expected", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        crashreportcategory.addCrashSection("Tag name", key);
        return crashreport;
    }

    public tkkNBTTagCompound copy()
    {
        tkkNBTTagCompound nbttagcompound = new tkkNBTTagCompound();

        for (String s : this.tagMap.keySet())
        {
            nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Objects.equals(this.tagMap.entrySet(), ((tkkNBTTagCompound)p_equals_1_).tagMap.entrySet());
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(8);
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readUTF();
    }


    public void merge(tkkNBTTagCompound other)
    {
        for (String s : other.tagMap.keySet())
        {
            NBTBase nbtbase = other.tagMap.get(s);

            if (nbtbase.getId() == 10)
            {
                if (this.hasKey(s, 10))
                {
                    tkkNBTTagCompound nbttagcompound = this.getCompoundTag(s);
                    nbttagcompound.merge((tkkNBTTagCompound)nbtbase);
                }
                else
                {
                    this.setTag(s, nbtbase.copy());
                }
            }
            else
            {
                this.setTag(s, nbtbase.copy());
            }
        }
    }

    protected static String handleEscape(String p_193582_0_)
    {
        return SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.quoteAndEscape(p_193582_0_);
    }
}
