# Usa la imagen oficial de MySQL
FROM mysql:8.0

# Establece las variables de entorno necesarias para configurar la base de datos
ENV MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
ENV MYSQL_DATABASE=${MYSQL_DATABASE}
ENV MYSQL_USER=${MYSQL_USER}                 
ENV MYSQL_PASSWORD=${MYSQL_PASSWORD}          
# Copia un archivo .sql o .sh en caso de que quieras ejecutar algún script inicial
# COPY ./init.sql /docker-entrypoint-initdb.d/

# Expon el puerto 3306 para MySQL
EXPOSE 3306

# Instrucción CMD para correr el servidor MySQL
CMD ["mysqld"]