$("#logout").click(function(){
    firebase.auth().signOut();
})
