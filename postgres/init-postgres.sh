#!/bin/bash

# init postgres sandbox database

set - e

sandbox_db=query_sandbox

psql -v ON_ERROR_STOP=1 --username postgres <<-EOF
    CREATE DATABASE $sandbox_db;
EOF
