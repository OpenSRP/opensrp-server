FROM tomcat:9.0 AS build

# Copy OpenSRP Source
COPY . /tmp/opensrp-server

# Build WAR file
ARG opensrp_maven_package_profiles="postgres,jedis,oauth2"
RUN apt-get update \
  && apt-get install -y maven

RUN mvn clean package -Dmaven.test.skip=true -P $opensrp_maven_package_profiles -f /tmp/opensrp-server/pom.xml


# Explode WAR file ()
RUN jar -uvf /tmp/opensrp-server/opensrp-web/target/opensrp.war /tmp/opensrp-server-exploded

FROM tomcat:9.0
# Copy the exploded directory
COPY --from=build /tmp/opensrp-server-exploded /app

# copy the migration files
COPY --from=build /tmp/opensrp-server/assets/migrations/* /migrate

# Download mybatis
RUN mkdir -p /opt/mybatis
RUN wget --quiet --no-cookies https://github.com/mybatis/migrations/releases/download/mybatis-migrations-3.3.4/mybatis-migrations-3.3.4-bundle.zip -O /opt/mybatis/mybatis-migrations-3.3.4.zip
RUN unzip /opt/mybatis/mybatis-migrations-3.3.4.zip -d /opt/mybatis/
RUN rm -f /opt/mybatis/mybatis-migrations-3.3.4.zip
RUN chmod +x /opt/mybatis/mybatis-migrations-3.3.4/bin/migrate

# Run migrations (mybatis)
# RUN /opt/mybatis/mybatis-migrations-3.3.4/bin/migrate up --path=/migrations --env=deployment

EXPOSE 8080 8081
# Start app
CMD /usr/local/tomcat/bin/catalina.sh run

