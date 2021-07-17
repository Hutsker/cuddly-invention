Create or replace package equation is
    procedure init;
    procedure solve (a number, b number, c number, res1 out number, res2 out number);
    procedure solve (id_ur integer, res1 out number, res2 out number);
end equation;

@1.sql;

create or replace package body equation is
    --Процедура добавления уравнения в таблицу equations
    procedure add_equation(a number, b number, c number) is
    begin
        insert into equations (id,a,b,c) values(seq1.nextval, a,b,c);
    end add_equation;

    --Процедура для добавления решения уравнения в таблицу equation_solutions
    procedure add_eq_solution(res1 number, res2 number)is
    id_ur integer;
    begin
        select last_number-1 into id_ur from user_sequences where sequence_name = 'SEQ1';
        execute immediate 'insert into equation_solutions (id,id_eq,sol1, sol2) values(seq2.nextval, id_ur,res1,res2);';
    end add_eq_solution;
    --Процедура для нахождения корней уравнения и записи уравнения и решения в соответствующие таблицы (доступна для пользователя)
    procedure solve (a number, b number, c number, res1 out number, res2 out number)is
    begin
        res1:= round((-b - power(power(b,2)-4*a*c,0.5))/2*a,3);
        res2:= round((-b + power(power(b,2)-4*a*c,0.5))/2*a,3);
        add_equation(a,b,c);
        add_eq_solution(res1, res2);
    end solve;

    --Процедура для заполнения таблицы equations значениями по умолчанию и таблицы equation_solutions решениями полученных уравнений (доступна для пользователя)
    function init
    return integer is
    --локальные переменные объявлены только для того чтобы вызвать процедуру solve
    p1 number;
    p2 number;
    begin
        for i in 1..10
        loop
            solve(i,2*i+2,-3*i+1,p1,p2);
        end loop;
        commit;
        return 10;
    end init;
    --Процедура для нахождения корней уравнения, уже записанного в таблице по id уравнения (доступна для пользователя
    procedure solve (id_ur integer, res1 out number, res2 out number)is
    begin
        select sol1 into res1 from equation_solutions where id_eq=id_ur;
        select sol2 into res2 from equation_solutions where id_eq=id_ur;
        commit;
    end solve;

end equation;

CREATE VIEW sup_orders AS
      SELECT suppliers.supplier_id, orders.quantity, orders.price
      FROM suppliers
      INNER JOIN orders
      ON suppliers.supplier_id = orders.supplier_id
      WHERE suppliers.supplier_name = 'Microsoft';


CREATE OR REPLACE TRIGGER orders_before_insert
BEFORE INSERT
    ON orders
   FOR EACH ROW
DECLARE
   v_username varchar2(10);
BEGIN
   SELECT user INTO v_username
     FROM dual;
   :new.create_date := sysdate;
   :new.created_by := v_username;
END;

CREATE PUBLIC SYNONYM suppliers
FOR app.suppliers;