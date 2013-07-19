/**
 * Demo Extension class
 * @constructor
 */
var demoExtension = new DemoExtension();

jQuery(document).ready(function(){

  var $demoApplication = $("#populator_div");

  demoExtension.initOptions({
    "urlStart": $demoApplication.jzURL("PopulatorApplication.start"),
    "urlElements": $demoApplication.jzURL("PopulatorApplication.elements")
  });

  $(".btn-start").on("click", function(){
    if ($(this).hasClass("disabled")) return;
    demoExtension.startPopulating();
  });

});


function DemoExtension() {
  this.urlStart = "";
  this.urlElements = "";
  this.notifEventInt = "";
}

DemoExtension.prototype.initOptions = function(options) {
  this.urlStart = options.urlStart;
  this.urlElements = options.urlElements;

  this.notifEventInt = window.clearInterval(this.notifEventInt);
  this.notifEventInt = setInterval(jQuery.proxy(this.refreshElements, this), 1000);
  this.refreshElements();

};


DemoExtension.prototype.startPopulating = function() {
  $(".btn-start").addClass("disabled");

  jQuery.ajax({
    url: this.urlStart,
    dataType: "json",
    context: this,
    success: function(data){
      var status = data.status;
      $(".btn-start").removeClass("disabled");
    },
    error: function () {
      //setTimeout(jQuery.proxy(this.startPopulating, this), 3000);
      console.log("error in server call");
      $(".btn-start").removeClass("disabled");
    }
  });
};

DemoExtension.prototype.refreshElements = function() {

  jQuery.ajax({
    url: this.urlElements,
    dataType: "json",
    context: this,
    success: function(data){
      console.log("Refresh Elements");
      this.updateElementsContainer(data);
    },
    error: function () {
      //setTimeout(jQuery.proxy(this.startPopulating, this), 3000);
      console.log("error in server call");
      $(".btn-start").removeClass("disabled");
    }
  });
};

DemoExtension.prototype.updateElementsContainer = function(elements) {
  var html = "";
  for (ie=0 ; ie<elements.length ; ie++) {
    var element = elements[ie];

    html += '<div class="row">';
    html += '  <div class="span3">';
    html += '    <label>'+element.name+'</label>';
    html += '  </div>';
    html += '  <div class="span9">';
    html += '    <div class="progress">';
    html += '      <div class="bar" style="width: '+element.percentage+';"></div>';
    html += '    </div>';
    html += '  </div>';
    html += '</div>';

    $(".elements-container").html(html);

  }
};

