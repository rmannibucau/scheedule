define([
    'can',
    'controller',
    'model/configuration',
    'helper/paginator',
    'text!../../../partial/configuration/configuration-list.mustache'
], function (can, controller, Configuration, Paginator) {
    return function (params) {
        var paginator = Paginator(params && params.page ? parseInt(params.page) : 1, 'configurations');

        Configuration.findAll({first: paginator.first(), max: paginator.pageSize})
            .then(
                function (configs) {
                    paginator.setTotal(configs.total);
                    can.view(
                        '../partial/configuration/configuration-list',
                        new can.Map({
                            configurations: configs,
                            pagination: paginator,
                            removeConfig: function (config) {
                                Configuration.destroy(config.key);
                                this.configurations.replace(this.configurations.filter(function (current) {
                                    return current.attr('key') != config.attr('key');
                                }));
                            }
                        }),
                        controller.swapContent);
                },
                function (e) {
                    console.log(e);
                });
    };
});