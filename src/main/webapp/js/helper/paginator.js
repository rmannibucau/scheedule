define(['can', 'jquery'], function (can, $) {
    $.get('partial/helper/paginator.mustache')
        .then(function (subTemplate) {
            can.mustache('paginator', subTemplate);
        });

    return function (page, prefixUrl) {
        return {
            pageSize: 20,
            page: (page ? page : 1),
            pages: -1,

            hasMultiplePages: function () {
                return this.pages > 1;
            },
            hasPrevious: function () {
                return this.page > 1;
            },
            hasNext: function () {
                return this.page < this.pages;
            },
            previousPage: function () {
                return prefixUrl + '/' + (this.page - 1);
            },
            nextPage: function () {
                return prefixUrl + '/' + (this.page + 1);
            },
            first: function () {
                return (this.page - 1) * this.pageSize;
            },
            setTotal: function (total) {
                this.pages = total / this.pageSize;
                if (this.pageSize * this.pages < total) {
                    this.pages++;
                }
                return this;
            }
        };
    };
});