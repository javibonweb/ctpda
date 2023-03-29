rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/008_01_TABLAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

ALTER TABLE GE_PLAZOS_EXPDTE MODIFY (F_FECHA_LIMITE NOT NULL);

-- --------------------------------------------------------------------
-- Tablas para "Mi mesa"

CREATE TABLE GE_RESP_TRAMITACION (
	RESTRA_ID NUMBER(19,0) NOT NULL, 
	F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0),
	T_COD_RESP VARCHAR2(20 CHAR) NOT NULL,		-- Código que identifica el responsable de tramitación. No es el ID secuencial. El ID secuencial se tiene que crear como indice único de la tabla adicionalmente. Campo obligatorio.
	D_DESCRIPCION VARCHAR2(50 CHAR) NOT NULL,	-- Descripción del responsable de tramitación. Obligatorio. 50 caracteres.
	D_DESC_ABREV VARCHAR2(15 CHAR), 			-- Descripción abreviada del responsable de tramitación. No obligatorio.15 caracteres.
	RESTRA_ID_RESP_SUPERIOR NUMBER(19,0),	-- Contiene el ID del Responsable superior. Este campo permite crear una jerarquía de dependencia entre responsables de tramitación, para que un responsable pueda acceder a visualizar las tareas de sus subordinados. Campo no obligatorio. Es una clave externa sobre el ID_RESPONSABLE de esta misma tabla.
	L_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL	--Indicador Verdadero/Falso que indica si el registro está activo o no.
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_RESP_TRAMITACION_H (
	RESTRA_ID NUMBER(19,0) NOT NULL, 
	REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
	T_COD_RESP VARCHAR2(20 CHAR),
	D_DESCRIPCION VARCHAR2(50 CHAR),
	D_DESC_ABREV VARCHAR2(15 CHAR),
	RESTRA_ID_RESP_SUPERIOR NUMBER(19,0),
	L_ACTIVO NUMBER(1,0)
) TABLESPACE GESTOR_DAT;

-- ---------
CREATE TABLE GE_USUARIO_RESP (
	USRES_ID NUMBER(19,0) NOT NULL,
	F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0),
	USRRES_USU_ID NUMBER(19,0) NOT NULL,	-- Identificador del usuario en la tabla “GE_USUARIOS”. Obligatorio.
	USRRES_RESP_TR_ID NUMBER(19,0) NOT NULL,-- Identificador del responsable de tramitación en la tabla “GE_RESP_TRAMITACION”; Obligatorio.
	F_FECHA_INICIO DATE NOT NULL,			-- Fecha de inicio de la asignación. Obligatorio.
	F_FECHA_FIN    DATE NOT NULL,			-- Fecha de fin de la asignación. Obligatorio.
	L_POR_DEFECTO NUMBER(1,0) DEFAULT 1 NOT NULL,	-- Es un indicador de tipo “Verdadero/Falso” que indica para los usuarios con varios grupos de responsabilidad cual es el principal. Por defecto, Verdadero.
	L_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL			-- Indicador Verdadero/Falso que indica si el registro está activo o no.
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_USUARIO_RESP_H (
	USRES_ID NUMBER(19,0) NOT NULL,
	REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
	USRRES_USU_ID NUMBER(19,0),
	USRRES_RESP_TR_ID NUMBER(19,0),
	F_FECHA_INICIO DATE,
	F_FECHA_FIN    DATE,
	L_POR_DEFECTO NUMBER(1,0),
	L_ACTIVO NUMBER(1,0)			
) TABLESPACE GESTOR_DAT;

