package org.openbankdata.core.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BankRequestTest {

    private BankRequest mBankRequest;

    private static final String BASE_URI = "http://www.foo.bar/?foobar=bar";

    @Before
    public void setUp() {
        mBankRequest = new BankRequest();
    }

    @Test
    public void testGenerateUriWithEmptyUriString() {
        // Given
        // An empty URI
        mBankRequest.setUri("");

        // When
        // Generating the full URI
        String actual = mBankRequest.generateUri();

        // Then
        assertEquals("The generated URI should be empty", "", actual);
    }

    @Test
    public void testGenerateUriWithNullAsInput() {
        // Given
        // URI is null
        mBankRequest.setUri(null);

        // When
        // Generating the full URI
        String actual = mBankRequest.generateUri();

        // Then
        assertEquals("The generated URI should be empty", "", actual);
    }

    @Test
    public void testGenerateUriWithoutParams() {
        // Given
        // An URI with inline parameters

        mBankRequest.setUri(BASE_URI);

        // When
        // Generating the full URI
        String actual = mBankRequest.generateUri();

        // Then
        assertEquals("The generated URI should be the same as the given",
                BASE_URI, actual);
    }

    @Test
    public void testGenerateUriWithBothInlineParamsAndExternalParameters() {
        // Given
        // An URI with both inline parameters and external parameters
        mBankRequest.setUri(BASE_URI);
        mBankRequest.addParam("key", "value");

        // When
        // Generating the full URI
        String actual = mBankRequest.generateUri();
        // Then
        String expected = BASE_URI + "&key=value";
        assertEquals(
                "The generated URI should contain both internal and external parameters",
                expected, actual);
    }

    @Test
    public void testGenerateUriWithMultipleInlineParamsAndExternalParameters() {
        // Given
        // An URI with multiple inline parameters and external parameters
        mBankRequest.setUri(BASE_URI + "&foo=bar");
        mBankRequest.addParam("key", "value");

        // When
        // Generating the full URI
        String actual = mBankRequest.generateUri();

        // Then
        String expected = BASE_URI + "&foo=bar&key=value";
        System.out.println(expected);
        System.out.println(actual);
        assertEquals("The generated URI should contain all internal and external parameters", expected, actual);
    }

}
