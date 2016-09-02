# bootstrap-colorpalette
Simple color palette for Bootstrap

#### [Online Demo](http://extremefe.github.io/bootstrap-colorpalette/)
# Requirements

* [Bootstrap](http://twitter.github.com/bootstrap/) 2.3.2+
* [jQuery](http://jquery.com/) 1.8.1+

# Example

#### Basic
```html
<div class="colorpalette"></div>
```
```javascript
$('.colorpalette').colorPalette();
```
***

#### selectColor event
```html
<input class="selected-color">
<div class="colorpalette"></div>
```
```javascript
$('.colorpalette').colorPalette()
  .on('selectColor', function(e) {
    $('.selected-color').val(e.color);
  });
```
***

#### Gmail editor style
```html
<div class="btn-group">
  <a class="selected-color" class="btn btn-mini dropdown-toggle" data-toggle="dropdown"><i>A</i></a>
  <ul class="dropdown-menu">
    <li style="display:inline-block;">
      <div>&nbsp;font color</div>
      <div class="colorpalette1"></div>
    </li>
    <li style="display:inline-block;">
      <div>&nbsp;background color</div>
      <div class="colorpalette2"></div>
    </li>
  </ul>
</div>
```
```javascript
$('.colorpalette1').colorPalette()
  .on('selectColor', function(e) {
    $('.selected-color i').css('color', e.color);
  });
$('.colorpalette2').colorPalette()
  .on('selectColor', function(e) {
    $('.selected-color i').css('background-color', e.color);
  });
```
***

#### Custom color option
```html
<input class="selected-color">
<div class="colorpalette"></div>
```
```javascript
var options = {
  colors:[['#000000', '#424242', '#636363', '#9C9C94', '#CEC6CE', '#EFEFEF', '#EFF7F7', '#FFFFFF']]
}
$('.colorpalette').colorPalette(options)
  .on('selectColor', function(e) {
    $('.selected-color').val(e.color);
  });
```
