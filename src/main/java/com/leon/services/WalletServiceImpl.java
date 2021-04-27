package com.leon.services;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static Wallet wallet;

    @Autowired
    ConfigurationService configurationService;

    public WalletServiceImpl()
    {
    }

    @PostConstruct
    public void initialise()
    {
        String filePrefix = "bitcoin-service";
        NetworkParameters networkParameters = ConfigurationServiceImpl.getNetworkParams();
        KeyChainGroup group = KeyChainGroup.createBasic(networkParameters);
        wallet = new Wallet(networkParameters, group);
    }

    public void addKey(String privateKeyAsHex)
    {
        ECKey newKey = new ECKey().fromPrivate(new BigInteger(privateKeyAsHex, 16));
        wallet.importKey(newKey);
        logger.info("Imported key with public key: " + newKey.getPubKey());
    }

    public void addKey(ECKey key)
    {
        wallet.importKey(key);
        logger.info("Imported key with public key: " + key.getPubKey());
    }

    public void addKeys(List<ECKey> keys)
    {
        wallet.importKeys(keys);
        logger.info("Imported a list of " + keys.size() + " keys.");
    }

    public boolean hasKey(ECKey key)
    {
        return wallet.hasKey(key);
    }
}
