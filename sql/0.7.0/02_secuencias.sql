rem Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/007_02_SECUENCIAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.7.0 --
-------------------
CREATE SEQUENCE S_CFG_METADATOS_TRAM START WITH 1 INCREMENT BY  1;
CREATE SEQUENCE S_DETALLE_EXPDTE_TRAM START WITH 1 INCREMENT BY  1;
----------------------------------------------------------------------

CREATE SEQUENCE S_CFG_PLAZOS_ESTILOS START WITH 1 INCREMENT BY  1;
CREATE SEQUENCE S_CFG_PLAZOS_EXPDTE START WITH 1 INCREMENT BY  1;
CREATE SEQUENCE S_PLAZOS_EXPDTE START WITH 1 INCREMENT BY  1;
----------------------------------------------------------------------

CREATE SEQUENCE S_CFG_PLAZOS_AUT START WITH 1 INCREMENT BY  1;
----------------------------------------------------------------------

CREATE SEQUENCE S_DOCUMENTOS_EXPED_TRAMITES START WITH 1 INCREMENT BY  1;


SPOOL OFF;
