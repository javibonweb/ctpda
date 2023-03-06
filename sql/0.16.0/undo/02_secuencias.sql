rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/016_UNDO_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION 0.16.0 --
-------------------

DROP SEQUENCE S_LOG_CRON;



SPOOL OFF;
