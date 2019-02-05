This folder contains the default view configuration json files as deployed with this Release Version of TB Reach as well as the Shell Script to populate the db with them.

The script works with both postgres and couchdb.

For couchDb
Make sure you have set up couch database on this machine and that couch is accessible via port 5894 (You can modify the script if you use a different port)
Run the command below:

./setup_view_configs.sh -d <database name>        (e.g.   ./setup_view_configs.sh -d opensrp_tbreach)

For Postgres 

You must specify the parameter -t with value postgres. The default is couchdb
Make sure you have set up postgres database on this machine and that postgres is accessible via port 5432 (You can modify the script if you use a different port)
Run the command below:

./setup_view_configs.sh -t postgres  -u <postgres_user> -pwd <postgres _user_password> -d opensrp -d <database name>        (e.g.   ./setup_view_configs.sh -t postgres  -u opensrp_admin -pwd admin -d opensrp_tbreach)

Run ./setup_view_configs.sh --help to get a list of available options

  Specify (Optional) database type like ./setup_view_configs.sh -t postgres 

  Specify (Optional) a database name like ./setup_view_configs.sh -d <database name>

  Specify (Optional) a host like  ./setup_view_configs.sh -h <hostname>

  Specify (Optional) a port name like  ./setup_view_configs.sh -p <port>

  Specify (Optional) a source folder like  ./setup_view_configs.sh -f <folder path>

  Specify (Optional) a username like  ./setup_view_configs.sh -u <username>

  Specify (Optional) a password like  ./setup_view_configs.sh -pwd <password>

  Specify (Optional) a protocol e.g. http or https like  ./setup_view_configs.sh -proto <protocol>

  Specify (Optional) any multiple arguments like  ./setup_view_configs.sh -u <username> -pwd <password> -d <database name>

For Authentication you can choose to pass the username and password as part of the host e.g.  ./setup_view_configs.sh -h http://<username>:<password>@<hostname>

Happy Coding :)
   
