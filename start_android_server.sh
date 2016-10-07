#!/bin/bash

appium --port 4823 \
       --session-override \
       --log-level info \
       --log-timestamp \
       --local-timezone \
       --log-no-colors \
       --launch-timeout 20000 \
       --log /tmp/appium.android.log