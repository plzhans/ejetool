#!/bin/bash

# loading function
run_native_log(){
    local type="$1"; shift
    local text="$*";
    local dt; dt="$(date --rfc-3339=seconds)"
    printf '%s [%s] [DockerCompose]: %s\n' "$dt" "$type" "$text"
}
run_log(){
    run_native_log Info "$@"
}
run_warn(){
    run_native_log Warn "$@" >&2
}
run_error(){
    run_native_log ERROR "$@" >&2
    exit 1
}

if [ "$USER_ID" = "" ]; then
    export USER_ID="$(id -u)"
    run_log "export USER_ID=$USER_ID"
fi

if [ "$GROUP_ID" = "" ]; then
    export GROUP_ID="$(id -g)"
    run_log "export GROUP_ID=$GROUP_ID"
fi

# environment
run_log "---------- environment ----------"
run_log " --> USER_ID=$USER_ID"
run_log " --> GROUP_ID=$GROUP_ID"

# fake
if [ ! -f "./secrets.fake" ]; then
    run_log "---------- secrets file create ----------"
    echo -n $(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 20 | sed 1q) > ./secrets.fake
fi

#
run_log "---------- docker-compose build----------"
docker-compose build

#
run_log "---------- docker-compose up ----------"
docker-compose up