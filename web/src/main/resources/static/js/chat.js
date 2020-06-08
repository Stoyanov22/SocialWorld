$(document).ready(function(){
    $("#submitMessage").click(function (e) {
         var content = $("#content").val();
         var toUserId = $("#toUser").val();
         $.ajax({
             type: 'POST',
             url: "/chat",
             data: {
                 content: content,
                 toUserId: toUserId
             },
         });
    });
});