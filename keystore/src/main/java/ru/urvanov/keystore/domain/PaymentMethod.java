package ru.urvanov.keystore.domain;

public enum PaymentMethod {
    /**
     * Счёт
     */
    CHECK,

    /**
     * Платёжная система
     */
    PAYMENT_SYSTEM,

    /**
     * Наличными в кассу
     */
    CASH
}