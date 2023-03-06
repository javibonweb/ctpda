rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/016_03_INDICES.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION 0.16.0 --
-------------------

ALTER TABLE GE_LOG_CRON ADD CONSTRAINT GE_LOG_CRON_PK PRIMARY KEY (LOG_CRON_ID) USING INDEX TABLESPACE GESTOR_IND;


SPOOL OFF;
