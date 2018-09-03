new Vue({
    el: '#menu-top',
    data: function () {
        return {
            showDialog: false,
            loading: false,
        }
    },
    methods: {
        open: function () {
            this.showDialog = true
        }
    },
    created: function () {
    }
})
new Vue({
    el: '#menu-left',
    data: function () {
        return {
            loading: false,
        }
    },
    methods: {
    },
    created: function () {
    }
})