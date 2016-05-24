create table dict_client_group(
	id                                bigserial                 not null,
	name                              character varying(100)    NOT NULL,
	dict_client_group_id              bigint,
	version                           int                       not null default 0
);

alter table dict_client_group add constraint pk_dict_client_group primary key (id);

alter table dict_client_group add constraint fk_dict_client_group_dict_client_group_id foreign key (dict_client_group_id)
references dict_client_group(id) on update no action on delete no action;


create table client(
	id                                bigserial not null,
	createdAt                         timestamp with time zone    NOT NULL,
	name                              character varying(1024)     NOT NULL,
	client_type                       character varying(100)      NOT NULL,
	unique_id                         character varying(255)      NOT NULL,
	contact_person_name               character varying(255)      NOT NULL,
	contact_person_email              character varying(50)       NOT NULL,
	contact_person_phone              character varying(50)       NOT NULL,
	itn                               character varying(12),
	iec                               character varying(9),
	active                            boolean                     NOT NULL,
	key_activation_mode               character varying(100)      NOT NULL,
	dict_client_group_id              bigint                      NOT NULL,
	juridical_person_name             character varying(1024),
	juridical_person_address          character varying(1024),
	bank_name                         character varying(1024),
	account                           character varying(20),
	corr_account                      character varying(20),
	bic                               character varying(9),
	authority                         character varying(50)       NOT NULL,
	version                           int                         not null default 0,
	password                          character varying(60)       NOT NULL
);

alter table client add constraint pk_client primary key (id);

alter table client add constraint uq_client_unique_id unique(unique_id);

create index idx_client_active on client(active);

alter table client add constraint fk_client_dict_client_group_id foreign key (dict_client_group_id)
references dict_client_group(id) on update no action on delete no action;


create table users(
	id                                bigserial                   not null,
	username                          character varying(50)       not null,
	password                          character varying(60)       not null,
	enabled                           boolean                     not null,
	fullname                          character varying(1024),
	sex                               character varying(50),
	phone                             character varying(50),
	post                              character varying(50),
	birthdate                         date,
	client_id                         bigint                      not null,
	activation_key                    character varying(1024),
	activated                         boolean                     not null default false,
	change_password_key               character varying(1024),
	version                           int                         not null default 0
);


alter table users add constraint pk_users primary key (id);

create unique index uq_users_username on users(lower(username));

alter table users add constraint fk_users_client_id foreign key (client_id)
references client(id) on update no action on delete no action;

create table authorities(
	id bigserial  not null,
	user_id                           bigint                      not null,
	authority                         character varying(50)       not null,
	version                           int                         not null default 0
);

alter table authorities add constraint pk_authorities primary key (id);

alter table authorities add constraint fk_authorities_user_id foreign key(user_id)
references users(id) on update no action on delete no action;

create table dict_service_type(
	id                                bigserial                   not null,
	name                              character varying(100)      not null,
	description                       character varying(1024)     not null,
	status                            character varying(100)      not null,
	amount                            numeric (18,2)              not null,
	amount_30_days                    numeric (18,2)              not null,
	base_key                          character varying,
	version                           int                         not null default 0
);

alter table dict_service_type add constraint pk_dict_service_type primary key (id);


create table orders (
	id                                bigserial                   not null,
	createdAt                         timestamp with time zone    not null,
	createdBy                         bigint                      not null,
	dict_service_type_id              bigint                      not null,
	status                            character varying(100)      not null,
	pay_datetime                      timestamp with time zone,
	completed_datetime                timestamp with time zone,
	key_activation_mode               character varying(100)      not null,
	payu_ref                          character varying,
	version                           int                         not null default 0
);

alter table orders add constraint pk_orders primary key (id);

create index idx_orders_createdAt on orders(createdAt);

create index ids_orders_status on orders(status);

alter table orders add constraint fk_orders_createdBy foreign key (createdBy)
references users(id) on update no action on delete no action;

alter table orders add constraint fk_orders_dict_service_type_id foreign key (dict_service_type_id)
references dict_service_type(id) on update no action on delete no action;


create table payment(
	id                                bigserial                   not null,
	createdAt                         timestamp with time zone    not null,
	order_id                          bigint                      not null,
	payment_type                      character varying(100)      not null,
	status                            character varying(100)      not null,
	method                            character varying(100)      not null,
	amount_without_commission         numeric(18,2)               not null,
	amount_with_commission            numeric(18,2)               not null,
	amount_of_commission              numeric(18,2)               not null,
	info character varying(255),
	comment character varying(1024),
	document oid,
	payment_id bigint,
	version int not null default 0
);

