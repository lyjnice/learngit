#!/bin/bash
DOC_BASE=/App/opt/SRT/workselect-backend
LOG_PATH=/App/log/SRT/workselect/workselect-backend
source /etc/profile
java -classpath "$DOC_BASE:$DOC_BASE/WEB-INF/classes:$DOC_BASE/WEB-INF/lib/*" com.yanxiu.util.core.ExportWorks all >> ${LOG_PATH}/export.log 2>>${LOG_PATH}/export.err
# Minute   Hour   Day of Month       Month          Day of Week        Command
# (0-59)  (0-23)     (1-31)    (1-12 or Jan-Dec)  (0-6 or Sun-Sat)
#   @daily          /App/script/SRT/workselect/export_works_cron.sh
# 00  00  *  *  * su - root /App/script/SRT/crons/cron_export_works
