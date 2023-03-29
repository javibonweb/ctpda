rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

SPOOL logs/004_UNDO_07_VISTAS.LOG
ALTER SESSION SET "_ORACLE_SCRIPT"= TRUE;

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

SPOOL OFF;