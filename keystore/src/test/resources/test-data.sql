insert into dict_client_group(name) values('first dict client group');



insert into client(name, client_type, unique_id, contact_person_name, contact_person_email, contact_person_phone, itn, iec, active, key_activation_mode, dict_client_group_id, password)
values('client name', 'JURIDICAL_PERSON','DF4344', 'Иванов Василий Петрович', 'ivanov@nowhere.com', '00000000000', '0000000000', '000000000', true, 'AUTOMATIC', 1, '$2a$10$FsxpCiHn9aayTuFU4Ei5QeJ0p/afltW7H5mhXyDjjdIDb8TcaOV0K');

insert into client(name, client_type, unique_id, contact_person_name, contact_person_email, contact_person_phone, itn, iec, active, key_activation_mode, dict_client_group_id, password)
values('client name2', 'JURIDICAL_PERSON','FFFE', 'Петров Николай Иванович', 'petrov@nowhere.com', '00000000000', '0000000000', '000000000', true, 'AUTOMATIC', 1, '$2a$10$FsxpCiHn9aayTuFU4Ei5QeJ0p/afltW7H5mhXyDjjdIDb8TcaOV0K');

insert into client(name, client_type, unique_id, contact_person_name, contact_person_email, contact_person_phone, itn, iec, active, key_activation_mode, dict_client_group_id, password, role)
values('service client name', 'JURIDICAL_PERSON','SSSS', 'Сервисов Сервис Сервисович', 'service@nowhere.com', '00000000000', '0000000000', '000000000', true, 'AUTOMATIC', 1, '$2a$10$FsxpCiHn9aayTuFU4Ei5QeJ0p/afltW7H5mhXyDjjdIDb8TcaOV0K', 'ROLE_SERVICE');


insert into users(username, password, enabled, fullname, sex, phone, post, birthdate, client_id, activated)
values('firstUserName', '$2a$10$LHfT41eU1kbG8x1ZDRH3uuadtETDDRhHi7yNiu/yN5lMYhXwmzPTu', true, 'first full name', 'MAN', '00000000', 'Главный менеджер', '2000-01-01', 1, true);

insert into users(username, password, enabled, fullname, sex, phone, post, birthdate, client_id, activated)
values('secondUserName', '$2a$10$LHfT41eU1kbG8x1ZDRH3uuadtETDDRhHi7yNiu/yN5lMYhXwmzPTu', true, 'first full name', 'MAN', '00000000', 'Главный менеджер2', '2000-01-01', 2, true);

insert into users(username, password, enabled, fullname, sex, phone, post, birthdate, client_id, activated)
values('service@nowhere.com', '$2a$10$LHfT41eU1kbG8x1ZDRH3uuadtETDDRhHi7yNiu/yN5lMYhXwmzPTu', true, 'service full name', 'MAN', '00000000', 'Главный менеджер2', '2000-01-01', 3, true);

insert into users(username, password, enabled, fullname, sex, phone, post, birthdate, client_id, activated)
values('firstUserName2', '$2a$10$LHfT41eU1kbG8x1ZDRH3uuadtETDDRhHi7yNiu/yN5lMYhXwmzPTu', true, 'first2 full name', 'WOMAN', '11111111', 'Товарищ сотрудник', '2000-12-05', 1, true);

insert into users(username, password, enabled, fullname, sex, phone, post, birthdate, client_id, activated)
values('service2@nowhere.com', '$2a$10$LHfT41eU1kbG8x1ZDRH3uuadtETDDRhHi7yNiu/yN5lMYhXwmzPTu', true, 'service2 full name', 'MAN', '00000000', 'уборщик', '2000-01-01', 3, true);


insert into authorities(user_id, authority) values(1, 'ROLE_CLIENT');
insert into authorities(user_id, authority) values(1, 'ROLE_CLIENT_ADMIN');

insert into authorities(user_id, authority) values(2, 'ROLE_CLIENT');
insert into authorities(user_id, authority) values(2, 'ROLE_CLIENT_ADMIN');

insert into authorities(user_id, authority) values(3, 'ROLE_SERVICE');
insert into authorities(user_id, authority) values(3, 'ROLE_SERVICE_ADMIN');

insert into authorities(user_id, authority) values(4, 'ROLE_CLIENT');

insert into authorities(user_id, authority) values(5, 'ROLE_SERVICE');


