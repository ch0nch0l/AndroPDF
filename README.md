## AndroPDF
###### Generate PDF using iText Library in Android App

### Steps:
#### 1. Add iText Dependency to your build.gradle (App Module)
    implementation 'com.itextpdf:itextpdf:5.5.13'

#### 2. Add READ/WRITE permission to Menifest
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


#### 3. Copy `CreatePDF()` method into your Activity

#### 4. Call `CreatePDF()` method on button `OnClick()` event

#### 5. Check permission if `READ/WRITE permission` is not given.

##### `NOTE:` 
* The directory is by default set to **DOWNLOADS** folder of internal storage. Change as per your desired directory. 
* Report design is customizable. To change the design go through the comments declared in codes and customize as per your need.

### Fork & Star if it helped you.