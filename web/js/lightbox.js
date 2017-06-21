;
(function($, window, document, undefined) {

    var Lightbox = function(elem, options) {
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
        changePic: function() {
            var self = this;

            this.$pic.attr('src', this.groupData[this.index].src);
            this.$caption.text(this.groupData[this.index].caption);
        },

        btnSwitch: function() {
            var self = this;

            this.$btn_prev.click(function() {
                if (self.index <= 0) {
                    self.index = self.groupData.length - 1;
                } else {
                    self.index--;
                }

                self.changePic();
            });

            this.$btn_next.click(function() {
                if (self.index >= self.groupData.length - 1) {
                    self.index = 0;
                } else {
                    self.index++;
                }

                self.changePic();
            });
        },

        showPop: function(curSrc) {
            this.$popMask.fadeIn();
            this.$popWin.fadeIn();

            this.$pic.attr('src', curSrc);
            this.$caption.text(this.groupData[this.index].caption);

            this.autoTop($(window).height(), this.$pic.height());
        },

        initalPop: function() {
            var self = this;
            var curSrc = null;
            var curId = null;

            this.$pic_item.click(function() {
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

            $(window).resize(function() {
                self.autoTop($(window).height(), self.$pic.height());
            });

            this.$btn_close.click(function() {
                self.$popMask.fadeOut();
                self.$popWin.fadeOut();
            });

            this.$popMask.click(function() {
                self.$popMask.fadeOut();
                self.$popWin.fadeOut();
            });
 
            this.$pic.click(function() {
                 self.$popMask.fadeOut();
                 self.$popWin.fadeOut();
            });
 
            var target = document.getElementById('lightbox_popup');
            var slider = {
                //判断设备是否支持touch事件
                touch: ('ontouchstart' in window) || window.DocumentTouch && document instanceof DocumentTouch,
                isChange: false,
                //事件
                events: {
                    handleEvent: function(event) {
                        var self = this; //this指events对象
                        if (event.type == 'touchstart') {
                            self.start(event);
                        } else if (event.type == 'touchmove') {
                            self.move(event);
                        } else if (event.type == 'touchend') {
                            self.end(event);
                        }
                    },
                    //滑动开始
                    start: function(event) {
                        var touch = event.targetTouches[0]; //touches数组对象获得屏幕上所有的touch，取第一个touch
                        startPos = { x: touch.pageX, y: touch.pageY }; //取第一个touch的坐标值
                        isScrolling = 0; //这个参数判断是垂直滚动还是水平滚动
                        target.addEventListener('touchmove', this, false);
                        target.addEventListener('touchend', this, false);
                        this.isChange = false;
                    },
                    //移动
                    move: function(event) {
                        //当屏幕有多个touch或者页面被缩放过，就不执行move操作
                        if (event.targetTouches.length > 1 || event.scale && event.scale !== 1) return;
                        var touch = event.targetTouches[0];
                        endPos = { x: touch.pageX - startPos.x, y: touch.pageY - startPos.y };
                        isScrolling = Math.abs(endPos.x) < Math.abs(endPos.y) ? 1 : 0; //isScrolling为1时，表示纵向滑动，0为横向滑动
                        if (this.isChange === false && isScrolling === 0) {
                            // 往左滑
                            if (endPos.x < $('body').width() / -2) {
                                if (self.index <= 0) {
                                    self.index = self.groupData.length - 1;
                                } else {
                                    self.index--;
                                }
                                self.changePic();
                                this.isChange = true;
                            }
                            // 往右划
                            else if (endPos.x > $('body').width() / 2) {
                                if (self.index >= self.groupData.length - 1) {
                                    self.index = 0;
                                } else {
                                    self.index++;
                                }
                                self.changePic();
                                this.isChange = true;
                            }
                        }
                    },
                    //滑动释放
                    end: function(event) {
                        //解绑事件
                        target.removeEventListener('touchmove', this, false);
                        target.removeEventListener('touchend', this, false);
                        this.isChange = false;
                    }
                },
                //初始化
                init: function() {
                    var self = this; //this指slider对象
                    if (!!self.touch) target.addEventListener('touchstart', self.events, false); //addEventListener第二个参数可以传一个对象，会调用该对象的handleEvent属性
                }
            };
            $(document).ready(function() {
                slider.init();
            });
        },

        // 小方法
        autoTop: function(win_h, pic_h) {
            var pic_t = (win_h - 38 - pic_h - 10) / 2;

            if (pic_t <= 0) {
                pic_t = 0;
            }

            this.$picView.css({ top: pic_t });
        },

        getGroupData: function() {
            var self = this;

            this.groupData.length = 0;
            this.$pic_item.each(function() {
                self.groupData.push({
                    id: $(this).attr('data-id'),
                    src: $(this).attr('data-source'),
                    caption: $(this).attr('data-caption')
                });
            });
        },

        getIndexOf: function(curId) {
            var index = 0;

            $(this.groupData).each(function() {
                if (this.id === curId) {
                    return false;
                } else {
                    index++;
                }
            });

            return index;
        },

        constructor: Lightbox
    };

    $.fn.lightbox = function(options) {
        var lightbox = new Lightbox(this, options);

        return lightbox.initalPop();
    };

})(jQuery, window, document);
