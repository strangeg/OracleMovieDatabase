select * from mm_member;
/
select * from mm_movie;
/
select * from mm_movie_type;
/
select * from mm_pay_type;
/
select * from mm_rental;
/
CREATE OR REPLACE PROCEDURE check_user_sp
    (f_name IN VARCHAR2, 
    l_name IN VARCHAR2,
    is_member OUT VARCHAR2,
    member_id_out OUT NUMBER)
IS
    f_member VARCHAR2(20);
    l_member VARCHAR2(20);
    m_id NUMBER;
BEGIN
    SELECT member_id, first, last INTO m_id, f_member, l_member FROM mm_member
    WHERE first = f_name AND last = l_name;
    is_member := 'true';
    member_id_out := m_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            is_member := 'false';
END;
/
CREATE OR REPLACE PROCEDURE check_overdue_sp
    (m_id IN mm_rental.member_id%TYPE,
    over_due OUT VARCHAR2)
AS
    check_cur SYS_REFCURSOR;
    check_date NUMBER;
BEGIN
    FOR check_cur IN (select checkin_date, checkout_date from mm_rental where member_id = m_id)
    LOOP
    --checks to see if movie was returned, if not then checks for 7 day interval to flag overdue--
    IF check_cur.checkin_date IS NULL THEN
        check_date := sysdate - check_cur.checkout_date;
        IF check_date > 7 THEN
            over_due := 'true';
        END IF;
    END if;
    END LOOP;
    
    IF check_cur IS NULL and check_date IS NULL THEN
        over_due := 'false';
    END if;
END;
/
CREATE OR REPLACE PROCEDURE rent_movie_sp
BEGIN
END;
/
CREATE OR REPLACE TRIGGER rent_movie_tr
    AFTER INSERT ON mm_rental
    FOR EACH ROW
DECLARE
    CURSOR update_cur
        IS
        SELECT movie_id FROM mm_rental WHERE rental_id = :new.rental_id;
BEGIN
    FOR item IN update_cur
    LOOP
        UPDATE mm_movie
            SET movie_qty = movie_qty - 1
            WHERE movie_id = item.movie_id;
    END LOOP;
END;