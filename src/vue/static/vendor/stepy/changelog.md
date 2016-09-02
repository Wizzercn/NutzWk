# jQuery Stepy - A Wizard Plugin - http://wbotelhos.com/stepy

### Next Release

## News

+ Option `enter` was extracted to choose if the enter key will trigger the step change;
+ Added function `destroy` to rollback to original object before the bind.

## Changes

+ The container that holds the navigation buttons now has the class 'stepy-buttons';
+ The error container class was renamed to plural 'stepy-errors';
+ Forms without ID will receives a genarated hash to be used as hook;
+ The 'stepy-titles' class was renamed to 'stepy-header';
+ The 'stepy-{id}-titles' ID was renamed to 'stepy-{id || hash}-header';
+ The 'step' class was renamed to 'stepy-step';
+ The '{id}-titles-{index}' was renamed to '{id}-head-{index}';
+ All callbacks now receives the variable `this` as DOM element instead jQuery;
+ The 'current-step' class was renamed to 'stepy-active';
+ The finish button now should to have the class 'stepy-finish' instead just 'finish';
+ If you have a submit type button, then you don't need to set the class 'stepy-finish'.

## Fixes

+ Fields inside elements was not working the enter action.

## 1.1.0

+ Added 'ignore' option to choose the fields to be ignored on validation:
	- It must be blank '' to override jQuery Validation 1.9.0 default value and avoid error;
	- I hope my fix be accepted, then this option may be used freely;
	- But, you never can use ':hidden' as the do, because Wizards needs to validate hidden steps/fields. (;
+ Fixed the index number given to Stepy binded by class without id.

## 1.0.0

+ Now we using the best pratice to build the plugin;
+ Now the plugin is under test with Jasmine and Jasmine jQuery;
+ Now public functions is on context scope instead on $.fn;
+ Added support to validation to the function .stepy('step', number);
+ Added the 'select' callback to execute action when a step is displayed; (by Daniel Hoelbling)
+ Fixed the several invocation of select step on each validation;
+ Fixed validation to disabled fields do not validate;
+ Fixed validations when jQuery Validation returns undefined and blocks; (by Rafael Machado)
+ Better look of documentations using markdown. (by Almir Mendes)

## 0.5.1

+ Fixed the focus of the first error field (reported by luckyong);
+ Avoided highlight only the first field when has multiple invalids fields on the form.

## 0.5.0

+ Added the 'titleTarget' attribute to choose the place where titles will be placed;
+ Added the 'legend' attribute to choose if the legends of the steps will be showed;
+ Added the 'description' attribute to choose if the descriptions of the titles will be showed;
+ Added the 'finish' callback attribute that enables action before finish the steps;
+ Fixed the possible bug when the ID has more than one word or has number;
+ Fixed the possible bug when the ID has more than one word or has number;
+ Fixed: avoided the validation of the lastest step when the validation is not enabled;
+ Fixed: when there is only one step, the finish button does not appear; (reported by DevPHP)
+ The 'onBack' attribute was named just to 'back';
+ The 'onNext' attribute was named just to 'next';
+ The 'finish' attribute was named to 'finishButton';
+ The 'back' and 'next' callback no longer has a default function;
+ The finish button is now required for any kind of step if that option is enabled;
+ Keeped the options as data called 'options' in the wrapper element to future usages.

## 0.4.1

+ Fixed: the validation highlighted only the first field. (fixed by legigor)

## 0.4.0

+ Now the enter key goes to next step and just validate the currrent one; (reported by hennaTheGreat)
+ Public functions now supports actions by class; (reported by hennaTheGreat)
+ Public functions without specify ID or class is no longer supported;
+ Now when you to press the enter key into any text field, the form will go to next step;
+ Fixed: when the stepy has only one step the next button is not applied;
+ Fixed the title click when the number is higher than 10; (reported by neha1312)
+ Added ID to the set of titles to be possible to move the titles;
+ Added class to buttons container to simplify styling. (fixed by andreyfedoseev)

## 0.3.0

+ Added the 'onBack' attribute that enables action before the backward transition;
+ Added the 'onNext' attribute that enables action before the forward transition;

## 0.2.0

+ Added the 'block' attribute that blocks the next step if the current is invalid;
+ Added the 'errorImage' attribute that show a imagem in the title of the corresponding step if an error occur;
+ Now you can pass a optionally ID to be the target of the public function's actions;
+ All the code was refactored and improved.

## 0.1

+ Added a backLabel and nextLabel parameter to specify the labels of the corresponding buttons;
+ Added click on the title and description to go to the corresponding step;
+ Added a public function 'step' that is possible choose which step to go;
+ Added auto-focus in the first field of each step when it is the current;
+ Added the parameter includeFinish to format and embed the button with class named ".finish" to the last step;
+ Added function that automatically takes the title of the fieldset and become it as title of each step;
+ Now is possible to use more than one wizard on the same page;
+ Created default options;
+ Changed the function bind to newest live;
+ The links with # were changed to javascript:void(0) to prevent the page go up;
+ Changed to HTML code the special characters;
+ Removed the container tha was wrapped the fieldsets and added the actions directly on it;
+ Removed unused arguments form binds functions;
+ Changed the selectors for the same current element $(this);
+ Changed the extends to a defaults developers plugins;
+ Keys were placed on single line blocks to avoid error on IE;
+ Changed the styles name with a hifen style;
