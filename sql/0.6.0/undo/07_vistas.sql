rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/006_UNDO_07_VISTAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;
	
-- Eliminar Vista
DROP VIEW GE_LIST_EXP_PEREXP;

DROP VIEW GE_LISTADO_DOCUMENTOS_EXP;

SPOOL OFF;