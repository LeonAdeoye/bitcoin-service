package com.leon.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WalletServiceImplTest
{
    @Autowired
    private WalletServiceImpl walletService;
    @Autowired
    private KeyServiceImpl keyService;

    @Test
    public void addKey_whenCalled_shouldSaveKeyOnKeyChainGroup()
    {
        // Arrange
        String publicKeyAsHex = keyService.createNewKey();
        String privateKeyAsHex = keyService.getPrivateKeyHex(publicKeyAsHex);
        // Act
        walletService.addKey(privateKeyAsHex);
        // Assert
        assertEquals("addKey should add key to key chain and hasKey should return true.", true, walletService.hasKey(publicKeyAsHex));
    }

    @Test
    public void addKeys_whenCalled_shouldSaveKeyOnKeyChainGroup()
    {
        // Arrange
        List<String> keys = new ArrayList<>();
        int count = walletService.getKeyCount();
        // Act
        for(int index = 0; index < 5; ++index)
        {
            String publicKeyAsHex = keyService.createNewKey();
            String privateKeyAsHex = keyService.getPrivateKeyHex(publicKeyAsHex);
            keys.add(privateKeyAsHex);
        }
        // Act
        walletService.addKeys(keys);
        // Assert
        assertEquals("addKey should add key to key chain and hasKey should return true.", count + keys.size(), walletService.getKeyCount());
    }
}