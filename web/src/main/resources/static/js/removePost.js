$(document).ready(function(){
    $(".removePost").click(function (e) {
        if (window.confirm("Are you sure?")) {
            var id = $(this).attr('id')
            $.ajax({
                type: 'POST',
                url: "/remove_post",
                data: {
                    postId: id
                },

            });
            $("#post" + id).fadeOut();
        }
    });
});