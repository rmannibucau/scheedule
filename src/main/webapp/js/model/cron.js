define(['can', 'model/helper/pagination'], function (can, Pagination) {
    return can.Model.extend({
        resource: 'api/cron',
        findAll : function (params) {
            return can.ajax({
                url: 'api/cron' + Pagination.paginationUrlSuffix(params),
                type: 'GET'
            });
        },
        forceExecution: function (id) {
            return can.ajax({
                url: 'api/cron/force/' + id,
                type: 'HEAD'
            })
        }
    }, {
        validate: function () {
            var errors = new can.List();
            if (!this.attr('name')) {
                errors.push('No name filled!');
            }
            if (!this.attr('cron')) {
                errors.push('No cron expression filled!');
            }
            if (!this.attr('task')) {
                errors.push('No task filled!');
            }
            return errors;
        }
    });
});