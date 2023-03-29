rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/006_01_TABLAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

CREATE TABLE GE_SITUACIONES_EXPEDIENTES ( SIT_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), C_CODREL VARCHAR2(100 CHAR), SIT_VALDOM_TIPEXP_ID NUMBER(19,0), SIT_VALDOM_SIT_ID NUMBER(19,0), D_DESCRIPCION VARCHAR2(255 CHAR), D_DESC_ABREV VARCHAR2(100 CHAR), N_NIVEL_ANIDAMIENTO NUMBER(3,0), N_ORDEN NUMBER(3,0), SIT_SIT_SUPER_ID NUMBER(19,0), L_ACTIVO NUMBER(1,0), L_SIT_INICIAL NUMBER(1,0), L_SIT_FINAL NUMBER(1,0) ) TABLESPACE GESTOR_DAT;
CREATE TABLE GE_SITUACIONES_EXPEDIENTES_H ( SIT_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), C_CODREL VARCHAR2(100 CHAR), SIT_VALDOM_TIPEXP_ID NUMBER(19,0), SIT_VALDOM_SIT_ID NUMBER(19,0), D_DESCRIPCION VARCHAR2(255 CHAR), D_DESC_ABREV VARCHAR2(100 CHAR), N_NIVEL_ANIDAMIENTO NUMBER(3,0), N_ORDEN NUMBER(3,0), SIT_SIT_SUPER_ID NUMBER(19,0), L_ACTIVO NUMBER(1,0), L_SIT_INICIAL NUMBER(1,0), L_SIT_FINAL NUMBER(1,0) ) TABLESPACE GESTOR_DAT;


