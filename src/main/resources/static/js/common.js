let delay = 300;
let timer = null;

$(document).ready(function(){
    // AOS.init({
    //     duration: 800,
    //     // once: true,
    // });

    console.log("js연결")
    headerInit();

    $(window).resize(function(){
        clearTimeout(timer);
        timer = setTimeout(function(){
            hdlr_switch($(window).width());
        }, delay);
    });

    $(window).scroll(function(){
        scroll_switch();
    });

})

function headerInit(){
    $header = $(".header");
    $nav = $(".header nav");
    $gnb = $(".header nav .gnb");
    $depth2 = $(".depth2");
    $depth2Bg = $(".depth2_bg");
    $winUrl = location.href;
    $btnMenu = $(".btn_menu");
    scroll_switch();
    hdlr_switch($(window).width());
    $btnMenu.click(function(){
        if($nav.hasClass("on")) {
            nav_off("-100%");
        } else {
            nav_on("0");
        }
    });
}

function scroll_switch() {
    if ($(window).scrollTop() > 100) {
        $('.main_header .gnb').addClass('scroll_top');
    } else {
        $('.main_header .gnb').removeClass('scroll_top');
    }
}

//break_point
function hdlr_switch(val) {
    $gnb.children("li").children("a").unbind('click');
    $gnb.children("li").unbind('mouseenter').unbind('mouseleave');
    if (val > 1183) {
        $gnb.children("li").bind('mouseenter', function(){
            $depth2.stop().slideDown();
            $depth2Bg.stop().slideDown();
        }).bind('mouseleave', function(){
            $depth2.stop().slideUp();
            $depth2Bg.stop().slideUp();
        });
    } else {
        $gnb.children("li").children("a").bind('click', function(evt){
            var depth2 = $(this).siblings(".depth2");
            if (depth2.length) {
                evt.preventDefault();
                if(!depth2.is(":visible")) {
                    $gnb.find(".depth2").stop().slideUp();
                    $gnb.children("li").removeClass("on");
                    depth2.stop().slideDown();
                    $(this).parent().addClass("on");
                } else {
                    $gnb.find(".depth2").stop().slideUp();
                    $gnb.children("li").removeClass("on");
                }
            }
        });
    }
}



function nav_on(num){
    $nav.addClass("on");
    $nav.stop().animate({
        left:num
    });
    $btnMenu.find('.icon-wrap > i').removeClass('fa-bars');
    $btnMenu.find('.icon-wrap > i').addClass('fa-xmark');
    $('html').css('overflow-y','hidden');
}

function nav_off(num){
    $nav.removeClass("on");
    $nav.stop().animate({
        left:num
    });
    $gnb.find(".depth2").stop().slideUp();
    $gnb.children("li").removeClass("on");
    $btnMenu.find('.icon-wrap > i').removeClass('fa-xmark');
    $btnMenu.find('.icon-wrap > i').addClass('fa-bars');
    $('html').css('overflow-y','auto');
}