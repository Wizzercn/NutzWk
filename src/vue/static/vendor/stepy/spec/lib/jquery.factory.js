clean = true;

function context(description, spec) {
  describe(description, spec);
};

var Factory = {
  init: function() {
  }, _checks: function(tag, data) {
    var type   = $(tag)[0].type,
        single = tag.indexOf(' />') !== -1;

    if (data.checked) {
      if (type === 'checkbox') {
        tag = tag.replace(' />', ' checked="checked" />');
      } else {
        $.error('You can check just checkbox element!');
      }
    } else if (data.selected) {
      if (type === 'radio') {
        tag = tag.replace(' />', ' selected="selected" />');
      } else if (tag.indexOf('<option') !== -1) {
        tag = tag.replace('>', ' selected="selected">');
      } else {
        $.error('You can select just radio and option element!');
      }
    }

    return tag;
  }, _data: function(options) {
    options = options || {};

    var times      = Factory._getTimes(options),
        html       = Factory._getHtml(options),
        checked    = Factory._getChecked(options),
        selected   = Factory._getSelected(options),
        opt        = Factory._normalize(options),
        attributes = Factory._parameterize(opt);

    return { attributes: attributes, html: html, checked: checked, selected: selected, times: times };
  }, _getChecked: function(options) {
    return options.checked || false;
  }, _getHtml: function(options) {
    var html    = options.html || '',
        content = '';

    if (html instanceof Array) {
      for (var i = 0; i < html.length; i++) {
        content += html[i];
      }
    } else {
      content = html;
    }

    return content;
  }, _getSelected: function(options) {
    return options.selected || false;
  }, _getTimes: function(options) {
    return options.times || 1;
  }, _normalize: function(options) {
    delete options.times;
    delete options.html;
    delete options.selected;
    delete options.checked;

    return options;
  }, _parameterize: function(options) {
    var content = '';

    for (var option in options) {
      content += option + '="' + options[option] + '" ';
    }

    return content.replace(/\s$/, '');
  }, _repeat: function(tag, data) {
    var html = '';

    for (var i = 0; i < data.times; i++) {
      html += Factory._checks(tag, data).replace(/{index}/g, i + 1);
    }

    return html;
  }, _verify: function(options) {
    if (options && options.type) {
      $.error('You cannot set the "type" using an alias!');
    }
  }, append: function(html) {
    return $(html).appendTo('.factory');
  }, checkbox: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'checkbox');
  }, clear: function(args) {
    if (clean) {
      $('.factory').empty();

      if (args instanceof Array) {
        for (var i = 0; i < args.length; i++) {
          $(args[i]).remove();
        }
      } else {
        $(args).remove();
      }
    }
  }, double: function(options, name) {
    var data = Factory._data(options),
        tag  = '<' + name + ' ' + data.attributes + '>' + data.html + '</' + name + '>';

    return Factory._repeat(tag.replace(' >', '>'), data);
  }, fieldset: function(options) {
    return Factory.double(options, 'fieldset');
  }, form: function(options) {
    return Factory.double(options, 'form');
  }, hidden: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'hidden');
  }, html: function(html) {
    $('.factory').html(html);
  }, input: function(options, type) {
    options = options || {};

    if (type) {
      options['type'] = type;
    }

    return Factory.single(options, 'input');
  }, label: function(options) {
    return Factory.double(options, 'label');
  }, legend: function(options) {
    return Factory.double(options, 'legend');
  }, option: function(options) {
    return Factory.double(options, 'option');
  }, password: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'password');
  }, radio: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'radio');
  }, select: function(options) {
    return Factory.double(options, 'select');
  }, single: function(options, name) {
    var data = Factory._data(options),
        tag  = '<' + name + ' ' + data.attributes + ' />';

    return Factory._repeat(tag.replace('  />', ' />'), data);
  }, submit: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'submit');
  }, text: function(options) {
    Factory._verify(options);

    return Factory.input(options, 'text');
  }, textarea: function(options) {
    return Factory.double(options, 'textarea');
  }
};
