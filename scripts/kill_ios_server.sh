#!/bin/bash 

kill -9 $(lsof -ti tcp:4923) 2> /dev/null