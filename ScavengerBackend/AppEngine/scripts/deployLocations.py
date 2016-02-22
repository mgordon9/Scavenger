#!/usr/bin/env python

# This Script generated Objectives with coordinates in Santa Barbara 
# or the United States and stores them into the data store.
# ------------------------------------------------------------------

serveraddress = 'http://localhost:8080/rest/ds';
#serveraddress = 'http://cs263-ae-demo-119421.appspot.com/rest/ds'
#serveraddress = 'http://scavenger-game.appspot.com/rest/ds';

activities = ['drink', 'fuel', 'grocery', 'photo', 'player', 'ride', 'takeoutdog', 'tutoring']
offset = 0;

import os
import random
import itertools

counter=10
for _ in itertools.repeat(None, 10):
#   USA:
    latitude = random.uniform(31.253187, 48.474549)
    longitude = random.uniform(-82.759959, -122.233164)

#   Goleta:
#    latitude = random.uniform(34.446092, 34.425534)
#    longitude = random.uniform(-119.849631, -119.770910)

    commandstring = ('curl -H "Accept: application/json" -X POST --data "keyname=location'
                     + str(counter + offset)
                     + '&title=' + str('<title>')
                     + '&info=' + str('<info>')
                     + '&latitude=' + str(latitude)
                     + '&longitude=' + str(longitude)
                     + '&owner=' + str('<owner>')
                     + '&otherConfirmedUsers=' + str('<otherConfirmedUsers>')
                     + '&activity=' + str(random.choice(activities))
                     + '" '
                     + serveraddress
                     + '') 
    print commandstring
    print "------------------------------"
    os.system(commandstring)
    print "\n"

    counter = counter + 1
