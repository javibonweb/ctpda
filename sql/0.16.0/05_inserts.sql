rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/016_05_INSERTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

---------------------
-- VERSION 0.16.0 --
---------------------


UPDATE GE_CFG_TAREAS SET L_CAMBIO_AUT_TRAMITE = 1, CFGTAR_RESP_CAMBIO_AUT_ID = (SELECT RESTRA_ID FROM GE_RESP_TRAMITACION WHERE T_COD_RESP = 'ADMPD00')
WHERE CFGTAR_VALDOM_TIP_TAR_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'FYN' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TAREAS'));


-- ------------------

INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'SERIESRESOL'),'RRP','Resol Recurso Administrativo','Resol Recurso',0,4,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'SERIESRESOL')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);

INSERT INTO GE_ELEMENTOS_SERIES (ELEM_SER_ID, T_TIPO, T_DESCRIPCION, T_CODIGO_SERIE, F_FECHA_INICIAL, F_FECHA_FINAL, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_ELEMENTOS_SERIES.NEXTVAL, 'RRP', 'Resol Recursos', 'RRPSEC', TO_DATE('01/01/2022', 'dd/mm/yyyy'), TO_DATE('31/12/2023', 'dd/mm/yyyy'), SYSDATE, (SELECT USER FROM DUAL), 0);

INSERT INTO GE_SERIES (SER_ID, T_CODIGO_SERIE, F_FECHA_INICIAL, F_FECHA_FINAL, N_NUMERO_INICIAL, N_NUMERO_FINAL, N_ULTIMO_NUMERO, T_PATRON, N_MARGEN_AVISO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_SERIES.NEXTVAL, 'RRPSEC', TO_DATE('01/01/2022', 'DD/MM/YYYY'), TO_DATE('31/12/2022', 'DD/MM/YYYY'), 1, 999, 0, 'RRP-2022/{3}', 50, SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_SERIES (SER_ID, T_CODIGO_SERIE, F_FECHA_INICIAL, F_FECHA_FINAL, N_NUMERO_INICIAL, N_NUMERO_FINAL, N_ULTIMO_NUMERO, T_PATRON, N_MARGEN_AVISO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_SERIES.NEXTVAL, 'RRPSEC', TO_DATE('01/01/2023', 'DD/MM/YYYY'), TO_DATE('31/12/2023', 'DD/MM/YYYY'), 1, 999, 0, 'RRP-2023/{3}', 50, SYSDATE, (SELECT USER FROM DUAL), 0);

-- ----------

UPDATE GE_CFG_TIPOEXPEDIENTE SET CFG_VALDOM_SERIERESREC_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'RRP' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'SERIESRESOL'));
UPDATE GE_TIPO_TRAMITE SET T_COMPORTAMIENTO='C024' WHERE T_CODIGO = 'RESREC';

INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIPORESOL'),'REC','Recurso administrativo','Recurso',0,4,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIPORESOL')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);

UPDATE GE_CFG_TIPOEXPEDIENTE SET CFG_VALDOM_TIPRESREC_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'REC' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIPORESOL'));

-- ---------

INSERT INTO GE_DOMINIOS (DOM_ID, C_CODIGO, D_DESCRIPCION, N_NIVEL_ANIDAMIENTO_MAX, L_BLOQUEADO, L_VISIBLE, L_PUNTO_MENU, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_DOMINIOS.NEXTVAL,'ACTO_REC','Acto recurrido',0,1,1,0,SYSDATE, (SELECT USER FROM DUAL), 0);

INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC'),'ACTR','Acuerdo de traslado','ACTR',0,1,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC'),'ACINA','Acuerdo de inadmisión','ACINA',0,2,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC'),'RESARCH','Resolución archivo','RESARCH',0,3,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC'),'RESEJ','Resolución ejercicio derechos','RESEJ',0,4,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_VALORES_DOMINIOS (VAL_DOM_ID, VALDOM_DOM_ID, C_CODIGO, D_DESCRIPCION,T_ABREVIATURA, N_NIVEL_ANIDAMIENTO, N_ORDEN, VALDOM_VALDOM_ID, L_BLOQUEADO, L_ACTIVO, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_VALORES_DOMINIOS.NEXTVAL,(SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC'),'RESPSAN','Resolución procedimiento sancionador','RESPSAN',0,5,(SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = '' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'ACTO_REC')),1,1,SYSDATE, (SELECT USER FROM DUAL), 0);

UPDATE GE_CFG_METADATOS_TRAM SET D_ACTO_REC = 'Acto recurrido';

UPDATE GE_TIPO_TRAMITE SET T_COMPORTAMIENTO='C023' WHERE T_CODIGO = 'RECP';

-- ---------

UPDATE GE_TIPO_TRAMITE SET L_TRAT_VINCULADOS = 1 WHERE T_CODIGO IN ('EDOC', 'GRENT');

----------------

UPDATE GE_CFG_TIPOEXPEDIENTE SET N_DIASFINALIZACION = 45;

INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'0','VER_DOCS_OCULTOS','Ver Documentos Ocultos','1');
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'VER_DOCS_OCULTOS'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

----------------------

UPDATE GE_SITUACIONES_EXPEDIENTES SET L_NO_SUPERVISADA = 0 WHERE SIT_VALDOM_SIT_ID IN (SELECT ge_valores_dominios.val_dom_id FROM ge_valores_dominios WHERE valdom_dom_id = (SELECT ge_dominios.dom_id FROM ge_dominios WHERE ge_dominios.c_codigo = 'SIT') AND c_codigo NOT IN ('FNZ','RST'));
UPDATE GE_SITUACIONES_EXPEDIENTES SET L_NO_SUPERVISADA = 1 WHERE SIT_VALDOM_SIT_ID IN (SELECT ge_valores_dominios.val_dom_id FROM ge_valores_dominios WHERE valdom_dom_id = (SELECT ge_dominios.dom_id FROM ge_dominios WHERE ge_dominios.c_codigo = 'SIT') AND c_codigo IN ('FNZ','RST'));

-- borrados en cadenas tras eliminar dos situaciones de valores de dominios (SIT = PAC)
delete from ge_resol_persona where resper_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete ge_resol_sujobl where ressujobl_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete ge_resol_expedientes where resexp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete from ge_tareas_expediente where TAREXP_TRA_ID in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')));

delete from ge_agrup_documentos where agrdoc_doc_exp_tram_id in (select ge_documentos_exped_tramites.detr_id from ge_documentos_exped_tramites where detr_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'))));

delete from ge_documentos_exped_tramites where detr_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')));

delete from ge_documentos_expedientes where docexp_tram_exp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')));

delete from ge_detalle_expdte_tram where detexptram_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')));

delete from ge_agrup_expedientes where agrexp_tram_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')));

delete from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete from ge_sujetos_obligados_expdt where suj_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete from ge_plazos_expdte where plaexp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC'));

delete from ge_personas_expedientes where per_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC')); 

delete ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PAC');

delete from ge_valores_dominios where valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT') and ge_valores_dominios.c_codigo='PAC';

-- SIT = PRR
delete from ge_resol_persona where resper_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete ge_resol_sujobl where ressujobl_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete ge_resol_expedientes where resexp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete from ge_tareas_expediente where TAREXP_TRA_ID in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_agrup_documentos where agrdoc_doc_exp_tram_id in (select ge_documentos_exped_tramites.detr_id from ge_documentos_exped_tramites where detr_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'))));

delete from ge_documentos_exped_tramites where detr_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_documentos_resoluciones where docres_docexp_id in (select docexp_id from ge_documentos_expedientes where docexp_tram_exp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'))));

delete from ge_documentos_expedientes where docexp_tram_exp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_detalle_expdte_tram where detexptram_tramexp_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_agrup_expedientes where agrexp_tram_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_accesos_docs where acd_tram_id in (select ge_tramite_exped.tram_exp_id from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')));

delete from ge_tramite_exped where tram_exp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete from ge_sujetos_obligados_expdt where suj_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete from ge_plazos_expdte where plaexp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete from ge_personas_expedientes where per_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')); 

delete from ge_tareas_expediente where tarexp_exp_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')); 

delete from ge_expedientes_relacionados where exr_exprelac_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR')); 

delete from ge_expedientes_relacionados where exr_exporigen_id in (select ge_expedientes.exp_id from ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR'));

delete ge_expedientes where 
exp_valdom_sit_id = (select ge_valores_dominios.val_dom_id from ge_valores_dominios where ge_valores_dominios.valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT')
and ge_valores_dominios.c_codigo='PRR');

delete from ge_valores_dominios where valdom_dom_id = (select ge_dominios.dom_id from ge_dominios where ge_dominios.c_codigo='SIT') and ge_valores_dominios.c_codigo='PRR';


INSERT INTO GE_USUARIOS (USU_ID,F_CREACION,USU_CREACION,N_VERSION,L_ACTIVA,T_CONTRASENYA,T_IDENTIFICADOR,T_LOGIN,T_NOMBRE,T_PRIMER_APELLIDO) VALUES (S_USUARIOS.nextval,SYSDATE,(SELECT USER FROM DUAL),0,1,'sistema','sistema','SISTEMA','SISTEMA','SISTEMA');

----------------

DELETE FROM GE_PERMISOS_PERFILES  WHERE PERMPERF_PERF_ID = (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN') AND PERMPERF_PERM_ID = (SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'VER_DOCS_OCULTOS');
DELETE FROM GE_PERMISOS WHERE C_CODIGO = 'VER_DOCS_OCULTOS';

----------------

UPDATE GE_CFG_TAREAS SET D_CIERRE_AUTO_TRAMITE = 'REV;REVT'
WHERE CFGTAR_VALDOM_TIP_TAR_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'FYN' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TAREAS'));

--------------------------------

UPDATE GE_CFG_TIPOEXPEDIENTE SET N_DIASALEGACIONES = 15 WHERE CFG_VALDOM_TIP_EXP_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE C_CODIGO = 'PSAN' AND VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_EXP'));

-----------

UPDATE GE_CFG_TAREAS SET L_CIERRE_SUBTRA = 0;
UPDATE GE_CFG_TAREAS SET L_CIERRE_SUBTRA = 1 WHERE cfgtar_valdom_tip_tar_id = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TAREAS') AND  C_CODIGO = 'FYN');


---------------------------

INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','RCO_VIEW_CAMPOSESP','Ver campos pestaña Datos Generales expediente RCO','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','RCE_VIEW_CAMPOSESP','Ver campos pestaña Datos Generales expediente RCE','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','PSAN_VIEW_CAMPOSESP','Ver campos pestaña Datos Generales expediente PSAN','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','XPC_VIEW_CAMPOSESP','Ver campos pestaña Datos Generales expediente XPC','1');

INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCO_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCE_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'PSAN_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'XPC_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMTTVO'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCO_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMTTVO'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCE_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMTTVO'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'PSAN_VIEW_CAMPOSESP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

------------------------

UPDATE GE_CFG_TAREAS SET D_PLAZO_REFERENCIA = 'ACU', N_DIAS_PLAZO_REFERENCIA = 20
WHERE cfgtar_valdom_tip_tar_id IN (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TAREAS') AND  C_CODIGO IN ('GAA', 'GAAH', 'GANA'));

------------------------

INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','NEW_EXPEMPDOC','Nuevo empujar documentos existentes de trámite de expediente','1');
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'NEW_EXPEMPDOC'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

-------------------------------------

INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','RCO_EDIT_RESPEXP','Modificar responsable del expediente RCO','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','RCE_EDIT_RESPEXP','Modificar responsable del expediente RCE','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','PSAN_EDIT_RESPEXP','Modificar responsable del expediente PSAN','1');
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','XPC_EDIT_RESPEXP','Modificar responsable del expediente XPC','1');

INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCO_EDIT_RESPEXP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'RCE_EDIT_RESPEXP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'PSAN_EDIT_RESPEXP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'XPC_EDIT_RESPEXP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMTTVO'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'XPC_EDIT_RESPEXP'),1,SYSDATE, (SELECT USER FROM DUAL), 0);

----------------------

UPDATE GE_CFG_TIPOEXPEDIENTE SET CFGTIPOEXP_RESPTRAMITACION_ID = (SELECT RESTRA_ID FROM GE_RESP_TRAMITACION WHERE T_COD_RESP = 'DAPD1'), CFGTIPOEXP_PERSONAS_ID = (SELECT PER_ID FROM GE_PERSONAS WHERE T_NIF_CIF = 'PDTE' AND T_NOMBRE_RAZONSOCIAL = 'Sin asignar'), CFGTIPOEXP_SUJETOSOBLIGADOS_ID = (SELECT SUJ_ID FROM GE_SUJETOS_OBLIGADOS WHERE T_NIF = 'PDTE' AND D_DESCRIPCION = 'Sin asignar') WHERE CFG_VALDOM_TIP_EXP_ID = (SELECT VAL_DOM_ID FROM GE_VALORES_DOMINIOS WHERE VALDOM_DOM_ID = (SELECT DOM_ID FROM GE_DOMINIOS WHERE C_CODIGO = 'TIP_EXP') AND  C_CODIGO = 'XPC');

----------------------
INSERT INTO GE_PERMISOS (PER_ID,F_CREACION,F_MODIFICACION,USU_CREACION,USU_MODIFICACION,L_ACTIVA,C_CODIGO,D_DESCRIPCION,N_VERSION) VALUES (S_PERMISOS.NEXTVAL,SYSDATE,NULL,(SELECT USER FROM DUAL),NULL,'1','LIST_TAREAS_ENVIADAS_PDTES','Mostrar menú "Tareas enviadas pendientes"','1');
INSERT INTO GE_PERMISOS_PERFILES (PERM_PERF_ID, PERMPERF_PERF_ID, PERMPERF_PERM_ID, L_ACTIVA, F_CREACION, USU_CREACION, N_VERSION) VALUES (S_PERMISOS_PERFILES.NEXTVAL, (SELECT PER_ID FROM GE_PERFILES WHERE c_codigo = 'ADMIN'),(SELECT PER_ID FROM GE_PERMISOS WHERE c_codigo = 'LIST_TAREAS_ENVIADAS_PDTES'),1,SYSDATE, (SELECT USER FROM DUAL), 0);



SPOOL OFF;
