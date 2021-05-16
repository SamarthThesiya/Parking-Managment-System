###############################################################
# Supervisor
###############################################################

#FROM golang:1.10-alpine AS supervisor
#
#RUN apk update && \
#    apk add git

# uncomments this if golang.org is not accessible
#RUN mkdir -p $GOPATH/src/golang.org/x && \
#    cd $GOPATH/src/golang.org/x && \
#   git clone https://github.com/golang/crypto && \
#    git clone https://github.com/golang/sys

#RUN go get -v -u github.com/ochinchina/supervisord && \
#    go build -o /usr/local/bin/supervisord github.com/ochinchina/supervisord

###############################################################
# Backend
###############################################################

FROM php:7.1-fpm-alpine3.7

# Setup environment

ENV PHP_REDIS_VERSION 3.1.4
ENV PHP_XDEBUG_VERSION 2.9.5

ENV EXTENSIONS_DEPS \
    postgresql-dev \
    sqlite-dev \
    libressl-dev \
    curl-dev \
    icu-dev \
    zlib-dev \
    freetype-dev \
    libjpeg-turbo-dev \
    libpng-dev \
    libxml2-dev \
    imagemagick-dev \
    libtool \
    gmp-dev

ENV BUILD_DEPS="${PHPIZE_DEPS} ${EXTENSIONS_DEPS}"

ENV PERSISTENT_DEPS \
    bash \
    git \
    acl \
    libpq \
    icu-libs \
    freetype \
    libpng \
    libjpeg-turbo \
    imagemagick \
    postgresql-client \
    gmp \
    patch

ENV CORE_EXTENSIONS \
    gd \
    mbstring \
    intl \
    pdo_sqlite \
    pdo_pgsql \
    zip \
    curl \
    json \
    opcache \
    pcntl \
    bcmath \
    sockets \
    gmp

ENV PECL_EXTENSIONS_TO_BE_INSTALLED \
    imagick \
    redis-${PHP_REDIS_VERSION} \
    xdebug-${PHP_XDEBUG_VERSION}

ENV PECL_EXTENSIONS_TO_BE_ENABLED \
    imagick \
    redis \
    xdebug

# Install required dependencies and PHP extensions

RUN \
    set -xe \
    && apk add --no-cache --update --virtual .build-deps ${BUILD_DEPS} \
    && docker-php-ext-configure intl --enable-intl \
    && docker-php-ext-configure pdo_pgsql --with-pgsql \
    && docker-php-ext-configure mbstring --enable-mbstring \
    && docker-php-ext-configure gd \
        --with-gd \
        --with-png-dir=/usr/include/ \
        --with-freetype-dir=/usr/include/ \
        --with-jpeg-dir=/usr/include/ \
    && docker-php-ext-install ${CORE_EXTENSIONS} \
    && pecl install ${PECL_EXTENSIONS_TO_BE_INSTALLED} \
    && docker-php-ext-enable ${PECL_EXTENSIONS_TO_BE_ENABLED} \
    && apk del .build-deps \
    && apk add --no-cache --virtual .persistent-php-deps ${PERSISTENT_DEPS} \
    && rm -rf /tmp/pear \
    && rm -rf /var/cache/apk \
    && rm -rf /var/lib/apk \
    && rm -rf /etc/apk/cache

# Install supervisor

COPY --from=ochinchina/supervisord:latest /usr/local/bin/supervisord /usr/bin/supervisord

# Install composer

COPY --from=composer:1.9.3 /usr/bin/composer /usr/bin/composer

# Define default envrionment and working directories

ENV ENVIRONMENT=development
ENV DOCKYARD_SRC=.
ENV DOCKYARD_SRVPROJ=/srv/parking-management
ENV XDEBUG_CONFIG_BASE="remote_enable=1 idekey=PHPSTORM zend_extension=xdebug.so"

WORKDIR $DOCKYARD_SRVPROJ

# Install project dependencies using composer

COPY $DOCKYARD_SRC/composer.json \
     $DOCKYARD_SRC/composer.lock \
     ./

#COPY $DOCKYARD_SRC/patches ./patches

# https://getcomposer.org/doc/03-cli.md#composer-allow-superuser
ENV COMPOSER_ALLOW_SUPERUSER 1

RUN composer global require "hirak/prestissimo:^0.3" \
        --prefer-dist --classmap-authoritative --no-progress --no-suggest \
    && composer install \
        --prefer-dist --optimize-autoloader --no-progress --no-suggest --no-interaction \
    && composer clearcache

# Supply php and php-fpm with custom configs from the repo

COPY php_config/php.ini /usr/local/etc/php/
COPY php_config/php7.ini /usr/local/etc/php/conf.d/
COPY php_config/fpm/php-fpm.conf /usr/local/etc/
COPY php_config/fpm/pool.d /usr/local/etc/pool.d

# Prepare working directory

COPY $DOCKYARD_SRC/. ./
COPY $DOCKYARD_SRC/config/app.default.php ./config/app.php

RUN mkdir -p \
    ./logs \
    ./files \
    ./consumers_logs \
    ./tmp/cache/persistent \
    ./tmp/cache/models

RUN chown -R www-data ./tmp \
    && chmod +x ./bin/cake

# Add crontab entry for scheduler

RUN mkdir -p /var/log/cron \
    && echo "* * * * * cd $DOCKYARD_SRVPROJ && ./docker-entrypoint.sh scheduler" >> /etc/crontabs/root

#Setup SSH
RUN apk --update add --no-cache openssh bash \
  && sed -i s/#PermitRootLogin.*/PermitRootLogin\ yes/ /etc/ssh/sshd_config \
  && sed -i s/#PasswordAuthentication.*/PasswordAuthentication\ no/ /etc/ssh/sshd_config \
  && rm -rf /var/cache/apk/*
RUN /usr/bin/ssh-keygen -A

#COPY authorized_keys /root/.ssh/authorized_keys

# Expose php-fpm port

EXPOSE 9000

# Expose ssh port

EXPOSE 22
# Setup entry point

RUN chmod +x ./docker-entrypoint.sh
ENTRYPOINT ["./docker-entrypoint.sh"]
