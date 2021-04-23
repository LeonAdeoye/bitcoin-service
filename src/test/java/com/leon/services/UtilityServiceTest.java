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
public class UtilityServiceTest
{
    @Autowired
    private UtilityServiceImpl utilityService;

    @Test
    public void whenPassedUrlWithReplaceableHostname_transformHostnameInUrl_shouldReplaceHostname()
    {
        // Act
        String result = utilityService.transformHostnameInUrl("http://#hostname#:20003/usage");
        // Assert
        assertEquals("#hostname# should be replaced", result, "http://localhost:20003/usage");
    }

    @Test
    public void whenPassedUrlWithoutReplaceableHostname_transformHostnameInUrl_shouldHaveNoEffect()
    {
        // Act
        String result = utilityService.transformHostnameInUrl("http://#hostname:20003/usage");
        // Assert
        assertEquals("#hostname should not be replaced", result, "http://#hostname:20003/usage");
    }

    @Test
    public void whenPassedEmptyString_transformHostnameInUrl_shouldHaveNoEffect()
    {
        // Act
        String result = utilityService.transformHostnameInUrl("");
        // Assert
        assertEquals("empty string should have not effect", result, "");
    }

    @Test
    public void whenPassedNull_transformHostnameInUrl_shouldHaveNoEffect()
    {
        // Act
        String result = utilityService.transformHostnameInUrl(null);
        // Assert
        assertEquals("null should have not effect", result, null);
    }

    @Test
    public void convertHexToByteArray_whenPassedValidHexadecimal_shouldReturnValidByteArray()
    {
        // Act
        byte[] result = UtilityServiceImpl.convertHexadecimalToByteArray("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
        // Assert
        assertEquals("should return valid byte array", Arrays.toString(result), "[105, 44, -126, 24, 124, 81, -125, 107, 43, -21, 22, -90, 20, -18, 77, 42, 8, -67, -60, 72, 0, 49, 46, -57, 34, 36, 17, -99, -58, -5, 43, 108]");
    }

    @Test
    public void convertByteArrayToHex_whenPassedValidByteArray_shouldReturnValidHexadecimal()
    {
        // Arrange
        byte[] byteArray = UtilityServiceImpl.convertHexadecimalToByteArray("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
        // Act
        String hexadecimal = UtilityServiceImpl.convertByteArrayToHexadecimal(byteArray).toLowerCase();
        // Assert
        assertEquals("should return valid hexadecimal", hexadecimal, "692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
    }

    @Test
    public void hasWithSHA256_whenPassedValidHexadecimal_shouldReturnValidHexadecimal()
    {
        // Act
        String result = UtilityServiceImpl.hashWithSHA256("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
        // Assert
        assertEquals("should return valid hexadecimal after hash", result, "E9A78B0E7F40DD706872A89D95A9225114C84318B3A22DFD7C1A45B801F894D1");
    }

    @Test
    public void hasWithSHA256_whenPassedValidByteArray_shouldReturnValidByteArray()
    {
        // Act
        byte[] byteArray = UtilityServiceImpl.convertHexadecimalToByteArray("692c82187c51836b2beb16a614ee4d2a08bdc44800312ec72224119dc6fb2b6c");
        // Arrange
        byte[] hashedByteArray = UtilityServiceImpl.hashWithSHA256(byteArray);
        String result = UtilityServiceImpl.convertByteArrayToHexadecimal(hashedByteArray);
        // Assert
        assertEquals("should return valid hexadecimal after hash", result, "E9A78B0E7F40DD706872A89D95A9225114C84318B3A22DFD7C1A45B801F894D1");
    }
}