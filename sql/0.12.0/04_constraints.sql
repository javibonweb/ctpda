rem Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/012_04_CONSTRAINTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

--------------------
-- VERSION 0.12.0 --
--------------------

ALTER TABLE GE_RESOL_SUJOBL ADD CONSTRAINT RESSUJOBL_EXP_FK FOREIGN KEY (RESSUJOBL_EXP_ID) REFERENCES GE_EXPEDIENTES;

ALTER TABLE GE_RESOL_PERSONA ADD CONSTRAINT RESPER_EXP_FK FOREIGN KEY (RESPER_EXP_ID) REFERENCES GE_EXPEDIENTES;

ALTER TABLE GE_RESOL_EXPEDIENTES ADD CONSTRAINT GE_RESEXP_VALDOMSENRES_FK FOREIGN KEY (RESEXP_VALDOMSENRES_ID) REFERENCES GE_VALORES_DOMINIOS;

ALTER TABLE GE_CFG_EXPED_TRAMITE ADD CONSTRAINT GE_CFGEXPTR_RESP_FK FOREIGN KEY (CFGEXPTR_RESP_ID_DEFECTO) REFERENCES GE_RESP_TRAMITACION;

ALTER TABLE GE_CFG_EXPED_SUBTRAM ADD CONSTRAINT GE_CFGEXPST_RESP_FK FOREIGN KEY (CFGEXPST_RESP_ID_DEFECTO) REFERENCES GE_RESP_TRAMITACION;

-- --------------------

ALTER TABLE GE_CFG_AUTO_SITUACION ADD CONSTRAINT GE_AUTO_SIT_TIP_TRA_FK FOREIGN KEY (AUS_TIP_TRA_ID) REFERENCES GE_TIPO_TRAMITE;
ALTER TABLE GE_CFG_AUTO_SITUACION ADD CONSTRAINT GE_AUTO_SIT_VALDOM_TIP_EXP_FK  FOREIGN KEY (AUS_VALDOM_TIP_EXP_ID)  REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_CFG_AUTO_SITUACION ADD CONSTRAINT GE_AUTO_SIT_VALDOM_SIT_ORI_FK  FOREIGN KEY (AUS_VALDOM_SIT_ORI_ID)  REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_CFG_AUTO_SITUACION ADD CONSTRAINT GE_AUTO_SIT_VALDOM_SIT_DEST_FK FOREIGN KEY (AUS_VALDOM_SIT_DEST_ID) REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_CFG_AUTO_SITUACION_H ADD CONSTRAINT GE_CFG_AUTO_SITUACION_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_TIP_EXP_FK FOREIGN KEY (CFG_VALDOM_TIP_EXP_ID) REFERENCES GE_VALORES_DOMINIOS (VAL_DOM_ID) ENABLE;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_TIP_AREA_CTPDA_FK FOREIGN KEY (CFG_VALDOM_TIP_AREA_CTPDA_ID) REFERENCES GE_VALORES_DOMINIOS (VAL_DOM_ID) ENABLE;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_TIP_TRA_SEG_1_FK FOREIGN KEY (CFG_TIP_TRA_SEG_1_ID) REFERENCES GE_TIPO_TRAMITE (TIP_TRA_ID) ENABLE;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_TIP_TRA_SEG_2_FK FOREIGN KEY (CFG_TIP_TRA_SEG_2_ID) REFERENCES GE_TIPO_TRAMITE (TIP_TRA_ID) ENABLE;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_TIP_TRA_SEG_3_FK FOREIGN KEY (CFG_TIP_TRA_SEG_3_ID) REFERENCES GE_TIPO_TRAMITE (TIP_TRA_ID) ENABLE;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE_H ADD CONSTRAINT GE_CFG_TIP_EXP_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

-- -------------------

ALTER TABLE GE_DETALLE_EXPDTE_TRAM ADD CONSTRAINT GE_DETEXTRAM_VALDOMMOTINAD_FK FOREIGN KEY (DETEXTRAM_VALDOMMOTINAD_ID) REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_EXPEDIENTES ADD CONSTRAINT GE_EXP_VALDOM_MOTINADM_FK FOREIGN KEY (EXP_VALDOM_MOTINADM_ID) REFERENCES GE_VALORES_DOMINIOS;

