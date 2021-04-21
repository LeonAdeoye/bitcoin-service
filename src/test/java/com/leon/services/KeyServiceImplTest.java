package com.leon.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        // Act
        String result = KeyServiceImpl.derivePublicKeyHexFromPrivateKeyHex("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c", true);
        // Assert
        assertEquals("should return valid byte array", result, "03908a2fff182e5719c50e543ad67a345c37ae7258bb0cd9cdb59dfc5f20902100");
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