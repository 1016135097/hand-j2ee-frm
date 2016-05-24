create table CUX_TEST_TBL
(
       my_id NUMBER NOT NULL,
       my_name VARCHAR2(200),
       my_other_attribtues VARCHAR2(200),
       attirbute_category VARCHAR2(150),
       attribute1 VARCHAR2(240),
       row_version_number number NOT NULL,
       creation_date date NOT NULL,
       created_by number NOT NULL,
       last_update_date date NOT NULL,
       last_updated_by number NOT NULL,
       last_update_login number NOT NULL
)
