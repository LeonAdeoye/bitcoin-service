package com.leon.services;

import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.bitcoinj.core.ECKey;
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
}
