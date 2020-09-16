$(document).ready(function(){
    $(".submitComment").click(function (e) {
         var id = $(this).attr('id')
         var text = $("#c" + id).val();
         var username = $("#uName").text();
         $.ajax({
             type: 'POST',
             url: "/comment",
             data: {
                 postId: id,
                 text: text
             }
         });
         $(this).parent().next('.comments-list').append('<div class="comment"><h5 class="card-comment-user">' + username + ': </h5><h6 class="card-comment-text">' + text + '</h6></div>');
    });
});