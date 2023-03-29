rem  Comprobación UTF8: Esto aparece en árabe: أنا تظهر في اللغة العربية

set define off
spool 02_LANZADOR_UNDO.log

CONNECT GESTOR/X_1_GESTOR@143.47.59.212:8521/MORAL

@@0.17.0/undo/lanzador_undo_v1.0.0.SQL
@@0.16.0/undo/lanzador_undo_v1.0.0.SQL
@@0.15.0/undo/lanzador_undo_v1.0.0.SQL
@@0.14.0/undo/lanzador_undo_v1.0.0.SQL
@@0.13.0/undo/lanzador_undo_v1.0.0.SQL
@@0.12.0/undo/lanzador_undo_v1.0.0.SQL
@@0.11.0/undo/lanzador_undo_v1.0.0.SQL
@@0.10.0/undo/lanzador_undo_v1.0.0.SQL
@@0.9.0/undo/lanzador_undo_v1.0.0.SQL
@@0.8.0/undo/lanzador_undo_v1.0.0.SQL
@@0.7.0/undo/lanzador_undo_v1.0.0.SQL
@@0.6.0/undo/lanzador_undo_v1.0.0.SQL
@@0.5.0/undo/lanzador_undo_v1.0.0.SQL
@@0.4.0/undo/lanzador_undo_v1.0.0.SQL
@@0.3.0/undo/lanzador_undo_v1.0.0.SQL
@@0.2.0/undo/lanzador_undo_v1.0.0.SQL
@@0.1.0/undo/lanzador_undo_v1.0.0.SQL


spool off;
