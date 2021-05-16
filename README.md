# Parking Management System

<img src="https://drive.google.com/uc?export=view&id=12Yhcl35EdrRlM6qstVg55CnOtW_xFRAD" align="left" height="80px" />
<img align="left" width="0" height="80px" hspace="10" />

<div style="display:block; height: 168px;">
<b><i>Parking Management Systeam</i></b>

This is my final year project of BE-Computer. This project is made to manage the whole parking flow starting from registering fresh land to book parking slots for your vehicle.
</div>

<br/>

## Table of contents
* [Demo](#Demo)
* [About Project](#About-Project)
    * [Users](#Users)
    * [Workflow](#Workflow)
* [Libraries Used](#Libraries-Used)
* [TODO](#TODO)

## Demo
* Here is a demo video of our application.

  [![DEMO VIDEO](https://drive.google.com/uc?export=view&id=1N-iXcvZjzhT3lJpF-TZR8WIx6u7MXRtI)](https://www.youtube.com/watch?v=Hdt099NOA8M)

## About Project
### Users
*   Our project has 4 users.
    1. Land Owner: Who owns the land and registers it to our application.
    2. Auditor: Who authorizes the land audit request which is made by the auditor.
    3. People: Who book the parking plot for their vehicle.
    4. Service Person: Who verifies the booking details at the parking location. (Same as the person who verifies our BookMyShow ticket by scanning the code)

### Workflow
* High-level workflow diagram for the system. For more details please check our demo video [Here](#Demo).
  ![WORKFLOW-DIAGRAM](https://drive.google.com/uc?export=view&id=1tUnTtRkU3ZZ_mQCPKY6KCXna6Vk73GqY)

### Modules
1. Backend App: https://github.com/SamarthThesiya/Parking-Managment-System/tree/master/Backend
    * This backend app is developed on the cake-php framework and being used to serve all four android apps.
2. Land-Owner's App: https://github.com/SamarthThesiya/Parking-Managment-System/tree/master/Android/Land%20owner
3. Auditor's App: https://github.com/SamarthThesiya/Parking-Managment-System/tree/master/Android/Auditor
4. People's App: https://github.com/SamarthThesiya/Parking-Managment-System/tree/master/Android/Public%20users
4. Service-Person's App: https://github.com/SamarthThesiya/Parking-Managment-System/tree/master/Android/Service%20person


### Libraries Used
* Thank to all author of below library as it's helped me a lot to develop this project rapidly.
    * [me.dm7.barcodescanner:zxing:1.9](https://github.com/dm77/barcodescanner)
    * [com.github.bumptech.glide:glide:4.11.0](https://github.com/bumptech/glide)
    * [com.schibstedspain.android:leku:7.2.0](https://github.com/AdevintaSpain/Leku)
    * [com.github.dhaval2404:imagepicker:1.7.5](https://github.com/Dhaval2404/ImagePicker)
    * [com.hbb20:ccp:2.2.3](https://github.com/hbb20/CountryCodePickerProject)
    * [com.squareup.retrofit2:retrofit:2.9.0](https://github.com/square/retrofit)

### TODO
* As this is just a student project, few things need to Done/Enhance.
    * Implement payment gateway
    * Implement RBAC (Role-Based-Access-Control)