insert into dict_service_type(name, description, status, amount, amount_30_days, base_key)
values('first dict service type name', 'first dict service type description', 'ACTIVE', 121.01, 100, '+oBHXaxrdQF1dGklbUBNbmwzypcA140irDYiBtD4wadRu5haNr3QGQym3vMYm8vwZd3h+KNWb8b1
QIITsRrwcx+EQOlGL2l6EkEEvN3dNYpG0qI/A8ugTnlddciS8EuOUsQCH6iHjfruTtAH0k8ARFjD
FIjwboXPnAUszpFR/z+ehtjQrFg5WBcJG35x22aeJ6/jfbQZM0JjLXRTyBKPLki3WiHd5td1wQp5
8uh2JAgwdbWNoTBmC/Wn6mrlj6A6HpUW4KMvKCKDWJWPx06LZ0D/G8bTS2kwyHla0PU1RLbWgCDT
KI7QgkUZfeD+IHx3DH98w+XskwntQ3xw/gYxSgx/fMPl7JMJGCP5PvVbwNQMf3zD5eyTCdYOb82C
38MojiaqJKXtL2EwlV0WtF2cA3UuWM5RwIyDzs5yZqABGFhPXeh4SfD3Fp2T4ja5zgD9HgCwpbt2
1RSAYmDBHLyE7sQvB7Wey4S9fgCJB4v8rO7ELwe1nsuEvVDgYbOCMw23DE5mJIj8JnsZq016MAzz
/t1XbAstwS3Sl9mImqN0ae+DA5N/f1mt1CSuuz6gQ0LgdJdnbFzbjY07UXQ4wUMPGQ==');

insert into dict_service_type(name, description, status, amount, base_key)
values('second dict service type name', 'second dict service type description', 'BLOCKED', 99.99, '+oBHXaxrdQF1dGklbUBNbmwzypcA140irDYiBtD4wadRu5haNr3QGQym3vMYm8vwZd3h+KNWb8b1
QIITsRrwcx+EQOlGL2l6EkEEvN3dNYpG0qI/A8ugTnlddciS8EuOUsQCH6iHjfruTtAH0k8ARFjD
FIjwboXPnAUszpFR/z+ehtjQrFg5WBcJG35x22aeJ6/jfbQZM0JjLXRTyBKPLki3WiHd5td1wQp5
8uh2JAgwdbWNoTBmC/Wn6mrlj6A6HpUW4KMvKCKDWJWPx06LZ0D/G8bTS2kwyHla0PU1RLbWgCDT
KI7QgkUZfeD+IHx3DH98w+XskwntQ3xw/gYxSgx/fMPl7JMJGCP5PvVbwNQMf3zD5eyTCdYOb82C
38MojiaqJKXtL2EwlV0WtF2cA3UuWM5RwIyDzs5yZqABGFhPXeh4SfD3Fp2T4ja5zgD9HgCwpbt2
1RSAYmDBHLyE7sQvB7Wey4S9fgCJB4v8rO7ELwe1nsuEvVDgYbOCMw23DE5mJIj8JnsZq016MAzz
/t1XbAstwS3Sl9mImqN0ae+DA5N/f1mt1CSuuz6gQ0LgdJdnbFzbjY07UXQ4wUMPGQ==');

-- Для тестов PayU LU
insert into dict_service_type(name, description, status, amount, amount_30_days, base_key)
values('Test_pname', 'Test_info', 'ACTIVE', 1234, 100, null);

insert into orders(createdat, createdby, dict_service_type_id, status, pay_datetime, completed_datetime, key_activation_mode)
values('2000-01-01 10:00:01', 1, 1, 'COMPLETED', '2000-01-02 01:01:10', '2000-01-03 02:02:30', 'AUTOMATIC');


insert into orders(createdat, createdby, dict_service_type_id, status, pay_datetime, completed_datetime, key_activation_mode)
values('2000-01-02', 1, 1, 'COMPLETED', '2000-01-03', '2000-01-04', 'MANUAL');

insert into orders(createdat, createdby, dict_service_type_id, status, pay_datetime, completed_datetime, key_activation_mode)
values('2000-01-03', 2, 1, 'COMPLETED', '2000-01-05', '2000-01-05', 'MANUAL');

insert into orders(createdat, createdby, dict_service_type_id, status, pay_datetime, completed_datetime, key_activation_mode)
values('2014-01-13', 2, 4, 'PENDING_PAYMENT', null, null, 'MANUAL');

insert into payment(createdat, client_id, order_id, payment_type, status, method, amount_without_commission, amount_with_commission, amount_of_commission, info, comment, document, payment_id)
values('2000-01-01 13:00:34', 1, 1, 'ORDER_PAYMENT', 'SCHEDULED', 'CHECK', 90.01, 100.02, 10.01, 'Информация о платеже', 'Сложный комментарий', null, null);



insert into dict_action(name, description, date_begin, date_end, dict_service_type_id, dict_action_type, for_new_clients, email_title, email_body)
values('first dict action', 'Описание первой акции', '2000-01-01', '3000-02-02',1,'PERSONAL', true, 'title', 'body') ;


insert into user_access(code, access, user_id) values('SERVICE_READ_ORDER', true, 1);

insert into user_access(code, access, user_id) values('SERVICE_READ_PAYMENT', false, 1);

insert into user_access(code, access, user_id) values('SERVICE_ADMIN_CLIENT', true, 3);

insert into link_user_dict_event_notification(user_id, dict_event_id, allow_notification) values(1,1, true);

insert into link_user_dict_event_notification(user_id, dict_event_id, allow_notification) values(3,1, true);


insert into dict_action(name, description, date_begin, date_end, dict_service_type_id, dict_action_type, for_new_clients, email_title, email_body)
values('findKeysByDictActionTest', 'findKeysByDictActionTest', '2000-01-01', '3000-02-02',1,'PERSONAL', true, 'title', 'body') ;

insert into key(createdAt, order_id, dict_action_id, status, date_begin, date_end, client_id)
values('2000-01-01', 1, null, 'CREATED', '2000-01-01 00:00:00', '3000-01-01 00:00:00', 1);

insert into key(createdAt, dict_action_id, status, date_begin, date_end, client_id, code)
values('2000-01-01', 2, 'ACTIVE', '2000-01-01 00:00:00', '2000-01-30 00:00:00', 1, '+oBHXaxrdQF1dGklbUBNbmwzypcA140irDYiBtD4wadRu5haNr3QGQym3vMYm8vwZd3h+KNWb8b1
QIITsRrwcx+EQOlGL2l6EkEEvN3dNYpG0qI/A8ugTnlddciS8EuOUsQCH6iHjfruTtAH0k8ARFjD
FIjwboXPnAUszpFR/z+ehtjQrFg5WBcJG35x22aeJ6/jfbQZM0JjLXRTyBKPLki3WiHd5td1wQp5
8uh2JAgwdbWNoTBmC/Wn6mrlj6A6HpUW4KMvKCKDWJWPx06LZ0D/G8bTS2kwyHla0PU1RLbWgCDT
KI7QgkUZfeD+IHx3DH98w+XskwntQ3xw/gYxSgx/fMPl7JMJGCP5PvVbwNQMf3zD5eyTCdYOb82C
38MojiaqJKXtL2EwlV0WtF2cA3UuWM5RwIyDzs5yZqABGFhPXeh4SfD3Fp2T4ja5zgD9HgCwpbt2
1RSAYmDBHLyE7sQvB7Wey4S9fgCJB4v8rO7ELwe1nsuEvVDgYbOCMw23DE5mJIj8JnsZq016MAzz
/t1XbAstwS3Sl9mImqN0ae+DA5N/f1mt1CSuuz6gQ0LgdJdnbFzbjY07UXQ4wUMPGQ==');



insert into key(createdAt, dict_action_id, status, date_begin, date_end, client_id)
values('2000-01-01 01:00:00', 2, 'CREATED', null, null, 1);



insert into key(createdat, order_id, status, date_begin, date_end, code, client_id)
values('2000-01-01 02:00:00', 3,  'ACTIVE', '2014-01-01', '2014-01-31', null, 2);


insert into key(createdat, order_id, status, date_begin, date_end, code, client_id)
values('2000-01-01', 3,  'ACTIVE', '2014-01-31', '2014-03-02', null, 2);

insert into key(createdat, order_id, status, date_begin, date_end, code, client_id)
values('2000-01-01', 3,  'CREATED', null, null, null, 2);



insert into dict_service_type(name, description, status, amount, amount_30_days, base_key, version)
values('third dict service type', 'third dict service type', 'BLOCKED', 2.0, 3.0, null, 0);

insert into link_client_dict_service_type(client_id, dict_service_type_id) values(2,3);

