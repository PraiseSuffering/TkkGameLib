package com.twokktwo.tkklib.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;

public class networkTool {
    public static void writeString(ByteBuf buffer, String s) {
        byte[] bytes = s.getBytes(Charsets.UTF_8);
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static String readString(ByteBuf buffer) {
        try {
            byte[] bytes = new byte[buffer.readInt()];
            buffer.readBytes(bytes);
            return new String(bytes, Charsets.UTF_8);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
