-- CREAR DATABASE ms_procedure
CREATE DATABASE "ms_procedure"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;

-- COMENTAR DATABASE
COMMENT ON DATABASE "ms_procedure"
    IS 'Procedure Microservice Database';


-- CREAR TABLA procedure
CREATE TABLE procedure (
    id SERIAL PRIMARY KEY NOT NULL,
	procedure_config_id INTEGER NOT NULL,
	phase_id INTEGER NOT NULL,
	person_id INTEGER NOT NULL,
	student_id INTEGER NOT NULL,
	batch INTEGER,
	note INTEGER,
	collaborator_type_id INTEGER NOT NULL,
	active BOOLEAN NOT NULL
);


-- INSERTAR REGISTROS DE procedure
INSERT INTO procedure
(procedure_config_id,phase_id,person_id,student_id,batch,note,collaborator_type_id,active)
VALUES
(1,4,1,1,null,15,1,true),
(1,5,2,2,1,16,1,true),
(1,5,3,3,1,14,1,true),
(1,4,4,4,null,15,1,true),
(1,4,5,5,null,16,1,true),
(1,4,6,6,null,15,1,true);


--CONSULTAR REGISTROS DE procedure
SELECT * FROM procedure;
