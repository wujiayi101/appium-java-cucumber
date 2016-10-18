#!/bin/bash 

kill -9 $(lsof -ti tcp:4823) 2> /dev/null