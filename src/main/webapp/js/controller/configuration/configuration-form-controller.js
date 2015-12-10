define([
    'can',
    'controller',
    'model/configuration',
    'text!../../../partial/configuration/configuration-form.mustache'
], function (can, controller, Configuration) {
    // pre-load nested form to avoid async issue (jquery 2 deprecated async:true in $.ajax)
    $.get('partial/configuration/configuration-form.mustache')
        .then(function (subTemplate) {
            can.mustache('configurationForm', subTemplate);
        });

    return function (view, initialData, submitButtonText) {
        can.view('../partial/configuration/' + view, {
            entry: initialData,
            errors: new can.List([]),
            submitButtonText: submitButtonText,
            submit: function (config, errors, ev) {
                ev.preventDefault();
                errors.replace(config.validate());
                if (!errors.length) {
                    var copy = config.serialize();
                    new Configuration(copy).save()
                        .then(function () {
                            window.location.hash = '#!configurations/1';
                            if (config.attr('transient')) { // now it is not more transient
                                config.removeAttr('transient');
                            }
                        }, function (err) {
                            errors.push(err.statusText);
                        });
                }
            }
        }, controller.swapContent);
    };
});