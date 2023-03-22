rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/005_UNDO_01_TABLAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


ALTER TABLE GE_SERIES   ADD (SER_ELESER_ID NUMBER(19,0));
ALTER TABLE GE_SERIES_H ADD (SER_ELESER_ID NUMBER(19,0));

-- se añade aquí el script de update para poder insertar valor en la nueva columna con el valor de una columna que se borrará a continuación
UPDATE GE_SERIES SET SER_ELESER_ID = 
(SELECT ELEM_SER_ID FROM GE_ELEMENTOS_SERIES WHERE GE_ELEMENTOS_SERIES.T_CODIGO_SERIE = GE_SERIES.T_CODIGO_SERIE);

ALTER TABLE GE_SERIES DROP COLUMN T_CODIGO_SERIE;
ALTER TABLE GE_SERIES_H DROP COLUMN T_CODIGO_SERIE;

DROP TABLE GE_ARTICULOS_AFECTADOS_EXPDT CASCADE CONSTRAINTS;
DROP TABLE GE_ARTICULOS_AFECTADOS_EXPDT_H CASCADE CONSTRAINTS;
DROP TABLE GE_DERECHOS_RECLAMADOS_EXPDT CASCADE CONSTRAINTS;
DROP TABLE GE_DERECHOS_RECLAMADOS_EXPDT_H CASCADE CONSTRAINTS;

ALTER TABLE GE_EXPEDIENTES DROP ( L_COMPETENCIA_CTPDA, EXP_VALDOM_AUTCOMP_ID, L_AEPD, EXP_VALDOM_TIPADM_ID, L_IMPOSICION_MEDIDAS, L_API, L_OPOSICION_PERSONA, L_OPOSICION_REPRESENTANTE );
ALTER TABLE GE_EXPEDIENTES_H DROP ( L_COMPETENCIA_CTPDA, EXP_VALDOM_AUTCOMP_ID, L_AEPD, EXP_VALDOM_TIPADM_ID, L_IMPOSICION_MEDIDAS, L_API, L_OPOSICION_PERSONA, L_OPOSICION_REPRESENTANTE );
ALTER TABLE GE_SUJETOS_OBLIGADOS_EXPDT DROP COLUMN ( SUJ_PER_ID NUMBER(19,0) );
ALTER TABLE GE_SUJETOS_OBLIGADOS_EXPDT_H DROP COLUMN ( SUJ_PER_ID NUMBER(19,0) );

DROP TABLE GE_PERSONAS_REPRESENTANTES CASCADE CONSTRAINTS;
DROP TABLE GE_PERSONAS_REPRESENTANTES_H CASCADE CONSTRAINTS;

ALTER TABLE GE_PERSONAS   DROP COLUMN PER_VALDOM_TIPIDE_ID;
ALTER TABLE GE_PERSONAS_H DROP COLUMN PER_VALDOM_TIPIDE_ID;

ALTER TABLE GE_SUJETOS_OBLIGADOS   DROP COLUMN SUJ_VALDOM_TIPIDE_ID;
ALTER TABLE GE_SUJETOS_OBLIGADOS_H DROP COLUMN SUJ_VALDOM_TIPIDE_ID;

CREATE TABLE GE_COMUNICACIONES (COM_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, C_CODIGO VARCHAR2(5 CHAR) NOT NULL, D_DESCRIPCION VARCHAR2(100 CHAR) NOT NULL) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_COMUNICACIONES_H (COM_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), C_CODIGO VARCHAR2(255 CHAR), D_DESCRIPCION VARCHAR2(255 CHAR)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_MATERIAS (MAT_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, T_NOMBRE VARCHAR2(100 CHAR) NOT NULL, MAT_TIP_ID NUMBER(19,0)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_MATERIAS_H (MAT_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), T_NOMBRE VARCHAR2(255 CHAR), MAT_TIP_ID NUMBER(19,0)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_MOTIVOSRELACION (MOT_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, T_NOMBRE VARCHAR2(100 CHAR) NOT NULL) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_MOTIVOSRELACION_H (MOT_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), T_NOMBRE VARCHAR2(255 CHAR)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_SEXO (SEX_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, C_CODIGO VARCHAR2(5 CHAR) NOT NULL, D_DESCRIPCION VARCHAR2(100 CHAR) NOT NULL) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_SEXO_H (SEX_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), C_CODIGO VARCHAR2(255 CHAR), D_DESCRIPCION VARCHAR2(255 CHAR)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_TIPOEXPEDIENTES (TIP_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, C_CODIGO VARCHAR2(10 CHAR) NOT NULL, D_DESCRIPCION VARCHAR2(255 CHAR) NOT NULL) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_TIPOEXPEDIENTES_H (TIP_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), C_CODIGO VARCHAR2(255 CHAR), D_DESCRIPCION VARCHAR2(255 CHAR)) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_TIPOSPERSONAS (TIPPER_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, N_VERSION NUMBER(19,0), USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL, C_CODIGO VARCHAR2(5 CHAR) NOT NULL, D_DESCRIPCION VARCHAR2(100 CHAR) NOT NULL) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_TIPOSPERSONAS_H (TIPPER_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), L_ACTIVA NUMBER(1,0), C_CODIGO VARCHAR2(255 CHAR), D_DESCRIPCION VARCHAR2(255 CHAR)) TABLESPACE GESTOR_DAT;

ALTER TABLE GE_PERSONAS ADD PER_SEX_ID NUMBER(19,0);

DROP TABLE GE_USUARIOS CASCADE CONSTRAINTS;
DROP TABLE GE_USUARIOS_H CASCADE CONSTRAINTS;
DROP TABLE GE_USUARIOS_PERFILES CASCADE CONSTRAINTS;
DROP TABLE GE_USUARIOS_PERFILES_H CASCADE CONSTRAINTS;
DROP TABLE GE_PERFILES CASCADE CONSTRAINTS;
DROP TABLE GE_PERFILES_H CASCADE CONSTRAINTS;
DROP TABLE GE_PERMISOS CASCADE CONSTRAINTS;
DROP TABLE GE_PERMISOS_H CASCADE CONSTRAINTS;
DROP TABLE GE_PERMISOS_PERFILES CASCADE CONSTRAINTS;
DROP TABLE GE_PERMISOS_PERFILES_H CASCADE CONSTRAINTS;

UPDATE GE_PERSONAS SET PER_PAI_ID = NULL;
UPDATE GE_SUJETOS_OBLIGADOS SET SUJ_PAI_ID = NULL;
DELETE FROM GE_PAISES;
ALTER TABLE GE_PAISES   MODIFY D_DESCRIPCION VARCHAR2(7 CHAR);
ALTER TABLE GE_PAISES_H MODIFY D_DESCRIPCION VARCHAR2(7 CHAR);


SPOOL OFF;