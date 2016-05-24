drop function if exists get_payment_type_name(character varying);


create or replace function get_payment_type_name(
	IN payment_type_in character varying
)
returns character varying as 
$BODY$

begin
	return case payment_type_in
		when 'ORDER_PAYMENT' then 'Оплата заказа'
		when 'PAY_BACK' then 'Возврат средств'
		when 'CORRECTING' then 'Корректировка'
		
	    end;
end
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100;
