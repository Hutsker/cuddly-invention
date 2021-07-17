select a from b;

create or replace package test_package as
c_mobile_service_id$i      constant integer := 100541;
end test_package;


create or replace package body test_package as
    procedure proc(num integer) is
    num2 integer;
    begin
        num2 := num+2;
        commit;
    end;
end test_package;