-- --------
CREATE TABLE GE_CFG_TAREAS (
	CFGTAR_ID NUMBER(19,0) NOT NULL,
	F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0),
	CFGTAR_VALDOM_TIP_EXP_ID NUMBER(19,0) NOT NULL, -- Tipo de expediente. Relacionado con Valor de Dominio de dominio “TIP_EXP”. Campo obligatorio.
	CFGTAR_TIP_TRA_ID NUMBER(19,0), 				-- Tipo de trámite. Relacionado con el campo “TIP_TRA_ID” de la tabla “GE_TIPO_TRAMITE”. Campo Obligatorio.
	CFGTAR_TIP_SUBTRA_ID NUMBER(19,0), 				-- Tipo de trámite. Relacionado con el campo “TIP_TRA_ID” de la tabla “GE_TIPO_TRAMITE”: Campo no obligatorio.
	CFGTAR_VALDOM_TIP_TAR_ID NUMBER(19,0) NOT NULL,	-- Tipo de tarea: Relacionado con Valor de Dominio de dominio “TAREAS”. Campo obligatorio.
	CFGTAR_VALDOM_TIP_TAR_ORIG_ID NUMBER(19,0),		-- Es un Id de Tarea, relacionado con Valor de Dominio “TAREAS”. Campo no obligatorio.
	CFGTAR_RESP_ID_DEFECTO NUMBER(19,0),	 		-- Responsable por defecto para la tarea a crear. Campo no obligatorio. Si está informado significa que la tarea se debe crear para ese responsable. Relacionado con el campo “ID_Responsable” de la tabla “GE_RESP_TRAMITACION”
	D_DESCRIPCION VARCHAR2(50 CHAR) NOT NULL,		-- Descripción de la tarea. Campo obligatorio. 50 caracteres.
	D_DESC_ABREV VARCHAR2(15 CHAR),					-- Descripción abreviada de la tarea. No obligatorio. 15 caractéres.
	L_DOCUMENTO NUMBER(1,0) DEFAULT 0 NOT NULL,		-- Indicador de tipo verdadero/falso que indica si la tarea se refiere a un documento o no. Por defecto, Falso.
	L_RESP_EXPED NUMBER(1,0) DEFAULT 0 NOT NULL,	-- Es un indicador de tipo “Verdadero/Falso” que indica si el responsable de tramitación a asignar es el responsable de tramitación del expediente. Por defecto viene a Falso.
	L_RESP_TRAM NUMBER(1,0) DEFAULT 0 NOT NULL,		-- Es un indicador de tipo “Verdadero/Falso” que indica si el responsable de tramitación a asignar es el responsable de tramitación del trámite. Por defecto viene a Falso.
	L_RESP_ANTERIOR NUMBER(1,0) DEFAULT 0 NOT NULL, -- Es un indicador de tipo “Verdadero/Falso” que indica si el responsable de la tarea a crear es el responsable de la tarea que invoca la función automática. Por defecto, Falso.
	N_PLAZO NUMBER(3,0) DEFAULT 0 NOT NULL, 		-- Es un valor numérico que indica el número de días de plazo para el cálculo de la fecha límite de realización de la tarea. Obligatorio. Número siempre positivo, incluyendo el 0.
	L_AUTO_ALTA NUMBER(1,0) DEFAULT 0 NOT NULL,		-- Es un indicador de tipo “Verdadero/Falso” que indica si la tarea se debe generar de forma automática en el momento de generarse un trámite o subtrámite. Por defecto viene a Falso.
	L_AUTO_ACEPTAR NUMBER(1,0) DEFAULT 0 NOT NULL,	-- Es un indicador de tipo “Verdadero/Falso” que indica si la tarea se debe generar de forma automática en el momento de pulsar el botón “Aceptar” en un trámite o subtrámite. Por defecto viene a Falso.
	L_AUTO_FINALIZAR NUMBER(1,0) DEFAULT 0 NOT NULL,-- Es un indicador de tipo “Verdadero/Falso” que indica si la tarea se debe generar de forma automática en el momento de pulsar el botón “Finalizar” en un trámite o subtrámite. Por defecto viene a Falso
	L_ACTIVO NUMBER(1,0) DEFAULT 1 NOT NULL
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_CFG_TAREAS_H (
	CFGTAR_ID NUMBER(19,0) NOT NULL,
	REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
	CFGTAR_VALDOM_TIPEXP_ID NUMBER(19,0),
	CFGTAR_TIP_TRA_ID NUMBER(19,0),
	CFGTAR_TIP_SUBTRA_ID NUMBER(19,0),
	CFGTAR_VALDOM_TIP_TAR_ID NUMBER(19,0),
	CFGTAR_VALDOM_TIP_TAR_ORIG_ID NUMBER(19,0),
	CFGTAR_RESP_ID_DEFECTO NUMBER(19,0),
	D_DESCRIPCION VARCHAR2(50 CHAR),
	D_DESC_ABREV VARCHAR2(15 CHAR),
	L_DOCUMENTO NUMBER(1,0),
	L_RESP_EXPED NUMBER(1,0),	
	L_RESP_TRAM NUMBER(1,0),	
	L_RESP_ANTERIOR NUMBER(1,0),
	N_PLAZO NUMBER(3,0), 		
	L_AUTO_ALTA NUMBER(1,0),	
	L_AUTO_ACEPTAR NUMBER(1,0),	
	L_AUTO_FINALIZAR NUMBER(1,0),
	L_ACTIVO NUMBER(1,0)
) TABLESPACE GESTOR_DAT;

