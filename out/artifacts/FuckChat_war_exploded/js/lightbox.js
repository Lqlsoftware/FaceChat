;(function ($, window, document, undefined) {

  var Lightbox = function (elem, options) {
    var self = this;
    this.$elem = elem;
    this.$pic_item = this.$elem.find('.img');
    this.$popMask = $('#lightbox_mask');
    this.$popWin = $('#lightbox_popup');
    this.$picView = this.$popWin.find('.pic-view');
    this.$pic = this.$picView.find('.pic');
    this.$btn_prev = this.$popWin.find('.btn-prev');
    this.$btn_next = this.$popWin.find('.btn-next');
    this.$btn_close = this.$popWin.find('.btn-close');
    this.$caption = this.$popWin.find('.caption-view > p');

    this.groupName = null;
    this.groupData = [];
    this.index = 0;
    this.pic_t = 0;
    this.b_stop = true;

    this.defaults = {
      ifChange: false
    };

    this.opts = $.extend({}, this.defaults, options);
  };

  Lightbox.prototype = {
    changePic: function () {
      var self = this;

      this.$pic.attr('src', this.groupData[this.index].src);
      this.$caption.text(this.groupData[this.index].caption);
    },

    btnSwitch: function () {
      var self = this;

      this.$btn_prev.click(function () {
        if (self.index <= 0) {
          self.index = self.groupData.length - 1;
        } else {
          self.index --;
        }

        self.changePic();
      });

      this.$btn_next.click(function () {
        if (self.index >= self.groupData.length - 1) {
          self.index = 0;
        } else {
          self.index ++;
        }

        self.changePic();
      });
    },

    showPop: function (curSrc) {
      this.$popMask.fadeIn();
      this.$popWin.fadeIn();

      this.$pic.attr('src', curSrc);
      this.$caption.text(this.groupData[this.index].caption);

      this.autoTop($(window).height(), this.$pic.height());
    },

    initalPop: function () {
      var self = this;
      var curSrc = null;
      var curId = null;

      this.$pic_item.click(function () {
        var curGroup = $(this).attr('data-group');
        curSrc = $(this).attr('data-source');
        curId = $(this).attr('data-id');

        if (self.groupName !== curGroup) {
          self.groupName = curGroup;

          self.getGroupData();
        }

        self.index = self.getIndexOf(curId);

        self.showPop(curSrc);
        self.btnSwitch();

        if (self.opts.ifChange) {
          self.$btn_prev.show();
          self.$btn_next.show();
        } else {
          self.$btn_prev.hide();
          self.$btn_next.hide();
        }
      });

      $(window).resize(function () {
        self.autoTop($(window).height(), self.$pic.height());
      });

      this.$btn_close.click(function () {
        self.$popMask.fadeOut();
        self.$popWin.fadeOut();
      });

      this.$popMask.click(function () {
        self.$popMask.fadeOut();
        self.$popWin.fadeOut();
      });
    },

    // 小方法
    autoTop: function (win_h, pic_h) {
      var pic_t = (win_h - 38 - pic_h - 10) / 2;

      if (pic_t <= 0) {
        pic_t = 0;
      }

      this.$picView.css({top: pic_t});
    },

    getGroupData: function () {
      var self = this;

      this.groupData.length = 0;
      this.$pic_item.each(function () {
        self.groupData.push({
          id: $(this).attr('data-id'),
          src: $(this).attr('data-source'),
          caption: $(this).attr('data-caption')
        });
      });
    },

    getIndexOf: function (curId) {
      var index = 0;

      $(this.groupData).each(function () {
        if (this.id === curId) {
          return false;
        } else {
          index ++;
        }
      });

      return index;
    },

    constructor: Lightbox
  };

  $.fn.lightbox = function (options) {
    var lightbox = new Lightbox(this, options);

    return lightbox.initalPop();
  };

})(jQuery, window, document);