drop function if exists report_payment(bigint, date, date, integer);


create or replace function report_payment(
	IN user_id_in bigint,
	IN date_begin_in date,
	IN date_end_in date,
	IN report_mode_in integer,   -- 1 - по годам, 2 - по кварталам, 3 - по месяцам, 4 - по неделям, 5 - по дням
	OUT id_out bigint,
	OUT date_begin_out date,
	OUT date_end_out date,
	OUT dict_service_type_id_out bigint,
	OUT dict_service_type_name_out character varying,
	OUT profit_out numeric(18,2),
	OUT order_payment_amount_out numeric(18,2),
	OUT pay_back_amount_out numeric(18,2),
	OUT active_keys_out bigint,
	OUT new_keys_out bigint,
	OUT expired_keys_out bigint,
	OUT canceled_keys_out bigint
)
returns setof record as 
$BODY$
declare is_user_admin boolean;
declare user_client_id bigint;
declare var_date_begin date;
declare var_date_end date;
declare var_date_trunc_field character varying;
begin

	is_user_admin := exists(select * from authorities where user_id = user_id_in and authority='ROLE_SERVICE');

	user_client_id := (select client_id from users where id = user_id_in);
	
	create local temp table temp_report_payment(
		id                          bigserial,
		date_begin                  date               default null,
		date_end                    date               default null,
		dict_service_type_id        bigint             default null,
		dict_service_type_name      character varying  default null,
		profit                      numeric(18,2)      default 0.0,
		order_payment_amount        numeric(18,2)      default 0.0,
		pay_back_amount             numeric(18,2)      default 0.0,
		active_keys                 bigint             default 0,
		new_keys                    bigint             default 0,
		expired_keys                bigint             default 0,
		canceled_keys               bigint             default 0
	);

	case report_mode_in
		when 1 then
			begin
				var_date_trunc_field := 'year';
				var_date_begin := date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin + interval '1 year' - interval '1 day';
					insert into temp_report_payment(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '1 year';
				end loop;
			end;
		when 2 then
			begin
				var_date_trunc_field := 'quarter';
				var_date_begin = date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin + interval '3 month' - interval '1 day';
					insert into temp_report_payment(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '3 month';
				end loop;
			end;
		when 3 then
			begin
				var_date_trunc_field := 'month';
				var_date_begin = date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin + interval '1 month' - interval '1 day';
					insert into temp_report_payment(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '1 month';
				end loop;
			end;
		when 4 then
			begin
				var_date_trunc_field := 'week';
				var_date_begin = date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin + interval '1 week' - interval '1 day';
					insert into temp_report_payment(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '1 week';
				end loop;
			end;
		when 5 then
			begin
				var_date_trunc_field := 'day';
				var_date_begin = date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin;
					insert into temp_report_payment(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '1 day';
				end loop;
			end;
	end case;

	insert into temp_report_payment(date_begin, date_end, dict_service_type_id, dict_service_type_name)
	select t.date_begin, t.date_end, dst.id, dst.name
	from temp_report_payment t
		cross join dict_service_type dst;

	update temp_report_payment set
		order_payment_amount = t.order_payment_amount
	from (select date_trunc(var_date_trunc_field, p.createdAt) as createdAt,
		sum(p.amount_without_commission) as order_payment_amount,
		o.dict_service_type_id as dict_service_type_id
		from payment p
			join orders o on o.id = p.order_id
		where p.payment_type = 'ORDER_PAYMENT' and p.status = 'COMPLETED' and 
			p.createdAt >= date_begin_in and p.createdAt <= date_end_in
		group by o.dict_service_type_id, date_trunc(var_date_trunc_field, p.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;
	

	update temp_report_payment set
		pay_back_amount = t.order_payment_amount
	from (select date_trunc(var_date_trunc_field, p.createdAt) as createdAt,
		sum(p.amount_without_commission) as order_payment_amount,
		o.dict_service_type_id as dict_service_type_id
		from payment p
			join orders o on o.id = p.order_id
		where p.payment_type = 'PAY_BACK' and p.status = 'COMPLETED' and
			p.createdAt >= date_begin_in and p.createdAt <= date_end_in
		group by o.dict_service_type_id, date_trunc(var_date_trunc_field, p.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;

	update temp_report_payment set
		profit = order_payment_amount - pay_back_amount;




	create local temp table temp_report_payment_keys(
		id  bigint,
		createdAt date,
		dict_service_type_id bigint
	);



	-- Активные ключи
	insert into temp_report_payment_keys(id, createdAt, dict_service_type_id)
	select k.id, k.date_begin, (case
			when o.dict_service_type_id is null then da.dict_service_type_id 
			else o.dict_service_type_id
		end) as dict_service_type_id
	from key k
			left join orders o on o.id = k.order_id
			left join dict_action da on da.id = k.dict_action_id
		where
			(k.date_begin >= date_begin_in and k.date_begin <= date_end_in or 
			k.date_end >= date_begin_in and k.date_end <= date_end_in or
			date_begin_in >= k.date_begin and date_begin_in <= k.date_end or
			date_end_in >= k.date_begin and date_end_in <= k.date_end);
	
	
	
	update temp_report_payment set
		active_keys = t.active_keys
	from (select date_trunc(var_date_trunc_field, k.createdAt) as createdAt,
		count(k.id) as active_keys,
		k.dict_service_type_id as dict_service_type_id
		from temp_report_payment_keys k
		group by k.dict_service_type_id, date_trunc(var_date_trunc_field, k.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;



	-- Новые ключи
	truncate temp_report_payment_keys;

	insert into temp_report_payment_keys(id, createdAt, dict_service_type_id)
	select k.id, k.date_begin, (case
			when o.dict_service_type_id is null then da.dict_service_type_id 
			else o.dict_service_type_id
		end) as dict_service_type_id
	from key k
			left join orders o on o.id = k.order_id
			left join dict_action da on da.id = k.dict_action_id
		where
			(k.date_begin >= date_begin_in and k.date_begin <= date_end_in or 
			k.date_end >= date_begin_in and k.date_end <= date_end_in or
			date_begin_in >= k.date_begin and date_begin_in <= k.date_end or
			date_end_in >= k.date_begin and date_end_in <= k.date_end);

	update temp_report_payment set
		new_keys = t.new_keys
	from (select date_trunc(var_date_trunc_field, k.createdAt) as createdAt,
		count(k.id) as new_keys,
		k.dict_service_type_id as dict_service_type_id
		from temp_report_payment_keys k
		group by k.dict_service_type_id, date_trunc(var_date_trunc_field, k.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;



	-- Истёкшие ключи
	truncate temp_report_payment_keys;

	insert into temp_report_payment_keys(id, createdAt, dict_service_type_id)
	select k.id, k.date_end, (case
			when o.dict_service_type_id is null then da.dict_service_type_id 
			else o.dict_service_type_id
		end) as dict_service_type_id
	from key k
			left join orders o on o.id = k.order_id
			left join dict_action da on da.id = k.dict_action_id
		where 
			(k.date_begin >= date_begin_in and k.date_begin <= date_end_in or 
			k.date_end >= date_begin_in and k.date_end <= date_end_in or
			date_begin_in >= k.date_begin and date_begin_in <= k.date_end or
			date_end_in >= k.date_begin and date_end_in <= k.date_end);

	update temp_report_payment set
		expired_keys = t.expired_keys
	from (select date_trunc(var_date_trunc_field, k.createdAt) as createdAt,
		count(k.id) as expired_keys,
		k.dict_service_type_id as dict_service_type_id
		from temp_report_payment_keys k
		group by k.dict_service_type_id, date_trunc(var_date_trunc_field, k.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;



	-- Вернули ключи
	truncate temp_report_payment_keys;

	insert into temp_report_payment_keys(id, createdAt, dict_service_type_id)
	select k.id, k.createdAt, (case
			when o.dict_service_type_id is null then da.dict_service_type_id 
			else o.dict_service_type_id
		end) as dict_service_type_id
	from key k
			left join orders o on o.id = k.order_id
			left join dict_action da on da.id = k.dict_action_id
		where k.status = 'CANCELED' and
			k.createdAt >= date_begin_in and k.createdAt <= date_end_in;

	update temp_report_payment set
		expired_keys = t.expired_keys
	from (select date_trunc(var_date_trunc_field, k.createdAt) as createdAt,
		count(k.id) as expired_keys,
		k.dict_service_type_id as dict_service_type_id
		from temp_report_payment_keys k
		group by k.dict_service_type_id, date_trunc(var_date_trunc_field, k.createdAt)
		) t
	where t.createdAt = temp_report_payment.date_begin and t.dict_service_type_id = temp_report_payment.dict_service_type_id;
	
	drop table temp_report_payment_keys;


	update temp_report_payment set
		profit = t2.profit,
		order_payment_amount = t2.order_payment_amount,
		pay_back_amount = t2.pay_back_amount,
		active_keys = t2.active_keys,
		new_keys = t2.new_keys,
		expired_keys = t2.expired_keys,
		canceled_keys = t2.canceled_keys
	from (select sum(t.profit) as profit, sum(t.order_payment_amount) as order_payment_amount,
		sum(t.pay_back_amount) as pay_back_amount,
		sum(active_keys) as active_keys, sum(new_keys) as new_keys,
		sum(expired_keys) as expired_keys, sum(canceled_keys) as canceled_keys,
		t.date_begin as date_begin
		from temp_report_payment t
		where t.dict_service_type_id is not null
		group by t.date_begin) t2
	where temp_report_payment.dict_service_type_id is null and
		temp_report_payment.date_begin = t2.date_begin;

	
	
	return query select
		id,
		date_begin,
		date_end,
		dict_service_type_id,
		dict_service_type_name,
		profit,
		order_payment_amount,
		pay_back_amount,
		active_keys,
		new_keys,
		expired_keys,
		canceled_keys
	from
		temp_report_payment
	order by
		dict_service_type_id, date_begin;
	drop table temp_report_payment;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
