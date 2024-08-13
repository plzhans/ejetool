FROM mysql:8.0.37

#
ENV TZ=Asia/Seoul

# ADD mysql cnf
COPY ./config/my.utf8mb4.cnf /etc/mysql/conf.d/
RUN chmod 644 /etc/mysql/conf.d/my.utf8mb4.cnf

COPY ./config/my.custom.cnf /etc/mysql/conf.d/
RUN chmod 644 /etc/mysql/conf.d/my.custom.cnf
