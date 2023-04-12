rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

<<<<<<< HEAD
SPOOL logs/FORMACION.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;


-----------------------
-- VERSION FORMACION --
-----------------------

DROP TABLE FOR_PRUEBAS_ANTONIOF CASCADE CONSTRAINTS;
DROP TABLE FOR_PRUEBAS_ANTONIOF_H CASCADE CONSTRAINTS;

DROP SEQUENCE S_FOR_PRUEBAS_ANTONIOF;
=======
SPOOL logs/FORMACION_UNDO.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-----------------------
-- VERSION FORMACION --
-----------------------

DROP TABLE FOR_PRUEBAS CASCADE CONSTRAINTS;
DROP TABLE FOR_PRUEBAS_H CASCADE CONSTRAINTS;

DROP SEQUENCE S_FOR_PRUEBAS;
>>>>>>> branch 'beatriz.lamorena' of https://github.com/beatriz-lamorena/ctpda.git

SPOOL OFF;