-- -------------------
-- Scripts sin undo, corrigen un fallo en la definición de GE_PLANTILLAS_H
ALTER TABLE GE_PLANTILLAS_H DROP CONSTRAINT GE_PLANTILLAS_H_PK;

ALTER TABLE GE_PLANTILLAS_H ADD CONSTRAINT GE_PLANTILLAS_H_PK PRIMARY KEY ("PLA_ID", "REV")
USING INDEX TABLESPACE GESTOR_IND ENABLE;

ALTER TABLE GE_PLANTILLAS ADD CONSTRAINT GE_PLANTILLAS_UK UNIQUE (T_CODIGO);

-- --------------------

ALTER TABLE GE_CFG_DOC_EXPED_DESC ADD CONSTRAINT GE_CFG_DOC_EXPED_DES_FK FOREIGN KEY (CFGDE_DOC_ID) REFERENCES GE_CFG_DOC_EXPED_TRAM;
ALTER TABLE GE_CFG_DOC_EXPED_DESC_H ADD CONSTRAINT GE_CFG_DOC_EXP_DES_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

-----------------------------------------------

alter table GE_CFG_AUTO_SITUACION_H RENAME CONSTRAINT GE_AUTO_SITUACION_H_PK TO GE_CFG_AUTO_SITUACION_H_PK;
alter table GE_CFG_EXPED_TRAMITE_H RENAME CONSTRAINT GE_CFG_EXP_H_REVAUD_FK TO GE_CFG_EXPTRAM_H_REVAUD_FK;
alter table GE_CFG_PLAZOS_AUT_H RENAME CONSTRAINT FK55QGX7IOCTPNTIWAJ3JQEYU0W TO GE_CFG_PLAZOSAUT_H_REVAUD_FK;
alter table GE_CFG_PLAZOS_EXPDTE_H RENAME CONSTRAINT GE_PLEXP_VDOM_TIPPLA_H_REV_FK TO GE_CFG_PLAEXP_H_REVAUD_FK;
alter table GE_PERSONAS_REPRESENTANTES_H RENAME CONSTRAINT GE_PRP_H_REVAUD_FK TO GE_PERSONAS_REPRESENTANTES_H_REVAUD_FK;

alter table GE_DOCUMENTOS_EXPED_TRAMITES_H add constraint GE_DOCUMENTOS_EXPED_TRAMITES_H_PK primary key(REV,DETR_ID);

alter table GE_CFG_TAREAS_H add constraint GE_CFG_TAREAS_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_DOCUMENTOS_EXPED_TRAMITES_H add constraint GE_DOCEXPEDTRAM_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_ELEMENTOS_SERIES_H add constraint GE_ELEMENTOS_SERIES_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_PAISES_H add constraint GE_PAISES_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_RESP_TRAMITACION_H add constraint GE_RESP_TRAMITACION_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_SERIES_H add constraint GE_SERIES_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_TAREAS_EXPEDIENTE_H add constraint GE_TAREAS_EXPEDIENTE_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);
alter table GE_USUARIO_RESP_H add constraint GE_USUARIO_RESP_H_REVAUD_FK foreign key(REV) references GE_REVAUD(REV_ID);

-----------------------

ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_SERIERESOL_FK FOREIGN KEY (CFG_VALDOM_SERIERESOL_ID) REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_TIPORESOL_FK FOREIGN KEY (CFG_VALDOM_TIPORESOL_ID) REFERENCES GE_VALORES_DOMINIOS;

-----------------------

ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_MOTRELPER_ID_FK FOREIGN KEY (CFG_VALDOM_MOTRELPER_ID) REFERENCES GE_VALORES_DOMINIOS;
ALTER TABLE GE_CFG_TIPOEXPEDIENTE ADD CONSTRAINT GE_CFG_VALDOM_MOTRELSUJ_ID_FK FOREIGN KEY (CFG_VALDOM_MOTRELSUJ_ID) REFERENCES GE_VALORES_DOMINIOS;

----------------------------------

ALTER TABLE GE_DETALLE_EXPDTE_TRAM ADD CONSTRAINT GE_DETEXPTRAM_VALDOM_PROPAPI_ID_FK FOREIGN KEY (DETEXPTRAM_VALDOM_PROPAPI_ID) REFERENCES GE_VALORES_DOMINIOS;
SPOOL OFF;
