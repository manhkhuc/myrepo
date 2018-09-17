var oldY = 27;
var yArray = [27, 45, 63, 81, 99, 117, 135, 153];
var lineNum = 0;
var scrollHg = 10;
var fitHg = 25;
$(document).ready(function () {
  iOS = !!navigator.platform && /iPad|iPhone|iPod/.test(navigator.platform);
  console.log(iOS);
  
  
  $("#textAreaInput").on("input", function() {
		var y = this.scrollHeight;
		if (y > oldY) {
			if (lineNum > -1 && lineNum < 7) {
				lineNum = lineNum + 1;
				y = yArray[lineNum];
				$(".ms_nav").css("height", y + 73);
				$(this).css("height", y);
				$("#mainSection").css("margin-bottom", y + 29);
				deleteFitScreenPanel();
				createFitScreenPanel(y + 73);
				if ((window.innerHeight + window.scrollY + scrollHg) >= document.body.offsetHeight){
					$("html, body").animate({ scrollTop: $(document).height() }, 600);
				}
				oldY = y;
			} else {
				oldY = 153;
			}
		} else if (y < oldY) {
			if (lineNum > 0 && lineNum < 8) {
				lineNum = lineNum - 1;
				y = yArray[lineNum];
				$(".ms_nav").css("height", y + 73);
				$(this).css("height", y);
				$("#mainSection").css("margin-bottom", y + 29);
				deleteFitScreenPanel();
				createFitScreenPanel(y + 73);
				oldY = y;
			}
		}
	});

  	  $("#textAreaInput").on("focus", function () {
  		var $readMore = $('#wrap_moreread');
  		if ($readMore.length > 0) {
  			$readMore.slideUp();
  			$("#msgWrap").css("margin-top", 20);
		}
		if (lineNum != 0) {
			y = yArray[lineNum];
			$(".ms_nav").css("height", y + 73);
			$("#textAreaInput").css("height", y);
			$("#mainSection").css("margin-bottom", y + 29);
			createFitScreenPanel(y + 73);
		}else{
			$("#js-ms_nav").css("height", 100);
			$("#mainSection").css("margin-bottom", 56);
			createFitScreenPanel(100);
		}
	    $("#js-ms_nav").addClass("is-active");
	    $("#js-ms_nav").removeClass("is-active-sticker");
	    $('#js-nav_overlay').css('display', 'block');
	  });
	
	  $("#js-icon_sticker").on("click", function () {
		$("html, body").animate({ scrollTop: $(document).height() }, 600);
	    var $readMore = $('#wrap_moreread');
  		if ($readMore.length > 0) {
  			$readMore.slideUp();
  			$("#msgWrap").css("margin-top", 20);
		}
	  	if (lineNum != 0) {
	  		y = yArray[lineNum];
			$(".ms_nav").css("height", y + 223);
			$("#textAreaInput").css("height", y);
			$("#mainSection").css("margin-bottom", y + 180);
		}else{
			$("#js-ms_nav").css("height", 250);
			$("#mainSection").css("margin-bottom", 206);
		}
	    $("#js-ms_nav").addClass("is-active-sticker");
	    $("#js-ms_nav").removeClass("is-active");
	    $('#js-nav_overlay').css('display', 'block');
	  });

});

function createFitScreenPanel(inputH) {
	if (iOS) {
		var $OldFitPanel = $("#fitPanel");
		if ($OldFitPanel.length > 0) {
			return;
		}
		var scrollH = $("#mainSection").height() + inputH + fitHg + 375;
		var winH = window.screen.height;
		if (winH > scrollH) {
			var $fitPanel = $("<div>", { "id": "fitPanel", "style": "pointer-events: none; width: 100%; height: " + (winH - scrollH) + "px;" });
			$("#mainSection").after($fitPanel);
//			$("#mainSection").bind("touchmove", function(e){
//	  			e.preventDefault();
//	  		});
		} else {
			$("html, body").animate({ scrollTop: $(document).height() }, 600);
		}
	} else {
		$("html, body").animate({ scrollTop: $(document).height() }, 600);
	}
}

function deleteFitScreenPanel() {
	if (iOS) {
		var $fitPanel = $("#fitPanel");
		if ($fitPanel.length > 0) {
			$fitPanel.remove();
//			$("#mainSection").unbind("touchmove");
		}
	}
}