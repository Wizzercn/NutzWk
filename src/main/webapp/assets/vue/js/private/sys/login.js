/**
 * Created by wizzer on 2016/7/15.
 */
var vm = new Vue({
    el: 'body',
    data: {
        myform: {},
        model: {
            contactRequired: false
        }
    },
    methods: {
        onSubmit: function () {
            console.log(this.myform.$valid);
        }
    }
});