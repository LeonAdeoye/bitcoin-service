package com.leon.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UtilityServiceImpl
{
    @Value("${server.hostname}")
    private String hostname;

    public String transformHostnameInUrl(String url)
    {
        if(url != null && url.indexOf("#hostname#") != -1 && this.hostname != null && !this.hostname.trim().isEmpty())
            return url.replace("#hostname#", this.hostname);
        else
            return url;
    }

    public static byte[] convertHexToByteArray(String keyHex)
    {
        byte[] result = new byte[keyHex.length()/2];

        for(int index = 0; index < result.length; index++)
        {
            int step = index * 2;
            result[index] = (byte) Integer.parseInt(keyHex.substring(step, step + 2), 16);
        }

        return result;
    }

    public static String convertByteArraytoHexadecimal(byte[] input)
    {
        StringBuilder sb = new StringBuilder();

        for (byte b : input)
            sb.append(String.format("%02X", b));

        return sb.toString();
    }
}