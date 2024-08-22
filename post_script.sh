MYSQL_NODE=mysql_board

# Execute MySQL commands
EXEC_MYSQL="mysql -uroot -proot -N -e "

docker exec ${MYSQL_NODE} ${EXEC_MYSQL} "DROP USER IF EXISTS 'exporter'@'localhost'"
docker exec ${MYSQL_NODE} ${EXEC_MYSQL} "CREATE USER 'exporter'@'localhost' IDENTIFIED BY 'exporter123' WITH MAX_USER_CONNECTIONS 3" 2>&1 | grep -v "Using a password"
docker exec ${MYSQL_NODE} ${EXEC_MYSQL} "GRANT PROCESS, REPLICATION CLIENT, SELECT ON *.* TO 'exporter'@'localhost'" 2>&1 | grep -v "Using a password"

# Start the exporters
docker exec ${MYSQL_NODE} sh /opt/exporters/node_exporter/start_node_exporter.sh
docker exec ${MYSQL_NODE} sh /opt/exporters/mysqld_exporter/start_mysqld_exporter.sh