alter table payment add constraint pk_payment primary key (id);

create index idx_payment_payment_type on payment(payment_type);

create index idx_payment_status on payment(status);

create index idx_payment_method on payment(method);

create index idx_payment_createdAt on payment(createdAt);
alter table payment add constraint fk_payment_payment_id foreign key (payment_id)
references payment(id) on update no action on delete no action;

alter table payment add constraint fk_payment_order_id foreign key (order_id)
references orders(id) on update no action on delete no action;


create table dict_action(
	id                                bigserial                   not null,
	name                              character varying(100)      not null,
	description                       character varying(1024)     not null,
	date_begin                        date                        null,
	date_end                          date                        null,
	dict_service_type_id              bigint                      not null,
	dict_action_type                  character varying(100)      not null,
	for_new_clients                   boolean                     not null,
	version                           int                         not null default 0
);

alter table dict_action add constraint pk_dict_action primary key (id);

alter table dict_action add constraint pk_dict_action_dict_service_type_id foreign key (dict_service_type_id)
references dict_service_type(id) on update no action on delete no action;

create table key(
	id                                bigserial                   not null,
	createdAt                         timestamp with time zone    not null,
	order_id                          bigint,
	dict_action_id                    bigint,
	status                            character varying(100)      not null,
	date_begin                        date,
	date_end                          date,
	code                              character varying,
	client_id                         bigint                      not null,
	version                           int                         not null default 0
);

alter table key add constraint pk_key primary key(id);

create index idx_key_status on key(status);

create index idx_date_begin_date_end on key(date_begin, date_end);

alter table key add constraint fk_key_order_id foreign key (order_id)
references orders(id) on update no action on delete no action;

alter table key add constraint fk_key_dict_action_id foreign key (dict_action_id)
references dict_action(id) on update no action on delete no action;

alter table key add constraint fk_key_client_id foreign key (client_id)
references client(id) on update no action on delete no action;


create table user_access(
	id                                bigserial                   not null,
	code                              character varying(100)      not null,
	access                            boolean                     not null,
	user_id                           bigint                      not null,
	version                           int                         not null default 0
);

alter table user_access add constraint pk_user_access primary key(id);

alter table user_access add constraint fk_user_access_user_id foreign key (user_id)
references users(id) on update no action on delete no action;

alter table user_access add constraint uq_user_access_code_user_id unique (code, user_id);



create table dict_event(
	id                                bigint                      not null,
	code                              character varying(100)      not null,
	title                             character varying(1024)     not null,
	body                              character varying           not null,
	version                           int                         not null default 0
);

alter table dict_event add constraint pk_dict_event primary key(id);

alter table dict_event add constraint uq_dict_event_code unique(code);


create table link_user_dict_event_notification(
	id                                bigserial                   not null,
	user_id                           bigint                      not null,
	dict_event_id                     bigint                      not null,
	allow_notification                boolean                     not null,
	version                           int not null                default 0
);

alter table link_user_dict_event_notification add constraint pk_link_user_dict_event_notification_id primary key (id);

alter table link_user_dict_event_notification add constraint uq_link_user_dict_event_notification_user_id_dict_event_id unique(user_id, dict_event_id);

alter table link_user_dict_event_notification add constraint fk_link_user_dict_event_notification_user_id foreign key (user_id)
references users(id) on update no action on delete no action;

alter table link_user_dict_event_notification add constraint fk_link_user_dict_event_notification_dict_event_id foreign key (dict_event_id)
references dict_event(id) on update no action on delete no action;


create table link_client_dict_service_type(
	id                                bigserial                   not null,
	client_id                         bigint                      not null,
	dict_service_type_id              bigint                      not null,
	version                           int                         not null default 0
);

alter table link_client_dict_service_type add constraint pk_link_client_dict_service_type primary key(id);

alter table link_client_dict_service_type add constraint uq_link_client_dict_service_type_client_id_dict_service_type_id unique(client_id, dict_service_type_id);

alter table link_client_dict_service_type add constraint fk_link_client_dict_service_type_client_id foreign key (client_id)
references client(id) on update no action on delete no action;

