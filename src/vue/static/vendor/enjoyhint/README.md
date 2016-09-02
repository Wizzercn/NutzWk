EnjoyHint
=========
**EnjoyHint** is a web-tool that provides the simplest way to create interactive tutorials and hints for your site or web-application. It can also be used to highlight and sign application elements.  

EnjoyHint is free software distributed under the terms of MIT license.
  
#### Demo
* [TODO app demo](http://xbsoftware.github.io/enjoyhint/) ([downloadable package](http://xbsoftware.github.io/enjoyhint/enjoyhint_todo_demo.zip))
* [A small guide on EnjoyHint](http://xbsoftware.github.io/enjoyhint/example1.html)

#### Dependencies
EnjoyHint require the following plugins and libs:

* jQuery > 1.7
* KineticJS v5.1.0 (included into js file)

#### Installation
You can install it through `bower` package manager:
```
bower install enjoyhint
```
Alternative way:
- Download the latest version of EnjoyHint
- Extract the archive with EnjoyHint.
- Move the EnjoyHint directory to somewhere on your webserver
- Insert next lines into your page's \<head\> tag:
```html
  <link href="<pathontheserver>/enjoyhint/enjoyhint.css" rel="stylesheet">
  <script src="<pathontheserver>/enjoyhint/enjoyhint.min.js"></script>
```

#### Initialization and configuration:
```javascript
//initialize instance
var enjoyhint_instance = new EnjoyHint({});

//simple config. 
//Only one step - highlighting(with description) "New" button 
//hide EnjoyHint after a click on the button.
var enjoyhint_script_steps = [
  {
    'click .new_btn' : 'Click the "New" button to start creating your project'
  }  
];

//set script config
enjoyhint_instance.set(enjoyhint_script_steps);

//run Enjoyhint script
enjoyhint_instance.run();
```

#### Script Configuration
The sequence of steps can be only linear for now. So, the script config is an array. Every element of this array is the config for some step.

#### Example of script configuration 
Highlight some button and after you click on it, highlight some panel:
```javascript
var enjoyhint_script_steps = [
  {
    'click .some_btn' : 'Click on this btn'
  },  
  {
    'click .some_panel' : 'Click on this panel'
  }  
];
```

#### Properties of the step configuration
* `"event selector" : "description"` - to describe a step you should set an event type, selecte element and add description for this element (hint)
* `keyCode` - the code of a button, which triggers the next EnjoyHint step upon a click. Defined by the “key” event. (“key #block” : “hello”).
* `event_selector` - if you need to attach an event (that was set in "event" property) to other selector, you can use this one  
* `timeout` - delay before the moment, when an element is highlighted   
* `shape` - shape for highlighting (circle|rect)
* `radius` -  if the shape of "circle" is specified, we can set the radius.
* `margin` - margin for the highlight shape (for Ex.:10)  
* `top` - top margin for the shape of "rect" type  
* `right` - right margin for the shape of "rect" type  
* `bottom` - bottom margin for the shape of "rect" type  
* `left` - left margin for the shape of "rect" type
* `scrollAnimationSpeed` - sets the auto scroll speed (ms).
* `nextButton` - allows applying its classes and names for the button Nеxt.
* `skipButton` - allows applying its classes and names for the button Skip. For the example :
```javascript
	var options = {
                    "next #block": 'Hello.',
                    "nextButton" : {className: "myNext", text: "NEXT"},
                    "skipButton" : {className: "mySkip", text: "SKIP"},

                }
  ```
* `showSkip` - shows or hides the Skip button (true|false)
* `showNext` - shows or hides the Next button (true|false)




#### Non-standard events:
**auto** - for example, you need to click on the same button on the second step imediatelly after the first step and go to the next step after it. Then you can use "auto" in the "event_type" property and "click" in "event" property.
* `custom` - this value is very usefull if you need to go to the next step by event in your app code. For example, you want to go to the next step only after some data have been loaded in your application. Then you should use the "custom" event_type and the "trigger" method of the EnjoyHint instance.  
```javascript
//Example of using custom event_type
$.get('/load/some_data', function(data){
  //trigger method has only one argument: event_name.(equal to the value of event property in step config)
  enjoyhint_instance.trigger('custom_event_name');
});
```  
* `next` - when you set value of event_type to "next", you will see the "Next" btn on this step.
* `key` - tells EnjoyHint to go to the next step when you click on the button defined by the keyCode


#### Methods
* `set` - set current steps configuration. Arguments: config  
* `run` - run the current script. Has no arguments  
* `resume` - resume the script from the step where it was stopped. Has no arguments  
* `getCurrentStep` - returns the current step index  

#### Events
**Script Events**:
* `onStart` - fires on the first step.
* `onEnd` - fires after the last step in script.
```javascript
var enjoyhint_instance = new EnjoyHint({
  onStart:function(){
    //do something
  }
});
```
**Step Events**:  
* `onBeforeStart` - fires before the step is started.
```javascript
var enjoyhint_script_steps = [
  {
    selector:'.some_btn',//jquery selector
    event:'click',
    description:'Click on this btn',
    onBeforeStart:function(){
      //do something
    }
  }
];
```

#### Release notes

##### v.3

* New and simplified description of EnjoyHint steps
* Auto scroll to the element
* Possibility to hide or display the buttons showNext, showSkip.
* HTML usage allowed in description 
* Destructor
* Simplified property names 
* Grunt  to compress and merge files 
* New examples
* You can learn the step you are on by the class enjoyhint-step-* ( where * stands for the step number).




