define([
    'can',
    'controller',
    'model/event',
    'helper/paginator',
    'text!../../../partial/event/event-list.mustache'
], function (can, controller, Event, Paginator) {
    return function (params) {
        var paginator = Paginator(params && params.page ? parseInt(params.page) : 1, 'events');

        Event.findAll({first: paginator.first(), max: paginator.pageSize})
            .then(
                function (events) {
                    paginator.setTotal(events.total);
                    can.view('../partial/event/event-list',
                        {
                            events: events.serialize(), // serialize to ensure we can work on the date properly
                            pagination: paginator
                        },
                        controller.swapContent);
                },
                function (e) {
                    console.log(e);
                });
    };
});