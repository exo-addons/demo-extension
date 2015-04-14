(function($) {
  $(document).ready(function(){

    var pieData = [
      {
        value: 33,
        color:"#4581a5"
      },
      {
        value : 30,
        color : "#2eb87c"
      },
      {
        value : 20,
        color : "#f47b5e"
      },
      {
        value : 10,
        color : "#f3b665"
      }

    ];

    var options = {
      animation : false
    }
    var todoCanvas = document.getElementById("todo-canvas");
    if (todoCanvas !== null) {
      var myPie = new Chart(todoCanvas.getContext("2d")).Pie(pieData, options);
    }

  });

})(jQuery);
