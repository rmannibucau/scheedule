define(['can', 'model/helper/pagination'], function (can, Pagination) {
    return can.Model.extend({
        resource: 'api/event',
        findAll : function (params) {
            return can.ajax({
                url: 'api/event' + Pagination.paginationUrlSuffix(params),
                type: 'GET'
            });
        }
    }, {});
});
