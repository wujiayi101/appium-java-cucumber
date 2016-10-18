#!/bin/bash

appium --port 4923 \
       --session-override \
       --log-level info \
       --log-timestamp \
       --local-timezone \
       --log-no-colors \
       --command-timeout 130 \
       --launch-timeout 130000 \
       --show-sim-log \
       --log /tmp/appium.ios.log \
       --language zh-Hans
