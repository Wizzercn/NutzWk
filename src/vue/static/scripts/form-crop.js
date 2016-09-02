'use strict';

var imageCrop = function () {

  var cb, filter;

  var $targ = $('#target2');
  var $easing = $('#easing');

  function interface_load(obj) {
    cb = obj;

    cb.newSelection();
    cb.setSelect([147, 55, 456, 390]);
    cb.refresh();
    // Hack a "special" selection...
    var logosel = cb.newSelection().update($.Jcrop.wrapFromXywh([73, 268, 400, 100]));

    $.extend(logosel, {
      special: true, // custom value used in our local script here
      bgColor: '#999',
      bgOpacity: 0.8,
      canResize: false,
      canDelete: false
    });

    logosel.element.prepend('<img src="http://tapmodo.com/img/tapmodo-official.png" style="position:absolute;background-color:white;width:100%;height:100%;" />');
    logosel.aspectRatio = 400 / 100;
    logosel.refresh();
    cb.ui.multi[1].focus();

    // Select an interesting easing function
    $easing[0].selectedIndex = 24;

    // Set up the easing function select element event
    cb.opt.animEasing = $easing.change(function (e) {
      var $targ = $(e.target);
      cb.opt.animEasing = $targ.val();
      e.preventDefault();
      cb.ui.selection.animateTo([Math.random() * 300, Math.random() * 200, (Math.random() * 540) + 50, (Math.random() * 340) + 60]);
    }).val();

    $('#filter-selections input').attr('checked', false);
    $('#page-interface').on('startselect', function (e) {
      e.preventDefault();
    });

    /**
     *
     */
    cb.container.on('cropfocus cropblur cropstart cropend', function (e) {
      var sel = $(e.target).data('selection');
      switch (e.type) {
      case 'cropfocus':
        $('#can_size')[0].checked = sel.canResize ? true : false;
        $('#can_delete')[0].checked = sel.canDelete ? true : false;
        $('#can_drag')[0].checked = sel.canDrag ? true : false;
        $('#set_minsize')[0].checked = (sel.minSize[0] > 8) ? true : false;
        $('#set_maxsize')[0].checked = (sel.maxSize[0]) ? true : false;
        $('#set_bounds')[0].checked = (sel.edge.n) ? true : false;
        $('#is_linked')[0].disabled = sel.special ? true : false;
        $('#is_linked')[0].checked = sel.linked ? true : false;
        $('#shading-tools a')[0].disabled = sel.special ? true : false;
        $('#shading-tools a')[sel.special ? 'addClass' : 'removeClass']('disabled');

        $('#ar-links').find('.active').removeClass('active');
        if (sel.aspectRatio) {
          if (!$('#ar-links').find('[data-value="' + sel.aspectRatio + '"]').addClass('active').length) {
            $('#ar-lock').addClass('active');
          }
        } else {
          $('#ar-free').addClass('active');
        }
      }
    });

    $('#aspect_ratio').on('change', function (e) {
      var s = cb.ui.selection;
      var b = s.get();
      s.aspectRatio = e.target.checked ? b.w / b.h : 0;
      s.refresh();
    });
    $('#is_linked').on('change', function (e) {
      cb.ui.selection.linked = e.target.checked;
    });

    $('#selection-options').on('change', '[data-filter-toggle]', function (e) {
      var tog = $(e.target).data('filter-toggle');
      var o = {};
      o[tog] = e.target.checked ? true : false;
      cb.setOptions(o);
    });

    var cycle_colors = [
            'red',
            'blue',
            'gray',
            'yellow',
            'orange',
            'green',
            'white'
          ];

    function random_coords() {
      return [
              Math.random() * 300,
              Math.random() * 200,
        (Math.random() * 540) + 50,
        (Math.random() * 340) + 60
            ];
    }

    $('#can_drag,#can_size,#can_delete,#enablesel,#multisel,#anim_mode').attr('checked', 'checked');
    $('#is_linked,#aspect_ratio').attr('checked', false);

    function anim_mode() {
      return document.getElementById('anim_mode').checked;
    }

    // A simple function to cleanup multiple spawned selections
    function run_cleanup() {
      var m = cb.ui.multi,
        s = cb.ui.selection;

      for (var i = 0; i < m.length; i++) {
        if (s !== m[i]) {
          m[i].remove();
        }
      }

      cb.ui.multi = [s];
      s.center();
      s.focus();
    }

    // Animate button event
    $(document.body).on('click', '[data-action]', function (e) {
      var $targ = $(e.target);
      var action = $targ.data('action');

      switch (action) {
      case 'set-maxsize':
        cb.setOptions({
          maxSize: e.target.checked ? [400, 350] : [0, 0]
        });
        break;
      case 'set-minsize':
        cb.setOptions({
          minSize: e.target.checked ? [60, 60] : [8, 8]
        });
        break;
      case 'set-bounds':
        if (e.target.checked) {
          cb.setOptions({
            edge: {
              n: 15,
              e: -20,
              s: -40,
              w: 28
            }
          });
        } else {
          cb.setOptions({
            edge: {
              n: 0,
              e: 0,
              s: 0,
              w: 0
            }
          });
        }
        break;
      case 'set-image':
        $targ.parent().find('.active').removeClass('active');
        $targ.addClass('active');
        $('#target').attr('src', '//jcrop-cdn.tapmodo.com/assets/images/' + $targ.data('image') + '-750.jpg');
        break;
      case 'set-ar':
        var value = $targ.data('value');
        $targ.parent().find('.active').removeClass('active');
        if (value === 'lock') {
          var b = cb.ui.selection.get();
          value = b.w / b.h;
        }
        $targ.addClass('active');
        cb.setOptions({
          aspectRatio: value
        });
        break;
      case 'set-selmode':
        $targ.parent().find('.active').removeClass('active');
        $targ.addClass('active');
        switch ($targ.data('mode')) {
        case 'none':
          cb.container.addClass('jcrop-nodrag');
          cb.setOptions({
            allowSelect: false
          });
          break;
        case 'single':
          cb.container.removeClass('jcrop-nodrag');
          cb.setOptions({
            allowSelect: true,
            multi: false
          });
          break;
        case 'multi':
          cb.container.removeClass('jcrop-nodrag');
          cb.setOptions({
            allowSelect: true,
            multi: true
          });
          break;
        }
        break;
      case 'enable-selections':
        cb.ui.stage.dragger.active = e.target.checked;
        break;
      case 'enable-multi':
        cb.ui.stage.dragger.multi = e.target.checked;
        break;
      case 'color-cycle':
        var cc = cycle_colors.shift();
        cb.setOptions({
          bgColor: cc
        });
        cycle_colors.push(cc);
        break;
      case 'set-opacity':
        $targ.parent().find('.active').removeClass('active');
        $targ.addClass('active');
        cb.setOptions({
          bgOpacity: $targ.data('opacity'),
          bgColor: 'black'
        });
        break;
      case 'cleanup-all':
        run_cleanup();
        break;
      case 'random-move':
        cb[anim_mode() ? 'animateTo' : 'setSelect'](random_coords());
        break;
      }

    }).on('keydown', function (e) {
      if (e.keyCode === 8) {
        e.preventDefault();
      }
    }).on('selectstart', function (e) {
      e.preventDefault();
    }).on('click', 'a[data-action]', function (e) {
      e.preventDefault();
    });
  }


  // The function is pretty simple
  function initJcrop() //{{{
    {
      // Create a scope-wide variable to hold the Thumbnailer instance
      var thumbnail;

      // Instantiate Jcrop
      $('#target').Jcrop({
        aspectRatio: 1,
        setSelect: [175, 100, 400, 300]
      }, function () {
        var jcrop_api = this;
        thumbnail = new $.Jcrop.component.Thumbnailer(jcrop_api, {
          width: 130,
          height: 130
        });
      });

      $('#target2').Jcrop({
        animEasing: 'easeOutQuart',
        bgOpacity: 0.35,
        linked: false,
        multi: true
      }, function () {
        this.container.addClass('jcrop-dark jcrop-hl-active');
        interface_load(this);
      });
    }

  return {
    init: function () {
      initJcrop();
    }
  };
}();

$(function () {
  imageCrop.init();
});
