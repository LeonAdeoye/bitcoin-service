package com.leon.services;

import org.bitcoinj.core.Address;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.bitcoinj.core.ECKey;
import java.math.BigInteger;
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

    public Address getAddress(String publicKeyHex, Script.ScriptType scriptType)
    {
        if(keys.containsKey(publicKeyHex))
            return Address.fromKey(TestNet3Params.get(), keys.get(publicKeyHex), scriptType);

        return null;
    }

    public static Address getAddressFromKey(String publicKeyHex, Script.ScriptType scriptType)
    {
        byte[] result = UtilityServiceImpl.convertHexToByteArray(publicKeyHex);
        return Address.fromKey(TestNet3Params.get(), ECKey.fromPublicOnly(result), scriptType);
    }
}
