define([
    'model/cron',
    'controller/cron/cron-form-controller',
    'text!../../../partial/cron/new-cron.mustache'
], function (Cron, BaseController) {
    return function () {
        BaseController('new-cron', new Cron({ state: true }), 'Create');
    };
});