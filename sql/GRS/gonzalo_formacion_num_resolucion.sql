rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/FORMACION_GRS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

----------------------------
-- VERSION GONZALO RANDO --
----------------------------

--Añadimos la columna--
ALTER TABLE GESTOR.GE_RESOLUCIONES ADD T_CODIGO_RESOL_ORIG varchar2(50);
ALTER TABLE GESTOR.GE_RESOLUCIONES_H ADD T_CODIGO_RESOL_ORIG varchar2(50);

SPOOL OFF;
