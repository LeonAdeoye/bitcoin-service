package com.leon.services;

import org.bitcoinj.core.Address;
import org.bitcoinj.script.Script;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.bitcoinj.core.ECKey;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KeyServiceImplTest
{
    @Autowired
    private KeyServiceImpl keyService;

    @Test
    public void createNewKey_whenCalled_shouldReturnPublicKeyWith32Characters()
    {
        // Act
        String publicKey = this.keyService.createNewKey();
        // Assert
        assertEquals("public key should be 2 compressed/uncompressed digits prefix of 03/04/02 plus 64 digits in length", 66, publicKey.length());
    }

    @Test
    public void derivePublicKeyHexFromPrivateKeyHex_whenPassedValidPrivateKeyHex_shouldReturnValidPublicKeyHex()
    {
        // Arrange
        ECKey key = new ECKey();
        String privateKey = key.getPrivateKeyAsHex();
        // Act
        String result = KeyServiceImpl.derivePublicKeyHexFromPrivateKeyHex(privateKey);
        // Assert
        assertEquals("should return valid public key from private key", result, key.getPublicKeyAsHex());
    }

    @Test
    public void getAddress_whenPassedValidPublicKeyHex_shouldReturnValidAddress()
    {
        ECKey k = ECKey.fromPublicOnly(UtilityServiceImpl.convertHexadecimalToByteArray("035acae7cac451d3dced05c166462d37ed60c5babd51b7c56ed8d1a7f31cfd0226"));

        System.out.println("hash160: " + UtilityServiceImpl.convertByteArrayToHexadecimal(k.getPubKeyHash()) + " byte array length " + k.getPubKeyHash().length + " array: " + Arrays.toString(k.getPubKeyHash()));
        // Act
        Address result = keyService.getAddressFromKey("035acae7cac451d3dced05c166462d37ed60c5babd51b7c56ed8d1a7f31cfd0226", Script.ScriptType.P2PKH);

        // address = 00C1EA467BD8CEB175450414934D913EBEBA145E49

        // Assert
        assertEquals("should return valid address from public key", result.toString(), "myCHMp5AA4shcGxM1aBwL3x5zunY5WopUo"); // Bitcoin testnet addresses start with m or n.
    }
}