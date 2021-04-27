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
        for(int index = 0; index < wallet.getImportedKeys().size(); ++index)
        {
            if(wallet.getImportedKeys().get(index).getPubKey().equals(publicKeyAsHex))
                return true;
        }
        return false;
    }

    @Override
    public void createNondeterministicKeys(int numberOfKeys)
    {
        for(int count = 0; count < numberOfKeys; ++count)
            wallet.importKey(new ECKey());
    }

    @Override
    public void saveWallet()
    {

    }
}
