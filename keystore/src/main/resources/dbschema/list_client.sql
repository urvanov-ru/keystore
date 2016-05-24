drop function if exists list_client();

drop function if exists list_client(bigint, boolean, bigint, date, date);

drop function if exists list_client(bigint, boolean, bigint, date, date, bigint);

create or replace function list_client(
	IN user_id_in                     bigint,
	IN active_in                      boolean,
	IN dict_client_group_id_in        bigint,
	IN active_begin_in                date,
	IN active_end_in                  date,
	IN dict_action_id_in              bigint,
	OUT id_out                        bigint,
	OUT name_out                      character varying,
	OUT client_type_out               character varying,
	OUT unique_id_out                 character varying,
	OUT contact_person_name_out       character varying,
	OUT contact_person_email_out      character varying,
	OUT contact_person_phone_out      character varying,
	OUT itn_out                       character varying,
	OUT iec_out                       character varying,
	OUT active_out                    character varying,
	OUT client_group_name_out         character varying,
	OUT juridical_person_name_out     character varying
)
returns setof record as 
$BODY$
DECLARE var_is_user_admin boolean;
begin
	var_is_user_admin:= exists(select * from authorities where user_id = user_id_in and authority='ROLE_ADMIN');
	
	create local temp table temp_list_client(
		id                        bigint,
		name                      character varying,
		client_type               character varying,
		unique_id                 character varying,
		contact_person_name       character varying,
		contact_person_email      character varying,
		contact_person_phone      character varying,
		itn                       character varying,
		iec                       character varying,
		active                    character varying,
		client_group_name         character varying,
		juridical_person_name     character varying,
		flag                      boolean
	);


	insert into temp_list_client(
		id,
		name,
		client_type,
		unique_id,
		contact_person_name,
		contact_person_email,
		contact_person_phone,
		itn,
		iec,
		active,
		client_group_name,
		juridical_person_name)
	select
		c.id,
		c.name,
		c.client_type,
		c.unique_id,
		c.contact_person_name,
		c.contact_person_email,
		contact_person_phone,
		c.itn,
		c.iec,
		c.active,
		dcg.name,
		c.juridical_person_name
	from
		client c
	left outer join
		dict_client_group dcg on dcg.id = c.dict_client_group_id
	where (active_in is null or c.active = active_in) and
		(dict_client_group_id_in is null or c.dict_client_group_id = dict_client_group_id_in);

	if (dict_action_id_in is not null) then
		update temp_list_client set flag = true;
		
		update temp_list_client set
			flag = false
		from link_client_dict_action lcda
		where lcda.client_id = temp_list_client.id and lcda.dict_action_id = dict_action_id_in;
		
		delete from temp_list_client
		where flag = true;
	end if;
		
	update temp_list_client set
		client_type = case temp_list_client.client_type
			when 'JURIDICAL_PERSON' then 'Юридическое лицо'
			when 'INDIVIDUAL_ENTREPRENEUR' then 'Индивидуальный предприниматель'
			when 'NATURAL_PERSON' then 'Физическое лицо'
			end;

	update temp_list_client set
		active = 'Активен'
	where active = 'true';

	update temp_list_client set
		active = 'Заблокирован'
	where active = 'false';
	

	return query select
		id,
		name,
		client_type,
		unique_id,
		contact_person_name,
		contact_person_email,
		contact_person_phone,
		itn,
		iec,
		active,
		client_group_name,
		juridical_person_name
	from
		temp_list_client;
	drop table temp_list_client;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
