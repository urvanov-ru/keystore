drop function if exists list_user(bigint);

create or replace function list_user(
	IN user_id_in                     bigint,
	OUT id_out                        bigint,
	OUT username_out                  character varying,
	OUT fullname_out                  character varying,
	OUT sex_name_out                  character varying,
	OUT phone_out                     character varying,
	OUT post_out                      character varying,
	OUT birthdate_out                 date,
	OUT client_name_out               character varying,
	OUT enabled_out                   boolean
)
returns setof record as 
$BODY$
DECLARE var_client_id bigint;
DECLARE var_service_admin_client boolean;
begin

	var_client_id := (select client_id from users where id = user_id_in limit 1);

	var_service_admin_client := exists(select 1 from user_access ua where ua.user_id = user_id_in and ua.access = true and ua.code = 'SERVICE_ADMIN_CLIENT')
		and exists(select 1 from authorities a where a.user_id = user_id_in and a.authority = 'ROLE_SERVICE');
	
	create local temp table temp_list_user(
		id                        bigint,
		username                  character varying,
		fullname                  character varying,
		sex                       character varying,
		sex_name                  character varying,
		phone                     character varying,
		post                      character varying,
		birthdate                 date,
		client_id                 bigint,
		client_name               character varying,
		enabled                   boolean
	);


	insert into temp_list_user(
		id,
		username,
		fullname,
		sex,
		phone,
		post,
		birthdate,
		client_id,
		enabled
	)
	select
		u.id,
		u.username,
		u.fullname,
		u.sex,
		u.phone,
		u.post,
		u.birthdate,
		u.client_id,
		u.enabled
	from
		users u
	where
		u.client_id = var_client_id or var_service_admin_client;
	
		
	update temp_list_user set
		sex_name = case temp_list_user.sex
			when 'MAN' then 'Мужской'
			when 'WOMAN' then 'Женский'
			end;

	update temp_list_user set
		client_name = c.name
	from
		client c
	where c.id = temp_list_user.client_id;
	

	return query select
		id,
		username,
		fullname,
		sex_name,
		phone,
		post,
		birthdate,
		client_name,
		enabled
	from
		temp_list_user;
	drop table temp_list_user;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
