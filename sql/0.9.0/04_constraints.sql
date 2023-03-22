rem Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/009_04_CONSTRAINTS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-------------------
-- VERSION 0.9.0 --
-------------------

ALTER TABLE GE_EXPEDIENTES ADD CONSTRAINT GE_EXPEDIENTES_RESP_FK   FOREIGN KEY (EXP_RESTRA_ID) REFERENCES GE_RESP_TRAMITACION;

ALTER TABLE GE_TRAMITE_EXPED ADD CONSTRAINT GE_TRAMITE_EXPED_RESP_FK   FOREIGN KEY (TRAM_EXP_RESTRA_ID) REFERENCES GE_RESP_TRAMITACION;

ALTER TABLE GE_MATERIAS_EXPDT ADD CONSTRAINT GE_MATEXP_EXP_FK FOREIGN KEY (MATEXP_EXP_ID) REFERENCES GE_EXPEDIENTES;
ALTER TABLE GE_MATERIAS_EXPDT ADD CONSTRAINT GE_MATEXP_MATTIPEXP_FK FOREIGN KEY (MATEXP_MATTIPEXP_ID) REFERENCES GE_MATERIAS_TIPEXPEDIENTES;
ALTER TABLE GE_MATERIAS_EXPDT_H ADD CONSTRAINT GE_MATEXP_H_REVAUD_FK FOREIGN KEY (REV) REFERENCES GE_REVAUD;

ALTER TABLE GE_TRAMITE_EXPED ADD CONSTRAINT GE_TRAM_EXP_USU_FIN_FK FOREIGN KEY (TRAM_EXP_USU_FIN_ID) REFERENCES GE_USUARIOS;



SPOOL OFF;