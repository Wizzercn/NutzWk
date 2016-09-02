describe('Stepy', function() {
  beforeEach(function() {
    Factory.append(Factory.form({ id: 'stepy', html: [
      Factory.fieldset({ title: 'Step 1', html: [
        Factory.legend({ html: 'description 1' }),
        Factory.label({ html: 'User' }),
        Factory.hidden({ name: 'hidden' }),
        Factory.text({ value: 'wbotelhos', name: 'user', disabled: true }),

        Factory.label({ html: 'E-mail' }),
        Factory.text({ name: 'email' }),

        Factory.label({ html: 'Checked?' }),
        Factory.checkbox({ name: 'checked' }),

        Factory.label({ html: 'Newsletter?' }),
        Factory.label({ html: 'Yep' }),
        Factory.radio({ name: 'newsletter' }),
        Factory.label({ html: 'Nop' }),
        Factory.radio({ name: 'newsletter' }),

        Factory.label({ html: 'Password' }),
        Factory.password({ name: 'password' })
      ]}), Factory.fieldset({ title: 'Step 2', html: [
        Factory.legend({ html: 'description 2' }),
        Factory.label({ html: 'Bio' }),
        Factory.textarea({ name: 'bio' })
      ]}), Factory.fieldset({ title: 'Step 3', html: [
        Factory.legend({ html: 'description 3' }),
        Factory.label({ html: 'Birthday' }),
        Factory.select({ name: 'day', html: [Factory.option(), Factory.option({ html: 23 })]}),
        Factory.label({ html: 'Site' }),
        Factory.text({ name: 'site' })
      ]}),
      Factory.submit()
    ]}));
  });

  afterEach(function() { Factory.clear(); });

  it ('is chainable', function() {
    // given
    var self = $('form');

    // when
    var ref = self.stepy();

    // then
    expect(ref).toBe(self);
  });

  it ('has the right values', function() {
    // given
    var stepy = $.fn.stepy

    // when
    var opt = stepy.defaults

    // then
    expect(opt.bacck).toBeUndefined();
    expect(opt.backLabel).toEqual('&lt; Back');
    expect(opt.block).toBeFalsy();
    expect(opt.description).toBeTruthy();
    expect(opt.duration).toBeUndefined();
    expect(opt.enter).toBeTruthy();
    expect(opt.errorImage).toBeFalsy();
    expect(opt.finish).toBeUndefined();
    expect(opt.finishButton).toBeTruthy();
    expect(opt.ignore).toEqual('');
    expect(opt.legend).toBeTruthy();
    expect(opt.next).toBeUndefined();
    expect(opt.nextLabel).toEqual('Next &gt;');
    expect(opt.select).toBeUndefined();
    expect(opt.titleClick).toBeFalsy();
    expect(opt.titleTarget).toBeUndefined();
    expect(opt.transition).toBeUndefined();
    expect(opt.validate).toBeFalsy();
  });

  it ('receives the bind indicator', function() {
    // given
    var self = $('form');

    // when
    self.stepy();

    // then
    expect(self.data('stepy')).toBeTruthy();
  });

  describe('header', function() {
    it ('is created', function() {
      // given
      var self = $('form');

      // when
      self.stepy();

      // then
      expect($('#' + self.attr('id') + '-header')).toExist();
    });

    it ('starts with the first actived', function() {
      // given
      var self = $('form');

      // when
      self.stepy();

      // then
      var menus = $('#' + self.attr('id') + '-header').children('li');

      expect(menus.eq(0)).toHaveClass('stepy-active');
      expect(menus.eq(1)).not.toHaveClass('stepy-active');
      expect(menus.eq(2)).not.toHaveClass('stepy-active');
    });

    describe('titles', function() {
      it ('is created', function() {
        // given
        var self = $('form').stepy();

        // when
        var menus  = $('#' + self.attr('id') + '-header').children('li');

        // then
        expect(menus.eq(0).children('div')).toHaveHtml('Step 1');
        expect(menus.eq(1).children('div')).toHaveHtml('Step 2');
        expect(menus.eq(2).children('div')).toHaveHtml('Step 3');
      });

      it ('has the click disabled', function() {
        // given
        var self  = $('form').stepy(),
            steps = self.children();

        // when
        $('#' + self.attr('id') + '-header').children('li:eq(1)').click();

        // then
        expect(steps.eq(0)).toBeVisible();
        expect(steps.eq(1)).toBeHidden();
        expect(steps.eq(2)).toBeHidden();
      });
    });

    describe('descriptions', function() {
      it ('is created', function() {
        // given
        var self = $('form').stepy();

        // when
        var menus = $('#' + self.attr('id') + '-header').children('li');

        // then
        expect(menus.eq(0).children('span')).toHaveHtml('description 1');
        expect(menus.eq(1).children('span')).toHaveHtml('description 2');
        expect(menus.eq(2).children('span')).toHaveHtml('description 3');
      });
    });
  });

  describe('body', function() {
    describe('steps', function() {
      describe('fields', function() {
        it ('focused the first', function() {
          // given
          var self  = $('form'),
              steps = self.children('fieldset');

          // when
          self.stepy();

          // then
          expect(steps.first().find(':input:enabled:visible:first')).toBeFocused();
        });
      });

      describe('first', function() {
        it ('starts actived', function() {
          // given
          var self  = $('form'),
              steps = self.children();

          // when
          self.stepy();

          // then
            expect(steps.eq(0)).toBeVisible();
            expect(steps.eq(1)).toBeHidden();
            expect(steps.eq(2)).toBeHidden();
        });

        describe('navigator', function() {
          it ('has the next button', function() {
            // given
            var self = $('form'),
                step = self.children('fieldset:first');

            // when
            self.stepy();

            // then
            var nav = step.children('.stepy-navigator');

            expect(step).toContain('p.stepy-navigator');
            expect(nav).not.toContain('.button-back');
            expect(nav).toContain('.button-next');
          });

          it ('has the right labels', function() {
            // given
            var self = $('form'),
                step = self.children('fieldset:first');

            // when
            self.stepy();

            // then
            expect(step.find('.button-next')).toHaveHtml('Next &gt;');
          });

          context('clicking on next', function() {
            it ('goes to second step', function() {
              // given
              var self  = $('form').stepy(),
                  steps = self.children();

              // when
              steps.eq(0).find('.button-next').click();

              // then
              expect(steps.eq(0)).toBeHidden();
              expect(steps.eq(1)).toBeVisible();
              expect(steps.eq(2)).toBeHidden();
            });

            it ('focus the first field', function() {
              // given
              var self  = $('form').stepy(),
                  steps = self.children('fieldset');

              // when
              steps.eq(0).find('.button-next').click();

              // then
              expect(steps.eq(1).find(':input:enabled:visible:first')).toBeFocused();
            });
          });
        });
      });

      describe('middle', function() {
        describe('navigator', function() {
          it ('has the back and next button', function() {
            // given
            var self = $('form'),
                step = self.children('fieldset:eq(1)');

            // when
            self.stepy();

            // then
            var nav = step.children('.stepy-navigator');

            expect(step).toContain('.stepy-navigator');
            expect(nav).toContain('.button-back');
            expect(nav).toContain('.button-next');
          });

          it ('has the right labels', function() {
            // given
            var self = $('form'),
                step = self.children('fieldset:eq(1)');

            // when
            self.stepy();

            // then
            expect(step.find('.button-back')).toHaveHtml('&lt; Back');
            expect(step.find('.button-next')).toHaveHtml('Next &gt;');
          });

          context('clicking on next', function() {
            it ('goes to third step', function() {
              // given
              var self  = $('form').stepy(),
                  steps = self.children();

              // when
              steps.eq(0).find('.button-next').click();
              steps.eq(1).find('.button-next').click();

              // then
              expect(steps.eq(0)).toBeHidden();
              expect(steps.eq(1)).toBeHidden();
              expect(steps.eq(2)).toBeVisible();
            });
          });

          context('clicking on back', function() {
            it ('goes to first step', function() {
              // given
              var self  = $('form').stepy(),
                  steps = self.children();

              steps.eq(0).find('.button-next').click();

              // when
              steps.eq(1).find('.button-back').click();

              // then
              expect(steps.eq(0)).toBeVisible();
              expect(steps.eq(1)).toBeHidden();
              expect(steps.eq(2)).toBeHidden();
            });

            it ('focus the first field', function() {
              // given
              var self  = $('form').stepy(),
                  steps = self.children('fieldset');

              steps.eq(0).find('.button-next').click();

              // when
              steps.eq(1).find('.button-back').click();

              // then
              expect(steps.eq(0).find(':input:enabled:visible:first')).toBeFocused();
            });
          });
        });
      });
    });

    describe('last', function() {
      it ('has the back', function() {
        // given
        var self = $('form'),
            step = self.children('fieldset:last');

        // when
        self.stepy();

        // then
        var nav = step.children('.stepy-navigator');

        expect(step).toContain('.stepy-navigator');
        expect(nav).toContain('.button-back');
        expect(nav).not.toContain('.button-next');
      });

      it ('has the right labels', function() {
        // given
        var self = $('form'),
            step = self.children('fieldset:last');

        // when
        self.stepy();

        // then
        expect(step.find('.button-back')).toHaveHtml('&lt; Back');
      });

      it ('has the finish button', function() {
        // given
        var self = $('form'),
            step = self.children('fieldset:last');

        // when
        self.stepy();

        // then
        expect(step.find(':submit')).toExist();
      });

      context('clicking on back', function() {
        it ('goes to first step', function() {
          // given
          var self  = $('form').stepy(),
              steps = self.children();

          // when
          steps.eq(0).find('.button-next').click();
          steps.eq(1).find('.button-next').click();
          steps.eq(2).find('.button-back').click();

          // then
          expect(steps.eq(0)).toBeHidden();
          expect(steps.eq(1)).toBeVisible();
          expect(steps.eq(2)).toBeHidden();
        });
      });
    });
  });

  describe('options', function() {
    describe('back', function() {
      it ('is called on trigger back button', function() {
        // given
        var self = $('form').stepy({
          back: function() {
            $(this).data('called', true);
          }
        }),
        steps     = self.children('fieldset');

        steps.eq(0).find('.button-next').click();

        // when
        steps.eq(1).find('.button-back').click();

        // then
        expect(self.data('called')).toBeTruthy();
      });

      it ('receives the right index', function() {
        // given
        var self  = $('form').stepy({ back: function(index) { $(this).data('index', index); } }),
            steps = self.children('fieldset');

        steps.eq(0).find('.button-next').click();

        // when
        steps.eq(1).find('.button-back').click();

        // then
        expect(self.data('index')).toEqual(1);
      });
    });

    describe('backLabel', function() {
      it ('changes the back button label', function() {
        // given
        var self  = $('form'),
            steps = self.children('fieldset');

        // when
        self.stepy({ backLabel: '&lt;&lt;' });

        // then
        expect(steps.eq(1).find('.button-back')).toHaveHtml('&lt;&lt;');
        expect(steps.eq(2).find('.button-back')).toHaveHtml('<<');
      });
    });

    describe('description', function() {
      context('disabled', function() {
        it ('is not created', function() {
          // given
          var self = $('form');

          // when
          self.stepy({ description: false });

          // then
          var menus = $('#' + self.attr('id') + '-header').children('li');

          expect(menus.eq(0)).not.toContain('span');
          expect(menus.eq(1)).not.toContain('span');
          expect(menus.eq(2)).not.toContain('span');
        });
      });
    });

    describe('enter', function() {
      context('enabled', function() {
        context('with valid step', function() {
          it ('goes to the next step', function() {
            // given
            var self  = $('form').stepy({ enter: true, validate: false }),
                steps = self.children('fieldset'),
                evt   = $.Event('keypress');

              evt.which   = 13;
              evt.keyCode = 13;

            // when
            steps.eq(0).children('input:visible:last').trigger(evt);

            // then
            expect(steps.eq(0)).toBeHidden();
            expect(steps.eq(1)).toBeVisible();
            expect(steps.eq(2)).toBeHidden();
          });

          it ('focus the first field', function() {
            // given
            var self  = $('form').stepy({ enter: true, validate: false }),
                steps = self.children('fieldset')
                evt   = $.Event('keypress');

            evt.which   = 13;
            evt.keyCode = 13;

            // when
            steps.eq(0).children('input:visible:last').trigger(evt);

            // then
            expect(steps.eq(1).find(':input:enabled:visible:first')).toBeFocused();
          });

          context('with next callback', function() {
            it ('receives the right index', function() {
              // given
              var self  = $('form').stepy({
                            enter    : true,
                            validate: false,
                            next    : function(index) { $(this).data('index', index); }
                          }),
                  steps = self.children('fieldset'),
                  evt   = $.Event('keypress');

                evt.which   = 13;
                evt.keyCode = 13;

              // when
              steps.eq(0).children('input:visible:last').trigger(evt);

              // then
              expect(self.data('index')).toEqual(2);
            });
          });

          context('with the fields inside elements', function() {
            beforeEach(function() {
              $('form').children('fieldset:first').find(':input').wrap('<p />');
            });

            it ('goes to the next step', function() {
              // given
              var self  = $('form').stepy({ enter: true, validate: false }),
                  steps = self.children('fieldset'),
                  evt   = $.Event('keypress');

                evt.which   = 13;
                evt.keyCode = 13;

              // when
              steps.eq(0).find('input:visible:last').trigger(evt);

              // then
              expect(steps.eq(0)).toBeHidden();
              expect(steps.eq(1)).toBeVisible();
              expect(steps.eq(2)).toBeHidden();
            });
          });
        });

        context('with invalid step', function() {
          beforeEach(function() {
            $('form').validate({
              rules: { 'password':  'required' }
            });
          });

          context('with block enabled', function() {
            it ('does not goes to the next step', function() {
              // given
              var self  = $('form').stepy({ enter: true, block: true, validate: true }),
                  steps = self.children('fieldset'),
                  evt   = $.Event('keypress');

                evt.which   = 13;
                evt.keyCode = 13;

              // when
              steps.eq(0).children('input:visible:last').trigger(evt);

              // then
              expect(steps.eq(0)).toBeVisible();
              expect(steps.eq(1)).toBeHidden();
              expect(steps.eq(2)).toBeHidden();
            });
          });

          context('with block disabled', function() {
            it ('goes to the next step', function() {
              // given
              var self  = $('form').stepy({ enter: true, block: false, validate: true }),
                  steps = self.children('fieldset'),
                  evt   = $.Event('keypress');

                evt.which   = 13;
                evt.keyCode = 13;

              // when
              steps.eq(0).children('input:visible:last').trigger(evt);

              // then
              expect(steps.eq(0)).toBeHidden();
              expect(steps.eq(1)).toBeVisible();
              expect(steps.eq(2)).toBeHidden();
            });
          });
        });

        context('on the last step', function() {
          it ('submits the form', function() {
            // given
            var self  = $('form').stepy({
                          finish: function() {
                            $(this).data('submited', true);
                          }
                        }),
                steps = self.children('fieldset'),
                evt   = $.Event('keypress');

            evt.which   = 13;
            evt.keyCode = 13;

            self.on('submit', function(evt) {
              evt.preventDefault();
            });

            steps.eq(1).find('.button-next').click();

            // when
            steps.eq(2).children('input:visible:last').trigger(evt);

            // then
            expect(self.data('submited')).toBeTruthy();
          });
        });
      });

      context('disabled', function() {
        it ('does not go to the next step', function() {
          // given
          var self  = $('form').stepy({ enter: false, validate: false }),
              steps = self.children('fieldset'),
              evt   = $.Event('keypress');

            evt.which   = 13;
            evt.keyCode = 13;

          // when
          steps.eq(0).children('input:visible:last').trigger(evt);

          // then
          expect(steps.eq(0)).toBeVisible();
          expect(steps.eq(1)).toBeHidden();
          expect(steps.eq(2)).toBeHidden();
        });
      });
    });

    describe('finish', function() {
      it ('is called on trigger finish button', function() {
        // given
        var self = $('form').stepy({
          finish: function() {
            $(this).data('called', true);
          }
        }),
        steps     = self.children('fieldset');

         self.on('submit', function(evt) {
          evt.preventDefault();
        });

        steps.eq(1).find('.button-next').click();

        // when
        steps.eq(2).find(':submit').click();

        // then
        expect(self.data('called')).toBeTruthy();
      });
    });

    describe('finishButton', function() {
      context('disabled', function() {
        it ('will not to move the finish button inside the last step', function() {
          // given
          var self = $('form'),
              step = self.children('fieldset:last');

          // when
          self.stepy({ finishButton: false });

          // then
          expect(step).not.toContain('input[type="submit"]');
        });
      });

      context('enabled', function() {
        it ('will to move the finish button inside the last step', function() {
          // given
          var self = $('form'),
              step = self.children('fieldset:last');

          // when
          self.stepy({ finishButton: true });

          // then
          expect(step).toContain('input[type="submit"]');
        });

        it ('starts hidden', function() {
          // given
          var self = $('form'),
              step = self.children('fieldset:last');

          // when
          self.stepy();

          // then
          expect(step.find(':submit')).toBeHidden();
        });

        context('on the last step becomes active', function() {
          it ('becomes visible', function() {
            // given
            var self  = $('form').stepy(),
                steps = self.children('fieldset');

            // when
            steps.eq(1).find('.button-next').click();

            // then
            expect(steps.last().find(':submit')).toBeVisible();
          });
        });

        context('without the class stepy-finish', function() {
          beforeEach(function() {
            $('form').children(':submit').removeClass('stepy-finish');
          });

          context('with type submit', function() {
            it ('will be getted', function() {
              // given
              var self = $('form'),
                  step = self.children('fieldset:last');

              // when
              self.stepy({ finishButton: true });

              // then
              expect(step).toContain('input[type="submit"]');
            });
          });

          context('without type submit', function() {
            beforeEach(function() {
              $('form').children(':submit').attr('type', 'image');
            });

            it ('raise exception', function() {
              // given
              var self = $('form');

              // when
              var lambda = function() { self.stepy(); };

              // then
              expect(lambda).toThrow(new Error('Submit button or element with class "stepy-finish" missing!'));
            });
          });
        });
      });
    });

    describe('legend', function() {
      context('disabled', function() {
        it ('is not displayed', function() {
          // given
          var self = $('form');

          // when
          self.stepy({ legend: false });

          // then
          var legends = self.find('legend');

          expect(legends.eq(0)).toBeHidden();
          expect(legends.eq(1)).toBeHidden();
          expect(legends.eq(2)).toBeHidden();
        });
      });
    });

    describe('next', function() {
      it ('is called on trigger next button', function() {
        // given
        var self  = $('form').stepy({ next: function() { $(this).data('called', true); } }),
            steps = self.children('fieldset');

        // when
        steps.eq(0).find('.button-next').click();

        // then
        expect(self.data('called')).toBeTruthy();
      });

      it ('receives the right index', function() {
        // given
        var self  = $('form').stepy({ next: function(index) { $(this).data('index', index); } }),
            steps = self.children('fieldset');

        // when
        steps.eq(0).find('.button-next').click();

        // then
        expect(self.data('index')).toEqual(2);
      });
    });

    describe('nextLabel', function() {
      it ('changes the next button label', function() {
        // given
        var self  = $('form'),
            steps = self.children('fieldset');

        // when
        self.stepy({ nextLabel: '&gt;&gt;' });

        // then
        expect(steps.eq(0).find('.button-next')).toHaveHtml('&gt;&gt;');
        expect(steps.eq(1).find('.button-next')).toHaveHtml('>>');
      });
    });

    describe('select', function() {
      it ('is called on change the step', function() {
        // given
        var self  = $('form').stepy({ select: function() { $(this).data('called', true); } }),
            steps = self.children('fieldset');

        // when
        steps.eq(0).find('.button-next').click();

        // then
        expect(self.data('called')).toBeTruthy();
      });

      it ('receives the right index', function() {
        // given
        var self  = $('form').stepy({ select: function(index) { $(this).data('index', index); } }),
            steps = self.children('fieldset');

        // when
        steps.eq(0).find('.button-next').click();

        // then
        expect(self.data('index')).toEqual(2);
      });
    });

    describe('titleTarget', function() {
      context('with target', function() {
        beforeEach(function() {
           this.target = Factory.append('<div id="target"></div>');
        });

        it ('receives the titles of the steps', function() {
          // given
          var self = $('form');

          // when
          self.stepy({ titleTarget: '#' + this.target.attr('id') });

          // then
          expect(target).toContain('#' + self.attr('id') + '-header');
        });
      });

      context('clicking on next title', function() {
        context('with next callback', function() {
          it ('receives the right index', function() {
            // given
            var self  = $('form').stepy({
                          titleClick: true,
                          next      : function(index) { $(this).data('index', index); }
                        }),
                steps = self.children('fieldset');

            // when
            $('#' + self.attr('id') + '-header').children('li').eq(1).click();

            // then
            expect(self.data('index')).toEqual(2);
          });
        });
      });

      context('clicking on back title', function() {
        context('with back callback', function() {
          it ('receives the right index', function() {
            // given
            var self   = $('form').stepy({
                          titleClick: true,
                          back      : function(index) {
                            console.log(index);
                            $(this).data('index', index);
                          }
                        }),
                steps  = self.children('fieldset'),
                titles = $('#' + self.attr('id') + '-header').children('li');

            titles.eq(1).click();

            // when
            titles.eq(0).click();

            // then
            expect(self.data('index')).toEqual(1);
          });
        });
      });

      context('clicking on other title', function() {
        context('with select callback', function() {
          it ('receives the right index', function() {
            // given
            var self   = $('form').stepy({
                          titleClick: true,
                          select    : function(index) { $(this).data('index', index); }
                        }),
                steps  = self.children('fieldset'),
                titles = $('#' + self.attr('id') + '-header').children('li');

            // when
            titles.eq(1).click();

            // then
            expect(self.data('index')).toEqual(2);
          });
        });
      });

      context('with validate enabled', function() {
        context('and block enabled', function() {
          it ('blocks the step change', function() {
            // given
            var self   = $('form').stepy({ block: true, titleClick: true, validate: true }),
                steps  = self.children('fieldset'),
                titles = $('#' + self.attr('id') + '-header').children('li');

            self.validate({
              errorPlacement: function(error, element) {
                $('#stepy div.stepy-errors').append(error);
              }, rules: {
                'password':  'required'
              }, messages: {
                'password':  { required: 'Password field is requerid!' }
              }
            });

            // when
            titles.eq(2).click();

            // then
            expect(steps.eq(0)).toBeVisible();
            expect(steps.eq(1)).toBeHidden();
            expect(steps.eq(2)).toBeHidden();
          });
        });

        context('and errorImage enabled', function() {
          it ('display the error image', function() {
            // given
            var self   = $('form').stepy({ errorImage: true, titleClick: true, validate: true }),
                steps  = self.children('fieldset'),
                titles = $('#' + self.attr('id') + '-header').children('li');

            self.validate({
              errorPlacement: function(error, element) {
                $('#stepy div.stepy-errors').append(error);
              }, rules: {
                'password':  'required'
              }, messages: {
                'password':  { required: 'Password field is requerid!' }
              }
            });

            // when
            titles.eq(2).click();

            // then
            expect(titles.eq(0)).toHaveClass('stepy-error');
          });
        });
      });
    });

    describe('validate', function() {
      context('enabled', function() {
        context('fails', function() {
          it ('displays the error', function() {
            // given
            var self  = $('form').stepy({ validate: true }),
                steps = self.children('fieldset');

            self.validate({
              errorPlacement: function(error, element) {
                $('#stepy div.stepy-errors').append(error);
              }, rules: {
                'password':  'required'
              }, messages: {
                'password':  { required: 'Password field is requerid!' }
              }
            });

            // when
            steps.eq(1).find('.button-next').click();

            // then
            expect(self.children('.stepy-errors')).toContain('label.error');
          });
        });
      });
    });

    describe('block', function() {
      context('enabled', function() {
        context('with invalid field', function() {
          it ('blocks the step change', function() {
            // given
            var self  = $('form').stepy({ block: true, validate: true }),
                steps = self.children('fieldset');

            self.validate({
              errorPlacement: function(error, element) {
                $('#stepy div.stepy-errors').append(error);
              }, rules: {
                'password':  'required'
              }, messages: {
                'password':  { required: 'Password field is requerid!' }
              }
            });

            // when
            steps.eq(0).find('.button-next').click();

            // then
            expect(steps.eq(0)).toBeVisible();
            expect(steps.eq(1)).toBeHidden();
            expect(steps.eq(2)).toBeHidden();
          });

          context('click on title', function() {
            it ('blocks the step change', function() {
              // given
              var self   = $('form').stepy({ block: true, validate: true }),
                  steps  = self.children('fieldset'),
                  titles = $('#' + self.attr('id') + '-header').children('li');

              self.validate({
                errorPlacement: function(error, element) {
                  $('#stepy div.stepy-errors').append(error);
                }, rules: {
                  'password':  'required'
                }, messages: {
                  'password':  { required: 'Password field is requerid!' }
                }
              });

              // when
              titles.eq(2).click();

              // then
              expect(steps.eq(0)).toBeVisible();
              expect(steps.eq(1)).toBeHidden();
              expect(steps.eq(2)).toBeHidden();
            });
          });
        });

        context('with valid field', function() {
          it ('does not block the step change', function() {
            // given
            var self  = $('form').stepy({ block: true, validate: true }),
              steps  = self.children('fieldset');

            self.validate({
              errorPlacement: function(error, element) {
                $('#stepy div.stepy-errors').append(error);
              }, rules: {
                'password':  'required'
              }, messages: {
                'password':  { required: 'Password field is requerid!' }
              }
            });

            self.find('input[name="password"]').val('valid');

            // when
            steps.eq(0).find('.button-next').click();

            // then
            expect(steps.eq(0)).toBeHidden();
            expect(steps.eq(1)).toBeVisible();
            expect(steps.eq(2)).toBeHidden();
          });
        });
      });
    });

    describe('errorImage', function() {
      context('with invalid field', function() {
        it ('appears on step title', function() {
          // given
          var self   = $('form').stepy({ errorImage: true, validate: true }),
              steps  = self.children('fieldset'),
              titles = $('#' + self.attr('id') + '-header').children('li');

          self.validate({
            errorPlacement: function(error, element) {
              $('#stepy div.stepy-errors').append(error);
            }, rules: {
              'password':  'required'
            }, messages: {
              'password':  { required: 'Password field is requerid!' }
            }
          });

          // when
          steps.eq(0).find('.button-next').click();

          // then
          expect(titles.eq(0)).toHaveClass('stepy-error');
        });
      });
    });
  });

  describe('functions', function() {
    describe('#destroy', function() {
      it ('is chainable', function() {
        // given
        var self = $('form').stepy();

        // when
        var ref = self.stepy('destroy');

        // then
        expect(ref).toBe(self);
      });

      it ('shows all steps', function() {
        // given
        var self  = $('form').stepy(),
            steps = self.children('fieldset');

        // when
        self.stepy('destroy');

        // then
        expect(steps.eq(0)).toBeVisible();
        expect(steps.eq(1)).toBeVisible();
        expect(steps.eq(2)).toBeVisible();
      });

      it ('removes the back buttons', function() {
        // given
        var self  = $('form').stepy(),
            steps = self.children('fieldset');

        // when
        self.stepy('destroy');

        // then
        expect(steps).not.toContain('.button-back');
      });

      it ('puts the finish button as children of the last step', function() {
        // given
        var self  = $('form').stepy(),
            steps = self.children('fieldset');

        // when
        self.stepy('destroy');

        // then
        expect(steps).toContain(':submit');
      });

      it ('removes the bind indicator', function() {
        // given
        var self = $('form').stepy();

        // when
        self.stepy('destroy');

        // then
        expect(self.data('stepy')).toBeFalsy();
      });

      context('with validate enabled', function() {
        it ('removes the error container', function() {
          // given
          var self  = $('form').stepy({ validate: true }),
              steps = self.children('fieldset');

          // when
          self.stepy('destroy');

          // then
          expect(self).not.toContain('.stepy-errors');
        });
      });
    });

    describe('#step', function() {
      it ('is chainable', function() {
        // given
        var self = $('form').stepy();

        // when
        var ref = self.stepy('step', 2);

        // then
        expect(ref).toEqual(ref);
      });

      it ('changes the step', function() {
        // given
        var self  = $('form').stepy(),
            steps = self.children('fieldset');

        // when
        self.stepy('step', 2);

        // then
        expect(steps.eq(0)).toBeHidden();
        expect(steps.eq(1)).toBeVisible();
        expect(steps.eq(2)).toBeHidden();
      });
    });
  });

  context('with form without id', function() {
    beforeEach(function() { $('form').removeAttr('id'); });

    describe('ID', function() {
      it ('is generated', function() {
        // given
        var self = $('form');

        // when
        self.stepy();

        // then
        expect(self[0].hash).not.toBeUndefined();
      });

      it ('is used on the header', function() {
        // given
        var self = $('form');

        // when
        self.stepy();

        // then
        expect(self.prev('ul').attr('id')).toEqual(self[0].hash + '-header');
      });
    });
  });
});
