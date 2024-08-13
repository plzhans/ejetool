#!/bin/sh -e

# loading function
run_native_log(){
    local type="$1"; shift
    local text="$*";
    local dt; dt="$(date -Iseconds)"
    printf '%s [%s] [Redis]: %s\n' "$dt" "$type" "$text"
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

#
redis_command="redis-server"

#
run_log "---------- redis server option ----------"
if [ "$REDIS_PASS_FILE" != "" ]; then
    if [ ! -f "$REDIS_PASS_FILE" ]; then
        run_error "notfound pass file. path=$REDIS_PASS_FILE"
        exit 1
    fi
    
    xRedisPass=`cat $REDIS_PASS_FILE`
    if [ "$xRedisPass" = "" ]; then
        run_error "pass empty. path=$REDIS_PASS_FILE"
        exit 2
    fi

    #
    redis_command="$redis_command --requirepass $xRedisPass"
    run_log "--requirepass actived."
elif [ "$REDIS_PASS" != "" ]; then
    #
    redis_command="$redis_command --requirepass $REDIS_PASS"
    run_log "--requirepass actived."
fi

# run dotnet app
run_log "---------- redis server start ----------"
echo $redis_command
$redis_command