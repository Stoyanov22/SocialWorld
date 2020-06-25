$(document).ready(function(){
    $(".like").click(function (e) {
         var id = $(this).attr('id')
         $.ajax({
             type: 'POST',
             url: "/like",
             data: {
                 postId: id
             }
         });

        var likesElement = $(this).closest('div').closest(".card").find('.card-post-likes')
        var text = likesElement.text();
        var likesCount = text.split(' ')[0];
        if($(this).attr("src") == "images/tick.png"){
            $(this).attr('src', "images/tickBlack.png");
            likesElement.text((Number(likesCount) + 1) + " likes");
        } else {
            $(this).attr('src', "images/tick.png");
            likesElement.text((Number(likesCount) - 1) + " likes");
        }

    });
});