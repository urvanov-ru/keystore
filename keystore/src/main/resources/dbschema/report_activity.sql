drop function if exists report_activity(date, date, integer);
drop function if exists report_activity(bigint, date, date, integer);
drop function if exists report_activity(bigint, date, date, integer, bigint);

create or replace function report_activity(
	IN user_id_in bigint,
	IN date_begin_in date,
	IN date_end_in date,
	IN report_mode_in integer,   -- 1 - по годам, 2 - по кварталам, 3 - по месяцам, 4 - по неделям, 5 - по дням
	IN client_id_in bigint,    -- для формирования в разрезе клиента.
	OUT id_out bigint,
	OUT date_begin_out date,
	OUT date_end_out date,
	OUT clients_out bigint,
	OUT active_clients_out bigint,
	OUT not_active_clients_out bigint,
	OUT connections_out bigint,
	OUT client_connections_out bigint,
	OUT service_connections_out bigint,
	OUT sessions_time_out bigint, -- в секундах
	OUT client_sessions_time_out bigint, -- в секундах
	OUT service_sessions_time_out bigint -- в секундах
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
	
	create local temp table temp_report_activity(
		id                          bigserial,
		date_begin                  date,
		date_end                    date,
		clients                     bigint default 0,
		active_clients              bigint default 0,
		not_active_clients          bigint default 0,
		connections                 bigint default 0,
		client_connections          bigint default 0,
		service_connections         bigint default 0,
		sessions_time               bigint default 0,
		client_sessions_time        bigint default 0,
		service_sessions_time       bigint default 0
	);

	case report_mode_in
		when 1 then
			begin
				var_date_trunc_field := 'year';
				var_date_begin := date_trunc(var_date_trunc_field, date_begin_in);
				--var_date_begin := date_begin_in;
				while (var_date_begin <= date_end_in) loop
					var_date_end := var_date_begin + interval '1 year' - interval '1 day';
					insert into temp_report_activity(date_begin, date_end) values(var_date_begin, var_date_end);
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
					insert into temp_report_activity(date_begin, date_end) values(var_date_begin, var_date_end);
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
					insert into temp_report_activity(date_begin, date_end) values(var_date_begin, var_date_end);
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
					insert into temp_report_activity(date_begin, date_end) values(var_date_begin, var_date_end);
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
					insert into temp_report_activity(date_begin, date_end) values(var_date_begin, var_date_end);
					var_date_begin := var_date_begin + interval '1 day';
				end loop;
			end;
	end case;

	update temp_report_activity set
		connections = t.cnt,
		sessions_time = t.tm
	from (select date_trunc(var_date_trunc_field, sl.session_begin) as createdAt, count(*) as cnt, sum(extract('second' from (sl.session_end - sl.session_begin))) as tm
		from session_log sl
			inner join users u on u.id = sl.user_id
		where sl.session_begin::date >= date_begin_in and sl.session_begin::date <= date_end_in and
			(client_id_in is null or client_id_in = u.client_id)
		group by date_trunc(var_date_trunc_field, sl.session_begin)) t
	where t.createdAt = temp_report_activity.date_begin;

	if (client_id_in is null) then
	begin
		update temp_report_activity set
			client_connections = t.cnt,
			client_sessions_time = t.tm
		from (select date_trunc(var_date_trunc_field, sl.session_begin) as createdAt, count(*) as cnt, sum(extract('second' from (sl.session_end - sl.session_begin))) as tm
			from session_log sl
				inner join users u on u.id = sl.user_id
				inner join authorities a on a.user_id = u.id and a.authority = 'ROLE_CLIENT'
			where sl.session_begin::date >= date_begin_in and sl.session_begin::date <= date_end_in and a.authority = 'ROLE_CLIENT'
			group by date_trunc(var_date_trunc_field, sl.session_begin)) t
		where t.createdAt = temp_report_activity.date_begin;

		update temp_report_activity set
			service_connections = t.cnt,
			service_sessions_time = t.tm
		from (select date_trunc(var_date_trunc_field, sl.session_begin) as createdAt, count(*) as cnt, sum(extract('second' from (sl.session_end - sl.session_begin))) as tm
			from session_log sl
				inner join users u on u.id = sl.user_id
				inner join authorities a on a.user_id = u.id and a.authority = 'ROLE_SERVICE'
			where sl.session_begin::date >= date_begin_in and sl.session_begin::date <= date_end_in and a.authority='ROLE_SERVICE'
			group by date_trunc(var_date_trunc_field, sl.session_begin)) t
		where t.createdAt = temp_report_activity.date_begin;

		update temp_report_activity set
			clients = (select count(*) from client c where c.createdAt <= temp_report_activity.date_end);

		update temp_report_activity set
			active_clients = (select count(*) from client c where exists (select 1
			from session_log sl
				inner join users u on u.id = sl.user_id
				inner join authorities a on a.user_id = u.id and a.authority = 'ROLE_CLIENT'
			where date_trunc(var_date_trunc_field, sl.session_begin) = temp_report_activity.date_begin and
				sl.session_begin::date >= date_begin_in and sl.session_begin::date <= date_end_in
				and a.authority = 'ROLE_CLIENT' and u.client_id = c.id));

		update temp_report_activity set
			not_active_clients = clients - active_clients;
	end;
	end if;
		
	return query select
		id,
		date_begin,
		date_end,
		clients,
		active_clients,
		not_active_clients,
		connections,
		client_connections,
		service_connections,
		sessions_time,
		client_sessions_time,
		service_sessions_time
	from
		temp_report_activity
	order by
		date_begin;
	drop table temp_report_activity;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
