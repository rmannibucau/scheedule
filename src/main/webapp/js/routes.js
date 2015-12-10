define([
    'can', 'jquery',
    'controller/cron/cron-list-controller',
    'controller/cron/new-cron-controller',
    'controller/cron/edit-cron-controller',
    'controller/configuration/configuration-list-controller',
    'controller/configuration/new-configuration-controller',
    'controller/configuration/edit-configuration-controller',
    'controller/event/event-list-controller'
], function (
        can, $,
        CronListController, NewCronController, EditCronController,
        ConfigurationListController, NewConfigurationController, EditConfigurationController,
        EventListController) {
    return {
        'new-cron route': NewCronController,
        'cron/:id route': EditCronController,
        'crons/:page route': CronListController,
        'new-configuration route': NewConfigurationController,
        'configuration/:key route': EditConfigurationController,
        'configurations/:page route': ConfigurationListController,
        'events/:page route': EventListController,
        'route': CronListController
    };
});

