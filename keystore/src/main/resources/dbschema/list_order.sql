drop function if exists list_order(bigint, character varying, character varying, date, date);

create or replace function list_order(
	IN user_id_in                     bigint,
	IN client_name_in                 character varying,
	IN status_in                      character varying,
	IN createdAt_begin_in             date,
	IN createdAt_end_in               date,
	OUT id_out                        bigint,
	OUT dict_service_type_name_out    character varying,
	OUT status_out                    character varying,
	OUT status_name_out               character varying,
	OUT createdBy_name_out            character varying,
	OUT createdAt_out                 timestamp with time zone,
	OUT pay_datetime_out              timestamp with time zone,
	OUT completed_datetime_out        timestamp with time zone,
	OUT client_name_out               character varying
)
returns setof record as 
$BODY$
declare is_user_admin boolean;
declare user_client_id bigint;
begin

	is_user_admin := exists(select * from authorities where user_id = user_id_in and authority='ROLE_ADMIN');

	user_client_id := (select client_id from users where id = user_id_in);
	
	create local temp table temp_list_order(
		id                        bigint,
		dict_service_type_name    character varying,
		status                    character varying,
		status_name               character varying,
		createdBy_name            character varying,
		createdAt                 timestamp with time zone,
		pay_datetime              timestamp with time zone,
		completed_datetime        timestamp with time zone,
		client_name               character varying,
		createdBy                 bigint
	);


	insert into temp_list_order(
		id,
		dict_service_type_name,
		status,
		createdBy_name,
		createdAt,
		pay_datetime,
		completed_datetime,
		createdBy
	)
	select
		o.id,
		dct.name,
		o.status,
		u.fullname,
		o.createdAt,
		o.pay_datetime,
		o.completed_datetime,
		o.createdBy
	from
		orders o
	left outer join
		dict_service_type dct on dct.id = o.dict_service_type_id
	left outer join
		users u on u.id = o.createdBy
	where
		(createdAt_begin_in is null or createdAt_begin_in::date<= o.createdAt::date) and
		(createdAt_end_in is null or createdAt_end_in::date >= o.createdAt::date) and
		(status_in is null or status_in = o.status) and
		(coalesce(u.client_id,0) = coalesce(user_client_id,0) OR is_user_admin);

	update temp_list_order set
			client_name = c.name
		from users u
			join client c on u.client_id = c.id
		where u.id = temp_list_order.createdBy;
	
	if (client_name_in is not null) then
		
		delete from temp_list_order
		where not lower(coalesce(client_name, '')) like '%' || lower(client_name_in) || '%';
	end if;
		
	update temp_list_order set
		status_name = case temp_list_order.status
			when 'PENDING_PAYMENT' then 'Ожидает оплаты'
			when 'PAYED' then 'Оплачен'
			when 'COMPLETED' then 'Выполнен'
			when 'CANCEL' then 'Отмена'
			when 'PAY_BACK' then 'Возврат'
			when 'REJECT' then 'Отказ'
			end;
		
	return query select
		id,
		dict_service_type_name,
		status,
		status_name,
		createdBy_name,
		createdAt,
		pay_datetime,
		completed_datetime,
		client_name
	from
		temp_list_order;
	drop table temp_list_order;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
