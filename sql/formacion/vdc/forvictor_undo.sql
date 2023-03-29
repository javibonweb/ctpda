rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION_UNDO.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

--------------------------
-- VERSION FORMACION VDC--
--------------------------

DROP TABLE FOR_PRUEBAS_VDC CASCADE CONSTRAINTS;
DROP TABLE FOR_PRUEBAS_VDC_H CASCADE CONSTRAINTS;

DROP SEQUENCE S_FOR_PRUEBAS_VDC;

SPOOL OFF;