rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/012_UNDO_05_INSERTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

---------------------
-- VERSION 0.12.0  --
---------------------

UPDATE GE_VALORES_DOMINIOS SET C_CODIGO = 'FOR', D_DESCRIPCION = 'Formal' WHERE C_CODIGO = 'ADM' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_ADM');
UPDATE GE_VALORES_DOMINIOS SET C_CODIGO = 'DE_HEC', D_DESCRIPCION = 'De hecho' WHERE C_CODIGO = 'AHE' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_ADM');

DELETE FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'PD' AND VALDOM_VALDOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'AREA_CTPDA');
DELETE FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'TR' AND VALDOM_VALDOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'AREA_CTPDA');
DELETE FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'SG' AND VALDOM_VALDOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'AREA_CTPDA');
DELETE FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'DIR' AND VALDOM_VALDOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'AREA_CTPDA');

DELETE FROM GE_DOMINIOS WHERE C_CODIGO = 'AREA_CTPDA';

DELETE FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'MOT_REL') AND C_CODIGO = 'EDOC';

DELETE FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'MOT_INADM');

DELETE FROM GE_DOMINIOS WHERE C_CODIGO = 'MOT_INADM';

-- ------------------------------

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'LIST_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'LIST_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'ACT_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'ACT_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'CONS_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'CONS_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'DESAC_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'DESAC_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'EDIT_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'EDIT_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'SAVE_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'SAVE_PLANTILLASDOC';

DELETE FROM GE_PERMISOS_PERFILES WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'NEW_PLANTILLASDOC');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'NEW_PLANTILLASDOC';

-- ------------------------------------

DELETE FROM GE_PARAMETROS WHERE T_CAMPO = 'extensionesPlantillas';

-- ------------------------------------

DELETE FROM GE_CFG_EXPED_TRAMITE WHERE CFG_TIP_TRA_ID = (SELECT TIP_TRA_ID FROM GE_TIPO_TRAMITE WHERE T_CODIGO = 'DOCREL');
DELETE FROM GE_TIPO_TRAMITE WHERE T_CODIGO = 'DOCREL';

-- ------------------------------------

UPDATE GE_CFG_TIPOEXPEDIENTE SET CFG_VALDOM_SERIERESOL_ID = NULL, CFG_VALDOM_TIPORESOL_ID = NULL WHERE CFG_VALDOM_TIP_EXP_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'RCE' AND valdom_dom_id = (SELECT DOM_ID FROM ge_dominios WHERE C_CODIGO = 'TIP_EXP'));
UPDATE GE_CFG_TIPOEXPEDIENTE SET CFG_VALDOM_SERIERESOL_ID = NULL, CFG_VALDOM_TIPORESOL_ID = NULL WHERE CFG_VALDOM_TIP_EXP_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'RCO' AND valdom_dom_id = (SELECT DOM_ID FROM ge_dominios WHERE C_CODIGO = 'TIP_EXP'));
UPDATE GE_CFG_TIPOEXPEDIENTE SET CFG_VALDOM_SERIERESOL_ID = NULL, CFG_VALDOM_TIPORESOL_ID = NULL WHERE CFG_VALDOM_TIP_EXP_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'PSAN' AND valdom_dom_id = (SELECT DOM_ID FROM ge_dominios WHERE C_CODIGO = 'TIP_EXP'));										

-- ------------------------------------

UPDATE GE_CFG_METADATOS_TRAM SET D_API = NULL, D_INSTRUCTOR_API = NULL WHERE METTRAM_TIPTRAM_ID = (SELECT TIP_TRA_ID FROM GE_TIPO_TRAMITE WHERE T_COMPORTAMIENTO = 'C008');

------------------------------------

DELETE FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'PROP_API');
DELETE FROM GE_DOMINIOS WHERE C_CODIGO = 'PROP_API';

SPOOL OFF;
