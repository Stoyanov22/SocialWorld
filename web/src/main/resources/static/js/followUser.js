$(document).ready(function(){
    $("#follow").click(function (e) {
            var uid = $("#uid").val();
            $.ajax({
                type: 'POST',
                url: "/follow_user",
                data: {
                    followedId: uid
                },
                success:function(data) {
                    location.reload()
                }
            });

    });

    $("#unfollow").click(function (e) {
            var uid = $("#uid").val();
            $.ajax({
                type: 'POST',
                url: "/unfollow_user",
                data: {
                    unfollowedId: uid
                },
                success:function(data) {
                    location.reload()
                }
            });

    });
});