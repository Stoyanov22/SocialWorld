$(document).ready(function(){
    $("#delete").click(function (e) {
        if (window.confirm("Are you sure?")) {
            var uid = $("#uid").val();
            $.ajax({
                type: 'POST',
                url: "/remove_user",
                data: {
                    uid: uid
                },
                success:function(data) {
                      window.location.href = '/';
                }
            });
        }
    });
});