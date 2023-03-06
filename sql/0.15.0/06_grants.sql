rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/015_06_GRANTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.15.0 --
-------------------

GRANT SELECT ON GE_LISTADO_FIRMAS_TRAM TO GE_ROL_GESTOR_DML;

-- ----------------

GRANT SELECT ON GE_NOTIF_PENDIENTES TO GE_ROL_GESTOR_DML;



SPOOL OFF;