-- --------
CREATE TABLE GE_TAREAS_EXPEDIENTE (
	TAREXP_ID NUMBER(19,0) NOT NULL,
	F_CREACION TIMESTAMP, F_MODIFICACION TIMESTAMP, USU_CREACION VARCHAR2(25 CHAR), USU_MODIFICACION VARCHAR2(25 CHAR), N_VERSION NUMBER(19,0),
	TAREXP_EXP_ID NUMBER(19,0) NOT NULL,	-- Identificador único del expediente al que se asocia la tarea. Obligatorio.
	TAREXP_TRA_ID NUMBER(19,0),				-- Identificador único del trámite al que se asocia la tarea. No obligatorio.
	TAREXP_DOCEXP_TRA_ID NUMBER(19,0),		-- Identificador único de la relación "documento-expediente-trámite", tabla GE_DOCUMENTOS_EXPED_TRAMITES al que se asocia la tarea. No obligatorio.
	TAREXP_VALDOM_TAR_ID NUMBER(19,0) NOT NULL, -- Identificador único del tipo de tarea. Relacionado con Valor de Dominio de dominio “TAREAS”. Campo obligatorio.
	D_DESCRIPCION VARCHAR2(50 CHAR) NOT NULL,	-- Descripción de la tarea. Obligatorio. Alfanumérico de 50 caracteres.
	D_DESC_ABREV VARCHAR2(15 CHAR),				-- Descripción abreviada de la tarea. No obligatorio. Alfanumérico de 15 caracteres.
	TAREXP_RESP_ID NUMBER(19,0) NOT NULL,		-- Identificador del responsable de tramitación asignado a la tarea. Obligatorio. Relacionado con el ID_RESP_TRAMITACION de la tabla “GE_RESP_TRAMITACION”.
	F_FECHA_INICIO DATE NOT NULL,				-- Fecha de inicio de la tarea. Obligatorio.
	F_FECHA_LIMITE DATE,						-- Fecha límite para cerrar o ejecutar la tarea. No obligatorio.
	F_FECHA_CIERRE DATE,						-- Fecha de cierre o ejecución de la tarea. No obligatorio.
	T_OBSERVACIONES VARCHAR2(500 CHAR),			-- Campo de texto libre para incluir observaciones. 500 caracteres. No obligatorio.
	T_SITUACION VARCHAR2(1 CHAR) DEFAULT 'P' NOT NULL,	-- Campo alfanumérico que indica la situación de la tarea: Valores posibles: “P” (Pendiente); “C” (Cerrada)
	TAREXP_USU_ID_CREA NUMBER(19,0) NOT NULL,	-- Usuario que ha creado la tarea. Relacionado con el Id de la tabla GE_USUARIOS. Es un campo distinto del campo de la auditoría, que debe existir igualmente. Obligatorio.
	TAREXP_USU_ID_CIERRA NUMBER(19,0),	-- Usuario que cierra la tarea. Relacionado con el Id de la tabla GE_USUARIOS. Es un campo distinto de los campos de usuarios de la auditoría, que deben existir igualmente. No obligatorio.
	T_TIPO_ALTA VARCHAR2(1 CHAR) DEFAULT 'M' NOT NULL,	-- Campo alfanumérico que indica como se ha generado la tarea. Valores posibles: “M” (Manual); “A” (Automática). Obligatorio.
	L_ACTIVA NUMBER(1,0) DEFAULT 1 NOT NULL
) TABLESPACE GESTOR_DAT;

