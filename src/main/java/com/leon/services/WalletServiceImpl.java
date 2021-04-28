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
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class WalletServiceImpl implements WalletService
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static Wallet wallet;

    private String walletName = "bitcoin-service";

    @Autowired
    ConfigurationService configurationService;

    public WalletServiceImpl()
    {
    }

    public void setWalletName(String walletName)
    {
        this.walletName = walletName;
    }

    public String getWalletName()
    {
        return walletName;
    }

    @PostConstruct
    public void initialise()
    {
        NetworkParameters networkParameters = ConfigurationServiceImpl.getNetworkParams();
        KeyChainGroup group = KeyChainGroup.createBasic(networkParameters);
        wallet = new Wallet(networkParameters, group);
        wallet.autosaveToFile(new File(getWalletName()),300, TimeUnit.SECONDS, null);
    }

    @PreDestroy
    public void close()
    {
        saveWalletToFile(getWalletName());
    }

    @Override
    public String addKey(String privateKeyAsHex)
    {
        ECKey newKey = new ECKey().fromPrivate(new BigInteger(privateKeyAsHex, 16));
        wallet.importKey(newKey);
        logger.info("Imported key with public key: " + newKey.getPubKey());
        return UtilityServiceImpl.convertByteArrayToHexadecimal(newKey.getPubKey());
    }

    @Override
    public String addKey(ECKey key)
    {
        wallet.importKey(key);
        logger.info("Imported key with public key: " + key.getPubKey());
        return UtilityServiceImpl.convertByteArrayToHexadecimal(key.getPubKey());
    }

    @Override
    public int addKeys(List<String> privateKeysAsHex)
    {
        privateKeysAsHex.stream().forEach(privateKeyAsHex ->
        {
            ECKey newKey = new ECKey().fromPrivate(new BigInteger(privateKeyAsHex, 16));
            wallet.importKey(newKey);
            logger.info("Imported key with public key: " + newKey.getPubKey());
        });

        logger.info("Imported a list of " + privateKeysAsHex.size() + " keys.");
        return privateKeysAsHex.size();
    }

    @Override
    public boolean hasKey(ECKey key)
    {
        return wallet.hasKey(key);
    }

    @Override
    public boolean hasKey(String publicKeyAsHex)
    {
        return wallet.isPubKeyMine(UtilityServiceImpl.convertHexadecimalToByteArray(publicKeyAsHex));
    }

    @Override
    public void createNondeterministicKeys(int numberOfKeys)
    {
        for(int count = 0; count < numberOfKeys; ++count)
            wallet.importKey(new ECKey());
    }

    private void saveWalletToFile(String fileName)
    {
        try
        {
            wallet.saveToFile(new File(fileName));
        }
        catch(IOException ioe)
        {
            logger.error(ioe.getMessage());
        }
    }
}
