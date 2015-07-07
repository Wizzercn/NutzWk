// jqGrid demo usage
// ----------------------------------- 


(function(window, document, $, undefined){

  $(function(){
    
    // JSON EXAMPLE
    // ---------------------

    var gridJSON = $("#jqGridJSON");

    gridJSON.jqGrid({
        url: '../server/jqgrid.json',
        datatype: "json",
         colModel: [
          { label: 'Category Name', name: 'CategoryName', width: 75 },
          { label: 'Product Name', name: 'ProductName', width: 90 },
          { label: 'Country', name: 'Country', width: 100 },
          { label: 'Price', name: 'Price', width: 80, sorttype: 'integer' },
          // sorttype is used only if the data is loaded locally or loadonce is set to true
          { label: 'Quantity', name: 'Quantity', width: 80, sorttype: 'number' }
        ],
        viewrecords: true, // show the current page, data rang and total records on the toolbar
        autowidth: true,
        shrinkToFit: true,
        height: 240,
        rowNum: 20,
        rowList: [10, 20, 30],
        loadonce: true, // this is just for the demo
        caption: "Basic JSON Example",
        hidegrid: false,
        pager: "#jqGridJSONPager"
      });

    // NESTED EXAMPLE
    // ---------------------

    var gridTree = $('#jqGridTree');

    gridTree.jqGrid({
      url: "../server/jqgrid-tree.json",
      colModel: [
        {
          name: "category_id",
          index: "accounts.account_id",
          sorttype: "int",
          key: true,
          hidden: true,
          width:  60
        },{
          name: "name",
          index: "name",
          sorttype: "string",
          label: "Name",
          width: 160
        },{
          name: "price",
          index: "price",
          sorttype: "numeric",
          label: "Price",
          width: 90,
          align: "right"
        },{
          name: "qty_onhand",
          index: "qty_onhand",
          sorttype: "int",
          label: "Qty",
          width: 90,
          align: "right"
        },{
          name: "color",
          index: "color",
          sorttype: "string",
          label: "Color",
          width: 70
        },{
          name: "lft",
          hidden: true
        },{
          name: "rgt",
          hidden: true
        },{
          name: "level",
          hidden: true
        },{
          name: "uiicon",
          hidden: true
        }
      ],
      // width: "780",
      hoverrows: false,
      // viewrecords: false,
      // gridview: true,
      height: "auto",
      sortname: "lft",
      loadonce: true,
      rowNum: 30,
      // scrollrows: true,
      // enable tree grid
      treeGrid: true,
      // which column is expandable
      ExpandColumn: "name",
      // datatype
      treedatatype: "json",
      // the model used
      treeGridModel: "nested",
      // configuration of the data comming from server
      treeReader: {
        left_field: "lft",
        right_field: "rgt",
        level_field: "level",
        leaf_field: "isLeaf",
        expanded_field: "expanded",
        loaded: "loaded",
        icon_field: "icon"
      },
      sortorder: "asc",
      datatype: "json",
      pager: "#jqGridTreePager",
      viewrecords: true, // show the current page, data rang and total records on the toolbar
      autowidth: true,
      shrinkToFit: true,
      caption: "Nested Example"
    });

    $(window).on('resize', function() {
        var width = $('.jqgrid-responsive').width();
        gridJSON.setGridWidth( width );
        gridTree.setGridWidth( width );
    }).resize();


  });


})(window, document, window.jQuery);