rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/016_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.16.0 --
-------------------

CREATE SEQUENCE S_LOG_CRON START WITH 1 INCREMENT BY  1;


SPOOL OFF;

