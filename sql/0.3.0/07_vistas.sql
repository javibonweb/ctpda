rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/003_07_VISTAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

-- vista de expedientes
CREATE VIEW GE_EXP_PEREXP_SUJEXP
AS
SELECT 
expe.EXP_ID AS "EXP_ID",
expe.F_CREACION AS "F_CREACION",
expe.F_MODIFICACION AS "F_MODIFICACION",
expe.USU_CREACION AS "USU_CREACION",
expe.USU_MODIFICACION AS "USU_MODIFICACION",
expe.F_FECHA_REGISTRO AS "F_FECHA_REGISTRO",
valortipoexp.T_ABREVIATURA AS "T_TIP_EXP",
expe.T_NUMERO_EXPEDIENTE AS "T_NUM_EXPEDIENTE",
valorsituacion.D_DESCRIPCION AS "T_SITUACION",
expe.F_FECHA_ENTRADA AS "T_FECHA_ENTRADA",
CONCAT(CONCAT(CONCAT(CONCAT(persona.T_NOMBRE_RAZONSOCIAL, ' '),persona.T_PRIMER_APELLIDO), ' '), persona.T_SEGUNDO_APELLIDO)AS "T_PERSONA",
sujetoobligado.D_DESCRIPCION AS "T_SUJETO_OBLIGADO"
FROM GE_EXPEDIENTES expe
INNER JOIN GE_VALORES_DOMINIOS valortipoexp ON valortipoexp.VAL_DOM_ID = expe.EXP_VALDOM_TIPEXP_ID 
INNER JOIN GE_VALORES_DOMINIOS valorsituacion ON valorsituacion.VAL_DOM_ID = expe.EXP_VALDOM_SIT_ID 
INNER JOIN GE_PERSONAS_EXPEDIENTES perexpe ON  perexpe.PER_EXP_ID=expe.EXP_ID
INNER JOIN GE_SUJETOS_OBLIGADOS_EXPDT sujetoexpe ON sujetoexpe.SUJ_EXP_ID=expe.EXP_ID
INNER JOIN GE_PERSONAS persona ON persona.PER_ID =perexpe.PER_PER_ID 
INNER JOIN GE_SUJETOS_OBLIGADOS sujetoobligado ON sujetoobligado.SUJ_ID = sujetoexpe.SUJ_SUJ_ID 
WHERE perexpe.N_PRINCIPAL =1 AND sujetoexpe.N_PRINCIPAL =1
;
GRANT SELECT ON GE_EXP_PEREXP_SUJEXP TO GE_ROL_GESTOR_DML;

-- vista de expedientes y materias
CREATE OR REPLACE VIEW GE_LISTADO_MAT_TIPEXP AS
  SELECT 
mat.val_dom_id AS "MAT_TIPEXP_ID",
mat.f_creacion AS "F_CREACION",
mat.f_modificacion AS "F_MODIFICACION",
mat.usu_creacion AS "USU_CREACION",
mat.usu_modificacion AS "USU_MODIFICACION",
mat.C_CODIGO AS "C_CODIGO",
mat.D_DESCRIPCION AS "D_DESCRIPCION",
mat.L_BLOQUEADO AS "L_BLOQUEADO",
mat.L_ACTIVO AS "L_ACTIVO",
mat.N_NIVEL_ANIDAMIENTO AS "N_NIVEL_ANIDAMIENTO",
mat.n_orden AS "N_ORDEN",
mat.valdom_valdom_id AS "VALDOM_VALDOM_ID",
mat.t_abreviatura,
LISTAGG(tipexp.d_descripcion|| '; ') WITHIN GROUP (ORDER BY tipexp.d_descripcion) AS "TIP_EXPE",
LISTAGG('-' ||tipexp.val_dom_id||'-') WITHIN GROUP (ORDER BY tipexp.val_dom_id) AS "ID_EXPE"
FROM ge_valores_dominios mat
LEFT OUTER JOIN GE_MATERIAS_TIPEXPEDIENTES mattipexp ON mat.VAL_DOM_ID = mattipexp.MATTIPEXP_VALDOMMAT_ID
LEFT OUTER JOIN GE_VALORES_DOMINIOS tipexp ON tipexp.VAL_DOM_ID = mattipexp.MATTIPEXP_VALDOMTIPEXP_ID
WHERE mat.valdom_dom_id = (select ge_dominios.dom_id from GE_DOMINIOS where C_CODIGO='MAT')
group by (mat.val_dom_id,
mat.f_creacion,
mat.f_modificacion,
mat.usu_creacion,
mat.usu_modificacion,mat.C_CODIGO,
mat.D_DESCRIPCION,
mat.L_BLOQUEADO,
mat.L_ACTIVO,
mat.N_NIVEL_ANIDAMIENTO,
mat.n_orden, mat.valdom_valdom_id,mat.t_abreviatura);
GRANT SELECT ON GE_LISTADO_MAT_TIPEXP TO GE_ROL_GESTOR_DML;

SPOOL OFF;
