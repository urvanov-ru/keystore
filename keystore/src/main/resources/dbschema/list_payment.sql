drop function if exists list_payment(bigint, character varying, character varying, date, date);


create or replace function list_payment(
	IN user_id_in                     bigint,
	IN client_name_in                 character varying,
	IN payment_type_in                character varying,
	IN status_in                      character varying,
	IN method_in                      character varying,
	IN createdAt_begin_in             date,
	IN createdAt_end_in               date,
	IN order_id_in                    bigint,
	OUT id_out                        bigint,
	OUT createdAt_out                 timestamp with time zone,
	OUT order_createdAt_out           timestamp with time zone,
	OUT order_dict_service_type_name_out character varying,
	OUT payment_type_out              character varying,
	OUT payment_type_name_out         character varying,
	OUT status_out                    character varying,
	OUT status_name_out               character varying,
	OUT method_out                    character varying,
	OUT method_name_out               character varying,
	OUT client_name_out               character varying,
	OUT amount_without_commission_out    numeric(18,2),
	OUT amount_with_commission_out    numeric(18,2),
	OUT amount_of_commission_out      numeric(18,2),
	OUT info_out                      character varying(255),
	OUT comment_out                   character varying(1024)
)
returns setof record as 
$BODY$
declare is_user_admin boolean;
declare user_client_id bigint;
begin

	is_user_admin := exists(select * from authorities where user_id = user_id_in and authority='ROLE_SERVICE');

	user_client_id := (select client_id from users where id = user_id_in);
	
	create local temp table temp_list_payment(
		id                               bigint,
		createdAt                        timestamp with time zone,
		order_createdAt                  timestamp with time zone,
		order_dict_service_type_name     character varying,
		payment_type                     character varying,
		payment_type_name                character varying,
		status                           character varying,
		status_name                      character varying,
		method                           character varying,
		method_name                      character varying,
		client_name                      character varying,
		amount_without_commission        numeric(18,2),
		amount_with_commission           numeric(18,2),
		amount_of_commission             numeric(18,2),
		info                             character varying(255),
		comment                          character varying(1024)
	);


	insert into temp_list_payment(
		id,
		createdAt,
		order_createdAt,
		order_dict_service_type_name,
		payment_type,
		status,
		method,
		client_name,
		amount_without_commission,
		amount_with_commission,
		amount_of_commission,
		info,
		comment
	)
	select
		p.id,
		p.createdAt,
		o.createdAt,
		dct.name,
		p.payment_type,
		p.status,
		p.method,
		c.name,
		p.amount_without_commission,
		p.amount_with_commission,
		p.amount_of_commission,
		p.info,
		p.comment
	from
		payment p
	left outer join
		orders o on p.order_id = o.id
	left outer join
		dict_service_type dct on dct.id = o.dict_service_type_id
	left outer join
		users u on u.id = o.createdBy
	left outer join
		client c on c.id = u.client_id
	where
		(createdAt_begin_in is null or createdAt_begin_in::date<= p.createdAt::date) and
		(createdAt_end_in is null or createdAt_end_in::date >= p.createdAt::date) and
		(status_in is null or status_in = p.status) and
		(payment_type_in is null or payment_type_in = p.payment_type) and
		(method_in is null or method_in = p.method) and
		(order_id_in is null or order_id_in = p.order_id) and 
		(coalesce(c.id,0) = coalesce(user_client_id,0) OR is_user_admin);

	
	if (client_name_in is not null) then
		
		delete from temp_list_payment
		where not lower(coalesce(client_name, '')) like '%' || lower(client_name_in) || '%';
	end if;
		
	update temp_list_payment set
		status_name = get_payment_status_name(temp_list_payment.status);

	update temp_list_payment set
		payment_type_name = get_payment_type_name(temp_list_payment.payment_type);

	update temp_list_payment set
		method_name = get_payment_method_name(temp_list_payment.method);
		
	return query select
		id,
		createdAt,
		order_createdAt,
		order_dict_service_type_name,
		payment_type,
		payment_type_name,
		status,
		status_name,
		method,
		method_name,
		client_name,
		amount_without_commission,
		amount_with_commission,
		amount_of_commission,
		info,
		comment
	from
		temp_list_payment;
	drop table temp_list_payment;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
