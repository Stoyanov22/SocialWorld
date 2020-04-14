$("#login").click(function(){
   $("#loginForm").toggle(1000);
});

var ui = new firebaseui.auth.AuthUI(firebase.auth());

ui.start('#firebaseui-auth-container', {
  callbacks: {
    signInSuccessWithAuthResult: function(authResult, redirectUrl) {
      firebase.auth().setPersistence(firebase.auth.Auth.Persistence.LOCAL);
      var user = authResult.user;
      $.ajax({
            type: 'POST',
            url: "/login",
            async: false,
            data: {
                uid: user.uid,
                email: user.email,
            },
            dataType: "json",
      });
      return true;
    }
  },
  signInOptions: [
    firebase.auth.EmailAuthProvider.PROVIDER_ID
  ],
  queryParameterForSignInSuccessUrl: 'signInSuccessUrl',
  signInSuccessUrl: '/feed',
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