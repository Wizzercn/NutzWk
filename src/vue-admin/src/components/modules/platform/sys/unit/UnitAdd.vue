<template>
  <div>
    <header class="header navbar bg-white shadow">
      <div class="btn-group tool-button">
        <router-link :to="base + '/platform/sys/unit'" class="btn btn-primary navbar-btn">
          <i class="ti-plus"></i>返回
        </router-link>
      </div>
    </header>
    <div class="content-wrap">
      <div class="wrapper" style="min-height:500px;">
      </div>
    </div>
  </div>
</template>
<script>
    export default{
        name:'UnitAdd',
        data(){
           return {
            base:base
           }
        }
    }

</script>