CREATE TABLE GE_TIPO_TRAMITE (
TIP_TRA_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(10 CHAR) NOT NULL, 	     -- CdoTipTra: Código identificativo único del tipo de trámite;
D_DESCRIPCION VARCHAR2(255 CHAR),    	     -- DescTipoTramite: Descripción del tipo de trámite;
D_DESC_ABREV VARCHAR2(100 CHAR),      	     -- DescTipoTramiteAbrev: Descripción abreviada del tipo de trámite;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL,    -- Activo: Indicativo de si está activo o no para su uso;
T_COMPORTAMIENTO VARCHAR2(50 CHAR) NOT NULL  -- Comportamiento: Contiene un código del tipo: CXXX (ejemplo: C001, C015, ..) que usará el sistema para saber que parte de código ejecutar en el momento de invocar en un formulario cualquier trámite que tenga asociado este tipo.
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_TIPO_TRAMITE_H (
TIP_TRA_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(10 CHAR), 	     -- CdoTipTra: Código identificativo único del tipo de trámite;
D_DESCRIPCION VARCHAR2(255 CHAR),    -- DescTipoTramite: Descripción del tipo de trámite;
D_DESC_ABREV VARCHAR2(100 CHAR),     -- DescTipoTramiteAbrev: Descripción abreviada del tipo de trámite;
L_ACTIVO NUMBER(1, 0),               -- Activo: Indicativo de si está activo o no para su uso;
T_COMPORTAMIENTO VARCHAR2(50 CHAR)   -- Comportamiento: Contiene un código del tipo: CXXX (ejemplo: C001, C015, ..) que usará el sistema para saber que parte de código ejecutar en el momento de invocar en un formulario cualquier trámite que tenga asociado este tipo.
) TABLESPACE GESTOR_DAT;

-- ----------------------------------------------------------------
   
CREATE TABLE GE_CFG_EXPED_TRAMITE (
CFG_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(15 CHAR) NOT NULL, 	     -- CodTra: Identificador único de la relación. Puede ser cualquiera, aunque por convenio en los datos de configuración inicial se ha construido con el código del tipo de expediente + código de situación + secuencial numÃ©rico de 2 posiciones (XX; ej: 01);
CFG_VALDOM_TIPEXP_ID NUMBER(19, 0) NOT NULL, -- CodExp: Valor de dominio correspondiente al tipo de expediente (Dominio: TIP_EXP);
CFG_VALDOM_SIT_ID    NUMBER(19, 0),          -- Sit: Valor de dominio correspondiente a la situación (Dominio: SIT);
CFG_TIP_TRA_ID       NUMBER(19, 0) NOT NULL, -- CodTipTra: Código del tipo de trámite (vinculado con la tabla GE_TIPO_TRAMITE);
D_DESCRIPCION VARCHAR2(255 CHAR),            -- DescTramite: Descripción del trámite cuando se utiliza en el tipo de expediente del registro;
D_DESC_ABREV VARCHAR2(100 CHAR),             -- DescTramiteAbrev: Descripción abreviada del trámite cuando se utiliza en el tipo de expediente del registro;
L_AUTO NUMBER(1, 0) DEFAULT 0 NOT NULL       -- Auto: Indicador Verdadero/falso que indica si el trámite se tiene que generar automáticamente en el expediente cuando se modifica la situación del mismo y evoluciona hacia la situación del registro. El comportamiento exacto se describirá en la HdU correspondiente.
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_CFG_EXPED_TRAMITE_H (
CFG_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(15 CHAR), 	    -- CodTra: Identificador único de la relación. Puede ser cualquiera, aunque por convenio en los datos de configuración inicial se ha construido con el código del tipo de expediente + código de situación + secuencial numÃ©rico de 2 posiciones (XX; ej: 01);
CFG_VALDOM_TIPEXP_ID NUMBER(19, 0), -- CodExp: Valor de dominio correspondiente al tipo de expediente (Dominio: TIP_EXP);
CFG_VALDOM_SIT_ID    NUMBER(19, 0), -- Sit: Valor de dominio correspondiente a la situación (Dominio: SIT);
CFG_TIP_TRA_ID       NUMBER(19, 0), -- CodTipTra: Código del tipo de trámite (vinculado con la tabla GE_TIPO_TRAMITE);
D_DESCRIPCION VARCHAR2(255 CHAR),   -- DescTramite: Descripción del trámite cuando se utiliza en el tipo de expediente del registro;
D_DESC_ABREV VARCHAR2(100 CHAR),    -- DescTramiteAbrev: Descripción abreviada del trámite cuando se utiliza en el tipo de expediente del registro;
L_AUTO NUMBER(1, 0) DEFAULT 0       -- Auto: Indicador Verdadero/falso que indica si el trámite se tiene que generar automáticamente en el expediente cuando se modifica la situación del mismo y evoluciona hacia la situación del registro. El comportamiento exacto se describirá en la HdU correspondiente.
) TABLESPACE GESTOR_DAT;

-- ----------------------------------------------------------------

CREATE TABLE GE_CFG_EXPED_SUBTRAM (
CFG_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
CFG_VALDOM_TIPEXP_ID NUMBER(19, 0) NOT NULL, -- TipExped: Valor de dominio correspondiente al tipo de expediente (Dominio: TIP_EXP);
CFG_TIP_TRA_SUP_ID NUMBER(19, 0) NOT NULL, 	 -- CodTraSup: Identificador único del código de trámite superior (vinculado con la tabla GE_TIPO_TRAMITE). Indica el código de trámite desde el que se puede crear o invocar otro trámite);
CFG_TIP_TRA_ID NUMBER(19, 0) NOT NULL,       -- CodTra: Identificador único del código de trámite (vinculado con la tabla GE_TIPO_TRAMITE). Indica el código de trámite que se puede crear dependiente (por debajo) del trámite superior;
D_DESCRIPCION VARCHAR2(255 CHAR),            -- Descripción: Descripción que se asigna por defecto al trámite cuando se crea. El usuario puede modificarla en el momento de la creación (funcionalidad objeto de otra HdU);
D_DESC_ABREV VARCHAR2(100 CHAR),             -- DescripciónAbrev: Descripción abreviada que se asigna por defecto al trámite cuando se crea. El usuario puede modificarla en el momento de la creación (funcionalidad objeto de otra HdU);
N_MAXIMO NUMBER(2, 0) DEFAULT 0 NOT NULL,    -- Máximo: Valor numÃ©rico que indica el número máximo de veces que se puede crear un trámite para un expediente dependiendo de un trámite superior. Por convenio, si el valor es 0, significa que no hay valor máximo y que se pueden crear cuantos trámites se quieran.
N_MINIMO NUMBER(2, 0) DEFAULT 0 NOT NULL     -- MÃ­nimo: Valor numÃ©rico que indica el número mÃ­nimo de trámites que se deben crear y cerrar dependiendo de un trámite superior para poder cerrar el trámite superior. Si el valor es 0, significa que no es necesario que haya ningún trámite de este tipo para poder cerrar el trámite superior. (Funcionalidad objeto de otra HdU).
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_CFG_EXPED_SUBTRAM_H (
CFG_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
CFG_VALDOM_TIPEXP_ID NUMBER(19, 0), -- TipExped: Valor de dominio correspondiente al tipo de expediente (Dominio: TIP_EXP);
CFG_TIP_TRA_SUP_ID NUMBER(19, 0),   -- CodTraSup: Identificador único del código de trámite superior (vinculado con la tabla GE_TIPO_TRAMITE). Indica el código de trámite desde el que se puede crear o invocar otro trámite);
CFG_TIP_TRA_ID NUMBER(19, 0),       -- CodTra: Identificador único del código de trámite (vinculado con la tabla GE_TIPO_TRAMITE). Indica el código de trámite que se puede crear dependiente (por debajo) del trámite superior;
D_DESCRIPCION VARCHAR2(255 CHAR),   -- Descripción: Descripción que se asigna por defecto al trámite cuando se crea. El usuario puede modificarla en el momento de la creación (funcionalidad objeto de otra HdU);
D_DESC_ABREV VARCHAR2(100 CHAR),    -- DescripciónAbrev: Descripción abreviada que se asigna por defecto al trámite cuando se crea. El usuario puede modificarla en el momento de la creación (funcionalidad objeto de otra HdU);
N_MAXIMO NUMBER(2, 0),              -- Máximo: Valor numÃ©rico que indica el número máximo de veces que se puede crear un trámite para un expediente dependiendo de un trámite superior. Por convenio, si el valor es 0, significa que no hay valor máximo y que se pueden crear cuantos trámites se quieran.
N_MINIMO NUMBER(2, 0)               -- MÃ­nimo: Valor numÃ©rico que indica el número mÃ­nimo de trámites que se deben crear y cerrar dependiendo de un trámite superior para poder cerrar el trámite superior. Si el valor es 0, significa que no es necesario que haya ningún trámite de este tipo para poder cerrar el trámite superior. (Funcionalidad objeto de otra HdU).
) TABLESPACE GESTOR_DAT;

-- ----------------------------------------------------------------

CREATE TABLE GE_TRAMITE_EXPED (
TRAM_EXP_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
TRAM_EXP_EXP_ID NUMBER(19,0) NOT NULL,     -- IdExpdte: Identificador único del expediente al que está asociado el trámite;
TRAM_EXP_TIP_TRA_ID NUMBER(19,0) NOT NULL, -- CodTipTra: Identificador del tipo de trámite (Vinculado a la tabla GE_TIPO_TRAMITE);
N_NIVEL NUMBER(3,0) DEFAULT 1 NOT NULL,    -- Nivel: Nivel jerárquico de anidamiento de trámites, para gestionar los trámites que dependen de un trámite superior (valor numÃ©rico);
TRAM_EXP_TRAMEXPSUP_ID NUMBER(19,0) NOT NULL,     -- IdTramExpdteSup: Identificar unico del trámite de nivel superior jerárquicamente, del que depende el trámite del registro;
D_DESCRIPCION VARCHAR2(255 CHAR),          -- Descripción: Descripción del trámite: Por defecto cuando se crea un trámite nuevo se propone al usuario la descripción que se encuentra en las tablas GE_CFG_EXPED_TRAMITE o GE_CFG_EXPED_SUBTRA, según como se cree el tramite, aunque el usuario tiene posibilidad de modificarla. (Funcionalidad objeto de otra HdU);
D_DESC_ABREV VARCHAR2(100 CHAR),           -- DescAbrev: Descripción abreviada del trámite: Por defecto cuando se crea un trámite nuevo se propone al usuario la descripción abreviada que se encuentra en las tablas GE_CFG_EXPED_TRAMITE o GE_CFG_EXPED_SUBTRA, según como se cree el tramite, aunque el usuario tiene posibilidad de modificarla. (Funcionalidad objeto de otra HdU);
T_RESPONSABLE VARCHAR2(255 CHAR),          -- Responsable: Identifica al actor responsable de la tramitación. Inicialmente, será un campo texto de libre entrada. En una HdU separada se desarrollará la funcionalidad de responsables de tramitación y se modificará este campo;
F_FECHA_INI DATE NOT NULL,                 -- Fecha: Fecha inicio del trámite;
T_OBSERVACIONES VARCHAR2(255 CHAR),        -- Observaciones: Campo de texto similar al existente en el expediente (en cuanto al tamaÃ±o) para que el usuario pueda realizar las anotaciones que estime oportunas;
L_FINALIZADO NUMBER(1,0) DEFAULT 0 NOT NULL, -- Finalizado: Indicador de tipo verdadero/falso que indica si el expediente está terminado o no.
L_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL    -- Activo: Indicador de tipo verdadero/falso que indica si el trámite ha sido borrado (de manera lógica) para el expediente. Por defecto estará siempre a 1.
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_TRAMITE_EXPED_H (
TRAM_EXP_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
TRAM_EXP_EXP_ID NUMBER(19,0),       -- IdExpdte: Identificador único del expediente al que está asociado el trámite;
TRAM_EXP_TIP_TRA_ID NUMBER(19,0),   -- CodTipTra: Identificador del tipo de trámite (Vinculado a la tabla GE_TIPO_TRAMITE);
N_NIVEL NUMBER(3,0),                -- Nivel: Nivel jerárquico de anidamiento de trámites, para gestionar los trámites que dependen de un trámite superior (valor numÃ©rico);
TRAM_EXP_TRAMEXPSUP_ID NUMBER(19,0),       -- IdTramExpdteSup: Identificar unico del trámite de nivel superior jerárquicamente, del que depende el trámite del registro;
D_DESCRIPCION VARCHAR2(255 CHAR),   -- Descripción: Descripción del trámite: Por defecto cuando se crea un trámite nuevo se propone al usuario la descripción que se encuentra en las tablas GE_CFG_EXPED_TRAMITE o GE_CFG_EXPED_SUBTRA, según como se cree el tramite, aunque el usuario tiene posibilidad de modificarla. (Funcionalidad objeto de otra HdU);
D_DESC_ABREV VARCHAR2(100 CHAR),    -- DescAbrev: Descripción abreviada del trámite: Por defecto cuando se crea un trámite nuevo se propone al usuario la descripción abreviada que se encuentra en las tablas GE_CFG_EXPED_TRAMITE o GE_CFG_EXPED_SUBTRA, según como se cree el tramite, aunque el usuario tiene posibilidad de modificarla. (Funcionalidad objeto de otra HdU);
T_RESPONSABLE VARCHAR2(255 CHAR),   -- Responsable: Identifica al actor responsable de la tramitación. Inicialmente, será un campo texto de libre entrada. En una HdU separada se desarrollará la funcionalidad de responsables de tramitación y se modificará este campo;
F_FECHA_INI DATE,                   -- Fecha: Fecha inicio del trámite;
T_OBSERVACIONES VARCHAR2(255 CHAR), -- Observaciones: Campo de texto similar al existente en el expediente (en cuanto al tamaÃ±o) para que el usuario pueda realizar las anotaciones que estime oportunas;
L_FINALIZADO NUMBER(1,0),           -- Finalizado: Indicador de tipo verdadero/falso que indica si el expediente está terminado o no.
L_ACTIVO NUMBER(1,0)                -- Activo: Indicador de tipo verdadero/falso que indica si el trámite ha sido borrado (de manera lógica) para el expediente. Por defecto estará siempre a 1.
) TABLESPACE GESTOR_DAT;

-- ------------------------------------------------------------------------------------

CREATE TABLE GE_TIPOS_DOCUMENTOS (
TIP_DOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(10 CHAR) NOT NULL, 	     --TipoDoc: Código identificativo único del tipo de documento;
D_DESCRIPCION VARCHAR2(255 CHAR),    	     --DescDoc: Descripción del tipo de documento;
D_DESC_ABREV VARCHAR2(100 CHAR),      	     --DescDocAbrev: Descripción abreviada del tipo de documento;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL,    --Activo: Indicativo de si está activo o no para su uso;
T_CODIGO_TIPO_ENI VARCHAR2(10 CHAR) NOT NULL, --TipoDocENI: Contiene el código del metadato correspondiente al “tipo documental” del ENI.
T_CODIGO_ORIGEN VARCHAR2(1 CHAR) DEFAULT '0' NOT NULL --Origen: Contiene el código identificativo del origen según el ENI: Los valores posibles son: 0: Ciudadano; 1: Administración;
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_TIPOS_DOCUMENTOS_H (
TIP_DOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(10 CHAR), 	     --TipoDoc: Código identificativo único del tipo de documento;
D_DESCRIPCION VARCHAR2(255 CHAR),    --DescDoc: Descripción del tipo de documento;
D_DESC_ABREV VARCHAR2(100 CHAR),     --DescDocAbrev: Descripción abreviada del tipo de documento;
L_ACTIVO NUMBER(1, 0),               --Activo: Indicativo de si está activo o no para su uso;
T_CODIGO_TIPO_ENI VARCHAR2(10 CHAR), --TipoDocENI: Contiene el código del metadato correspondiente al “tipo documental” del ENI.
T_CODIGO_ORIGEN VARCHAR2(1 CHAR)     --Origen: Contiene el código identificativo del origen según el ENI: Los valores posibles son: 0: Ciudadano; 1: Administración;
) TABLESPACE GESTOR_DAT;

COMMENT ON COLUMN GE_TIPOS_DOCUMENTOS.T_CODIGO_ORIGEN IS '0: Ciudadano; 1: Administración;';

-- ----------------------------------------------------------------

CREATE TABLE GE_CFG_DOC_EXPED_TRAM (
CFG_DOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
--Contiene la relación entre tipo de expediente, la situación y el tipo de trámite y los documentos que se pueden generar o importar desde ese punto de la tramitación.
--Por convenio, si la situación (campo Sit) no está informada (Null) se entiende que el tipo de documento del registro se puede utilizar con independencia de la situación en la que se encuentra el expediente. Igualmente con el Tipo de Trámite y el Tipo de Trámite Superior.
T_COD_REL_DET VARCHAR2(25) NOT NULL,            -- CodRedDET: Identificador único de la relación. Puede ser cualquiera, aunque por convenio en los datos de configuración inicial se ha construido con el código del tipo de expediente + código de tramite + secuencial numérico de 2 posiciones (XX; ej: 01);
CFGDOC_VALDOM_TIPEXP_ID NUMBER(19, 0) NOT NULL, -- CodExp: Valor de dominio correspondiente al tipo de expediente (Dominio: “TIP_EXP”);
CFGDOC_VALDOM_SIT_ID NUMBER(19, 0),             -- Sit: Valor de dominio correspondiente a la situación (Dominio: “SIT”);
CFGDOC_TIP_TRA_ID NUMBER(19, 0) NOT NULL,       -- CodTipTra: Código del tipo de trámite (vinculado con la tabla GE_TIPO_TRAMITE);
CFGDOC_TIP_TRA_SUP_ID NUMBER(19, 0),            -- CodTipTraSup: Código del tipo de trámite superior (vinculado con la tabla GE_TIPO_TRAMITE);
CFGDOC_TIP_DOC_ID NUMBER(19, 0) NOT NULL,       -- TipoDoc: Código del tipo de documento que se permite importar o generar (Vinculado con la tabla GE_TIPOS_DOCUMENTOS);
D_DESC_TIP_DOC VARCHAR2(255 CHAR),              -- DescDoc: Descripción del tipo de documento. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
D_DESC_ABREV_TIP_DOC VARCHAR2(100 CHAR),         -- DescAbrevDoc: Descripción abreviada del tipo de documento. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
L_OBLIGATORIO NUMBER(1, 0) DEFAULT 0 NOT NULL,  -- Obligatorio: Indicativo de tipo verdadero/falso (0/1) que indica si el tipo de documento es de uso obligatorio o no en el trámite para poder cerrarlo.
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL,       -- Activo: Indicador de tipo verdadero/falso (0/1) que indica si el registro está en vigor y se puede utilizar o no.
T_CODIGO_TIPO_ENI VARCHAR2(10 CHAR),            -- TipoDocENI: Contiene el código del metadato correspondiente al “tipo documental” del ENI. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
T_CODIGO_ORIGEN_ENI VARCHAR2(1 CHAR) DEFAULT '0' NOT NULL -- Origen: Contiene el código identificativo del origen según el ENI: Los valores posibles son: 0: Ciudadano; 1: Administración; Aunque ya existe en la tabla GE_TIPOS_DOCUMENTOS, para los efectos de metadatar un documento prevalecerá el valor el “Origen” de esta tabla de configuración si son distitnos.
) TABLESPACE GESTOR_DAT;
   
COMMENT ON COLUMN GE_CFG_DOC_EXPED_TRAM.T_COD_REL_DET       IS 'código del tipo de expediente + código de tramite + secuencial numérico de 2 posiciones';
COMMENT ON COLUMN GE_CFG_DOC_EXPED_TRAM.T_CODIGO_ORIGEN_ENI IS '0: Ciudadano; 1: Administración;';

CREATE TABLE GE_CFG_DOC_EXPED_TRAM_H (
CFG_DOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_COD_REL_DET VARCHAR2(25),                     -- CodRedDET: Identificador único de la relación. Puede ser cualquiera, aunque por convenio en los datos de configuración inicial se ha construido con el código del tipo de expediente + código de tramite + secuencial numérico de 2 posiciones (XX; ej: 01);
CFGDOC_VALDOM_TIPEXP_ID NUMBER(19, 0),          -- CodExp: Valor de dominio correspondiente al tipo de expediente (Dominio: “TIP_EXP”);
CFGDOC_VALDOM_SIT_ID NUMBER(19, 0),             -- Sit: Valor de dominio correspondiente a la situación (Dominio: “SIT”);
CFGDOC_TIP_TRA_ID NUMBER(19, 0),                -- CodTipTra: Código del tipo de trámite (vinculado con la tabla GE_TIPO_TRAMITE);
CFGDOC_TIP_TRA_SUP_ID NUMBER(19, 0),            -- CodTipTraSup: Código del tipo de trámite superior (vinculado con la tabla GE_TIPO_TRAMITE);
CFGDOC_TIP_DOC_ID NUMBER(19, 0),                -- TipoDoc: Código del tipo de documento que se permite importar o generar (Vinculado con la tabla GE_TIPOS_DOCUMENTOS);
D_DESC_TIP_DOC VARCHAR2(255 CHAR),              -- DescDoc: Descripción del tipo de documento. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
D_DESC_ABREV_TIP_DOC VARCHAR2(100 CHAR),         -- DescAbrevDoc: Descripción abreviada del tipo de documento. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
L_OBLIGATORIO NUMBER(1, 0),                     -- Obligatorio: Indicativo de tipo verdadero/falso (0/1) que indica si el tipo de documento es de uso obligatorio o no en el trámite para poder cerrarlo.
L_ACTIVO NUMBER(1, 0),                          -- Activo: Indicador de tipo verdadero/falso (0/1) que indica si el registro está en vigor y se puede utilizar o no.
T_CODIGO_TIPO_ENI VARCHAR2(10 CHAR),            -- TipoDocENI: Contiene el código del metadato correspondiente al “tipo documental” del ENI. Prevalece sobre el valor de la tabla GE_TIPOS_DOCUMENTOS;
T_CODIGO_ORIGEN_ENI VARCHAR2(1 CHAR)            -- Origen: Contiene el código identificativo del origen según el ENI: Los valores posibles son: 0: Ciudadano; 1: Administración; Aunque ya existe en la tabla GE_TIPOS_DOCUMENTOS, para los efectos de metadatar un documento prevalecerá el valor el “Origen” de esta tabla de configuración si son distitnos.
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_PLANTILLAS (
PLA_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(25) NOT NULL,   -- CodPlantilla: Código único de la plantilla;
D_DESCRIPCION VARCHAR2(255 CHAR), -- DescPlantilla: Descripción de la plantilla;
D_DESC_ABREV VARCHAR2(100 CHAR),  -- DescAbrevPlantilla: Descripción Abreviada de la plantilla;
T_FICHERO VARCHAR2(100) NOT NULL, -- Fichero: Nombre del fichero de plantilla importado;
B_BYTES BLOB,                     -- MapaBits: Campo de tipo CLOB que contiene el mapa de bits de la plantilla.
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL -- Activo: Indicador verdadero/falso. 
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_PLANTILLAS_H (
PLA_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(25),            -- CodPlantilla: Código único de la plantilla;
D_DESCRIPCION VARCHAR2(255 CHAR), -- DescPlantilla: Descripción de la plantilla;
D_DESC_ABREV VARCHAR2(100 CHAR),  -- DescAbrevPlantilla: Descripción Abreviada de la plantilla;
T_FICHERO VARCHAR2(100),          -- Fichero: Nombre del fichero de plantilla importado;
B_BYTES BLOB,                     -- MapaBits: Campo de tipo CLOB que contiene el mapa de bits de la plantilla.
L_ACTIVO NUMBER(1, 0)             -- Activo: Indicador verdadero/falso. 
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_PLANTILLA_DOC (
PLDOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
-- Esta tabla es la que contiene la información de las plantillas que se pueden usar para cada una de las relaciones definidas en la tabla “GE_CFG_DOC_EXPED_TRAM”. Para cada documento definido en esta relación, se puede definir una o múltiples plantillas. Si hay una sola definida, el sistema generará directamente el documento con esta plantilla. Si hubiera más de una, el sistema solicitará al usuario que selecciones la que quiere usar (Funcionalidad objeto de otra HdU).
PLDOC_CFGDOC_ID NUMBER(19, 0) NOT NULL,  -- CodRelDET: Identificador único de la relación entre documento, situación, trámite y tramite superior. Vinculado a la tabla GE_CFG_DOC_EXPED_TRAM;
PLDOC_PLA_ID NUMBER(19, 0) NOT NULL,     -- CodPlantilla: Código de la plantilla. Vinculado a la tabla GE_PLANTILLAS;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL -- Activo: Indicador de tipo verdadero/falso. 
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_PLANTILLA_DOC_H (
PLDOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
PLDOC_CFGDOC_ID NUMBER(19, 0),           -- CodRelDET: Identificador único de la relación entre documento, situación, trámite y tramite superior. Vinculado a la tabla GE_CFG_DOC_EXPED_TRAM;
PLDOC_PLA_ID NUMBER(19, 0),              -- CodPlantilla: Código de la plantilla. Vinculado a la tabla GE_PLANTILLAS;
L_ACTIVO NUMBER(1, 0)                    -- Activo: Indicador de tipo verdadero/falso. 
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_TIPOS_METADATOS_ENI (
-- Tabla que contiene el catálogo de tipos de metadatos ENI definidos en el sistema. La funcionalidad para metadatar los expedientes y los documentos no se desarrollará en el PMV. Se deja la base de datos preparada para ello, pero el desarrollo de la HdU que se cree al efecto se postpondrá hasta una fase posterior del proyecto.
TMETENI_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_CODIGO VARCHAR2(25 CHAR) NOT NULL,           -- CodMetadato: Código único identificador del tipo de metadato;
D_DESCRIPCION VARCHAR2(255 CHAR),              -- DescMetadato: Descripción del tipo de metadato;
D_DESC_ABREV VARCHAR2(100 CHAR),               -- DescAbrevMetadato: Descripción abreviada del tipo de metadato;
L_OBLIGATORIO NUMBER(1, 0) DEFAULT 0 NOT NULL, -- Obligatorio: Indicador con valores posibles “O” o “C” que indica si el metadato es de tipo “Obligatorio” o “Complementario”;
T_OBJETO VARCHAR2(3 CHAR) NOT NULL,            -- Objeto: Código con valores posible “DOC” o “EXP” que indica si el metadato se aplica a documentos o a expedientes;
N_ORDEN NUMBER(3,0),                           -- Orden: Valor numérico que indica el orden del metadato;
T_TIPO_DATO VARCHAR2(25 CHAR) NOT NULL,        -- TipoDato: Indica el tipo de dato que contiene el metadato: String, fecha, Lista, …;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL,      -- Activo: Indicativo de tipo Verdadero/Falso. Indica si el tipo de metadato está en vigor o no;
L_CAPTURA_USR NUMBER(1, 0) DEFAULT 0 NOT NULL  -- CapturaUsr: Indicativo de tipo Verdadero/Falso que indica si el valor del metadato lo debe captura el usuario en el sistema (en los demás casos, el valor será asignado automáticamente por el sistema) Funcionalidad objeto de otra HdU.
) TABLESPACE GESTOR_DAT;

COMMENT ON COLUMN GE_TIPOS_METADATOS_ENI.L_OBLIGATORIO IS 'O:Obligatorio; C:Complementario;';
COMMENT ON COLUMN GE_TIPOS_METADATOS_ENI.T_OBJETO      IS 'DOC:documentos; EXP:expedientes;';

CREATE TABLE GE_TIPOS_METADATOS_ENI_H (
TMETENI_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
T_CODIGO VARCHAR2(25 CHAR),       -- CodMetadato: Código único identificador del tipo de metadato;
D_DESCRIPCION VARCHAR2(255 CHAR), -- DescMetadato: Descripción del tipo de metadato;
D_DESC_ABREV VARCHAR2(100 CHAR),  -- DescAbrevMetadato: Descripción abreviada del tipo de metadato;
L_OBLIGATORIO NUMBER(1, 0),       -- Obligatorio: Indicador con valores posibles “O” o “C” que indica si el metadato es de tipo “Obligatorio” o “Complementario”;
T_OBJETO VARCHAR2(3 CHAR),        -- Objeto: Código con valores posible “DOC” o “EXP” que indica si el metadato se aplica a documentos o a expedientes;
N_ORDEN NUMBER(3,0),              -- Orden: Valor numérico que indica el orden del metadato;
T_TIPO_DATO VARCHAR2(25 CHAR),    -- TipoDato: Indica el tipo de dato que contiene el metadato: String, fecha, Lista, …;
L_ACTIVO NUMBER(1, 0),            -- Activo: Indicativo de tipo Verdadero/Falso. Indica si el tipo de metadato está en vigor o no;
L_CAPTURA_USR NUMBER(1, 0)        -- CapturaUsr: Indicativo de tipo Verdadero/Falso que indica si el valor del metadato lo debe captura el usuario en el sistema (en los demás casos, el valor será asignado automáticamente por el sistema) Funcionalidad objeto de otra HdU.
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_VALORES_META_ENI (
-- Tabla que contiene la lista de valores que pueden tomar determinados metadatos ENI según el catálogo de estándares vigente. Para los tipos de metadatos que tengan un solo valor asociado en esta tabla, el sistema asociará automáticamente dicho valor a todo elemento (Documento o Expediente) a metadatar. La funcionalidad para metadatar los expedientes y los documentos no se desarrollará en el PMV. Se deja la base de datos preparada para ello, pero el desarrollo de la HdU que se cree al efecto se postpondrá hasta una fase posterior del proyecto.
VMETENI_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
VMETENI_TMETENI_ID NUMBER(19,0) NOT NULL,  -- CodMetadato: Código único identificador del tipo de metadato (Vinculado a la tabla GE_TIPOS_METADATOS_ENI);
T_CODIGO_ENI VARCHAR2(255 CHAR),           -- CodigoENI: Es el valor que debe tomar el metadato en la tabla de metadatos del elemento;
T_VALOR VARCHAR2(255 CHAR) NOT NULL,        -- Valor: Es una descripción representativa que se presentará al usuario para su asignación en caso de que esta sea manual;
N_ORDEN NUMBER(3,0),                       -- Orden: Orden en que se presentarán los valores en la lista de selección que se presente al usuario llegado el caso;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL   -- Activo: Indicador de tipo Verdadero/Falso que indica si el valor está en vigor o no;
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_VALORES_META_ENI_H (
VMETENI_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
VMETENI_TMETENI_ID NUMBER(19,0),  -- CodMetadato: Código único identificador del tipo de metadato (Vinculado a la tabla GE_TIPOS_METADATOS_ENI);
T_CODIGO_ENI VARCHAR2(255 CHAR),  -- CodigoENI: Es el valor que debe tomar el metadato en la tabla de metadatos del elemento;
T_VALOR VARCHAR2(255 CHAR),        -- Valor: Es una descripción representativa que se presentará al usuario para su asignación en caso de que esta sea manual;
N_ORDEN NUMBER(3,0),              -- Orden: Orden en que se presentarán los valores en la lista de selección que se presente al usuario llegado el caso;
L_ACTIVO NUMBER(1, 0)             -- Activo: Indicador de tipo Verdadero/Falso que indica si el valor está en vigor o no;
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_DOCUMENTOS (
-- Esta tabla contiene todos los documentos del sistema, con sus características propias, sin relación ninguna con ningún expediente. Hay que considerar que un mismo documento puede pertenecer a varios expedientes.
DOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
T_IDEN_DOC VARCHAR2(25 CHAR) NOT NULL,                 -- IdDoc: Identificador único del documento. Este identificador se construirá en el momento del alta de cualquier documento en base a una Serie automática gestionada por el gestor de series del Sistema. La funcionalidad se desarrollará en HdU separada. Este identificador debe coincidir por comodidad con el valor del metadato ENI “Identificador” (CodMetadato);
DOC_TIPDOC_ID NUMBER(19,0) NOT NULL,                   -- TipoDoc: Código del tipo de documento. Vinculado con la tabla GE_TIPOS_DOCUMENTOS;
D_DESCRIPCION VARCHAR2(255 CHAR),                      -- DescDoc: Descripción del documento: A capturar en el momento del alta del documento,
D_DESC_ABREV VARCHAR2(100 CHAR),                       -- DescAbrevDoc: Descripción abreviada del documento: A capturar en el momento del alta del documento: No obligatorio;
T_NOMBRE_FICHERO VARCHAR2(100) NOT NULL,               -- NombreFich: Nombre del fichero que se importa o se genera, sin extensión ni Path;
T_EXTENSION_FICHERO VARCHAR2(10) NOT NULL,             -- Extension: Extensión del documento, sin necesidad de añadir el punto, sólo pdf, odt, ott, ...
B_BYTES BLOB,                                          -- MapaBits: Campo de tipo CLOB que contiene el documento en sí;
T_ORIGEN VARCHAR2(1 CHAR),                             -- OrigenDoc: Indicador que puede tomar los valores “I” o “G” indicando si el documento es importado desde un fichero o sistema externo o si es generado por el propio sistema;
DOC_PLA_ID NUMBER(19,0),                               -- PlantillaUsada: Código de la plantilla que se ha usado para generar el documento, en caso de que fuese un documento generado a partir de una plantilla. Vinculado con la tabla GE_PLANTILLAS;
F_FECHA_CAPTURA DATE,                                  -- FechaCaptura: Contiene la fecha en la que el fichero se importa al sistema o la fecha en la que se genera. Es uno de los valores que se deben capturar para incluirlo como metadato ENI del documento;
N_VERSION_DOC NUMBER(19, 0) DEFAULT 1 NOT NULL,        -- Version_Doc: numérico
DOC_ORIGINAL_ID NUMBER(19, 0),                         -- IdDocOriginal: relacionado con el IdDoc de la misma tabla GE_DOCUMENTOS. FK a GE_DOCUMENTOS
L_ULTIMA_VERSION NUMBER(1,0) DEFAULT 1 NOT NULL,       -- Ultimaversion: Indicador verdadero/falso. Por defecto: 1
L_DOC_VERSIONADO NUMBER(1,0) DEFAULT 0 NOT NULL,       -- DocVersionado: Indicador verdadero/falso. Por defecto 0
L_FIRMADO NUMBER(1, 0) DEFAULT 0 NOT NULL,             --Firmado: Indicador Verdadero/Falso que indica si el documento está firmado;
L_SELLADO NUMBER(1, 0) DEFAULT 0 NOT NULL,             --Sellado: Indicador Verdadero/Falso que indica si el documento está sellado con un sello de registro;
L_EDITABLE NUMBER(1, 0) DEFAULT 0 NOT NULL,            --Editable: Indicador Verdadero/Falso que indica si el documento es editable con el editor online;
L_ANONIMIZADO NUMBER(1, 0) DEFAULT 0 NOT NULL,         --Anonimizado: Indicador Verdadero/Falso que indica si el documento está anonimizado;
L_ANONIMIZADO_PARCIAL NUMBER(1, 0) DEFAULT 0 NOT NULL, --Anonimizado Parcial: Indicador Verdadero/Falso que indica si el documento está anonimizado parcialmente;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL               --Activo: Indicador Verdadero/Falso de si está en vigor o no el documento;
) TABLESPACE GESTOR_DAT;

COMMENT ON COLUMN GE_DOCUMENTOS.T_ORIGEN IS 'I:importado desde un fichero o sistema externo; G:generado por el propio sistema.';

CREATE TABLE GE_DOCUMENTOS_H (
DOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
T_IDEN_DOC VARCHAR2(25 CHAR),         --IdDoc: Identificador único del documento. Este identificador se construirá en el momento del alta de cualquier documento en base a una Serie automática gestionada por el gestor de series del Sistema. La funcionalidad se desarrollará en HdU separada. Este identificador debe coincidir por comodidad con el valor del metadato ENI “Identificador” (CodMetadato);
DOC_TIPDOC_ID NUMBER(19,0),          --TipoDoc: Código del tipo de documento. Vinculado con la tabla GE_TIPOS_DOCUMENTOS;
D_DESCRIPCION VARCHAR2(255 CHAR),    --DescDoc: Descripción del documento: A capturar en el momento del alta del documento,
D_DESC_ABREV VARCHAR2(100 CHAR),     --DescAbrevDoc: Descripción abreviada del documento: A capturar en el momento del alta del documento: No obligatorio;
T_NOMBRE_FICHERO VARCHAR2(100),      --NombreFich: Nombre del fichero que se importa o se genera, sin extensión ni Path;
T_EXTENSION_FICHERO VARCHAR2(10),    --Extension: Extensión del documento, sin necesidad de añadir el punto, sólo pdf, odt, ott, ...
B_BYTES BLOB,                        --MapaBits: Campo de tipo CLOB que contiene el documento en sí;
T_ORIGEN VARCHAR2(1 CHAR),           --OrigenDoc: Indicador que puede tomar los valores “I” o “G” indicando si el documento es importado desde un fichero o sistema externo o si es generado por el propio sistema;
DOC_PLA_ID NUMBER(19,0),             --PlantillaUsada: Código de la plantilla que se ha usado para generar el documento, en caso de que fuese un documento generado a partir de una plantilla. Vinculado con la tabla GE_PLANTILLAS;
F_FECHA_CAPTURA DATE,                --FechaCaptura: Contiene la fecha en la que el fichero se importa al sistema o la fecha en la que se genera. Es uno de los valores que se deben capturar para incluirlo como metadato ENI del documento;
N_VERSION_DOC NUMBER(19, 0),         -- Version_Doc: numérico
DOC_ORIGINAL_ID NUMBER(19, 0),       -- IdDocOriginal: relacionado con el IdDoc de la misma tabla GE_DOCUMENTOS. FK a GE_DOCUMENTOS
L_ULTIMA_VERSION NUMBER(1,0),        -- Ultimaversion: Indicador verdadero/falso. Por defecto: 1
L_DOC_VERSIONADO NUMBER(1,0),        -- DocVersionado: Indicador verdadero/falso. Por defecto 0
L_FIRMADO NUMBER(1, 0) ,             --Firmado: Indicador Verdadero/Falso que indica si el documento está firmado;
L_SELLADO NUMBER(1, 0) ,             --Sellado: Indicador Verdadero/Falso que indica si el documento está sellado con un sello de registro;
L_EDITABLE NUMBER(1, 0) ,            --Editable: Indicador Verdadero/Falso que indica si el documento es editable con el editor online;
L_ANONIMIZADO NUMBER(1, 0) ,         --Anonimizado: Indicador Verdadero/Falso que indica si el documento está anonimizado;
L_ANONIMIZADO_PARCIAL NUMBER(1, 0) , --Anonimizado Parcial: Indicador Verdadero/Falso que indica si el documento está anonimizado parcialmente;
L_ACTIVO NUMBER(1, 0)                --Activo: Indicador Verdadero/Falso de si está en vigor o no el documento;
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_DOCUMENTOS_EXPEDIENTES (
--Esta tabla contiene la información que relaciona cada documento con el o los expedientes y trámite o trámites a los que pertenece. Un mismo documento se puede vincular con distintos expedientes, e incluso varias veces con el mismo expediente en trámites distintos (por ejemplo, un acuerdo que se notifica a varios interesados).
DOCEXP_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
DOCEXP_DOC_ID NUMBER(19,0) NOT NULL,      -- IdDoc: Identificador del documento. Vinculado a la tabla GE_DOCUMENTOS;
DOCEXP_EXP_ID NUMBER(19,0) NOT NULL,      -- IdExpdte: Identificador único del expediente. Vinculado a la tabla GE_EXPEDIENTES;
DOCEXP_TRAM_EXP_ID NUMBER(19,0) NOT NULL,     -- IdTramite: Identificador único del trámite al que pertenece el documento. Vinculado a la tabla GE_TRAMITE_EXPED;
D_DESCRIPCION_DOC VARCHAR2(255 CHAR),     -- Desc: Descripción del documento dentro del expediente. Por defecto tomará el valor de la tabla GE_DOCUMENTOS, pero se puede personalizar para cada expediente y trámite.
D_DESC_ABREV_DOC VARCHAR2(100 CHAR),      -- DescAbrev: Descripción abreviada del documento dentro del expediente. Por defecto tomará el valor de la tabla GE_DOCUMENTOS, pero se puede personalizar para cada expediente y trámite.
T_DOC_TRABAJO VARCHAR2(1 CHAR) DEFAULT 'E' NOT NULL, -- DocExpdteTrab: Indicador que puede tomar los valores “E” o “T” para indicar si se trata de un documento que forma parte del expediente ENI o si es un documento de trabajo que no forma parte de él.
DOCEXP_AGRDOC_ID NUMBER(19,0) NOT NULL,   -- IdAgrupación: Identificador único de la agrupación de documentos desde la que se ha añadido la relación del documento con el expediente. Puede estar a nulo si el documento no se ha añadido por medio de una agrupación de documentos. La funcionalidad de “Agrupación de documentos” se describe más adelante.
N_ORDEN NUMBER(3,0),                      -- Orden: Indica el orden del documento en el expediente ENI. Solo tiene sentido inicialmente para la construcción del índice del expediente ENI. Se valida para los documentos con “DocExpdteTrab” igual a “E” y sobre el expediente global, no sobre un trámite concreto.
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL  -- Activo: Indicador Verdadero/Falso que indica si la relación está en vigor o no.
) TABLESPACE GESTOR_DAT;
   
COMMENT ON COLUMN GE_DOCUMENTOS_EXPEDIENTES.T_DOC_TRABAJO IS 'E:documento que forma parte del expediente ENI; T:documento de trabajo;';

CREATE TABLE GE_DOCUMENTOS_EXPEDIENTES_H (
DOCEXP_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
DOCEXP_DOC_ID NUMBER(19,0),            -- IdDoc: Identificador del documento. Vinculado a la tabla GE_DOCUMENTOS;
DOCEXP_EXP_ID NUMBER(19,0),            -- IdExpdte: Identificador único del expediente. Vinculado a la tabla GE_EXPEDIENTES;
DOCEXP_TRAM_EXP_ID NUMBER(19,0),           -- IdTramite: Identificador único del trámite al que pertenece el documento. Vinculado a la tabla GE_TRAMITE_EXPED;
D_DESCRIPCION_DOC VARCHAR2(255 CHAR),  -- Desc: Descripción del documento dentro del expediente. Por defecto tomará el valor de la tabla GE_DOCUMENTOS, pero se puede personalizar para cada expediente y trámite.
D_DESC_ABREV_DOC VARCHAR2(100 CHAR),   -- DescAbrev: Descripción abreviada del documento dentro del expediente. Por defecto tomará el valor de la tabla GE_DOCUMENTOS, pero se puede personalizar para cada expediente y trámite.
T_DOC_TRABAJO VARCHAR2(1 CHAR),        -- DocExpdteTrab: Indicador que puede tomar los valores “E” o “T” para indicar si se trata de un documento que forma parte del expediente ENI o si es un documento de trabajo que no forma parte de él.
DOCEXP_AGRDOC_ID NUMBER(19,0),         -- IdAgrupación: Identificador único de la agrupación de documentos desde la que se ha añadido la relación del documento con el expediente. Puede estar a nulo si el documento no se ha añadido por medio de una agrupación de documentos. La funcionalidad de “Agrupación de documentos” se describe más adelante.
N_ORDEN NUMBER(3,0),                   -- Orden: Indica el orden del documento en el expediente ENI. Solo tiene sentido inicialmente para la construcción del índice del expediente ENI. Se valida para los documentos con “DocExpdteTrab” igual a “E” y sobre el expediente global, no sobre un trámite concreto.
L_ACTIVO NUMBER(1, 0)                  -- Activo: Indicador Verdadero/Falso que indica si la relación está en vigor o no.
) TABLESPACE GESTOR_DAT;
   
-- --------------------------------------------

CREATE TABLE GE_AGRUP_EXPEDIENTES (
AGREXP_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
AGREXP_EXP_ID NUMBER(19,0) NOT NULL,      -- IdExpdte: Identificador único del expediente al que pertenece la agrupación (Vinculado a la tabla GE_EXPEDIENTES);
D_DESCRIPCION_DOC VARCHAR2(255 CHAR),     -- Desc: Descripción de la agrupación;
D_DESC_ABREV_DOC VARCHAR2(100 CHAR),      -- DescAbrev: Descripción abreviada de la agrupación;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL  -- Activo: Indicador Verdadero/Falso que indica si la agrupación está en vigor o no;
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_AGRUP_EXPEDIENTES_H (
AGREXP_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
AGREXP_EXP_ID NUMBER(19,0),           -- IdExpdte: Identificador único del expediente al que pertenece la agrupación (Vinculado a la tabla GE_EXPEDIENTES);
D_DESCRIPCION_DOC VARCHAR2(255 CHAR), -- Desc: Descripción de la agrupación;
D_DESC_ABREV_DOC VARCHAR2(100 CHAR),  -- DescAbrev: Descripción abreviada de la agrupación;
L_ACTIVO NUMBER(1, 0)                 -- Activo: Indicador Verdadero/Falso que indica si la agrupación está en vigor o no;
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------

CREATE TABLE GE_AGRUP_DOCUMENTOS (
AGRDOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
AGRDOC_AGREXP_ID NUMBER(19,0) NOT NULL,    -- IdAgrupacion: Identificador único de la agrupación. Vinculado a la tabla GE_AGRUP_EXPEDIENTES;
AGRDOC_DOC_ID NUMBER(19,0) NOT NULL,       -- IdDoc: Identificador único del documento. Vinculado a la tabla GE_DOCUMENTOS;
N_ORDEN NUMBER(3,0),                       -- Orden: Indica el orden en el que se deben mostrar los documentos en una agrupación;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL   -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_AGRUP_DOCUMENTOS_H (
AGRDOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
AGRDOC_AGREXP_ID NUMBER(19,0),    -- IdAgrupacion: Identificador único de la agrupación. Vinculado a la tabla GE_AGRUP_EXPEDIENTES;
AGRDOC_DOC_ID NUMBER(19,0),       -- IdDoc: Identificador único del documento. Vinculado a la tabla GE_DOCUMENTOS;
N_ORDEN NUMBER(3,0),              -- Orden: Indica el orden en el que se deben mostrar los documentos en una agrupación;
L_ACTIVO NUMBER(1, 0)             -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------
   
CREATE TABLE GE_METADATOS_DOCUMENTOS (
MET_DOC_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
METDOC_DOC_ID NUMBER(19,0) NOT NULL,       -- IdDoc: Identificador único del documento. Vinculado a la tabla GE_DOCUMENTOS;
METDOC_TMETENI_ID NUMBER(19,0) NOT NULL,   -- CodMetadato: Identificador del tipo de metadato. Vinculado a la tabla GE_TIPOS_METADATOS_ENI;
N_ORDEN NUMBER(3,0),                       -- Orden: Indica el orden en que se debe presentar el metatado;
T_VALOR VARCHAR2(255 CHAR) NOT NULL,        -- Valor: Contiene el valor del metadato para el documento; 
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL   -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;
                                      
CREATE TABLE GE_METADATOS_DOCUMENTOS_H (
MET_DOC_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
METDOC_DOC_ID NUMBER(19,0),       -- IdDoc: Identificador único del documento. Vinculado a la tabla GE_DOCUMENTOS;
METDOC_TMETENI_ID NUMBER(19,0),   -- CodMetadato: Identificador del tipo de metadato. Vinculado a la tabla GE_TIPOS_METADATOS_ENI;
N_ORDEN NUMBER(3,0),              -- Orden: Indica el orden en que se debe presentar el metatado;
T_VALOR VARCHAR2(255 CHAR),        -- Valor: Contiene el valor del metadato para el documento; 
L_ACTIVO NUMBER(1, 0)             -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;

-- --------------------------------------------
   
CREATE TABLE GE_METADATOS_EXPEDIENTES (
MET_EXP_ID NUMBER(19,0) NOT NULL, F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0), 
METEXP_EXP_ID NUMBER(19,0) NOT NULL,       -- IdExpdte: Identificador único del expediente. Vinculado a la tabla GE_EXPEDIENTES;
METEXP_TMETENI_ID NUMBER(19,0) NOT NULL,   -- CodMetadato: Identificador del tipo de metadato. Vinculado a la tabla GE_TIPOS_METADATOS_ENI;
N_ORDEN NUMBER(3,0),                       -- Orden: Indica el orden en que se debe presentar el metatado;
T_VALOR VARCHAR2(255 CHAR) NOT NULL,        -- Valor: Contiene el valor del metadato para el expediente;
L_ACTIVO NUMBER(1, 0) DEFAULT 1 NOT NULL   -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_METADATOS_EXPEDIENTES_H (
MET_EXP_ID NUMBER(19,0) NOT NULL, REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0), 
METEXP_EXP_ID NUMBER(19,0),         -- IdExpdte: Identificador único del expediente. Vinculado a la tabla GE_EXPEDIENTES;
METEXP_TMETENI_ID NUMBER(19,0),     -- CodMetadato: Identificador del tipo de metadato. Vinculado a la tabla GE_TIPOS_METADATOS_ENI;
N_ORDEN NUMBER(3,0),                -- Orden: Indica el orden en que se debe presentar el metatado;
T_VALOR VARCHAR2(255 CHAR),          -- Valor: Contiene el valor del metadato para el expediente;
L_ACTIVO NUMBER(1, 0)               -- Activo: Indicador Verdadero/Falso;
) TABLESPACE GESTOR_DAT;

-- ----------------------------------------------------------------

ALTER TABLE GE_PERMISOS  MODIFY (C_CODIGO VARCHAR2(50 CHAR) ); 
ALTER TABLE GE_PERMISOS_H  MODIFY (C_CODIGO VARCHAR2(50 CHAR) );

ALTER TABLE GE_PERMISOS  MODIFY (D_DESCRIPCION VARCHAR2(255 CHAR) ); 
ALTER TABLE GE_PERMISOS_H MODIFY (D_DESCRIPCION VARCHAR2(255 CHAR) );

-- ----------------------------------------------------------------

ALTER TABLE GE_DOCUMENTOS   MODIFY (T_IDEN_DOC VARCHAR2(255 CHAR));
ALTER TABLE GE_DOCUMENTOS_H MODIFY (T_IDEN_DOC VARCHAR2(255 CHAR));
ALTER TABLE GE_DOCUMENTOS   MODIFY (DOC_PLA_ID NOT NULL);
ALTER TABLE GE_DOCUMENTOS   MODIFY (D_DESCRIPCION NOT NULL);

ALTER TABLE GE_DOCUMENTOS_EXPEDIENTES MODIFY (DOCEXP_AGRDOC_ID NULL);

ALTER TABLE GE_USUARIOS  MODIFY (T_CONTRASENYA VARCHAR2(255 CHAR) ); 

-- -----------------------------------------------------------------

ALTER TABLE GE_TRAMITE_EXPED DROP COLUMN TRAM_EXP_TRAMEXPSUP_ID;
ALTER TABLE GE_TRAMITE_EXPED ADD (TRAM_EXP_TRAMEXPSUP_ID NUMBER(19,0));



SPOOL OFF;
