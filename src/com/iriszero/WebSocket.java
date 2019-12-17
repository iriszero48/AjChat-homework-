package com.iriszero;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WebSocket
{
    private static byte[] SHA1Hash(byte[] bytes) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(bytes);
        return md.digest();
    }

    static String Decode(byte[] raw, int len)
    {
        byte rLength;
        int rMaskIndex = 2;
        int rDataStart;
        byte data = raw[1];
        byte op = (byte) 127;
        rLength = (byte) (data & op);
        if (rLength == (byte) 126) rMaskIndex = 4;
        if (rLength == (byte) 127) rMaskIndex = 10;
        byte[] masks = new byte[4];
        int j = 0;
        int i;
        for (i = rMaskIndex; i < (rMaskIndex + 4); i++)
        {
            masks[j] = raw[i];
            j++;
        }
        rDataStart = rMaskIndex + 4;
        int messLen = len - rDataStart;
        byte[] message = new byte[messLen];
        for (i = rDataStart, j = 0; i < len; i++, j++)
        {
            message[j] = (byte) (raw[i] ^ masks[j % 4]);
        }
        return new String(message, StandardCharsets.UTF_8);
    }

    static byte[] Encode(String raw)
    {
        byte[] rawData = raw.getBytes(StandardCharsets.UTF_8);
        int frameCount;
        byte[] frame = new byte[10];
        frame[0] = (byte) 129;
        if (rawData.length <= 125)
        {
            frame[1] = (byte) rawData.length;
            frameCount = 2;
        }
        else if (rawData.length <= 65535)
        {
            frame[1] = (byte) 126;
            int len = rawData.length;
            frame[2] = (byte) ((len >> 8) & (byte) 255);
            frame[3] = (byte) (len & (byte) 255);
            frameCount = 4;
        }
        else
        {
            frame[1] = (byte) 127;
            int len = rawData.length;
            frame[2] = (byte) ((len >> 24) & (byte) 255);
            frame[3] = (byte) ((len >> 16) & (byte) 255);
            frame[4] = (byte) ((len >> 8) & (byte) 255);
            frame[5] = (byte) ((len) & (byte) 255);
            frame[6] = (byte) ((len >> 24) & (byte) 255);
            frame[7] = (byte) ((len >> 16) & (byte) 255);
            frame[8] = (byte) ((len >> 8) & (byte) 255);
            frame[9] = (byte) (len & (byte) 255);
            frameCount = 10;
        }
        int bLength = frameCount + rawData.length;
        byte[] reply = new byte[bLength];
        int bLim = 0;
        for (int i = 0; i < frameCount; i++)
        {
            reply[bLim] = frame[i];
            bLim++;
        }
        for (byte rawDatum : rawData)
        {
            reply[bLim] = rawDatum;
            bLim++;
        }
        return reply;
    }

    static String SecWebSocketAccept(String key) throws NoSuchAlgorithmException
    {
        return new String(Base64.getEncoder().encode((SHA1Hash((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()))));
    }
}
