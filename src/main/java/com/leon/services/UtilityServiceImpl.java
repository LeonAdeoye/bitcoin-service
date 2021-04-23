package com.leon.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UtilityServiceImpl
{
    private static final Logger logger = LoggerFactory.getLogger(UtilityServiceImpl.class);

    @Value("${server.hostname}")
    private String hostname;

    public String transformHostnameInUrl(String url)
    {
        if(url != null && url.indexOf("#hostname#") != -1 && this.hostname != null && !this.hostname.trim().isEmpty())
            return url.replace("#hostname#", this.hostname);
        else
            return url;
    }

    public static byte[] convertHexadecimalToByteArray(String keyHex)
    {
        byte[] result = new byte[keyHex.length()/2];

        for(int index = 0; index < result.length; index++)
        {
            int step = index * 2;
            result[index] = (byte) Integer.parseInt(keyHex.substring(step, step + 2), 16);
        }

        return result;
    }

    public static String convertByteArrayToHexadecimal(byte[] input)
    {
        StringBuilder sb = new StringBuilder();

        for (byte b : input)
            sb.append(String.format("%02X", b));

        return sb.toString();
    }

    public static String hashWithSHA256(String stringToHash)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return UtilityServiceImpl.convertByteArrayToHexadecimal(digest.digest(UtilityServiceImpl.convertHexadecimalToByteArray(stringToHash)));
        }
        catch(NoSuchAlgorithmException nsae)
        {
           logger.error("Exception thrown:" + nsae.getMessage());
           return "";
        }
    }

    public static byte[] hashWithSHA256(byte[] byteArrayToHash)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(byteArrayToHash);
        }
        catch(NoSuchAlgorithmException nsae)
        {
            logger.error("Exception thrown:" + nsae.getMessage());
            return new byte[] {};
        }
    }
}