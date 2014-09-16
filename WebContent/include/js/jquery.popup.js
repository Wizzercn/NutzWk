//example:
//$('<p>some tooltip here.</p>').popup('this is title.', $('#source'), {widht:200}, {close:'timeout'});
//
//title
//	    string, title text of popup
//element
//	    element, the element that popup's position is based on
//location
//	width, height
//      integer, size of the popup
//	space
//      integer, space between element and popup
//	vertical
//      string, one of 'default', 'below', 'above', 'middle'
//	horizontal
//      string, one of 'left', 'right', 'center', 'default'
//closeType
//	close
//      string, one of 'timeout', 'mouseout', 'click', 'closeButton'
//	delay
//      integer, when using 'timeout', how long before hiding
//  element
//      element, when using 'mouseout', 'click', the event source element
//classes
//	popup
//      string, class of the whole popup
//	title
//      string, class of the title
//  closeButton
//      string, class of the close button
//  content
//      string, class of tooltip content

(function($)
{
    $.fn.popup = function(title, element, location, closeType, classes)
    {
        if(!element.popupinstance)
        {
            popup.count++;
            var content = this.html();
            if(!classes)
                classes={popup:'popup',title:'title',closeButton:'closeButton',content:'content'};
                
            var pop = new popup(popup.count, content, title, element, location, closeType, classes);
            pop.show();
        }
    }
    
    function popup(id, content, title, element, location, closeType, classes)
    {
        this.id = id;
        this.element = element;
        this.location = location;
        this.content = content;
        this.closeType = closeType;
        this.classes = classes;
        this.title = title;
        this.closeElement = closeType.element || element;
        
        this.show = function()
        {
            if(this.element[0].popupinstance)
                return;
            this.element[0].popupinstance=true;
            this.position = this.getPositon();
            var html = this.getPopupHtml();
            $(html).appendTo($('body'));
            this.setClose();
        }
        
        this.getPopupHtml = function()
        {
            var css='';
            for(var attr in this.position)
                css+=attr+':'+this.position[attr]+'px;';
            css += 'display:block;position:absolute;z-index:100;';
            
            var pTitle='';
            if(title || closeType.close == 'closeButton')
            {
                var titleCss ='';
                pTitle = '<div';
                if(classes.title)
                    pTitle+=' class="'+classes.title+'"';
                pTitle+=' style="' +titleCss +'">'
                
                var closeCss ='float:right;cursor:pointer;font-family:arial, "lucida console", sans-serif;';
                if(closeType.close=="closeButton")
                {
                    pTitle +='<span';
                    if(classes.closeButton)
                        pTitle+= ' class="' +classes.closeButton +'"';
                    pTitle +=' style="' +closeCss +'" id="'+this.getCloseButtonId() +'"> X </span>';
                }
                    
                pTitle += title+'<div style="clear:both" /></div>';
            }
            
            var c='<div id="'+this.getContentId()+'"'
            if(classes.content)
                c +=' class="'+classes.content+'"';
            c += '>' +content +'</div>';
            var result= '<div';
            if(classes.popup)
                result+=' class="'+classes.popup +'"';
            result+=' id="' +this.getPopupId() +'" style="' + css +'">' +pTitle +c +'</div>';
            return result;
        }
        
        this.getPositon = function()
        {
            var offset=element.offset();
            var result={};
            if(location.width)
                result.width=location.width;
            if(location.height)
                result.height=location.height;
            var space= location.space || 0;
            var vertical = location.vertical || 'default';
            var horizontal = location.horizontal || 'default';
            switch(vertical)
            {
                case 'default':
                case 'below':
                    result.top = offset.top + element.height() + space;
                    break;
                case 'above':
                    result.top = offset.top - space - (result.height||0);
                    if(result.top<0)
                        result.top=0;
                    break;
                case 'middle':
                    result.top = offset.top - (result.height -element.height())/2 + space;
                    if(result.top<0)
                        result.top=0;
                    break;
                default:
                    break;
            }
            switch(horizontal)
            {
                case 'left':
                    result.left = offset.left - space - (result.width||0);
                    if(result.left < 0)
                        result.left=0;
                    break;
                case 'right':
                    result.left = offset.left + element.width() + space - (result.width||0);
                    if(result.left+(result.width||0) > $(window).width())
                        result.left = $(window).width - (result.width||0);
                    break;
                case 'center':
                    result.left = offset.left + (element.width() - result.width)/2;
                    break;
                case 'default':
                    result.left = offset.left;
                    break;
                default:
                    break;
            }
            return result;
        }
        
        this.setClose = function()
        {
            var id ='#'+this.getPopupId();
            var closeId ='#'+this.getCloseButtonId();
            var delay =closeType.delay || 0;
            var closeElement = this.closeElement;
            var element = this.element;
            if(closeType.close=='timeout')
            {
                window.setTimeout(hidePopup, delay);
            }
            else if(closeType.close=='mouseout')
            {
                element.mouseout(function()
                {
                    window.setTimeout(hidePopup, delay);
                });
            }
            else if(closeType.close=='click')
            {
                closeElement.click(hidePopup);
            }
            else if(closeType.close=='closeButton')
            {
                $(closeId).click(hidePopup);
            }
            function hidePopup()
            {
                $(closeId).unbind('click', hidePopup);
                $(closeElement).unbind('click',hidePopup);
                $(id).fadeOut('slow');
                element[0].popupinstance=false;
            }
        }
        
        
        this.getPopupId = function()
        {
            return 'popup'+this.id;
        }

        this.getCloseButtonId = function()
        {
            return 'close'+this.id;
        }

        this.getContentId = function()
        {
            return 'content'+this.id;
        }
    }
    popup.count=0;
})(jQuery);