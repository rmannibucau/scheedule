define([
    'model/cron',
    'controller/cron/cron-form-controller',
    'text!../../../partial/cron/edit-cron.mustache'
], function (Cron, BaseController) {
    return function (params) {
        Cron.findOne(params)
            .then(function (loaded) {
                BaseController('new-cron', loaded, 'Update');
            });
    };
});