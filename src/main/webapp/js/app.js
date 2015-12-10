(function () {
    require.config({
        paths: {
            'jquery': '../webjars/jquery/2.1.4/jquery.min',
            'text': '../webjars/requirejs-text/2.0.10-3/text',
            'bootstrap': '../webjars/bootstrap/3.3.4/js/bootstrap.min',
            'can': '//cdn.rawgit.com/bitovi/canjs.com/v2.2.5/amd/can' // TODO when available
        },
        shim: {
            'bootstrap': {
                'deps': [ 'jquery' ]
            }
        }
    });
    define([ 'can', 'jquery', 'bootstrap', 'routes' ], function (can, $, b, routes) {
        $.ajaxPrefilter(function(options, originalOptions) { // we rely on json only
            options["contentType"] = 'application/json';
            options["data"] = JSON.stringify(originalOptions['data']);
        });

        // some global templates
        $.get('partial/helper/errors.mustache') // form errors
            .then(function (subTemplate) {
                can.mustache('errors', subTemplate);
            });

        // nice bootstrap tooltips
        $('[data-toggle="tooltip"]').tooltip();

        // now start the routing
        can.route.ready(false);
        new (can.Control(routes))(document);
        can.route.ready();

        console.log('Scheedule Application Started');
    });
})();
