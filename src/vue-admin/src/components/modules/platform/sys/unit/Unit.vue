<template>
  <div>
    <header class="header navbar bg-white shadow">
      <div class="btn-group tool-button">
        <a class="btn btn-primary navbar-btn" v-link="{path:base+'/platform/sys/unit/add'}"><i class="ti-plus"></i>新建单位</a>
      </div>
    </header>
    <div class="content-wrap">
      <div class="wrapper" style="min-height:500px;">
        <div class="row mb15">
          <div class="col-lg-12">
            <table id="unitTreeTable" class="table no-m">
              <thead>
              <tr>
                <th class="col-md-2 pd-l-lg">
                  <span class="pd-l-sm ml20"></span>单位名称
                </th>
                <th class="col-md-2">编码</th>
                <th class="col-md-2">地址</th>
                <th class="col-md-2">电话</th>
                <th class="col-md-2">操作</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="unit in units" :data-tt-id="unit.id" :data-tt-parent-id="unit.parentId" :data-tt-branch="unit.hasChildren">
                <td>
                  <span class="pd-l-sm"></span>{{unit.name}}
                </td>
                <td>{{unit.unitcode}}</td>
                <td>{{unit.address}}</td>
                <td>{{unit.telephone}}</td>
                <td>
                  <div class="btn-group">
                    <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                      <i class="ti-settings"></i>
                      <span class="ti-angle-down"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu">
                      <li><a v-link="{path:base+'/platform/sys/detail/'+unit.id}" data-toggle="modal"
                             data-target="#dialogDetail">查看</a></li>
                      <li><a href="${base}/private/sys/unit/edit/${o.id}" data-pjax>修改</a></li>
                      <li><a href="javascript:;" @click="del(unit.id)">删除</a></li>
                      <li class="divider"></li>
                      <li><a href="${base}/private/sys/unit/add?pid=${o.id}" data-pjax>添加子单位</a></li>

                    </ul>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <a class="exit-offscreen"></a>
    <!-- 排序 -->
    <div id="dialogSort" class="modal fade bs-modal-sm" tabindex="-1" role="dialog" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">

        </div>
      </div>
    </div>
    <!-- 删除 -->
    <div id="dialogDelete" class="modal fade bs-modal-sm" tabindex="-2" role="dialog" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h4 class="modal-title">删除单位</h4>
          </div>
          <div class="modal-body">
            <div class="row">
              <div class="col-xs-12">
                删除后无法恢复，并且用户也将失去此单位的关系； <br/>
                如果选中的是父级单位，那么父级单位下面的所有子单位也全部会删除，请谨慎操作！
                <br/>确定要删除吗？
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取 消</button>
            <button id="ok" type="button" class="btn btn-primary" data-loading-text="正在删除...">确 定</button>
          </div>
        </div>
      </div>
    </div>
    <!-- 详情 -->
    <div id="dialogDetail" class="modal fade bs-modal-sm" tabindex="-3" role="dialog" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">

        </div>
      </div>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return {
        base:base,
        units: []
      }
    },
    methods: {
      del:function (id) {

      },
      load:function () {
        sublimeApp.showLoadingbar()
        this.$http.get(vue+'/platform/sys/unit/list').then((resp) => {
          sublimeApp.closeLoadingbar()
          sublimeApp.tree
          return resp.json()
        }).then((d)=> {
          if (d.code == 0) {
            this.units=d.data
          }else {
            toastr.error(d.msg)
          }
        });
      }
    },
    ready: function () {
      this.load()
    }
  }
</script>
