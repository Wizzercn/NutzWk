
(function(window, document, $, undefined){

    if (typeof $ === 'undefined') { throw new Error('This application\'s JavaScript requires jQuery'); }

    $(function(){

        // Restore body classes
        // -----------------------------------
        var $body = $('body');
        new StateToggler().restoreState( $body );

        // enable settings toggle after restore
        $('#chk-fixed').prop('checked', $body.hasClass('layout-fixed') );
        $('#chk-collapsed').prop('checked', $body.hasClass('aside-collapsed') );
        $('#chk-boxed').prop('checked', $body.hasClass('layout-boxed') );
        $('#chk-float').prop('checked', $body.hasClass('aside-float') );
        $('#chk-hover').prop('checked', $body.hasClass('aside-hover') );

        // When ready display the offsidebar
        $('.offsidebar.hide').removeClass('hide');

    }); // doc ready


})(window, document, window.jQuery);

// Start Bootstrap JS
// -----------------------------------

(function(window, document, $, undefined){

    $(function(){

        // POPOVER
        // -----------------------------------

        $('[data-toggle="popover"]').popover();

        // TOOLTIP
        // -----------------------------------

        $('[data-toggle="tooltip"]').tooltip({
            container: 'body'
        });

        // DROPDOWN INPUTS
        // -----------------------------------
        $('.dropdown input').on('click focus', function(event){
            event.stopPropagation();
        });

    });

})(window, document, window.jQuery);
// CLASSYLOADER
// -----------------------------------

(function(window, document, $, undefined){

    $(function(){

        var $scroller       = $(window),
            inViewFlagClass = 'js-is-in-view'; // a classname to detect when a chart has been triggered after scroll

        $('[data-classyloader]').each(initClassyLoader);

        function initClassyLoader() {

            var $element = $(this),
                options  = $element.data();

            // At lease we need a data-percentage attribute
            if(options) {
                if( options.triggerInView ) {

                    $scroller.scroll(function() {
                        checkLoaderInVIew($element, options);
                    });
                    // if the element starts already in view
                    checkLoaderInVIew($element, options);
                }
                else
                    startLoader($element, options);
            }
        }
        function checkLoaderInVIew(element, options) {
            var offset = -20;
            if( ! element.hasClass(inViewFlagClass) &&
                $.Utils.isInView(element, {topoffset: offset}) ) {
                startLoader(element, options);
            }
        }
        function startLoader(element, options) {
            element.ClassyLoader(options).addClass(inViewFlagClass);
        }

    });

})(window, document, window.jQuery);
// GLOBAL CONSTANTS
// -----------------------------------


(function(window, document, $, undefined){

    window.APP_COLORS = {
        'primary':                '#5d9cec',
        'success':                '#27c24c',
        'info':                   '#23b7e5',
        'warning':                '#ff902b',
        'danger':                 '#f05050',
        'inverse':                '#131e26',
        'green':                  '#37bc9b',
        'pink':                   '#f532e5',
        'purple':                 '#7266ba',
        'dark':                   '#3a3f51',
        'yellow':                 '#fad732',
        'gray-darker':            '#232735',
        'gray-dark':              '#3a3f51',
        'gray':                   '#dde6e9',
        'gray-light':             '#e4eaec',
        'gray-lighter':           '#edf1f2'
    };

    window.APP_MEDIAQUERY = {
        'desktopLG':             1200,
        'desktop':                992,
        'tablet':                 768,
        'mobile':                 480
    };

})(window, document, window.jQuery);

// FULLSCREEN
// -----------------------------------

(function(window, document, $, undefined){

    if ( typeof screenfull === 'undefined' ) return;

    $(function(){

        var $doc = $(document);
        var $fsToggler = $('[data-toggle-fullscreen]');

        // Not supported under IE
        var ua = window.navigator.userAgent;
        if( ua.indexOf("MSIE ") > 0 || !!ua.match(/Trident.*rv\:11\./) ) {
            $fsToggler.addClass('hide');
        }

        if ( ! $fsToggler.is(':visible') ) // hidden on mobiles or IE
            return;

        $fsToggler.on('click', function (e) {
            e.preventDefault();

            if (screenfull.enabled) {

                screenfull.toggle();

                // Switch icon indicator
                toggleFSIcon( $fsToggler );

            } else {
                console.log('Fullscreen not enabled');
            }
        });

        if ( screenfull.raw && screenfull.raw.fullscreenchange)
            $doc.on(screenfull.raw.fullscreenchange, function () {
                toggleFSIcon($fsToggler);
            });

        function toggleFSIcon($element) {
            if(screenfull.isFullscreen)
                $element.children('em').removeClass('fa-expand').addClass('fa-compress');
            else
                $element.children('em').removeClass('fa-compress').addClass('fa-expand');
        }

    });

})(window, document, window.jQuery);
// LOAD CUSTOM CSS
// -----------------------------------

