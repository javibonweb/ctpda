rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/016_06_GRANTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.16.0 --
-------------------

GRANT SELECT, UPDATE, INSERT, DELETE ON GE_LOG_CRON TO GE_ROL_GESTOR_DML;
GRANT SELECT, ALTER ON S_LOG_CRON TO GE_ROL_GESTOR_DML;



SPOOL OFF;
