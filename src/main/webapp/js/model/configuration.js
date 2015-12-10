define(['can', 'model/helper/pagination'], function (can, Pagination) {
    return can.Model.extend({
        id: 'key', // field is not 'id' here
        resource: 'api/configuration',
        findAll : function (params) { // handle pagination
            return can.ajax({
                url: 'api/configuration' + Pagination.paginationUrlSuffix(params),
                type: 'GET'
            });
        }
    }, {
        id: function () {
            return encodeURIComponent(this.attr('key'));
        },
        isNew: function () { // we can't rely on id since we'll almost always get an id
            return !!this.transient;
        },

        validate: function () {
            var errors = new can.List();
            if (!this.attr('key')) {
                errors.push('No key filled!');
            }
            return errors;
        }
    });
});