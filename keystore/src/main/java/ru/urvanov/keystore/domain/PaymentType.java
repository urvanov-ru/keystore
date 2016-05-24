package ru.urvanov.keystore.domain;

public enum PaymentType {
    /**
     * Оплата заказа
     */
    ORDER_PAYMENT,

    /**
     * Возврат средств
     */
    PAY_BACK,
    
    /**
     * Корректировка
     */
    CORRECTING
}