(function(window, document, $, undefined){

    $(function(){

        $('[data-load-css]').on('click', function (e) {

            var element = $(this);
            if(element.is('a'))
                e.preventDefault();

            var uri = element.data('loadCss'),
                link;

            if(uri) {
                link = createLink(uri);
                if ( !link ) {
                    $.error('Error creating stylesheet link element.');
                }
            }
            else {
                $.error('No stylesheet location defined.');
            }

        });
    });

    function createLink(uri) {
        var linkId = 'autoloaded-stylesheet',
            oldLink = $('#'+linkId).attr('id', linkId + '-old');
        $('head').append($('<link/>').attr({
            'id':   linkId,
            'rel':  'stylesheet',
            'href': uri
        }));
        if( oldLink.length ) {
            oldLink.remove();
        }
        $.post(base+"/private/theme", { path: uri } );
        return $('#'+linkId);
    }


})(window, document, window.jQuery);
// NAVBAR SEARCH
// -----------------------------------


(function(window, document, $, undefined){

    $(function(){

        var navSearch = new navbarSearchInput();

        // Open search input
        var $searchOpen = $('[data-search-open]');

        $searchOpen
            .on('click', function (e) { e.stopPropagation(); })
            .on('click', navSearch.toggle);

        // Close search input
        var $searchDismiss = $('[data-search-dismiss]');
        var inputSelector = '.navbar-form input[type="text"]';

        $(inputSelector)
            .on('click', function (e) { e.stopPropagation(); })
            .on('keyup', function(e) {
                if (e.keyCode == 27) // ESC
                    navSearch.dismiss();
            });

        // click anywhere closes the search
        $(document).on('click', navSearch.dismiss);
        // dismissable options
        $searchDismiss
            .on('click', function (e) { e.stopPropagation(); })
            .on('click', navSearch.dismiss);

    });

    var navbarSearchInput = function() {
        var navbarFormSelector = 'form.navbar-form';
        return {
            toggle: function() {

                var navbarForm = $(navbarFormSelector);

                navbarForm.toggleClass('open');

                var isOpen = navbarForm.hasClass('open');

                navbarForm.find('input')[isOpen ? 'focus' : 'blur']();

            },

            dismiss: function() {
                $(navbarFormSelector)
                    .removeClass('open')      // Close control
                    .find('input[type="text"]').blur() // remove focus
                    .val('')                    // Empty input
                ;
            }
        };

    }

})(window, document, window.jQuery);
// SIDEBAR
// -----------------------------------


