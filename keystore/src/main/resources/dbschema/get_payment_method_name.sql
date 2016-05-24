drop function if exists get_payment_method_name(character varying);


create or replace function get_payment_method_name(
	IN method_in character varying
)
returns character varying as 
$BODY$

begin
	return case method_in
		when 'CHECK' then 'Счёт'
		when 'PAYMENT_SYSTEM' then 'Платёжная система'
		when 'CASH' then 'Наличными в кассу'
	    end;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100;
