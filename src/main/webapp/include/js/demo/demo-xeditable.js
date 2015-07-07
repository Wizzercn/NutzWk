// Xeditable Demo
// ----------------------------------- 

(function(window, document, $, undefined){
    
    $(function(){

        // Font Awesome support
        $.fn.editableform.buttons =
          '<button type="submit" class="btn btn-primary btn-sm editable-submit">'+
            '<i class="fa fa-fw fa-check"></i>'+
          '</button>'+
          '<button type="button" class="btn btn-default btn-sm editable-cancel">'+
            '<i class="fa fa-fw fa-times"></i>'+
          '</button>';

       //defaults
       $.fn.editable.defaults.url = '../server/xeditable.res';

        //enable / disable
       $('#enable').click(function() {
           $('#user .editable').editable('toggleDisabled');
       });
        
        //editables 
        $('#username').editable({
               url: '../server/xeditable.res',
               type: 'text',
               pk: 1,
               name: 'username',
               title: 'Enter username'
        });
        
        $('#firstname').editable({
            validate: function(value) {
               if($.trim(value) === '') return 'This field is required';
            }
        });
        
        $('#sex').editable({
            prepend: "not selected",
            source: [
                {value: 1, text: 'Male'},
                {value: 2, text: 'Female'}
            ],
            display: function(value, sourceData) {
                 var colors = {"": "gray", 1: "green", 2: "blue"},
                     elem = $.grep(sourceData, function(o){return o.value == value;});
                     
                 if(elem.length) {
                     $(this).text(elem[0].text).css("color", colors[value]);
                 } else {
                     $(this).empty();
                 }
            }
        });
        
        $('#status').editable();
        
        $('#group').editable({
           showbuttons: false
        });
            
        $('#dob').editable();
              
        $('#event').editable({
            placement: 'right',
            combodate: {
                firstItem: 'name'
            }
        });
               
        $('#comments').editable({
            showbuttons: 'bottom'
        });
        
        $('#note').editable();
        $('#pencil').click(function(e) {
            e.stopPropagation();
            e.preventDefault();
            $('#note').editable('toggle');
       });
        
       $('#fruits').editable({
           pk: 1,
           limit: 3,
           source: [
            {value: 1, text: 'banana'},
            {value: 2, text: 'peach'},
            {value: 3, text: 'apple'},
            {value: 4, text: 'watermelon'},
            {value: 5, text: 'orange'}
           ]
        });
             
       $('#user .editable').on('hidden', function(e, reason){
            if(reason === 'save' || reason === 'nochange') {
                var $next = $(this).closest('tr').next().find('.editable');
                if($('#autoopen').is(':checked')) {
                    setTimeout(function() {
                        $next.editable('show');
                    }, 300);
                } else {
                    $next.focus();
                }
            }
       });
       
       // TABLE
       // ----------------------------------- 

        $('#users a').editable({
            type: 'text',
            name: 'username',
            title: 'Enter username'
        });

    });

})(window, document, window.jQuery);