alter table link_client_dict_service_type add constraint fk_link_client_dict_service_type_dict_service_type_id foreign key (dict_service_type_id)
references dict_service_type(id) on update no action on delete no action;

create table link_client_dict_action(
	id                                bigserial                   not null,
	client_id                         bigint                      not null,
	dict_action_id                    bigint                      not null,
	version                           int not null default 0
);

alter table link_client_dict_action add constraint pk_link_client_dict_action primary key(id);

alter table link_client_dict_action add constraint uq_link_client_dict_action_client_id_dict_action_id unique(client_id, dict_action_id);

alter table link_client_dict_action add constraint fk_link_client_dict_action_client_id foreign key (client_id)
references client(id) on update no action on delete no action;

alter table link_client_dict_action add constraint fk_link_client_dict_action_dict_action_id foreign key (dict_action_id)
references dict_action(id) on update no action on delete no action;


create table session_log(
	user_id                           bigint                      not null,
	session_begin                     timestamp with time zone    not null,
	session_end                       timestamp with time zone    not null
);


alter table session_log add constraint fk_session_log_user_id foreign key (user_id)
references users(id) on update no action on delete no action;

create table global_settings(
	id                                bigint                     not null,
	default_dict_client_group_id      bigint                     not null,
	session_store_days                int                        not null,
	version                           int                        not null default 0
);

alter table global_settings add constraint pk_global_settings primary key (id);

alter table global_settings add constraint fk_global_settings_default_dict_client_group_id foreign key (default_dict_client_group_id)
references dict_client_group(id) on update no action on delete no action;


insert into dict_client_group(name) values('Группа по умолчанию');



