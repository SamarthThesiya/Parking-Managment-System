#!/bin/bash

set -e

. ./scripts/entrypoint-helpers.sh

Echo_Message 'Running ssh'
/usr/sbin/sshd

if [ "${1}" = "test" ]
then
    Echo_Message 'Running Tests'
    Run_Tests
elif [ "${1}" = "run_migrations" ]
then
    Echo_Message 'Running Database Schema Migrations'
    Run_Migrations
elif [ "${1}" = "post_deploy" ]
then
    Echo_Message 'Running Post-Deploy Tasks'
    Persist_Scheduler_Tasks
elif [ "${1}" = "web" ]
then
    Echo_Message 'Building ORM Cache'
    Build_ORM_Cache
    Echo_Message 'Running Database Schema Migrations'
    Run_Migrations
    Echo_Message 'Launching PHP-FPM With Debug Support'
    Serve_With_Debug
elif [ "${1}" = "message_consumers" ]
then
    Echo_Message 'Starting message consumers'
    Run_Consumers
elif [ "${1}" = "scheduler" ]
then
    Run_Scheduler
else
    exec "$@"
fi
