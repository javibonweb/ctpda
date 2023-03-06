rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/014_UNDO_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION 0.14.0 --
-------------------


DROP SEQUENCE S_CONEXIONES_USUARIOS;


SPOOL OFF;
