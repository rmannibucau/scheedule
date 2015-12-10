define([
    'model/configuration',
    'controller/configuration/configuration-form-controller',
    'text!../../../partial/configuration/new-configuration.mustache'
], function (Configuration, BaseController) {
    return function () {
        BaseController('new-configuration', new Configuration({ transient:true }), 'Create');
    };
});