var ui = new firebaseui.auth.AuthUI(firebase.auth());

ui.start('#firebaseui-auth-container', {
  callbacks: {
    signInSuccessWithAuthResult: function(authResult, redirectUrl) {
      firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL);
      var user = authResult.user;
      $.ajax({
            type: 'POST',
            url: "/login",
            data: {
                uid: user.uid,
                email: user.email,
            },
            dataType: "json",
            success: function(resultData) { alert("Save Complete") }
      });
    }
  },
  signInOptions: [
    firebase.auth.EmailAuthProvider.PROVIDER_ID
  ],
  credentialHelper: firebaseui.auth.CredentialHelper.NONE,
});


//
//$('#submit').click(function () {
//    firebase.auth().signInWithEmailAndPassword("asd@asd.bg", "asdasd").catch(function(error) {
//        console.log(error.message)
//    });
//    var user = firebase.auth().currentUser;
//
//    if (user) {
//        $('#token').val(user.getIdToken())
//    }
//})