ARG PG_SERVER_VERSION=13

FROM postgres:${PG_SERVER_VERSION}
LABEL maintainer="postgres.ai"
LABEL name=hint_plan_postgres

ARG PG_SERVER_VERSION
ENV PG_SERVER_VERSION=${PG_SERVER_VERSION:-14}

ARG WALG_VERSION
ENV WALG_VERSION=${WALG_VERSION:-0.2.19}

RUN apt-get clean && rm -rf /var/lib/apt/lists/partial \
    && apt-get update -o Acquire::CompressionTypes::Order::=gz \
    && apt-get install --no-install-recommends -y wget make gcc unzip sudo git \
       curl libc6-dev apt-transport-https ca-certificates pgxnclient bc \
       build-essential libevent-dev libssl-dev krb5-multidev libkrb5-dev lsb-release apt-utils \
    && apt-get install --no-install-recommends -y \
       postgresql-server-dev-${PG_SERVER_VERSION} \
    && apt-get install --no-install-recommends -y postgresql-${PG_SERVER_VERSION}-repack \
    && apt-get install --no-install-recommends -y \
       postgresql-plpython3-${PG_SERVER_VERSION} \
     # hypopg
    && apt-get install --no-install-recommends -y \
         postgresql-${PG_SERVER_VERSION}-hypopg \
    && apt-get install --no-install-recommends -y \
         postgresql-${PG_SERVER_VERSION}-hypopg-dbgsym \
    # pgaudit extension
    && apt-get install --no-install-recommends -y postgresql-${PG_SERVER_VERSION}-pgaudit \
    && if [ $(echo "$PG_SERVER_VERSION < 14" | /usr/bin/bc) = "1" ]; then \
         # pg_hint_plan extension (dots are to be skipped here, e.g., "9.6" -> "96")
         export PG_PLAN_HINT_VERSION=$(echo $PG_SERVER_VERSION | sed 's/\.//') \
         && wget --quiet -O /tmp/pg_hint_plan.zip \
           https://github.com/ossc-db/pg_hint_plan/archive/PG${PG_PLAN_HINT_VERSION}.zip \
         && unzip /tmp/pg_hint_plan.zip -d /tmp \
           && cd /tmp/pg_hint_plan-PG${PG_PLAN_HINT_VERSION} \
           && make && make install; \
         fi \
    # extensions supported in PostgreSQL 12 and below
    # bc is used to manage PostgreSQL versions with dot like 9.6
    && if [ $(echo "$PG_SERVER_VERSION < 13" | /usr/bin/bc) = "1" ]; then \
         # powa extension
         apt-get install postgresql-${PG_SERVER_VERSION}-powa \
         # pg_auth_mon extension
         && git clone https://github.com/RafiaSabih/pg_auth_mon.git \
         && cd pg_auth_mon && USE_PGXS=1 make && USE_PGXS=1 make install; \
       fi \
    # timescaledb extension
    && if [ $(echo "$PG_SERVER_VERSION < 11" | /usr/bin/bc) = "1" ]; then \
         echo 'deb https://packagecloud.io/timescale/timescaledb/debian/' \
           $(env -i bash -c '. /etc/os-release; echo ${VERSION_CODENAME}') \
           'main' > /etc/apt/sources.list.d/timescaledb.list \
           && wget --quiet -O - https://packagecloud.io/timescale/timescaledb/gpgkey | sudo apt-key add - \
           && apt-get update \
           && apt-get install -y timescaledb-postgresql-${PG_SERVER_VERSION}; \
       elif [ $(echo "$PG_SERVER_VERSION < 14" | /usr/bin/bc) = "1" ]; then \
         echo "deb https://packagecloud.io/timescale/timescaledb/debian/ $(lsb_release -c -s) main" > /etc/apt/sources.list.d/timescaledb.list \
           && wget --quiet -O - https://packagecloud.io/timescale/timescaledb/gpgkey | sudo apt-key add - \
           && apt-get update \
           && apt install -y timescaledb-2-postgresql-${PG_SERVER_VERSION}; \
       fi \
   # citus extension; only versions Postgres 11+ are supported
   && if [ "${PG_SERVER_VERSION}" = "11" ]; then \
      curl -s https://install.citusdata.com/community/deb.sh | bash \
      && apt-get install -y postgresql-${PG_SERVER_VERSION}-citus-9.4 \
         postgresql-${PG_SERVER_VERSION}-hll=2.14.citus-1 \
         postgresql-${PG_SERVER_VERSION}-topn=2.3.0; \
      fi \
   && if [ $(echo "$PG_SERVER_VERSION > 11" | /usr/bin/bc) = "1" ]; then \
      curl -s https://install.citusdata.com/community/deb.sh | bash \
      && apt-get install -y postgresql-${PG_SERVER_VERSION}-citus-10.2 \
         postgresql-${PG_SERVER_VERSION}-hll=2.16.citus-1 \
         postgresql-${PG_SERVER_VERSION}-topn=2.4.0; \
      fi \
   # pg_timetable extension
   && wget https://github.com/cybertec-postgresql/pg_timetable/releases/download/v2.3.0/pg_timetable_2.3.0_Linux_x86_64.deb \
     && dpkg -i pg_timetable_2.3.0_Linux_x86_64.deb \
     && rm -rf pg_timetable_2.3.0_Linux_x86_64.deb \
   # pg_show_plans extension
   && git clone https://github.com/cybertec-postgresql/pg_show_plans.git \
     && cd pg_show_plans \
     && export USE_PGXS=1 && make && make install && cd .. && rm -rf pg_show_plans \
   # pg_cron extension
   && apt-get install -y postgresql-${PG_SERVER_VERSION}-cron \
   # postgresql_anonymizer extension
   && pgxn install ddlx && pgxn install postgresql_anonymizer \
   # pg_stat_kcache extension
   && apt-get install postgresql-${PG_SERVER_VERSION}-pg-stat-kcache \
   # pg_wait_sampling extension
   && apt-get install postgresql-${PG_SERVER_VERSION}-pg-wait-sampling \
   # add pg_qualstats extension
   && apt-get install postgresql-${PG_SERVER_VERSION}-pg-qualstats \
   && if [ $(echo "$PG_SERVER_VERSION < 12" | /usr/bin/bc) = "1" ]; then \
   # bg_mon extension
     apt-get install -yq --no-install-suggests --no-install-recommends brotli \
     && git clone https://github.com/CyberDem0n/bg_mon.git && cd bg_mon \
     && USE_PGXS=1 make && USE_PGXS=1 make install && cd .. ; \
     fi \
   # pgextwlist extension
   && apt-get install postgresql-${PG_SERVER_VERSION}-pgextwlist \
   # set_user extension
   && git clone https://github.com/pgaudit/set_user.git \
   && cd set_user && git checkout REL3_0_0 && make USE_PGXS=1 && make USE_PGXS=1 install \
  # errorlogs extension
  && if [ $(echo "$PG_SERVER_VERSION > 9.6" | /usr/bin/bc) = "1" ]; then \
    cd /tmp && wget https://github.com/munakoiso/logerrors/archive/v2.0.tar.gz \
    && tar -xf v2.0.tar.gz && rm v2.0.tar.gz && cd logerrors-2.0 \
    && USE_PGXS=1 make && USE_PGXS=1 make install; \
  fi \
 # WAL-G
   && wget --quiet -O /tmp/wal-g.linux-amd64.tar.gz "https://github.com/wal-g/wal-g/releases/download/v${WALG_VERSION}/wal-g.linux-amd64.tar.gz" \
   && tar -zxvf /tmp/wal-g.linux-amd64.tar.gz && mv wal-g /usr/local/bin/ \
   # remove all auxilary packages to reduce final image size
   && cd / && rm -rf /tmp/* && apt-get purge -y --auto-remove gcc \
      make wget unzip curl libc6-dev apt-transport-https git \
      postgresql-server-dev-${PG_SERVER_VERSION} pgxnclient build-essential \
      libssl-dev krb5-multidev comerr-dev krb5-multidev libkrb5-dev apt-utils lsb-release \
      libgssrpc4 \
     && apt-get clean -y autoclean \
     && rm -rf /var/lib/apt/lists/* \
   # remove standard pgdata
   && rm -rf /var/lib/postgresql/${PG_SERVER_VERSION}/

EXPOSE 5432

# Prepare Postgres start script
RUN echo "#!/bin/bash" > /pg_start.sh && chmod a+x /pg_start.sh \
    && printf "initdb --username=postgres --pwfile=\"/pass\"\n" \
      >> /pg_start.sh \
    && printf "echo \"shared_preload_libraries = 'pg_hint_plan'\" >> /var/lib/postgresql/data/postgresql.conf\n" \
      >> /pg_start.sh \
    && printf "echo \"host all all all md5\" >> /var/lib/postgresql/data/pg_hba.conf\n" \
      >> /pg_start.sh \
    && printf "chown -R postgres:postgres \${PGDATA} /var/run/postgresql\n" \
      >> /pg_start.sh \
    && printf "sudo -Eu postgres /usr/lib/postgresql/${PG_SERVER_VERSION}/bin/postgres -D \${PGDATA} >& /proc/1/fd/1 &\n" \
      >> /pg_start.sh \
    && printf "while ! netcat -z localhost 5432; do sleep 1; done; \n" \
      >> /pg_start.sh \
    && printf "psql -c 'create database '\"\$POSTGRES_DB\"\n" \
      >> /pg_start.sh \
    && printf "psql -c \"create user \$POSTGRES_USER with password '\$POSTGRES_PASSWORD';\"\n" \
      >> /pg_start.sh \
    && printf "/bin/bash -c \"trap : TERM INT; sleep infinity & wait\"" \
      >> /pg_start.sh


RUN touch /pass
RUN echo "admin" >> /pass

RUN adduser postgres sudo

RUN sudo apt update
RUN sudo apt -y install netcat

USER postgres

CMD ["/pg_start.sh"]
