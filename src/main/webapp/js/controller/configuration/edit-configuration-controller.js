define([
    'model/configuration',
    'controller/configuration/configuration-form-controller',
    'text!../../../partial/configuration/edit-configuration.mustache'
], function (Configuration, BaseController) {
    return function (params) {
        Configuration.findOne(params)
            .then(function (loaded) {
                BaseController('edit-configuration', new Configuration(loaded), 'Update');
            });
    };
});