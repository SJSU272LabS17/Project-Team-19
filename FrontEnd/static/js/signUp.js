/**
 * Created by shivam on 4/29/17.
 */

$(function() {
    $('#btnSignUp').click(function() {
    console.log("This is working so far");
        $.ajax({
            url: '/signUp',
            data: $('form').serialize(),
            type: 'POST',
            success: function(response) {
                console.log(response);
            },
            error: function(error) {
                console.log(error);
            }
        });
    });
});
