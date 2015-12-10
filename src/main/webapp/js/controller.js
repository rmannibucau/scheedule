define([ 'jquery' ], function ($) {
    return {
        swapContent: function (html) {
            $('#content').html(html);
        }
    };
});