package com.mylibrary;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentSystemTest {

    @Test
    void should_returnFalse_when_callingUnimplementedMethods(){
        PaymentSystem paymentSystem = new PaymentSystem();
        assertFalse(paymentSystem.paymentSuccess());
    }

}