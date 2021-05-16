#!/bin/bash

##############################################################
# FUNCTIONS declarations
##############################################################

abort()
{
    echo $1 >&2
    exit $2
}

function Echo_Message()
{
    printf '%*s\n' "80" '' | tr ' ' -
    echo $1
    printf '%*s\n' "80" '' | tr ' ' -
}

function Set_Permissions()
{
    # 82 is the standard uid/gid for "www-data" in Alpine

    HTTPD_USER=82
    setfacl -R -m u:${HTTPD_USER}:rwx ./tmp
    setfacl -R -d -m u:${HTTPD_USER}:rwx ./tmp
}

function Build_ORM_Cache()
{
    ./bin/cake orm_cache clear \
    && ./bin/cake orm_cache build
}

function Ensure_Proper_Directory_Permissions()
{
    DIR="./logs"
    PERMISSIONS="rwx"
    if output=$(find ${DIR} -maxdepth 0 -type d -perm -o=${PERMISSIONS}) &&  [ -z "$output" ]; then
        abort "Directory ${DIR} must have ${PERMISSIONS} permissions for world!. Fix: chmod o+${PERMISSIONS} ${DIR}" 100
    fi

    declare -a DIRS=("./files" "./consumers_logs")
    PERMISSIONS="rw"

    for DIR in "${DIRS[@]}"
    do
        :
        if output=$(find ${DIR} -maxdepth 0 -type d -perm -o=${PERMISSIONS}) &&  [ -z "$output" ]; then
            abort "Directory ${DIR} must have ${PERMISSIONS} permissions for world!. Fix: chmod o+${PERMISSIONS} ${DIR}" 100
        fi
    done
}

function Install_Dependencies()
{
    composer install --prefer-dist --optimize-autoloader --no-progress --no-suggest --no-interaction
    composer dump-autoload
}

function Run_Migrations()
{
    ./bin/cake migrations migrate
}

function Run_Scheduler()
{
    ./bin/cake Scheduler.Scheduler >> ./logs/scheduler.log 2>&1
}

function Persist_Scheduler_Tasks()
{
    ./bin/cake Scheduler.PersistentTasks
}

function Generate_Api_Docs()
{
    ./bin/cake maintenance generate_docs
}

function Run_Tests()
{
    ./bin/cake migrations migrate --connection test -p Scheduler \
    && ./bin/cake migrations migrate --connection test -p Queue \
    && ./bin/cake migrations migrate --connection test

    COMPOSER_PROCESS_TIMEOUT=900 composer test
}

function Run_Cron()
{
    mkdir -p /var/log/cron
    crond -b -L /var/log/cron/cron.log
}

function Serve_In_Production()
{
    php-fpm
}

function Serve_With_Debug()
{
    XDEBUG_HOST=${XDEBUG_HOST:-172.17.0.1}
    XDEBUG_PORT=${XDEBUG_PORT:-9001}

    export PHP_IDE_CONFIG="serverName=demoApp2"
    export XDEBUG_CONFIG="${XDEBUG_CONFIG_BASE} remote_host=${XDEBUG_HOST} remote_port=${XDEBUG_PORT}"

    php-fpm
}

function Run_Consumers()
{
    if [ -z "$1" ]; then
        supervisord -c ./supervisor.conf
    else
        supervisord -c ./supervisor.conf $1
    fi
}

##############################################################
# End of FUNCTIONS declarations
##############################################################
