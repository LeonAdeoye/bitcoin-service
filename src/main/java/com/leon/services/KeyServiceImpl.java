package com.leon.services;

import org.bitcoinj.core.Base58;
import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.bitcoinj.core.ECKey;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeyServiceImpl implements KeyService
{
    private static final Logger logger = LoggerFactory.getLogger(KeyServiceImpl.class);
    private Map<String, ECKey> keys = new HashMap<>();

    public KeyServiceImpl()
    {
    }

    @Override
    public String createNewKey()
    {
        ECKey key = new ECKey();
        logger.info("Created new key:\n" + key);
        keys.put(key.getPublicKeyAsHex(), key);
        return key.getPublicKeyAsHex();
    }

    public static String derivePublicKeyHexFromPrivateKeyHex(String privateKeyHex)
    {
        ECKey newKey = ECKey.fromPrivate(new BigInteger(privateKeyHex, 16));
        logger.info("Created public key: {} from private key hex: {}", newKey.getPublicKeyAsHex(), privateKeyHex);
        return newKey.getPublicKeyAsHex();
    }

    @Override
    public String getPrivateKeyHex(String publicKeyHex)
    {
        if(keys.containsKey(publicKeyHex))
            return keys.get(publicKeyHex).getPrivateKeyAsHex();

        return "";
    }

    @Override
    public String getPrivateKeyWIF(String publicKeyHex)
    {
        if(keys.containsKey(publicKeyHex))
            return keys.get(publicKeyHex).getPrivateKeyAsWiF(TestNet3Params.get());

        return "";
    }

    public static String getAddressFromPublicKey(String publicKeyHex)
    {
        ECKey k = ECKey.fromPublicOnly(UtilityServiceImpl.convertHexadecimalToByteArray(publicKeyHex));

        // First create the 20 bytes public key HASH160 'payload' and then prefix with 00 for bitcoin address.
        String versionedPayload = "00" + UtilityServiceImpl.convertByteArrayToHexadecimal(k.getPubKeyHash());

        // Double SHA256 hash the concatenation of the prefix and HASH160 payload and extract the first 4 bytes for the checksum.
        String checksum = UtilityServiceImpl.hashWithSHA256(UtilityServiceImpl.hashWithSHA256(versionedPayload)).substring(0,8);

        // Create a byte array from the concatenation of the versioned payload and the checksum
        byte[] versionedPayloadWithChecksumByteArray = UtilityServiceImpl.convertHexadecimalToByteArray(versionedPayload + checksum);

        return Base58.encode(versionedPayloadWithChecksumByteArray);
    }

    public static String generateVanityAddress(String vanityPattern)
    {
        if(vanityPattern.length() > 4)
            throw new IllegalArgumentException("string length of prefix cannot exceed 4 characters");

        Instant startTime = Instant.now();
        String result = "";
        int attempts = 0;
        boolean notFound = true;
        while(notFound)
        {
            attempts++;
            ECKey key = new ECKey();
            result = getAddressFromPublicKey(key.getPublicKeyAsHex());
            notFound = !result.substring(0,vanityPattern.length() + 1).equals("1" + vanityPattern);
        }
        Instant endTime = Instant.now();
        logger.info("Created new vanity address: [" + result + "] after " + attempts + " attempts and it took " + Duration.between(endTime, startTime).toMillis() + " ms.");

        return result;
    }
}
