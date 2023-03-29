rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/002_UNDO_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.2.0 --
-------------------

DROP SEQUENCE S_TIPOAGRUPACION;

DROP SEQUENCE S_SUJETOS_OBLIGADOS;

SPOOL OFF;
