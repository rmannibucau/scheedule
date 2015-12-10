define([], function () {
    return {
        paginationUrlSuffix: function (params) {
            url = '';
            if (params.first) {
                url += '?first=' + params.first;
                if (params.max) {
                    url += '&max=' + params.max;
                }
            } else if (params.max) {
                url += '?max=' + params.max;
            }
            return url;
        }
    };
});