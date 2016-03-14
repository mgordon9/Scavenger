# Welcome to the ScavengerGame!

## Progress

### Week 1 (2/1/2016 - 2/7/2016)
* Brainstorming for ideas
* Preparation of the presentation

### Week 2 (2/8/2016 - 2/14/2016)
* Preparation of the presentation

### Week 3 (2/15/2016 - 2/21/2016)
* Clarification of Interfaces and Entities
* Split of Work, based on Interfaces
* Implementation of a view prototype, a prototype of the backend and a rest client object on Android that requests and parses entities stored in the backend

### Week 4 (2/22/2016 - 2/28/2016)
* User functionality added to Backend
* Split into fragments to conform with Material Design
* Implementation of CreateObjectiveFragment

### Week 5 (2/29/2016 - 3/6/2016)
* Implementation of CompletedObjectivesFragment
* Refinements in MapFragment
* Start Objective implemented
* ConfirmationActivity (OpenCV-based check in) implemented
* Implementation of Feature-based image compairison

### Week 6 (3/7/2016 - 3/10/2016)
* Removed debugging statements
* Took [Demo Video](https://youtu.be/62d4VuZV2cI)

## Planning

### Team Members
* Alexander Brauckmann
* James Mckenna
* Matthew Gordon

### Goal/Objective
* Provide a fun app for users that allows for a real world scavenger hunt
* Allow the hidden object be anything someone could take a picture of.

Possible Design Options:
* Create user database in which profiles are stored with points given to users 
* Allow users to create their own scavenger hunts for other users.

### Project description
* Allow a user to take a photo of a secret object or location, starting the scavenger hunt
* Store the picture and geolocation data, allowing us to pinpoint the object/location
* Searchers can go on the hunt, receiving periodic clues and progress updates as they get closer to the target
* Users confirm they arrive at the right place by matching pictures with the original

### Final Presentation Requirements
Worst-Case
* Database: Locally stored and shipped with the app
* Sensors for hints: Compass, GPS
* Check-In: Based on location
* Video Demo

Best-Case
* Database: In Backend
* Check-In: Real-time Camera Image Matching
* Live Demo (User 1 hides Object, User 2 searches and finds it)

### APIs
* Real time photo comparison using OpenCV API and Camera API.
* Extract GPS location using Location API.
* Google App Engine for backend processing and data storage
* REST Client API to retrieve data

Possible Technologies:
* Google sign-In API.
