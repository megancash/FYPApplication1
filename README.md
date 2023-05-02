# **First Year Companion**

## **Introduction**
First Year Companion is a mobile application aimed to help with the onboarding of new students beginning college. This application will work as a type of workflow 
that will lead the student through the things they need to do to integrate and to be onboarded into the university, with a heavy importance on the social aspect of beginning university. 

## **Project Objectives**
My objective when developing this project was to create an application that aids new students when onboarding into college.
All students have expectations about college life before they have even started their first week. Some students are enthusiastic and eager for their new 
independence and educational journey. However, other students can be quite anxious beginning college. All new students, regardless of their initial expectations, 
can encounter obstacles and challenges that they did not expect during their transition into college. 

My overall objective was to have all of the relevant needs of the student in one place, with the application being easily navigable.  

Users should be able to:

-	Register and login to their account
-	Reset their password if they have forgotten it
-	Logout of their account
-	View all of the features on the home dashboard
-	Edit their own profile information
-	View and search through users on the application
-	Individually chat with another user
-	Create groupchats
-	Search through groupchats list
-	Chat within groupchats
-	Edit group chat information
-	Create an event within a group chat
-	Access the transport tile to find the quickest route to their campus
-	Access the accommodation tile to access the accommodation links
-	Access the calendar tile to add dates, view key dates and academic calendar
-	Access the timetable tile to create and store their college timetable
-	Access to-do list tile to create to-do list tasks and mark them off when they are completed
-	Access additional links tile to view important resources such as Brightspace, Moodle, Publish, TU Dublin library etc.

The system should provide the following:

-	Authenticate user for register and login
-	A way to access core functionality

## **System Architecture**
First Year Companion is an Android Firebase application, the system architecture includes:
-	**Presentation Layer:**
This includes activities, views, and fragments. By using the Firebase SDK’s these components interact with the Firebase backend. This layer is for handling the UI of the application. This layer is the layer that can be viewed by the student.

-	**Application Layer:** 
This layer is needed to handle all of the business logic of the application. This layer interacts with the Firebase backend and performs user authentication and firebase data storage.

-	**Data Layer:**
This layer includes Firebase cloud storage and the Realtime database, this layer is needed to handle the data persistence of the application. It provides all of the necessary data to the Application layer above.

-	**Firebase Backend:**
The Firebase backend is a cloud-based infrastructure. This includes the Firebase Realtime database, Firebase Authentication and Firebase Cloud Storage for my application. The backend provides all of the relevant services and APIs for the Android application to communicate with it.

