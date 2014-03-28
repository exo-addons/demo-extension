(function($){

  $(document).ready(function(){

    $(".responsiveMenu").on("click", function(){
      var $leftNavigationTDContainer = $(".LeftNavigationTDContainer");
      if ($leftNavigationTDContainer.css("display")==="none") {
        $leftNavigationTDContainer.animate({width: 'show', duration: 50});
      } else {
        $leftNavigationTDContainer.animate({width: 'hide', duration: 50});
      }
    });

  });

})(jQuery);
