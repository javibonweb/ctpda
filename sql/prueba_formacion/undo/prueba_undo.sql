rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/011_UNDO_01_TABLAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

--------------------
-- VERSION 0.11.0 --
--------------------
DROP TABLE PRU_FORMACION CASCADE CONSTRAINT;
DROP TABLE PRU_FORMACION_H CASCADE CONSTRAINT;

SPOOL OFF;