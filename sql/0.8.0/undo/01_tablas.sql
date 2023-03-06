rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/008_UNDO_01_TABLAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

ALTER TABLE GE_PLAZOS_EXPDTE MODIFY (F_FECHA_LIMITE NULL);


DROP TABLE GE_RESP_TRAMITACION     CASCADE CONSTRAINTS;
DROP TABLE GE_USUARIO_RESP         CASCADE CONSTRAINTS;
DROP TABLE GE_CFG_TAREAS           CASCADE CONSTRAINTS;
DROP TABLE GE_TAREAS_EXPEDIENTE    CASCADE CONSTRAINTS;

DROP TABLE GE_RESP_TRAMITACION_H   CASCADE CONSTRAINTS;
DROP TABLE GE_USUARIO_RESP_H       CASCADE CONSTRAINTS;
DROP TABLE GE_CFG_TAREAS_H         CASCADE CONSTRAINTS;
DROP TABLE GE_TAREAS_EXPEDIENTE_H  CASCADE CONSTRAINTS;


ALTER TABLE GE_DOCUMENTOS_EXPED_TRAMITES   DROP (T_ORIGEN);
ALTER TABLE GE_DOCUMENTOS_EXPED_TRAMITES_H DROP (T_ORIGEN);

-- Nuevos campos para guardar ultima fecha y usuario de persistencia sobre los expedientes
ALTER TABLE GE_EXPEDIENTES DROP (F_ULTIMA_PERSISTENCIA);
ALTER TABLE GE_EXPEDIENTES DROP (USU_ULTIMA_PERSISTENCIA);
ALTER TABLE GE_EXPEDIENTES_H DROP (F_ULTIMA_PERSISTENCIA);
ALTER TABLE GE_EXPEDIENTES_H DROP (USU_ULTIMA_PERSISTENCIA);



SPOOL OFF;
