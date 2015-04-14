/**
 * Demo Extension class
 * @constructor
 */
var demoExtension = new DemoExtension();

(function($) {

  $(document).ready(function(){

    var $demoApplication = $("#populator_div");

    demoExtension.initOptions({
      "urlStart": $demoApplication.jzURL("PopulatorApplication.start"),
      "urlElements": $demoApplication.jzURL("PopulatorApplication.elements"),
      "urlSave": $demoApplication.jzURL("PopulatorApplication.save")
    });

    $(".btn-start").on("click", function(){
      if ($(this).hasClass("disabled")) return;
      demoExtension.startPopulating();
    });

    $(".btn-save-data").on("click", function(){
      if ($(this).hasClass("disabled")) return;
      var username = $("#custom-username").val();
      var fullname = $("#custom-fullname").val();
      var language = $("#custom-language").val();
      demoExtension.saveCustomData(username, fullname, language);
    });

  });
})(jQuery);


function DemoExtension() {
  this.urlStart = "";
  this.urlElements = "";
  this.urlSave = "";
  this.notifEventInt = "";
}

DemoExtension.prototype.initOptions = function(options) {
  this.urlStart = options.urlStart;
  this.urlElements = options.urlElements;
  this.urlSave = options.urlSave;

  this.notifEventInt = window.clearInterval(this.notifEventInt);
  this.notifEventInt = setInterval(jQuery.proxy(this.refreshElements, this), 1000);
  this.refreshElements();

};


DemoExtension.prototype.startPopulating = function() {
  jQuery(".btn-start").addClass("disabled");

  demoExtension.populate(1, function() {
    demoExtension.populate(2, function () {
      demoExtension.populate(3, function () {
        demoExtension.populate(4, function() {
          demoExtension.populate(5, function() {
            demoExtension.populate(6, function () {
              demoExtension.populate(7, function () {
                demoExtension.populate(8, function () {
                  demoExtension.populate(9, function () {
                    demoExtension.populate(9, function() {
                      demoExtension.populate(10, function() {
                        jQuery(".btn-start").removeClass("disabled");
                      });
                    });
                  });
                });
              });
            });
          });
        });
      });
    });
  });

};

DemoExtension.prototype.populate = function(filter, callback) {

  jQuery.ajax({
    url: this.urlStart,
    dataType: "json",
    data: {
      "filter": filter
    },
    context: this,
    success: function(data){
      var status = data.status;
      if (typeof callback === "function") {
        callback();
      }
    },
    error: function () {
      console.log("error in server call");
    }
  });


};

DemoExtension.prototype.saveCustomData = function(username, fullname, language, callback) {

  jQuery.ajax({
    url: this.urlSave,
    dataType: "json",
    data: {
      "username": username,
      "fullname": fullname,
      "language": language
    },
    context: this,
    success: function(data){
      var status = data.status;
      if (typeof callback === "function") {
        callback();
      }
    },
    error: function () {
      console.log("error in server call");
    }
  });


};


DemoExtension.prototype.refreshElements = function() {

  jQuery.ajax({
    url: this.urlElements,
    dataType: "json",
    context: this,
    success: function(data){
//      console.log("Refresh Elements");
      this.updateElementsContainer(data);
    },
    error: function () {
      //setTimeout(jqchat.proxy(this.startPopulating, this), 3000);
      console.log("error in server call");
      jQuery(".btn-start").removeClass("disabled");
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

    jQuery(".elements-container").html(html);

  }
};

