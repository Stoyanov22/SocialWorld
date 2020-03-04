var ui = new firebaseui.auth.AuthUI(firebase.auth());

ui.start('#firebaseui-auth-container', {
  signInOptions: [
    firebase.auth.EmailAuthProvider.PROVIDER_ID
  ],
  signInSuccessUrl: '/index',
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