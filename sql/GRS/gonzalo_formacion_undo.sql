rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION_UNDO.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

----------------------------
-- VERSION GONZALO RANDO --
----------------------------

DROP TABLE FOR_PRUEBAS_GONZALO CASCADE CONSTRAINTS;
DROP TABLE FOR_PRUEBAS_GONZALO_H CASCADE CONSTRAINTS;

DROP SEQUENCE S_FOR_PRUEBAS_GONZALO;

SPOOL OFF;