insert into dict_event(id, code, title, body, version) select 1,'CLIENT_ADDED', 'Добавление клиентского аккаунта','Уважаемый ${#user.fullName}$.
В систему KeyStore был добавлен новый клиентский аккаунт ${#newClient.name}$.',0;

insert into dict_event(id, code, title, body, version) select 2,'CLIENT_CHANGED', 'Изменение данных аккаунта','Уважаемый ${#user.fullName}$.
Были изменены данные вашего аккаунта в системе KeyStore.
Новые данные:
Название клиента: ${#changedClient.name == null ? '''' : #changedClient.name}$
Контактное лицо: ${#changedClient.contactPersonName == null ? '''' : #changedClient.contactPersonName}$
e-mail: ${#changedClient.email == null ? '''' : #changedClient.email}$
телефон: ${#changedClient.contactPersonPhone == null ? '''' : #changedClient.contactPersonPhone}$
Название юридического лица: ${#changedClient.juridicalPersonName == null ? '''' : #changedClient.juridicalPersonName}$ 
ИНН: ${#changedClient.itn == null ? '''' : #changedClient.itn}$
КПП: ${#changedClient.iec == null ? '''' : #changedClient.iec}$',0;

insert into dict_event(id, code, title, body, version) select 3,'CLIENT_BLOCKED', 'Блокировка аккаунта','Уважаемый ${#user.fullName}$.
Ваш аккаунт ${#blockClient.name}$ заблокирован.',0;

insert into dict_event(id, code, title, body, version) select 4,'USER_ADDED', 'Добавление учётной записи пользователя','Уважаемый ${#user.fullName}$.
В Ваш аккаунт в системе KeyStore была добавлена новая учётная запись пользователя ${#newUser.fullName}$.',0;

insert into dict_event(id, code, title, body, version) select 5,'USER_CHANGED', 'Изменение данных учётной записи пользователя','Уважаемый(ая) ${#user.fullName}$.
Были изменены данные вашей учётной записи в системе KeyStore.
Новые данные:
email: ${#user.userName == null ? '''' : #user.userName}$
полное имя: ${#user.fullName == null ? '''' : #user.fullName}$
телефон: ${#user.phone == null ? '''' : #user.phone}$
Должность: ${#user.post == null ? '''' : #user.post}$
Дата рождения: ${#user.birthdate == null ? '''' : #user.birthdate}$',0;

insert into dict_event(id, code, title, body, version) select 6,'USER_BLOCKED', 'Блокировка вашей учётной записи','Уважаемый ${#user.fullName}$.
Ваша учётная запись в KeyStore была заблокирована.',0;

insert into dict_event(id, code, title, body, version) select 7,'ORDER_CREATED', 'Размещение заказа','Уважаемый(ая) ${#user.fullName}$.
Уведомляем Вас, что в KeyStore был размещён новый заказ ${#newOrder.createdAt}$.',0;

insert into dict_event(id, code, title, body, version) select 8,'PAYED', 'Поступление платежа','Уважаемый(ая) ${#user.fullName}$.
В Ваш аккаунт поступил новый платёж на сумму ${#newPayment.amountWithoutCommission}$.',0;

insert into dict_event(id, code, title, body, version) select 9,'PAY_BACK', 'Возврат средств','Уважаемый ${#user.fullName}$.
В KeyStore была инициирована процедура возврата средств на сумму ${#payBackPayment.amountWithoutCommission}$.',0;

insert into dict_event(id, code, title, body, version) select 10,'PAY_CORRECTED', 'Уведомление о создании корректирующей проводки','Уважаемый(ая) ${#user.fullName}$.
В системе KeyStore была создана новая корректирующая проводка на сумму ${#correctingPayment.amountWithoutCommission}$',0;

insert into dict_event(id, code, title, body, version) select 11,'ACTION', 'Вы учавствуете в специальной акции','Уважаемый(ая) ${#user.fullName}$.
Уведомляем Вас, что вы участвуете в специальной акции ${#givenDictAction.name}$',0;

insert into dict_event(id, code, title, body, version) select 12,'SERVICE', 'Предложение пакета услуг','Уважаемый(ая) ${#user.fullName}$.
Вам был предложен новые пакет услуг ${#givenDictServiceType.name}$. Вы можете выбрать его при создании нового заказа в системе KeyStore.',0;

insert into dict_event(id, code, title, body, version) select 13,'UPDATE', 'Появление обновлений приложения','Появление обновлений приложения',0;

insert into dict_event(id, code, title, body, version) select 14,'CLIENTS', 'Обращение к клиентам','Обращение к клиентам.',0;

insert into dict_event(id, code, title, body, version) select 15,'USERS', 'Обращение к пользователям','Обращение к пользователям',0;
insert into dict_event(id, code, title, body, version) select 16,'BIRTHDAY', 'С Днём Рождения','Уважаемый(ая) ${#user.fullName}$.
Поздравляем с Днём Рождения.',0;

insert into global_settings(id, default_dict_client_group_id, session_store_days) values(1, 1,1);


insert into client(createdAt, name,client_type,unique_id,contact_person_name,contact_person_email,contact_person_phone,itn,iec,active,
key_activation_mode,dict_client_group_id,juridical_person_name, authority, password)
values(now(), 'АДМИНИСТРАТОР KeyStore',	'JURIDICAL_PERSON',	'UNIQUE_ID',	'Иванов Василий Петрович',	
'test2@urvanov.ru',	'325234623',	'112343245124',	'136789098',	true,	'AUTOMATIC',	1,	'АДМИНИСТАТОР KeyStore', 'ROLE_SERVICE', '__');

insert into users(username, password, enabled, fullname, client_id, activated)
select 'test2@urvanov.ru', '$2a$10$1j.CcluRCpa2JTa9UZ.yKeijRDZHOlUKchBqLXcH5lHt.VDxv4y0C', true, 'Иванов Василий Петрович', c.id, true
from client c
where c.name = 'АДМИНИСТРАТОР KeyStore' and authority = 'ROLE_SERVICE';

insert into authorities(user_id, authority)
select u.id, 'ROLE_SERVICE'
from users u where username = 'test2@urvanov.ru';



insert into user_access(code, access, user_id) select 'SERVICE_ADMIN_ACCOUNT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_ADMIN_CLIENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_READ_CLIENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_NEW_CLIENT_NOTIFICATION', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_ADD_CLIENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_EXPORT_CLIENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_READ_ORDER', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_EXPORT_ORDER', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_READ_PAYMENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_EXECUTE_PAYMENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_EXPORT_PAYMENT', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_READ_ACTION', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_WRITE_ACTION', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_EXPORT_ACTION', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_SEND_NOTIFICATION', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_SETTING_CLIENT_GROUP', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_SETTING_DICT_SERVICE_TYPE', true, u.id from users u where u.username = 'test2@urvanov.ru';
insert into user_access(code, access, user_id) select 'SERVICE_SETTING_EMAIL_TEMPLATE', true, u.id from users u where u.username = 'test2@urvanov.ru';







