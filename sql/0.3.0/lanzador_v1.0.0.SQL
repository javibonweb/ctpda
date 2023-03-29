rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

set define off
spool logs/003_LANZADOR.log

REM DO DE LA VERSIÓN 0.1.0

-- TABLAS TEMPORALES 
REM TABLAS TEMPORALES


-- TABLAS
REM TABLAS
@@01_tablas.sql


-- SECUENCIAS
REM SECUENCIAS
@@02_secuencias.sql


-- INDICES
REM INDICES
@@03_indices.sql


-- CONSTRAINTS
REM CONTRAINTS
@@04_constraints.sql


-- VISTAS 
REM VISTAS
@@07_vistas.sql


-- FUNCIONES/PROCEDIMIENTOS
REM FUNCIONES/PROCEDIMIENTOS


--TRIGGERS
REM TRIGGERS


-- GRANTS
REM GRANTS
@@06_grants.sql


--INSERTS/UPDATES/DELETES
REM INSERTS/UPDATES/DELETES
@@05_inserts.sql


PROMPT *****************************************
PROMPT * FIN DE LA INSTALACION DE LA VERSIÓN   *
PROMPT * Si todo es correcto realice el commit *
PROMPT * En caso contrario realice el rollback *
PROMPT *****************************************

spool off;
