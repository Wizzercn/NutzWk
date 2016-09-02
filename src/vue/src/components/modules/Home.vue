<template>
    <div class="hello">
        <h1>{{ menu.title }}</h1>
    </div>
</template>

<script>
    export default {
        data: function () {
            return {
                menu: {
                    name: 'home',
                    title: '首页',
                    icon: 'fa-home'
                }
            }
        },
        ready: function () {
            this.$parent.currMenu = this.menu;
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
