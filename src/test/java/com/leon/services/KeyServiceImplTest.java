package com.leon.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.bitcoinj.core.ECKey;
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
    public void getAddressFromPublicKey_whenPassedValidPublicKeyHex_shouldReturnValidBitcoinAddress()
    {
        // Act
        String result = KeyServiceImpl.getAddressFromPublicKey("0202a406624211f2abbdc68da3df929f938c3399dd79fac1b51b0e4ad1d26a47aa");
        // Assert
        assertEquals("should return valid address from public key", result, "1PRTTaJesdNovgne6Ehcdu1fpEdX7913CK");
    }

    @Test
    public void generateVanityAddress_whenPassedValidPattern_shouldReturnVanityAddress()
    {
        // Arrange
        String vanityPattern = "Leo";
        // Act
        String result = KeyServiceImpl.generateVanityAddress(vanityPattern);
        // Assert
        assertEquals("should return valid address from public key", result.substring(0,vanityPattern.length() + 1), "1" + vanityPattern);
    }
}