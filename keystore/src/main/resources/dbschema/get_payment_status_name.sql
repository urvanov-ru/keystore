drop function if exists get_payment_status_name(character varying);


create or replace function get_payment_status_name(
	IN status_in character varying
)
returns character varying as 
$BODY$

begin
	return case status_in
		when 'COMPLETED' then 'Выполнен'
		when 'SCHEDULED' then 'Запланирован'
	    end;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100;
