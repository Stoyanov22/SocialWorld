var storage = firebase.storage();
var storageRef = storage.ref();

$(document).ready(function(){
    $("form").submit(function (e) {
        var file = $("#picture")[0];

        if(file.files.length > 0){
          e.preventDefault(); // this will prevent from submitting the form from the HTML.
          picture = file.files[0];
          var metadata = {
            contentType: 'image/jpeg'
          };

          // Upload file and metadata to the object 'images/mountains.jpg'
          var uploadTask = storageRef.child('ProfilePictures/' + name).put(picture, metadata);

          // Listen for state changes, errors, and completion of the upload.
          uploadTask.on(firebase.storage.TaskEvent.STATE_CHANGED, // or 'state_changed'
            function(snapshot) {
              // Get task progress, including the number of bytes uploaded and the total number of bytes to be uploaded
              var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
              console.log('Upload is ' + progress + '% done');
            }, function(error) {
            switch (error.code) {
              case 'storage/unauthorized':
                alert("User doesn't have permission to access the object")
                break;

              case 'storage/canceled':
                alert("User canceled the upload")
                break;

              case 'storage/unknown':
                alert("Unknown error occurred")
                break;
            }
          }, function() {
            // Upload completed successfully, now we can get the download URL
            uploadTask.snapshot.ref.getDownloadURL().then(function(downloadURL) {
              var name = $("#name").val();
              var dob = $("#dob").val();
              var genderId = $("#genderId").val();
              var countryId = $("#countryId").val();
              $.ajax({
                    type: 'POST',
                    url: "/edit_profile",
                    async: false,
                    data: {
                        name: name,
                        dob: dob,
                        genderId: genderId,
                        countryId: countryId,
                        picture: downloadURL
                    },
                    dataType: "json",
              });
            });
          });
        }
    });
});