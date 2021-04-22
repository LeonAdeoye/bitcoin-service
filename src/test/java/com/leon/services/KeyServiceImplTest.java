package com.leon.services;

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
    public void convertHexToByteArray_whenPassedValidHexadecimal_shouldReturnValidByteArray()
    {
        // Act
        byte[] result = KeyServiceImpl.convertHexToByteArray("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
        // Assert
        assertEquals("should return valid byte array", Arrays.toString(result), "[44, -56, -126, 33, 24, -121, 124, -59, 81, 24, -125, 54, 107, -78, 43, -66, -21, -79, 22, 106, -90, 97, 20, 78, -18, -28, 77, -46, 42, -96, 8, -117]");
    }
}