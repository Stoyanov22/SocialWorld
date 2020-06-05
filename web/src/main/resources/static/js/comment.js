$(document).ready(function(){
    $(".submitComment").click(function (e) {
         var id = $(this).attr('id')
         var text = $("#c" + id).val();
         $.ajax({
             type: 'POST',
             url: "/comment",
             data: {
                 postId: id,
                 text: text
             },
         });
    });
});