package com.intuitveinc.product_service.service;

import com.intuitveinc.common.exception.PartnerAuthenticationException;
import com.intuitveinc.common.exception.PartnerNotFoundException;
import com.intuitveinc.common.model.Partner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class PartnerServiceTest {

    @Test
    void testGenerateToken() {
        PartnerService partnerService = Mockito.mock(PartnerService.class);
        Partner partner = new Partner();
        partner.setName("Contoso");
        partner.setAccessKey("accessKey");
        partner.setHashedSecretKey("$2a$10$7");
        Mockito.when(partnerService.generateToken("accessKey", "secretKey")).thenReturn("token");

        String token = partnerService.generateToken("accessKey", "secretKey");
        assertNotNull(token);

        Mockito.when(partnerService.generateToken("accessKey", "secretKey")).thenThrow(new PartnerNotFoundException("Partner not found with access key: accessKey"));
        assertThrows(PartnerNotFoundException.class, () -> partnerService.generateToken("accessKey", "secretKey"));
    }

    @Test
    void testValidateToken() {
        PartnerService partnerService = Mockito.mock(PartnerService.class);
        Mockito.when(partnerService.validateToken("token")).thenReturn(true);

        boolean isValid = partnerService.validateToken("token");
        assertTrue(isValid);

        Mockito.when(partnerService.validateToken("token")).thenReturn(false);
        boolean isInvalid = partnerService.validateToken("token");
        assertFalse(isInvalid);
    }

    @Test
    void testGetPartnerFromToken() {
        PartnerService partnerService = Mockito.mock(PartnerService.class);
        Partner partner = new Partner();
        partner.setName("Amazon");
        partner.setAccessKey("accessKey");
        partner.setHashedSecretKey("$2a$10$7");
        Mockito.when(partnerService.getPartnerFromToken("token")).thenReturn(partner);

        Partner fetchedPartner = partnerService.getPartnerFromToken("token");
        System.out.println(fetchedPartner);
        assertEquals("Amazon", fetchedPartner.getName());

        Mockito.when(partnerService.getPartnerFromToken("token")).thenThrow(new PartnerNotFoundException("Partner not found with ID: 1"));
        assertThrows(PartnerNotFoundException.class, () -> partnerService.getPartnerFromToken("token"));
    }
}