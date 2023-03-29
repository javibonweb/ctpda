rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/003_UNDO_05_INSERTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.3.0 --
-------------------

DELETE FROM GE_PROVINCIAS;
DELETE FROM GE_LOCALIDADES;

UPDATE GE_VALORES_DOMINIOS SET D_DESCRIPCION = 'Ejercicio de los derechos relativos a la protección de las personas físicas en lo que respecta al tratamiento de los datos personales realizados por el consejo', C_CODIGO = 'SG_EJE_DER', L_BLOQUEADO = 0 WHERE C_CODIGO = 'RCE' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_EXP');
UPDATE GE_VALORES_DOMINIOS SET D_DESCRIPCION = 'Reclamación por infracción de la normativa de protección de datos', C_CODIGO = 'PD_RCL_INF', L_BLOQUEADO = 0 WHERE C_CODIGO = 'RCO' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_EXP');


SPOOL OFF;
