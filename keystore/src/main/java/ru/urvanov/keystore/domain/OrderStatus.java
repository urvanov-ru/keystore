package ru.urvanov.keystore.domain;

public enum OrderStatus {
    /**
     * Ожидает оплаты
     */
    PENDING_PAYMENT,
    /**
     * Оплачен
     */
    PAYED,

    /**
     * Выполнен
     */
    COMPLETED,

    /**
     * Отмена
     */
    CANCEL,

    /**
     * Возврат
     */
    PAY_BACK,

    /**
     * Отказ
     */
    REJECT
}
