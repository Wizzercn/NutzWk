## Themes

To use a builtin theme, you must include the theme style sheet, and add the class name to the table to match:

```html
<link rel="stylesheet" href="sortable-theme-light.css" />
<table class="sortable-theme-light" data-sortable>
    <!-- ... -->
</table>
```

At the moment, there are 6 themes:

<table id="exampleTable" class="sortable-theme-light" data-sortable>
<thead>
<tr>
<th data-sortable="false"></th>
<th>Theme</th>
<th>Class name</th>
<th></th>
</tr>
</thead>
<tbody>
<tr><td>1</td><td data-value="0">Minimal</td><td>(No class name necessary)</td><td><a href data-theme="sortable-theme-default">Preview</td></tr>
<tr><td>2</td><td data-value="1">Light</td><td>sortable-theme-light</td><td><a href data-theme="sortable-theme-light">Preview</td></tr>
<tr><td>3</td><td data-value="2">Dark</td><td>sortable-theme-dark</td><td><a href data-theme="sortable-theme-dark">Preview</td></tr>
<tr><td>4</td><td data-value="3">Bootstrap</td><td>sortable-bootstrap</td><td><a href data-theme="sortable-theme-bootstrap">Preview</td></tr>
<tr><td>5</td><td data-value="4">Slick</td><td>sortable-theme-slick</td><td><a href data-theme="sortable-theme-flat-slick">Preview</td></tr>
<tr><td>6</td><td data-value="5">Finder</td><td>sortable-theme-finder</td><td><a href data-theme="sortable-theme-finder">Preview</td></tr>
</tbody>
</table>

You can download a release (containing all 6 themes) [here](/sortable). Or you can download them individually [here](https://github.com/HubSpot/sortable/tree/master/css).

<!-- Resources for the demos -->
<p style="-webkit-transform: translateZ(0)"></p>
<script src="/sortable/js/sortable.js"></script>
<style>
.hs-doc-content table {
    border: 0;
}
.hs-doc-content table th {
    background: initial;
}
</style>
<link rel="stylesheet" href="/sortable/css/sortable-theme-minimal.css" />
<link rel="stylesheet" href="/sortable/css/sortable-theme-light.css">
<link rel="stylesheet" href="/sortable/css/sortable-theme-dark.css">
<link rel="stylesheet" href="/sortable/css/sortable-theme-bootstrap.css">
<link rel="stylesheet" href="/sortable/css/sortable-theme-slick.css">
<link rel="stylesheet" href="/sortable/css/sortable-theme-finder.css">
<script>
$('[data-theme]').each(function(){
    $(this).click(function(e){
        e.preventDefault();
        $('#exampleTable').get(0).className = $(this).data('theme');
        return false;
    });
});
</script>
