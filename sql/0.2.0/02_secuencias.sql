rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/002_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION 0.2.0 --
-------------------

CREATE SEQUENCE S_TIPOAGRUPACION START WITH 1 INCREMENT BY  1;

CREATE SEQUENCE S_SUJETOS_OBLIGADOS START WITH 1 INCREMENT BY  1;

SPOOL OFF;
