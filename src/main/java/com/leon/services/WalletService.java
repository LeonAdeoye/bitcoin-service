package com.leon.services;
import org.bitcoinj.core.ECKey;

import java.util.List;

public interface WalletService
{
    String addKey(String privateKeyAsHex);
    String addKey(ECKey key);
    int addKeys(List<String> privateKeysAsHex);
    boolean hasKey(ECKey key);
    boolean hasKey(String publicKeyAsHex);
    void createNondeterministicKeys(int numberOfKeys);
    void saveWallet();
}
