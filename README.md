# RavelinSampleLibrary
# Ravelin sample library and application: 

### SDK + sample app

in test app gradle:
```sh
implementation project(path: ':ravelinlibrary')
```
Initialise:
```sh
sdk = RavelinSdk.Builder(this)
    .setEmail("smchala@hotmail.com")
    .setName("Sami Mchala")
    .setSecretKey(SECRET)//... :)
    .create()
```
Get Blob: 
```sh
sdk.getBlob()
```
Send blob to Ravelin: 
```sh
sdk.postDeviceInformation()
```
### Note
Getting device and customer information, building the json blob, after encryption + hash set deviceId.
Sample app presents 2 buttons
* 1st gets the blob
* 2nd button send blob
should have moved logic from activity into a view model, databinding to link up viewmodel and xml, keeping activity handling permissions only
unit tests examples
tried to make all dependencies passed, making the code testable, also could use Dagger2...
Seperation of concern, single responsability...

### Outstanding: 
due to time constraint had to stop and getting bad request, 400 code after api call, need fixing

License
----
MIT
