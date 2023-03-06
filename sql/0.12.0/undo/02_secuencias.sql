rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/012_UNDO_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-------------------
-- VERSION 0.12.0 --
-------------------


DROP SEQUENCE S_CFG_TIPOEXPEDIENTE;

DROP SEQUENCE S_CFG_AUTO_SITUACION;

DROP SEQUENCE S_CFG_DOC_EXPED_DESC;



SPOOL OFF;
