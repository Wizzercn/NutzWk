(function($) {

    $.fn.onScreen = function(options) {
        
        var params = {
            container: window,
            direction: 'vertical',
            toggleClass: null,
            doIn: null,
            doOut: null,
            tolerance: 0,
            throttle: null,
            lazyAttr: null,
            lazyPlaceholder: 'data:image/gif;base64,R0lGODlhEAAFAIAAAP///////yH/C05FVFNDQVBFMi4wAwEAAAAh+QQJCQAAACwAAAAAEAAFAAACCIyPqcvtD00BACH5BAkJAAIALAAAAAAQAAUAgfT29Pz6/P///wAAAAIQTGCiywKPmjxUNhjtMlWrAgAh+QQJCQAFACwAAAAAEAAFAIK8urzc2tzEwsS8vrzc3tz///8AAAAAAAADFEiyUf6wCEBHvLPemIHdTzCMDegkACH5BAkJAAYALAAAAAAQAAUAgoSChLS2tIyKjLy+vIyOjMTCxP///wAAAAMUWCQ09jAaAiqQmFosdeXRUAkBCCUAIfkECQkACAAsAAAAABAABQCDvLq83N7c3Nrc9Pb0xMLE/P78vL68/Pr8////AAAAAAAAAAAAAAAAAAAAAAAAAAAABCEwkCnKGbegvQn4RjGMx8F1HxBi5Il4oEiap2DcVYlpZwQAIfkECQkACAAsAAAAABAABQCDvLq85OLkxMLE9Pb0vL685ObkxMbE/Pr8////AAAAAAAAAAAAAAAAAAAAAAAAAAAABCDwnCGHEcIMxPn4VAGMQNBx0zQEZHkiYNiap5RaBKG9EQAh+QQJCQAJACwAAAAAEAAFAIOEgoTMysyMjozs6uyUlpSMiozMzsyUkpTs7uz///8AAAAAAAAAAAAAAAAAAAAAAAAEGTBJiYgoBM09DfhAwHEeKI4dGKLTIHzCwEUAIfkECQkACAAsAAAAABAABQCDvLq85OLkxMLE9Pb0vL685ObkxMbE/Pr8////AAAAAAAAAAAAAAAAAAAAAAAAAAAABCAQSTmMEGaco8+UBSACwWBqHxKOJYd+q1iaXFoRRMbtEQAh+QQJCQAIACwAAAAAEAAFAIO8urzc3tzc2tz09vTEwsT8/vy8vrz8+vz///8AAAAAAAAAAAAAAAAAAAAAAAAAAAAEIhBJWc6wJZAtJh3gcRBAaXiIZV2kiRbgNZbA6VXiUAhGL0QAIfkECQkABgAsAAAAABAABQCChIKEtLa0jIqMvL68jI6MxMLE////AAAAAxRoumxFgoxGCbiANos145e3DJcQJAAh+QQJCQAFACwAAAAAEAAFAIK8urzc2tzEwsS8vrzc3tz///8AAAAAAAADFFi6XCQwtCmAHbPVm9kGWKcEQxkkACH5BAkJAAIALAAAAAAQAAUAgfT29Pz6/P///wAAAAIRlI8SAZsPYnuJMUCRnNksWwAAOw==',
            debug: false
        };

        if (options !== 'remove') {
            $.extend(params, options);
        }

        return this.each(function() {

            var self = this;
            var isOnScreen = false; // Initialize boolean
            var scrollTop; // Initialize Vertical Scroll Position
            var scrollLeft; // Initialize Horizontal Scroll Position
            var $el = $(this); // Matched element

            // Initialize Viewport dimensions
            var $container;
            var containerHeight;
            var containerWidth;
            var containerBottom;
            var containerRight;

            // Initialize element dimensions & position
            var elHeight;
            var elWidth;
            var elTop;
            var elLeft;

            // Checks if params.container is the Window Object
            var containerIsWindow = $.isWindow(params.container);

            // Remove bindings from onScreen container
            function remove() {
                $(self).off('scroll.onScreen resize.onScreen');
                $(window).off('resize.onScreen');
            };

            function verticalIn() {
                if (containerIsWindow) {
                    return elTop < containerBottom - params.tolerance &&
                        scrollTop < (elTop + elHeight) - params.tolerance;
                } else {
                    return elTop < containerHeight - params.tolerance &&
                        elTop > (-elHeight) + params.tolerance;
                }
            }

            function verticalOut() {
                if (containerIsWindow) {
                    return elTop + (elHeight - params.tolerance) < scrollTop ||
                        elTop > containerBottom - params.tolerance;
                } else {
                    return elTop > containerHeight - params.tolerance ||
                        -elHeight + params.tolerance > elTop;
                }
            }

            function horizontalIn() {
                if (containerIsWindow) {
                    return elLeft < containerRight - params.tolerance &&
                        scrollLeft < (elLeft + elWidth) - params.tolerance;
                } else {
                    return elLeft < containerWidth - params.tolerance &&
                        elLeft > (-elWidth) + params.tolerance;
                }
            }

            function horizontalOut() {
                if (containerIsWindow) {
                    return elLeft + (elWidth - params.tolerance) < scrollLeft ||
                        elLeft > containerRight - params.tolerance;
                } else {
                    return elLeft > containerWidth - params.tolerance ||
                        -elWidth + params.tolerance > elLeft;
                }
            }

            function directionIn() {
                if (isOnScreen) {
                    return false;
                }

                if (params.direction === 'horizontal') {
                    return horizontalIn();
                } else {
                    return verticalIn();
                }
            }

            function directionOut() {
                if (!isOnScreen) {
                    return false;
                }

                if (params.direction === 'horizontal') {
                    return horizontalOut();
                } else {
                    return verticalOut();
                }
            }

            function throttle(fn, timeout, ctx) {
                var timer, args, needInvoke;
                return function() {
                    args = arguments;
                    needInvoke = true;
                    ctx = ctx || this;
                    if (!timer) {
                        (function() {
                            if (needInvoke) {
                                fn.apply(ctx, args);
                                needInvoke = false;
                                timer = setTimeout(arguments.callee, timeout);
                            } else {
                                timer = null;
                            }
                        })();
                    }

                };

            }

            if (options === 'remove') {
                remove();
                return;
            }

            var checkPos = function() {
                // Make container relative
                if (!containerIsWindow && $(params.container).css('position') === 'static') {
                    $(params.container).css('position', 'relative');
                }

                // Update Viewport dimensions
                $container = $(params.container);
                containerHeight = $container.height();
                containerWidth = $container.width();
                containerBottom = $container.scrollTop() + containerHeight;
                containerRight = $container.scrollLeft() + containerWidth;

                // Update element dimensions & position
                elHeight = $el.outerHeight(true);
                elWidth = $el.outerWidth(true);

                if (containerIsWindow) {
                    var offset = $el.offset();
                    elTop = offset.top;
                    elLeft = offset.left;
                } else {
                    var position = $el.position();
                    elTop = position.top;
                    elLeft = position.left;
                }

                // Update scroll position
                scrollTop = $container.scrollTop();
                scrollLeft = $container.scrollLeft();

                // This will spam A LOT of messages in your console
                if (params.debug) {
                    console.log(
                        'Container: ' + params.container + '\n' +
                        'Width: ' + containerHeight + '\n' +
                        'Height: ' + containerWidth + '\n' +
                        'Bottom: ' + containerBottom + '\n' +
                        'Right: ' + containerRight
                    );
                    console.log(
                        'Matched element: ' + ($el.attr('class') !== undefined ? $el.prop('tagName').toLowerCase() + '.' + $el.attr('class') : $el.prop('tagName').toLowerCase()) + '\n' +
                        'Left: ' + elLeft + '\n' +
                        'Top: ' + elTop + '\n' +
                        'Width: ' + elWidth + '\n' +
                        'Height: ' + elHeight
                    );
                }

                if (directionIn()) {
                    if (params.toggleClass) {
                        $el.addClass(params.toggleClass);
                    }
                    if ($.isFunction(params.doIn)) {
                        params.doIn.call($el[0]);
                    }
                    if (params.lazyAttr && $el.prop('tagName') === 'IMG') {
                        var lazyImg = $el.attr(params.lazyAttr);
                        if (lazyImg !== $el.prop('src')) {
                            $el.css({
                                background: 'url(' + params.lazyPlaceholder + ') 50% 50% no-repeat',
                                minHeight: '5px',
                                minWidth: '16px'
                            });

                            $el.prop('src', lazyImg).load(function() {
                                $(this).css({
                                    background: 'none'
                                });
                            });
                        }
                    }
                    isOnScreen = true;
                } else if (directionOut()) {
                    if (params.toggleClass) {
                        $el.removeClass(params.toggleClass);
                    }
                    if ($.isFunction(params.doOut)) {
                        params.doOut.call($el[0]);
                    }
                    isOnScreen = false;
                }
            };

            if (window.location.hash) {
                throttle(checkPos, 50);
            } else {
                checkPos();
            }

            if (params.throttle) {
                checkPos = throttle(checkPos, params.throttle);
            }

            // Attach checkPos
            $(params.container).on('scroll.onScreen resize.onScreen', checkPos);

            // Since <div>s don't have a resize event, we have
            // to attach checkPos to the window object as well
            if (!containerIsWindow) {
                $(window).on('resize.onScreen', checkPos);
            }

            // Module support
            if (typeof module === 'object' && module && typeof module.exports === 'object') {
                // Node.js module pattern
                module.exports = jQuery;
            } else {
                // AMD
                if (typeof define === 'function' && define.amd) {
                    define('jquery-onscreen', [], function() {
                        return jQuery;
                    });
                }
            }

        });
    };

})(jQuery);