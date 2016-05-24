drop function if exists list_key(bigint, character varying, character varying, date, bigint);
drop function if exists list_key(bigint, character varying, character varying, date, date);
drop function if exists list_key(bigint, character varying, character varying, date);

create or replace function list_key(
	IN user_id_in                     bigint,
	IN client_name_in                 character varying,
	IN status_in                      character varying,
	IN active_on_date_in              date,
	IN order_id_in                    bigint,
	OUT id_out                        bigint,
	OUT dict_service_type_name_out    character varying,
	OUT basis_out                     character varying,
	OUT status_out                    character varying,
	OUT status_name_out               character varying,
	OUT date_begin_out                character varying,
	OUT date_end_out                  character varying,
	OUT dict_action_id_out            bigint
)
returns setof record as 
$BODY$
declare is_user_admin boolean;
declare user_client_id bigint;
begin

	is_user_admin := exists(select * from authorities where user_id = user_id_in and authority='ROLE_ADMIN');

	user_client_id := (select client_id from users where id = user_id_in);
	
	create local temp table temp_list_key(
		id                            bigint,
		dict_service_type_id          bigint,
		dict_service_type_name        character varying,
		dict_action_id                bigint,
		order_id                      bigint,
		basis                         character varying,
		status                        character varying,
		status_name                   character varying,
		date_begin                    character varying,
		date_end                      character varying,
		client_id                     bigint,
		client_name                   character varying
	);


	insert into temp_list_key(
		id,
		dict_service_type_id,
		dict_action_id,
		order_id,
		status,
		date_begin,
		date_end,
		client_id
	)
	select
		k.id,
		null,
		k.dict_action_id,
		k.order_id,
		k.status,
		k.date_begin,
		k.date_end,
		k.client_id
	from
		key k
	where
		(active_on_date_in is null or k.date_begin is not null and k.date_end is not null
			and k.date_begin <= active_on_date_in and k.date_end >= active_on_date_in) and
		(status_in is null or status_in = k.status) and
		(k.client_id = user_client_id OR is_user_admin) and
		(order_id_in is null or k.order_id = order_id_in);

	update temp_list_key set
		client_name = c.name
	from client c
	where c.id = temp_list_key.client_id;

	update temp_list_key set
		basis = 'Акция ' || da.name,
		dict_service_type_id = da.dict_service_type_id
	from dict_action da
	where da.id = temp_list_key.dict_action_id;

	update temp_list_key set
		basis = 'Ордер ',
		dict_service_type_id = o.dict_service_type_id
	from orders o
	where o.id = temp_list_key.order_id;
		
	update temp_list_key set
		dict_service_type_name = dct.name
	from dict_service_type dct
	where dct.id = temp_list_key.dict_service_type_id;

	
	update temp_list_key set
			client_name = c.name
		from users u
			join client c on u.client_id = c.id
		where u.id = temp_list_key.client_id;
	
	if (client_name_in is not null) then
		
		delete from temp_list_key
		where not lower(coalesce(client_name, '')) like '%' || lower(client_name_in) || '%';
	end if;

	
		
	update temp_list_key set
		status_name = case temp_list_key.status
			when 'CREATED' then 'Создан'
			when 'ACTIVE' then 'Активен'
			when 'EXPIRED' then 'Истёк'
			when 'CANCELED' then 'Отменён'
			end;
		
	return query select
		id,
		dict_service_type_name,
		basis,
		status,
		status_name,
		date_begin,
		date_end,
		dict_action_id
	from
		temp_list_key;
	drop table temp_list_key;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
