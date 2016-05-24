package ru.urvanov.keystore.domain;

public enum KeyStatus {
    /**
     * Создан.
     */
    CREATED,
    /**
     * Активен
     */
    ACTIVE,

    /**
     * Истёк
     */
    EXPIRED,

    /**
     * Отменён
     */
    CANCELED
}