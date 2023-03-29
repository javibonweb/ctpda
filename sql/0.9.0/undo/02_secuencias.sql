rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/009_UNDO_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.9.0 --
-------------------


DROP SEQUENCE S_DATOS_TMP_USUARIO;

DROP SEQUENCE S_MATERIAS_EXPDT;


SPOOL OFF;
