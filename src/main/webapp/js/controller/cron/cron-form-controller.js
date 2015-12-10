define([
    'jquery', 'can',
    'controller',
    'model/cron',
    'text!../../../partial/cron/cron-form.mustache'
], function ($, can, controller, Cron) {
    // pre-load nested form to avoid async issue (jquery 2 deprecated async:true in $.ajax)
    $.get('partial/cron/cron-form.mustache')
        .then(function (subTemplate) {
            can.mustache('cronForm', subTemplate);
        });

    return function (view, initialData, submitButtonText) {
        can.view('../partial/cron/' + view, {
            scheduling: initialData,
            errors: new can.List([]),
            submitButtonText: submitButtonText,
            submit: function (scheduling, errors, ev) {
                ev.preventDefault();
                errors.replace(scheduling.validate());
                if (!errors.length) {
                    var copy = scheduling.serialize();
                    new Cron(copy).save()
                        .then(function () {
                            window.location.hash = '#!';
                        }, function (err) {
                            errors.push(err.statusText);
                            // errors.push(err.responseText); // debug only
                        });
                }
            }
        }, controller.swapContent);
    };
});