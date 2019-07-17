# UploadGalleryApp
This app can be used to upload image from gallery/camera to Firebase storage/database.

## Build Instruction
Please use android studio 3.4 or latest for building.

## Test coverage
Execute the gradle task jacocoTestReport :

    gradlew jacocoTestReport
The report will be geneated at *build/reports/jacoco/jacocoTestReport/html/index.html*

## App overview
The application uses firebase storage for storing images, the URI of the image in firebase storage is pushed to Firebase realtime database. A user can either select an image from their phone gallery or click an image using the phone's camera. They will be provided with an option to edit (basic functionalities like rotate and crop) and then upload the image to cloud aka firebase.

## Architecture overview
App follows MVP architecture pattern. The [ViewManagerImpl.java](https://github.com/rohanreji/UploadGalleryApp/blob/master/app/src/main/java/com/themaskedbit/uploadgalleryapp/gallery/manager/ViewManagerImpl.java) is the presenter which interacts between the editor as well as MainActivity and model through interfaces.
[FirebaseApi.java] (https://github.com/rohanreji/UploadGalleryApp/blob/master/app/src/main/java/com/themaskedbit/uploadgalleryapp/gallery/api/FirebaseApi.java) is the Model (repository) to fetch and upload images. Dependency Injection is done using [dagger component] (https://github.com/rohanreji/UploadGalleryApp/blob/master/app/src/main/java/com/themaskedbit/uploadgalleryapp/gallery/di/AppModule.java). The editor uses a third party library [SimpleCropView] (https://github.com/igreenwood/SimpleCropView).


## Thanks
[Rohan Reji]
(http://rohanreji.com/)