(function(window, document, $, undefined){

    var $win;
    var $html;
    var $body;
    var $sidebar;
    var mq;

    $(function(){

        $win     = $(window);
        $html    = $('html');
        $body    = $('body');
        $sidebar = $('.sidebar');
        mq       = APP_MEDIAQUERY;

        // AUTOCOLLAPSE ITEMS
        // -----------------------------------

        var sidebarCollapse = $sidebar.find('.collapse');
        sidebarCollapse.on('show.bs.collapse', function(event){

            event.stopPropagation();
            if ( $(this).parents('.collapse').length === 0 )
                sidebarCollapse.filter('.in').collapse('hide');

        });

        // SIDEBAR ACTIVE STATE
        // -----------------------------------

        // Find current active item
        var currentItem = $('.sidebar .active').parents('li');

        // hover mode don't try to expand active collapse
        if ( ! useAsideHover() )
            currentItem
                .addClass('active')     // activate the parent
                .children('.collapse')  // find the collapse
                .collapse('show');      // and show it

        // remove this if you use only collapsible sidebar items
        $sidebar.find('li > a + ul').on('show.bs.collapse', function (e) {
            if( useAsideHover() ) e.preventDefault();
        });

        // SIDEBAR COLLAPSED ITEM HANDLER
        // -----------------------------------


        var eventName = isTouch() ? 'click' : 'mouseenter' ;
        var subNav = $();
        $sidebar.on( eventName, '.nav > li', function() {

            if( isSidebarCollapsed() || useAsideHover() ) {

                subNav.trigger('mouseleave');
                subNav = toggleMenuItem( $(this) );

                // Used to detect click and touch events outside the sidebar
                sidebarAddBackdrop();
            }

        });

        var sidebarAnyclickClose = $sidebar.data('sidebarAnyclickClose');

        // Allows to close
        if ( typeof sidebarAnyclickClose !== 'undefined' ) {

            $('.wrapper').on('click.sidebar', function(e){
                // don't check if sidebar not visible
                if( ! $body.hasClass('aside-toggled')) return;

                var $target = $(e.target);
                if( ! $target.parents('.aside').length && // if not child of sidebar
                    ! $target.is('#user-block-toggle') && // user block toggle anchor
                    ! $target.parent().is('#user-block-toggle') // user block toggle icon
                ) {
                    $body.removeClass('aside-toggled');
                }

            });
        }

    });

    function sidebarAddBackdrop() {
        var $backdrop = $('<div/>', { 'class': 'dropdown-backdrop'} );
        $backdrop.insertAfter('.aside').on("click mouseenter", function () {
            removeFloatingNav();
        });
    }

    // Open the collapse sidebar submenu items when on touch devices
    // - desktop only opens on hover
    function toggleTouchItem($element){
        $element
            .siblings('li')
            .removeClass('open')
            .end()
            .toggleClass('open');
    }

    // Handles hover to open items under collapsed menu
    // -----------------------------------
    function toggleMenuItem($listItem) {

        removeFloatingNav();

        var ul = $listItem.children('ul');

        if( !ul.length ) return $();
        if( $listItem.hasClass('open') ) {
            toggleTouchItem($listItem);
            return $();
        }

        var $aside = $('.aside');
        var $asideInner = $('.aside-inner'); // for top offset calculation
        // float aside uses extra padding on aside
        var mar = parseInt( $asideInner.css('padding-top'), 0) + parseInt( $aside.css('padding-top'), 0);

        var subNav = ul.clone().appendTo( $aside );

        toggleTouchItem($listItem);

        var itemTop = ($listItem.position().top + mar) - $sidebar.scrollTop();
        var vwHeight = $win.height();

        subNav
            .addClass('nav-floating')
            .css({
                position: isFixed() ? 'fixed' : 'absolute',
                top:      itemTop,
                bottom:   (subNav.outerHeight(true) + itemTop > vwHeight) ? 0 : 'auto'
            });

        subNav.on('mouseleave', function() {
            toggleTouchItem($listItem);
            subNav.remove();
        });

        return subNav;
    }

    function removeFloatingNav() {
        $('.sidebar-subnav.nav-floating').remove();
        $('.dropdown-backdrop').remove();
        $('.sidebar li.open').removeClass('open');
    }

    function isTouch() {
        return $html.hasClass('touch');
    }
    function isSidebarCollapsed() {
        return $body.hasClass('aside-collapsed');
    }
    function isSidebarToggled() {
        return $body.hasClass('aside-toggled');
    }
    function isMobile() {
        return $win.width() < mq.tablet;
    }
    function isFixed(){
        return $body.hasClass('layout-fixed');
    }
    function useAsideHover() {
        return $body.hasClass('aside-hover');
    }

})(window, document, window.jQuery);
// TOGGLE STATE
// ----------------------------------- 

(function(window, document, $, undefined){

    $(function(){

        var $body = $('body');
        toggle = new StateToggler();

        $('[data-toggle-state]')
            .on('click', function (e) {
                // e.preventDefault();
                e.stopPropagation();
                var element = $(this),
                    classname = element.data('toggleState'),
                    noPersist = (element.attr('data-no-persist') !== undefined);

                if(classname) {
                    if( $body.hasClass(classname) ) {
                        $body.removeClass(classname);
                        if( ! noPersist)
                            toggle.removeState(classname);
                    }
                    else {
                        $body.addClass(classname);
                        if( ! noPersist)
                            toggle.addState(classname);
                    }

                }
                // some elements may need this when toggled class change the content size
                // e.g. sidebar collapsed mode and jqGrid
                $(window).resize();

            });

    });

    // Handle states to/from localstorage
    window.StateToggler = function() {

        var storageKeyName  = 'jq-toggleState';

        // Helper object to check for words in a phrase //
        var WordChecker = {
            hasWord: function (phrase, word) {
                return new RegExp('(^|\\s)' + word + '(\\s|$)').test(phrase);
            },
            addWord: function (phrase, word) {
                if (!this.hasWord(phrase, word)) {
                    return (phrase + (phrase ? ' ' : '') + word);
                }
            },
            removeWord: function (phrase, word) {
                if (this.hasWord(phrase, word)) {
                    return phrase.replace(new RegExp('(^|\\s)*' + word + '(\\s|$)*', 'g'), '');
                }
            }
        };

        // Return service public methods
        return {
            // Add a state to the browser storage to be restored later
            addState: function(classname){
                var data = $.localStorage.get(storageKeyName);

                if(!data)  {
                    data = classname;
                }
                else {
                    data = WordChecker.addWord(data, classname);
                }

                $.localStorage.set(storageKeyName, data);
            },

            // Remove a state from the browser storage
            removeState: function(classname){
                var data = $.localStorage.get(storageKeyName);
                // nothing to remove
                if(!data) return;

                data = WordChecker.removeWord(data, classname);

                $.localStorage.set(storageKeyName, data);
            },

            // Load the state string and restore the classlist
            restoreState: function($elem) {
                var data = $.localStorage.get(storageKeyName);

                // nothing to restore
                if(!data) return;
                $elem.addClass(data);
            }

        };
    };

})(window, document, window.jQuery);