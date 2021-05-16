#!/bin/bash

set -e

. ./scripts/entrypoint-helpers.sh

##############################################################
# FUNCTIONS declarations
##############################################################

function Wait_For_Postgres() {
    echo "Checking if postgres is up..."

    DB_HOSTNAME=${DB_HOSTNAME}
    DB_PORT=${DB_PORT}
    DB_USERNAME=${DB_USERNAME}
    DB_PASSWORD=${DB_PASSWORD}
    DB_NAME=${DB_NAME}

    export PGHOST="${DB_HOSTNAME}"
    export PGPORT="${DB_PORT}"
    export PGUSER="${DB_USERNAME}"
    export PGPASSWORD="${DB_PASSWORD}"
    export PGDATABASE="${DB_NAME}"

    until psql -c '\l' > /dev/null 2>&1; do
        echo "Postgres' down. Waiting..."
        sleep 1
    done

    echo "Postgres' up."
}

##############################################################
# End of FUNCTIONS declarations
##############################################################

Ensure_Proper_Directory_Permissions

if [ "${1}" = "run_migrations" ]
then
    Echo_Message 'Running Database Schema Migrations'
    Run_Migrations
elif [ "${1}" = "serve" ]
then
#    Echo_Message 'Waiting For Postgres'
    Wait_For_Postgres
#
#    Echo_Message 'Installing Composer Dependencies'
#    Install_Dependencies
#
#    Echo_Message 'Running Database Schema Migrations'
#    Run_Migrations
#
#    Echo_Message 'Persisting Scheduler Tasks'
#    Persist_Scheduler_Tasks

    Echo_Message 'Launching PHP-FPM With Debug Support'
    Serve_With_Debug
elif [ "${1}" = "scheduler" ]
then
    Echo_Message 'Running Scheduler'
    Run_Scheduler
elif [ "${1}" = "consumers" ]
then
    Echo_Message 'Waiting For Postgres'
    Wait_For_Postgres

    Echo_Message 'Running Message Consumers with Automatic Reload on Code Change'

    Run_Consumers -d

    inotifywait -e close_write,moved_to,create -r -m ./src/Shell |
    while read -r directory events filename; do
      supervisorctl reload;
    done
else
    exec "$@"
fi
