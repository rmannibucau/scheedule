define([
    'can',
    'controller',
    'model/cron',
    'helper/paginator',
    'text!../../../partial/cron/cron-list.mustache'
], function (can, controller, Cron, Paginator) {
    return function (params) {
        var paginator = Paginator(params && params.page ? parseInt(params.page) : 1, 'crons');

        Cron.findAll({first: paginator.first(), max: paginator.pageSize})
            .then(
                function (crons) {
                    paginator.setTotal(crons.total);
                    can.view(
                        '../partial/cron/cron-list',
                        new can.Map({
                            crons: crons,
                            pagination: paginator,
                            removeCron: function (cron) {
                                Cron.destroy(cron.id);
                                crons.replace(crons.filter(function (current) {
                                    return current.attr('id') != cron.attr('id');
                                }));
                            },
                            forceCron: function (cron) {
                                Cron.forceExecution(cron.attr('id'));
                            }
                        }),
                        controller.swapContent);
                },
                function (e) {
                    console.log(e);
                });
    };
});