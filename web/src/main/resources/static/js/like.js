$(document).ready(function(){
    $(".like").click(function (e) {
         var id = $(this).attr('id')
         $.ajax({
             type: 'POST',
             url: "/like",
             data: {
                 postId: id
             },
         });
    });
});