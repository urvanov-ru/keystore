package ru.urvanov.keystore.domain;

public enum UserAccessCode {
    /**
     * Администрирование аккаунта (данных аккаунта и пользователей)
     */
    SERVICE_ADMIN_ACCOUNT,
    /**
     * Администрирование клиентских аккаунтов (данных аккаунта и пользователей)
     */
    SERVICE_ADMIN_CLIENT,
    /**
     * Просмотр клиентских аккаунтов
     */
    SERVICE_READ_CLIENT,
    /**
     * Получение оповещений о регистрации клиентов (включает просмотр клиентских
     * аккаунтов)
     */
    SERVICE_NEW_CLIENT_NOTIFICATION,

    /**
     * Добавление клиентских аккаунтов (включает просмотр клиентских аккаунтов)
     */
    SERVICE_ADD_CLIENT,

    /**
     * Экспорт данных о клиентских аккаунтах (включает просмотр клиентских
     * аккаунтов)
     */
    SERVICE_EXPORT_CLIENT,

    /**
     * Просмотр заказов
     */
    SERVICE_READ_ORDER,

    /**
     * Экспорт заказов (включает просмотр заказов)
     */
    SERVICE_EXPORT_ORDER,

    /**
     * Просмотр платежей
     */
    SERVICE_READ_PAYMENT,

    /**
     * Проведение платежей (включает просмотр платежей)
     */
    SERVICE_EXECUTE_PAYMENT,

    /**
     * Экспорт данных о платежах (включает просмотр платежей)
     */
    SERVICE_EXPORT_PAYMENT,

    /**
     * Просмотр специальных акций
     */
    SERVICE_READ_ACTION,

    /**
     * Изменение специальных акций (включает просмотр специальных акций)
     */
    SERVICE_WRITE_ACTION,

    /**
     * Экспорт специальных акций (включает просмотр специальных акций)
     */
    SERVICE_EXPORT_ACTION,

    /**
     * Рассылка уведомлений
     */
    SERVICE_SEND_NOTIFICATION,

    /**
     * Настройка групп клиентов
     */
    SERVICE_SETTING_CLIENT_GROUP,

    /**
     * Настройка пакетов услуг
     */
    SERVICE_SETTING_DICT_SERVICE_TYPE,

    /**
     * Настройка шаблонов писем
     */
    SERVICE_SETTING_EMAIL_TEMPLATE,

    /**
     * Администрирование аккаунта (данных аккаунта и пользователей)
     */
    CLIENT_ADMIN_CLIENT_ACCOUNT,

    /**
     * Просмотр заказов
     */
    CLIENT_READ_ORDER,

    /**
     * Размещение заказов (включает просмотр заказов и просмотр платежей)
     */
    CLIENT_ADD_ORDER,

    /**
     * Просмотр платежей
     */
    CLIENT_READ_PAYMENT,

    /**
     * Просмотр ключей (включает просмотр заказов)
     */
    CLIENT_READ_KEY,

    /**
     * Активация ключей (включает просмотр ключей)
     */
    CLIENT_ACTIVATE_KEY,

    /**
     * Получение ключей
     */
    CLIENT_GET_KEY,

    /**
     * Получение оповещений
     */
    CLIENT_RECEIVE_NOTIFICATIONS,

    /**
     * Выгрузка данных
     */
    CLIENT_EXPORT_DATA

}