CREATE TABLE GE_TAREAS_EXPEDIENTE_H (
	TAREXP_ID NUMBER(19,0) NOT NULL,
	REV NUMBER(19,0) NOT NULL, REVTYPE NUMBER(3,0), N_VERSION NUMBER(19,0),
	TAREXP_EXP_ID NUMBER(19,0),			-- Identificador único del expediente al que se asocia la tarea. Obligatorio.
	TAREXP_TRA_ID NUMBER(19,0),			-- Identificador único del trámite al que se asocia la tarea. No obligatorio.
	TAREXP_DOCEXP_TRA_ID NUMBER(19,0),	-- Identificador único de la relación "documento-expediente-trámite", tabla GE_DOCUMENTOS_EXPED_TRAMITES al que se asocia la tarea. No obligatorio.
	TAREXP_VALDOM_TAR_ID NUMBER(19,0), 	-- Identificador único del tipo de tarea. Relacionado con Valor de Dominio de dominio “TAREAS”. Campo obligatorio.
	D_DESCRIPCION VARCHAR2(50 CHAR),	-- Descripción de la tarea. Obligatorio. Alfanumérico de 50 caracteres.
	D_DESC_ABREV VARCHAR2(15 CHAR),		-- Descripción abreviada de la tarea. No obligatorio. Alfanumérico de 15 caracteres.
	TAREXP_RESP_ID NUMBER(19,0),		-- Identificador del responsable de tramitación asignado a la tarea. Obligatorio. Relacionado con el ID_RESP_TRAMITACION de la tabla “GE_RESP_TRAMITACION”.
	F_FECHA_INICIO DATE,				-- Fecha de inicio de la tarea. Obligatorio.
	F_FECHA_LIMITE DATE,				-- Fecha límite para cerrar o ejecutar la tarea. No obligatorio.
	F_FECHA_CIERRE DATE,				-- Fecha de cierre o ejecución de la tarea. No obligatorio.
	T_OBSERVACIONES VARCHAR2(500 CHAR),	-- Campo de texto libre para incluir observaciones. 500 caracteres. No obligatorio.
	T_SITUACION VARCHAR2(1 CHAR),		-- Campo alfanumérico que indica la situación de la tarea: Valores posibles: “P” (Pendiente); “C” (Cerrada)
	TAREXP_USU_ID_CREA NUMBER(19,0),	-- Usuario que ha creado la tarea. Relacionado con el Id de la tabla GE_USUARIOS. Es un campo distinto del campo de la auditoría, que debe existir igualmente. Obligatorio.
	TAREXP_USU_ID_CIERRA NUMBER(19,0),	-- Usuario que cierra la tarea. Relacionado con el Id de la tabla GE_USUARIOS. Es un campo distinto de los campos de usuarios de la auditoría, que deben existir igualmente. No obligatorio.
	T_TIPO_ALTA VARCHAR2(1 CHAR),		-- Campo alfanumérico que indica como se ha generado la tarea. Valores posibles: “M” (Manual); “A” (Automática). Obligatorio.
	L_ACTIVA NUMBER(1,0)
) TABLESPACE GESTOR_DAT;

-- Fin "mi mesa"
-- -----------------------------------------

ALTER TABLE GE_DOCUMENTOS_EXPED_TRAMITES   ADD (T_ORIGEN VARCHAR(1 CHAR)); -- NOT NULL);
ALTER TABLE GE_DOCUMENTOS_EXPED_TRAMITES_H ADD (T_ORIGEN VARCHAR(1 CHAR)); -- NOT NULL);

-- Nuevos campos para guardar ultima fecha y usuario de persistencia sobre los expedientes
ALTER TABLE GE_EXPEDIENTES ADD (F_ULTIMA_PERSISTENCIA TIMESTAMP);
ALTER TABLE GE_EXPEDIENTES ADD (USU_ULTIMA_PERSISTENCIA VARCHAR2(25 CHAR));
ALTER TABLE GE_EXPEDIENTES_H ADD (F_ULTIMA_PERSISTENCIA TIMESTAMP);
ALTER TABLE GE_EXPEDIENTES_H ADD (USU_ULTIMA_PERSISTENCIA VARCHAR2(25 CHAR));

SPOOL OFF;
